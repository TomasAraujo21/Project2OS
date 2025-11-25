/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SYS;
import Audit.Audit;
import DS.LinkedList;
import Storage.Block;
import Storage.Disk;
import javax.swing.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;


/**
 *
 * @author Gabriel Flores
 */
public class FileSystem {
    private Directory root;
    private Disk disk;
    private Audit audit;

    public FileSystem(int sizeDisk, Audit audit) {
        this.disk = new Disk(sizeDisk);
        this.audit = audit;
        this.root = new Directory ("Raiz", audit, null, disk);
    }
    
    public Directory getRoot() {                      
        return root;
    }

    public Disk getDisk() {                           
        return disk;
    }

    public Audit getAudit() {                         
        return audit;
    }

    public void setRoot(Directory root) {
        this.root = root;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }
    
    
    public MyFile createFile(Directory targetDir,
                            String name,
                            int sizeBlocks,
                            String color,
                            String user,
                            boolean isPublic) {

        if (!disk.getStorage(sizeBlocks)) {
            System.out.println("No hay espacio suficiente en el disco");
            JOptionPane.showMessageDialog(
                null,
                "No hay espacio suficiente en el disco",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        int firstBlock = disk.asignBlocks(sizeBlocks);
        if (firstBlock == -1) {
            System.out.println("Error al asignar bloques.");
            JOptionPane.showMessageDialog(
                null,
                "Error al asignar bloques en el disco",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        MyFile file = new MyFile(name, sizeBlocks, firstBlock, color, isPublic);

        targetDir.addFile(file, user);

        return file;
    }
    
    public MyFile addFile(String name, int sizeBlocks, String color, Directory targetDir, String user, boolean isPublic) {
        // Verificar espacio en el disco
        if (!disk.getStorage(sizeBlocks)) {
            System.out.println("No hay espacio suficiente en el disco");
            JOptionPane.showMessageDialog(null,
                    "No hay espacio suficiente en el disco",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Pedir al disco que asigne bloques encadenados
        int firstBlock = disk.asignBlocks(sizeBlocks);
        if (firstBlock == -1) {
            System.out.println("Fallo en asignación de bloques");
            JOptionPane.showMessageDialog(null,
                    "Error al asignar bloques en el disco",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Crear el archivo
        MyFile file = new MyFile(name, sizeBlocks, firstBlock, color, isPublic);

        // Agregar el archivo al directorio y registrar en auditoría
        if (targetDir == null) {
            targetDir = root;
        }
        targetDir.addFile(file, user);

        return file;
    }
    
    public boolean deleteFile(Directory dir, MyFile file, String user) {
        if (dir == null || file == null) {
            return false;
        }

        int firstBlock = file.getFirstBlock();
        if (firstBlock >= 0) {
            disk.freeBlocks(firstBlock);
        }

        boolean removed = dir.deleteFile(file, user);

        if (removed) {
            audit.registerOperation(user,
                    "Se eliminó el archivo " + file.getName() +
                    " del directorio " + dir.getRute());
        }

        return removed;
    }
    
    public Directory addDirectory(String name, Directory parent, String user) {
        Directory newDir = new Directory(name, audit, parent, disk);
        parent.addDirectory(newDir);

        audit.registerOperation(user, "Directorio creado: " + newDir.getRute());
        return newDir;
    }
    
    public boolean deleteDirectory(Directory dir, String user) {
        if (dir == null) {
            return false;
        }

        // No permitir borrar la raíz
        if (dir == root) {
            System.out.println("[FileSystem] No se puede eliminar el directorio raíz.");
            return false;
        }

        while (dir.getFirstFile() != null) {
            MyFile f = dir.getFirstFile();
            deleteFile(dir, f, user);
        }

        while (dir.getSubdirectories().getHead() != null) {
            Directory child = dir.getSubdirectories().getHead().getData();
            deleteDirectory(child, user);
        }

        Directory parent = dir.getFather();
        if (parent != null) {
            return parent.deleteDirectory(dir);
        }

        return false;
    }
    
    public boolean renameFile(Directory dir, MyFile file, String newName, String user) {
        if (dir == null || file == null) {
            return false;
        }
        if (newName == null || newName.isBlank()) {
            return false;
        }

        String oldName = file.getName();
        file.setName(newName);

        if (audit != null) {
            audit.registerOperation(
                    user,
                    "Se renombró el archivo " + oldName + " a " + newName
                    + " en el directorio " + dir.getRute()
            );
        }

        return true;
    }
    
    public boolean renameDirectory(Directory dir, String newName, String user) {
        if (dir == null || newName == null || newName.isBlank()) {
            return false;
        }

        String oldName = dir.getName();
        dir.setName(newName);

        if (audit != null) {
            audit.registerOperation(
                    user,
                    "Se renombró el directorio " + oldName
                    + " a " + newName
                    + " en la ruta " + dir.getRute()
            );
        }

        return true;
    }
    
    public static FileSystem loadState(String path) {

    try (FileReader reader = new FileReader(path)) {

        Gson gson = new Gson();

        JsonObject rootJson = JsonParser.parseReader(reader).getAsJsonObject();

        // ============
        // 1. CARGAR DISCO
        // ============
        JsonObject diskJson = rootJson.getAsJsonObject("disk");
        int totalBlocks = diskJson.get("totalBlocks").getAsInt();

        Disk disk = new Disk(totalBlocks);

        boolean[] busy = gson.fromJson(diskJson.get("busy"), boolean[].class);
        disk.setBusy(busy);

        JsonObject nextMap = diskJson.getAsJsonObject("next");
        for (String key : nextMap.keySet()) {
            int id = Integer.parseInt(key);
            int next = nextMap.get(key).getAsInt();

            if (next != -1) {
                disk.getBlocks()[id].setNext(disk.getBlocks()[next]);
            }
        }

        // ============
        // 2. CARGAR AUDITORIA
        // ============
        JsonObject auditJson = rootJson.getAsJsonObject("audit");
        LinkedList<String> logs = new LinkedList<>();
        auditJson.getAsJsonArray("logs").forEach(log -> logs.add(log.getAsString()));

        Audit audit = new Audit(logs);

        // ============
        // 3. CARGAR DIRECTORIOS Y ARCHIVOS
        // ============
        Directory rootDir = loadDirectory(rootJson.getAsJsonObject("rootDirectory"), audit, disk, null);

        // ============
        // 4. RECONSTRUIR FILESYSTEM COMPLETO
        // ============
        FileSystem fs = new FileSystem(totalBlocks, audit);
        fs.setDisk(disk);
        fs.setRoot(rootDir);

        return fs;

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
    private static Directory loadDirectory(JsonObject dirJson, Audit audit, Disk disk, Directory father) {

    Directory dir = new Directory(
            dirJson.get("name").getAsString(),
            audit,
            father,
            disk
    );

    // Archivos
    dirJson.getAsJsonArray("files").forEach(f -> {
        JsonObject fo = f.getAsJsonObject();

        MyFile file = new MyFile(
                fo.get("name").getAsString(),
                fo.get("size").getAsInt(),
                fo.get("firstBlock").getAsInt(),
                fo.get("color").getAsString(),
                fo.get("isPublic").getAsBoolean()
        );

        dir.addFile(file, "Sistema");
    });

    // Subdirectorios
    dirJson.getAsJsonArray("subdirectories").forEach(s -> {
        Directory sub = loadDirectory(s.getAsJsonObject(), audit, disk, dir);
        dir.getSubdirectories().add(sub);
    });

    return dir;
}
    private JsonObject serializeDirectoryGson(Directory dir) {

    JsonObject obj = new JsonObject();
    obj.addProperty("name", dir.getName());

    // ======== FILES ========
    JsonArray filesArray = new JsonArray();
    var fileNode = dir.getFiles().getHead();

    while (fileNode != null) {
        MyFile f = fileNode.getData();
        JsonObject fJson = new JsonObject();

        fJson.addProperty("name", f.getName());
        fJson.addProperty("size", f.getSize());
        fJson.addProperty("firstBlock", f.getFirstBlock());
        fJson.addProperty("color", f.getColor());
        fJson.addProperty("isPublic", f.isIsPublic());

        filesArray.add(fJson);
        fileNode = fileNode.getNext();
    }

    obj.add("files", filesArray);

    // ======== SUBDIRECTORIES ========
    JsonArray dirArray = new JsonArray();
    var dirNode = dir.getSubdirectories().getHead();

    while (dirNode != null) {
        Directory sub = dirNode.getData();
        dirArray.add(serializeDirectoryGson(sub));
        dirNode = dirNode.getNext();
    }

    obj.add("subdirectories", dirArray);

    return obj;
}

    
    
    public void saveState(String path) {
    try {

        Gson gson = new Gson();

        JsonObject fsJson = new JsonObject();

        // ======================
        // 1. DISCO
        // ======================
        JsonObject diskJson = new JsonObject();
        diskJson.addProperty("totalBlocks", disk.getTotalBlocks());

        // busy[]
        diskJson.add("busy", gson.toJsonTree(disk.getBusy()));

        // next pointers
        JsonArray nextArray = new JsonArray();
        for (Block b : disk.getBlocks()) {
            if (b.getNext() != null)
                nextArray.add(b.getNext().getId());
            else
                nextArray.add(-1);
        }
        diskJson.add("next", nextArray);

        fsJson.add("disk", diskJson);

        // ======================
        // 2. AUDITORÍA
        // ======================
        JsonArray logsArray = new JsonArray();
        var logNode = audit.getrLogs().getHead();
        while (logNode != null) {
            logsArray.add(logNode.getData());
            logNode = logNode.getNext();
        }
        fsJson.add("audit", new JsonObject());
        fsJson.getAsJsonObject("audit").add("logs", logsArray);

        // ======================
        // 3. DIRECTORIOS Y ARCHIVOS
        // ======================
        JsonObject rootJson = serializeDirectoryGson(this.root);
        fsJson.add("rootDirectory", rootJson);

        // ======================
        // 4. GUARDAR ARCHIVO
        // ======================
        FileWriter fw = new FileWriter(path);
        fw.write(gson.toJson(fsJson));
        fw.close();

        System.out.println("[SAVE] Sistema guardado correctamente.");

    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("[SAVE] Error guardando estado.");
    }
}




}
