/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SYS;

import Audit.Audit;
import DS.LinkedList;
import Storage.Block;
import Storage.Disk;
import Process.Process;
import javax.swing.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    private String lastLoadedJsonPath;
    
    
    public void setLastLoadedJsonPath(String path) {
        this.lastLoadedJsonPath = path;
    }

    public String getLastLoadedJsonPath() {
        return this.lastLoadedJsonPath;
    }

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
                            boolean isPublic,
                            Process pOwner) {

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

        MyFile file = new MyFile(name, sizeBlocks, firstBlock, color, isPublic, pOwner);

        targetDir.addFile(file, user);

        return file;
    }
    
//    public MyFile addFile(String name, int sizeBlocks, String color, Directory targetDir, String user, boolean isPublic) {
//        // Verificar espacio en el disco
//        if (!disk.getStorage(sizeBlocks)) {
//            System.out.println("No hay espacio suficiente en el disco");
//            JOptionPane.showMessageDialog(null,
//                    "No hay espacio suficiente en el disco",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//
//        // Pedir al disco que asigne bloques encadenados
//        int firstBlock = disk.asignBlocks(sizeBlocks);
//        if (firstBlock == -1) {
//            System.out.println("Fallo en asignación de bloques");
//            JOptionPane.showMessageDialog(null,
//                    "Error al asignar bloques en el disco",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//
//        // Crear el archivo
//        MyFile file = new MyFile(name, sizeBlocks, firstBlock, color, isPublic);
//
//        // Agregar el archivo al directorio y registrar en auditoría
//        if (targetDir == null) {
//            targetDir = root;
//        }
//        targetDir.addFile(file, user);
//
//        return file;
//    }
    
    public MyFile addFile(String name, int size, String color,
            Directory targetDir, String user, boolean isPublic,
            Process pOwner) {

        if (targetDir == null) {
            targetDir = root;
        }

        if (!disk.getStorage(size)) {
            JOptionPane.showMessageDialog(null,
                    "No hay espacio suficiente en el disco",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int firstBlock = disk.asignBlocks(size);
        if (firstBlock == -1) {
            JOptionPane.showMessageDialog(null,
                    "No se pudieron asignar bloques",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        MyFile f = new MyFile(name, size, firstBlock, color, isPublic, pOwner);

        targetDir.addFile(f, user);
        audit.registerOperation(user, "Se creó el archivo " + name
                + " en " + targetDir.getRute());

        return f;
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

        // Cargar arreglo busy
        if (diskJson.has("busy")) {
            boolean[] busy = gson.fromJson(diskJson.get("busy"), boolean[].class);
            disk.setBusy(busy);
        }

        // Cargar bloques con next
        if (diskJson.has("blocks")) {
            JsonArray blocksJson = diskJson.getAsJsonArray("blocks");
            for (JsonElement b : blocksJson) {
                JsonObject blockObj = b.getAsJsonObject();
                int id = blockObj.get("id").getAsInt();
                int next = blockObj.get("next").getAsInt();
                if (next != -1) {
                    disk.getBlocks()[id].setNext(disk.getBlocks()[next]);
                }
            }
        } else if (diskJson.has("next")) {
            JsonArray nextArray = diskJson.getAsJsonArray("next");
            for (int i = 0; i < nextArray.size(); i++) {
                int next = nextArray.get(i).getAsInt();
                if (next != -1) {
                    disk.getBlocks()[i].setNext(disk.getBlocks()[next]);
                }
            }
        }

        if (diskJson.has("headPosition")) {
            disk.setHeadPosition(diskJson.get("headPosition").getAsInt());
        }

        // ============
        // 2. CARGAR AUDITORIA
        // ============
        LinkedList<String> logs = new LinkedList<>();
        if (rootJson.has("auditLogs")) {
            rootJson.getAsJsonArray("auditLogs").forEach(l -> logs.add(l.getAsString()));
        }
        Audit audit = new Audit(logs);

        // ============
        // 3. CARGAR DIRECTORIOS Y ARCHIVOS
        // ============
        JsonObject rootDirJson = rootJson.getAsJsonObject("rootDirectory");
        if (rootDirJson == null) {
            throw new RuntimeException("No se encontró 'rootDirectory' en el JSON");
        }

        Directory rootDir = loadDirectory(rootDirJson, audit, disk, null);

        // ============
        // 4. RECONSTRUIR FILESYSTEM COMPLETO
        // ============
        FileSystem fs = new FileSystem(totalBlocks, audit);
        fs.setDisk(disk);
        fs.setRoot(rootDir);
        fs.setLastLoadedJsonPath(path);
        
        return fs;

    } catch (Exception e) {
        System.err.println("Error cargando FileSystem desde: " + path);
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
    public JsonObject serializeDirectoryGson(Directory dir) {

    JsonObject obj = new JsonObject();
    obj.addProperty("name", dir.getName());

    // ======== FILES ========
    JsonArray filesArray = new JsonArray();
    if (dir.getFiles() != null) {
        for (MyFile f : dir.getFiles()) {
            JsonObject fJson = new JsonObject();
            fJson.addProperty("name", f.getName());
            fJson.addProperty("size", f.getSize());
            fJson.addProperty("firstBlock", f.getFirstBlock());
            fJson.addProperty("color", f.getColor());
            fJson.addProperty("isPublic", f.isIsPublic());
            filesArray.add(fJson);
        }
    }
    obj.add("files", filesArray);

    // ======== SUBDIRECTORIES ========
    JsonArray dirArray = new JsonArray();
    if (dir.getSubdirectories() != null) {
        for (Directory sub : dir.getSubdirectories()) {
            dirArray.add(serializeDirectoryGson(sub));
        }
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
        FileWriter fw = new FileWriter(path,false);
        fw.write(gson.toJson(fsJson));
        fw.close();
        this.lastLoadedJsonPath = path;
        System.out.println("[SAVE] Sistema guardado correctamente.");

    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("[SAVE] Error guardando estado.");
    }
}

    public void saveVersion(String path) {
        try {
            Gson gson = new Gson();

            JsonObject fsJson = serializeFullFileSystem();

            FileWriter fw = new FileWriter(path, false);
            fw.write(gson.toJson(fsJson));
            fw.close();

            System.out.println("[SAVE VERSION] Versión guardada correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private JsonObject serializeFullFileSystem() {
    Gson gson = new Gson();
    JsonObject fsJson = new JsonObject();

    // disco
    JsonObject diskJson = new JsonObject();
    diskJson.addProperty("totalBlocks", disk.getTotalBlocks());
    diskJson.add("busy", gson.toJsonTree(disk.getBusy()));

    JsonArray nextArray = new JsonArray();
    for (Block b : disk.getBlocks()) {
        nextArray.add(b.getNext() == null ? -1 : b.getNext().getId());
    }
    diskJson.add("next", nextArray);
    fsJson.add("disk", diskJson);

    // auditoría
    JsonArray logs = new JsonArray();
    var logNode = audit.getrLogs().getHead();
    while (logNode != null) {
        logs.add(logNode.getData());
        logNode = logNode.getNext();
    }
    JsonObject auditJson = new JsonObject();
    auditJson.add("logs", logs);
    fsJson.add("audit", auditJson);

    // directorios
    fsJson.add("rootDirectory", serializeDirectoryGson(root));

    return fsJson;
}
    
    public void saveStateToLastLoaded() {
    if (this.lastLoadedJsonPath == null || this.lastLoadedJsonPath.isEmpty()) {
        System.err.println("[SAVE] No hay JSON cargado para sobrescribir.");
        return;
    }
    saveState(this.lastLoadedJsonPath);
}

}
