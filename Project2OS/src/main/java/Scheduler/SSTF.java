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
 * 
 */
public class SSTF implements SchedulingAlgorithm {
    private Queue ready;
    private Disk disk;

    public SSTF(Queue ready, Disk disk) {
        this.ready = ready;
        this.disk = disk;
    }

    public Queue getReady() {
        return ready;
    }

    public void setReady(Queue ready) {
        this.ready = ready;
    }

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    
    
    /**
     * Distancia absoluta entre cabezal y bloque solicitado.
     */
    private int seekDistance(int head, Process p) {
        return Math.abs(p.getMAR() - head);
    }
    
    /**
     * Compara dos procesos por distancia al cabezal.
     * Si empatan, elige el de menor ID.
     */
    private int compare(Process a, Process b, int head) {
        int da = seekDistance(head, a);
        int db = seekDistance(head, b);

        if (da != db) return da - db;

        return a.getID().compareTo(b.getID());
    }
    
    /**
     * Reordena la cola de Ready según SSTF.
     * El proceso más cercano al cabezal debe quedar al inicio.
     */
    public void reorder() {
        Process[] arr = ready.getAllElements();
        if (arr == null || arr.length <= 1) return;

        // Necesitamos el cabezal actual para comparar
        int head = disk.getHeadPosition();

        // insertion sort por SSTF
        for (int i = 1; i < arr.length; i++) {
            Process key = arr[i];
            int j = i - 1;

            while (j >= 0 && compare(arr[j], key, head) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        // reconstruir la cola
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
