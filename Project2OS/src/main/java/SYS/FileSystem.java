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
        this.root = new Directory ("Raiz", audit,null);
        this.disk = new Disk(sizeDisk);
        this.audit = audit;
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
    
}
