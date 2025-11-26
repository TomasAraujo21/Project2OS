/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import DS.Queue;
import Storage.DiskRequest;
/**
 *
 * @author Gabriel Flores
 */
public class FIFO implements DiskSchedulingAlgorithm {
    private Queue ready;

//    public FIFO(Queue ready) {
//        this.ready = ready;
//    }

    public Queue getReady() {
        return ready;
    }

    public void setReady(Queue ready) {
        this.ready = ready;
    }
    
    @Override
    public void reorder(Queue<DiskRequest> queue, int headPos) {
        // do nothing
    }

}
