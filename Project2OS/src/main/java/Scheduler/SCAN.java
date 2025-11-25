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
public class SCAN implements DiskSchedulingAlgorithm {

    // Indica la dirección del cabezal: true = derecha, false = izquierda
    private boolean movingRight = true;

    // Último bloque del disco (máximo)
    private final int maxBlock;

    public SCAN(int maxBlock) {
        this.maxBlock = maxBlock;
    }

    @Override
    public void reorder(Queue<DiskRequest> queue, int headPos) {

        if (queue == null || queue.isEmpty() || queue.getSize() <= 1) return;

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
        for (DiskRequest req : arr) queue.enqueue(req);
    }

    private boolean shouldComeBefore(DiskRequest a, DiskRequest b, int headPos) {

        int ta = a.getTargetBlock();
        int tb = b.getTargetBlock();

        if (movingRight) {

            boolean aRight = ta >= headPos;
            boolean bRight = tb >= headPos;

            // Ambos están en la dirección del cabezal (derecha)
            if (aRight && bRight)
                return ta < tb;  // orden ascendente

            // Solo uno está en la dirección
            if (aRight && !bRight) return true;
            if (!aRight && bRight) return false;

            // Ambos a la izquierda (se atienden cuando regresen) → orden descendente
            return ta > tb;

        } else { // moviéndose a la izquierda

            boolean aLeft = ta <= headPos;
            boolean bLeft = tb <= headPos;

            if (aLeft && bLeft)
                return ta > tb;  // orden descendente

            if (aLeft && !bLeft) return true;
            if (!aLeft && bLeft) return false;

            // Ambos a la derecha (se atienden luego) → ascendente
            return ta < tb;
        }
    }

    // Llamar desde tu DiskScheduler cuando se despacha una solicitud
    public void updateDirection(int headPos) {
        if (headPos <= 0) movingRight = true;
        else if (headPos >= maxBlock) movingRight = false;
    }
}

