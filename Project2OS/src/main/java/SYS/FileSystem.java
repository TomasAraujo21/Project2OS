/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SYS;
import Audit.Audit;
import Storage.Disk;
import javax.swing.*;


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

    public MyFile createFile(Directory targetDir,
                             String name,
                             int sizeBlocks,
                             String color,
                             String user) {

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

        MyFile file = new MyFile(name, sizeBlocks, firstBlock, color);

        targetDir.addFile(file, user);

        return file;
    }
    
    public MyFile addFile(String name, int sizeBlocks, String color, Directory targetDir, String user) {
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

        // Crear el archivo lógico
        MyFile file = new MyFile(name, sizeBlocks, firstBlock, color);

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

        // 1) Liberar los bloques del disco (lista encadenada a partir de firstBlock)
        int firstBlock = file.getFirstBlock();
        if (firstBlock >= 0) {
            disk.freeBlocks(firstBlock);
        }

        // 2) Eliminar el archivo del directorio (usa tu método de Directory)
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

        // 1) Eliminar todos los archivos del directorio (liberando bloques)
        while (dir.getFirstFile() != null) {
            MyFile f = dir.getFirstFile();
            deleteFile(dir, f, user);  // ya libera bloques y registra en auditoría
        }

        // 2) Eliminar recursivamente todos los subdirectorios
        while (dir.getSubdirectories().getHead() != null) {
            Directory child = dir.getSubdirectories().getHead().getData();
            deleteDirectory(child, user);
        }

        // 3) Quitar este directorio de su padre
        Directory parent = dir.getFather();
        if (parent != null) {
            // Esto ya registra en auditoría y lo saca de la lista de subdirectorios
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

        // Usamos el Audit central del FileSystem
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
}
