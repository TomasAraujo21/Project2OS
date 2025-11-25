/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;
import Storage.DiskRequest;
import DS.Queue;

/**
 *
 * @author Gabriel Flores
 */
public class CSCAN implements DiskSchedulingAlgorithm {

    private final int maxBlock;  // último bloque del disco (totalBlocks - 1)

    public CSCAN(int maxBlock) {
        this.maxBlock = maxBlock;
    }

    @Override
    public void reorder(Queue<DiskRequest> queue, int headPos) {

        if (queue == null || queue.isEmpty() || queue.getSize() <= 1) {
            return;
        }

        Object[] objs = queue.getAllElements();

        DiskRequest[] arr = new DiskRequest[objs.length];
        for (int i = 0; i < objs.length; i++) {
            arr[i] = (DiskRequest) objs[i];
        }

        for (int i = 1; i < arr.length; i++) {
            DiskRequest key = arr[i];
            int j = i - 1;

            while (j >= 0 && shouldComeBefore(key, arr[j], headPos)) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }

        // reconstruir la cola
        queue.clear();
        for (DiskRequest req : arr) {
            queue.enqueue(req);
        }
    }

    private boolean shouldComeBefore(DiskRequest a, DiskRequest b, int headPos) {

        int ta = a.getTargetBlock();
        int tb = b.getTargetBlock();

        boolean aAhead = ta >= headPos;
        boolean bAhead = tb >= headPos;

        // 1) Ambos están por delante del cabezal → ordenar ascendente normal
        if (aAhead && bAhead)
            return ta < tb;

        // 2) Solo uno está por delante → ese va primero
        if (aAhead && !bAhead) return true;
        if (!aAhead && bAhead) return false;

        // 3) Ambos están detrás → ordenar ascendente también,
        //    pero estos van al final del recorrido (tras el salto circular)
        return ta < tb;
    }
}

