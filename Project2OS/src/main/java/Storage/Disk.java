/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Storage;

/**
 *
 * @author Gabriel Flores
 * 
 */
public class Disk {
    private Block[] blocks;
    private boolean[] busy;
    private int freeBlocks;
    
    //Funcion para inicializar el disco mediante un arreglo de bloques y el estado de cada uno.
    public Disk(int totalBlocks){
        this.blocks = new Block[totalBlocks];
        this.busy = new boolean[totalBlocks];
        this.freeBlocks = totalBlocks;
        
        for (int i = 0; i < totalBlocks; i++) {
            blocks[i] = new Block(i);
            busy[i] = false;
        }
    }
    
    
    public int asignBlocks(int number){
        if(number > freeBlocks){
            return -1;
        }
        int firstBlock = -1, last = -1;
        for (int i = 0; i < blocks.length && number > 0; i++) {
            if(!busy[i]){
                if (firstBlock == -1){
                    firstBlock = i;
                }
                if (last != -1){
                blocks[last].setNext(blocks[i]);
                }
                last = i;
                busy[i] = true;
                number --;
                freeBlocks --;
            }
        }
        return firstBlock;

    }
    
    
    //Esta función devuelve el número (ID) del siguiente bloque enlazado a un bloque actual.Si el bloque no existe o no tiene siguiente, devuelve -1.
    public int getNextBlock (int actualBlock){
        if (actualBlock < 0 || actualBlock >= blocks.length){
            return -1;
        }
        Block next = blocks[actualBlock].getNext();
        return (next != null) ? next.getId() : -1;
    }
    
    public void freeBlocks (int firstBlock){
        if (firstBlock < 0 || firstBlock >= blocks.length || !busy[firstBlock]){
            return;
        }
        Block actual = blocks[firstBlock];
        
        while (actual != null && busy[actual.getId()]){
            busy[actual.getId()] = false;
            freeBlocks ++;
            actual = actual.getNext();
        }
    }
    
   
}
