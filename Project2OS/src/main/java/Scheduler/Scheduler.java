/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;
import DS.Queue;
import Process.Process;
import Storage.Disk;

/**
 *
 * @author Gabriel Flores
 */
public class Scheduler {
    private Queue<Process> readyQueue;
    private SchedulingAlgorithm algorithm;
    
    public Scheduler(SchedulingAlgorithm algorithm, Queue<Process> readyQueue) {
        this.algorithm = algorithm;
        this.readyQueue = readyQueue;
    }

    // Expose the ready queue if needed
    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }

    public void reorder() {
        algorithm.reorder();
    }

    public void dispatch(Disk disk) {
        reorder();
        Process process = readyQueue.dequeue();
    }
    
    public void onTick(Disk disk) {
        if (algorithm == null) return;

        algorithm.onTick(disk);

        algorithm.reorder();

        if (disk.getBlocks() == null) {
            algorithm.dispatch(disk);
        }
    }
}
