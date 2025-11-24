/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;
import Process.Process;
import DS.Queue;
import Storage.Disk;
/**
 *
 * @author Gabriel Flores
 */
public class FIFO implements SchedulingAlgorithm {
    private Queue ready;

    public FIFO(Queue ready) {
        this.ready = ready;
    }

    public Queue getReady() {
        return ready;
    }

    public void setReady(Queue ready) {
        this.ready = ready;
    }
    
    /**
     * Comparación FIFO: primero por tiempo de llegada,
     * si empatan, por ID.
     */
    private int compare(Process a, Process b) {
        int ta = a.getArrivaltime();
        int tb = b.getArrivaltime();
        if (ta != tb) return ta - tb;
        return a.getID().compareTo(b.getID());
    }
    
    /**
     * Reordena la cola de Ready usando FIFO
     * (básicamente: ordenar por arrivalTime).
     */
    public void reorder() {
        Process[] arr = ready.getAllElements();
        if (arr == null || arr.length <= 1) return;

        // insertion sort por (arrivalTime, ID)
        for (int i = 1; i < arr.length; i++) {
            Process key = arr[i];
            int j = i - 1;

            while (j >= 0 && compare(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }
        ready.clear();
        for (int k = 0; k < arr.length; k++) {
            ready.enqueue(arr[k]);
        }
    
    }
    
    @Override
    public void onTick(Disk disk) {
        // do nothing
    }
    
    @Override
    public void dispatch(Disk disk) {
        // do nothing
    }
}
