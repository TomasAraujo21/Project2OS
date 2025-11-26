/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Storage;

/**
 *
 * @author Gabriel Flores
 */
public class Block {
    private int id;
    private Block next;
//    private boolean busy;

    public Block (int id){
        this.id = id;
        this.next = null;
    }
    
//    public Block(int id, int nextBlock, boolean busy) {
//        this.id = id;
//        this.next.id = nextBlock;
//        this.busy = busy;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Block getNext() {
        return next;
    }

    public void setNext(Block next) {
        this.next = next;
    }

//    public boolean isBusy() {
//        return busy;
//    }
//
//    public void setBusy(boolean busy) {
//        this.busy = busy;
//    }
//    
    
    
}
