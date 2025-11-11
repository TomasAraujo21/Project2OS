/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SYS;
import Audit.Audit;
import Storage.Disk;
import DS.LinkedList;
import DS.Node;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.lang.reflect.Type;


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
    
    public void addFile(String name, int size, String color, String ruteDirectory){
        if(!disk.getStorage(size)){
            System.out.println("No hay espacio suficiente en el disco");
            JOptionPane.showMessageDialog(null, "No hay espacio suficiente en el disco", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    
}
