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
 * 
 */
public class SSTF implements DiskSchedulingAlgorithm {

    public SSTF() {
        // No requiere parámetros adicionales
    }

    @Override
    public void reorder(Queue<DiskRequest> queue, int headPos) {

        if (queue == null || queue.isEmpty() || queue.getSize() <= 1)
            return;

        DiskRequest[] arr = queue.getAllElements();

        // Insertion Sort basado en la distancia mínima al cabezal
        for (int i = 1; i < arr.length; i++) {

            DiskRequest key = arr[i];
            int j = i - 1;

            while (j >= 0 && distance(key, headPos) < distance(arr[j], headPos)) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        // reconstruir la cola
        queue.clear();
        for (DiskRequest req : arr) queue.enqueue(req);
    }

    private int distance(DiskRequest req, int headPos) {
        return Math.abs(req.getTargetBlock() - headPos);
    }
}

