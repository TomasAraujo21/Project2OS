/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Storage;

import SYS.*;
import Process.Process;

/**
 *
 * @author tomasaraujo
 */
public class DiskRequest {
    
    public enum Type {
        Create,
        Read,
        Update,
        Delete
    }

    private Process process;
    private Type type;
    private String fileName;
    private int fileSize;
    private String color;
    private Directory dir;
    private MyFile file;
    private int targetBlock; // posición del cabezal objetivo

    // Para create:
    public DiskRequest(Process process, String fileName, int size, String color, Directory dir, int targetBlock) {
        this.process = process;
        this.type = Type.Create;
        this.fileName = fileName;
        this.fileSize = size;
        this.color = color;
        this.dir = dir;
        this.targetBlock = targetBlock;
    }

    // Para operations With files (read/update/delete):
    public DiskRequest(Process process, Type type, MyFile file, Directory dir, int targetBlock){
        this.process = process;
        this.type = type;
        this.file = file;
        this.dir = dir;
        this.targetBlock = targetBlock;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Directory getDir() {
        return dir;
    }

    public void setDir(Directory dir) {
        this.dir = dir;
    }

    public MyFile getFile() {
        return file;
    }

    public void setFile(MyFile file) {
        this.file = file;
    }

    public int getTargetBlock() {
        return targetBlock;
    }

    public void setTargetBlock(int targetBlock) {
        this.targetBlock = targetBlock;
    }


}
