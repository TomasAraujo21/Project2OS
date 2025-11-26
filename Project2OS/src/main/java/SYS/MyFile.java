/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SYS;

import Process.Process;

/**
 *
 * @author Gabriel Flores
 */
public class MyFile {
    private String name;
    private int size;
    private int firstBlock;
    private String color;
    private boolean isPublic;
    private Process pOwner;

    public MyFile(String name, int size, int firstBlock, String color, boolean isPublic) {
        this.name = name;
        this.size = size;
        this.firstBlock = firstBlock;
        this.color = color;
        this.isPublic = isPublic;
    }
    
    public MyFile(String name, int size, int firstBlock, String color, boolean isPublic, Process pOwner) {
        this.name = name;
        this.size = size;
        this.firstBlock = firstBlock;
        this.color = color;
        this.isPublic = isPublic;
        this.pOwner = pOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFirstBlock() {
        return firstBlock;
    }

    public void setFirstBlock(int firstBlock) {
        this.firstBlock = firstBlock;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Process getpOwner() {
        return pOwner;
    }

    public void setpOwner(Process pOwner) {
        this.pOwner = pOwner;
    }
      
    @Override
    public String toString() {
        return name;
    }
}
