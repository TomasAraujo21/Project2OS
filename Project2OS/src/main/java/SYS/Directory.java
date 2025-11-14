/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SYS;
import DS.LinkedList;
import Audit.Audit;
import Storage.Disk;

/**
 *
 * @author Gabriel Flores
 */
public class Directory {
    private String name;
    private LinkedList<MyFile> files;
    private LinkedList<Directory> subdirectories;
    private Disk refDisk; 
    private Audit refAudit;
    private transient Directory father;

    public Directory(String name, Audit refAudit, Directory father) {
        this.name = name;
        this.files = new LinkedList<>();
        this.subdirectories = new LinkedList<>();
        this.refAudit = refAudit;
        this.father = father;
    }
    
    public void addFile(MyFile file,String user){
        files.add(file);
        System.out.println("Archivo creado:" + file.getName());
        refAudit.registerOperation(user, "SE CREO EL ARCHIVO" + file.getName() + "en el directorio" + this.name);
    }
    
    public void addDirectory(Directory directory){
        subdirectories.add(directory);
        System.out.println("Directorio creado:" + directory.getName());
        refAudit.registerOperation("Usuario", "Directorio creado:" + directory.getName());
    }

    public boolean deleteFile(MyFile file, String user){
        System.out.println("Aechivo" + file.getName() + "eliminado del directorio" + this.name);
        refAudit.registerOperation(user, "Se elimino el archivo" + file.getName() + "del directorio" + this.name);
        return files.remove(file);
    }
    
    public MyFile getFirstFile(){
        if (files == null || files.getHead() == null){
            return null;
        }
        return files.getHead().getData();
    }
    
    public boolean deleteDirectory(Directory directory){
        if (directory == null){
            return false;
        }
        while (directory.getFirstFile() != null){
            String user = System.getProperty("user.name");
            directory.deleteFile(directory.getFirstFile(), user);
        }
        refAudit.registerOperation("Usuario", "Directorio eliminado" + directory.getName());
        return subdirectories.remove(directory);
    }
    
    public String getRute (){
        if (this.father == null){
            return "/" + name;
        }
        return father.getRute() + "/" + name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<MyFile> getFiles() {
        return files;
    }

    public void setFiles(LinkedList<MyFile> files) {
        this.files = files;
    }

    public LinkedList<Directory> getSubdirectories() {
        return subdirectories;
    }

    public void setSubdirectories(LinkedList<Directory> subdirectories) {
        this.subdirectories = subdirectories;
    }

    public Disk getRefDisk() {
        return refDisk;
    }

    public void setRefDisk(Disk refDisk) {
        this.refDisk = refDisk;
    }

    public Audit getRefAudit() {
        return refAudit;
    }

    public void setRefAudit(Audit refAudit) {
        this.refAudit = refAudit;
    }

    public Directory getFather() {
        return father;
    }

    public void setFather(Directory father) {
        this.father = father;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
    
}




