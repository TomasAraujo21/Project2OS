/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;
import DS.*;
import Process.Process;
import Storage.*;
import SYS.*;

/**
 *
 * @author Gabriel Flores
 */
public class DiskScheduler {

    private final Queue<DiskRequest> requestQueue; // cola de solicitudes
    private final DiskSchedulingAlgorithm algorithm;
    private final FileSystem fileSystem;
    private int headPosition = 0;

    public DiskScheduler(FileSystem fileSystem, DiskSchedulingAlgorithm algorithm) {
        this.fileSystem = fileSystem;
        this.algorithm = algorithm;
        this.requestQueue = new Queue<>();
    }

    // Agregar solicitud de disco
    public void submit(DiskRequest req) {
        requestQueue.enqueue(req);
        System.out.println("[DISK] Nueva solicitud encolada: " + req);
    }

    // Reordenar la cola según la política
    public void reorder() {
        if (algorithm == null) return;

        algorithm.reorder(requestQueue, headPosition);
    }

    // Ejecutar la siguiente solicitud
    public void dispatch() {
        if (requestQueue.isEmpty()) {
            System.out.println("[DISK] No hay solicitudes pendientes.");
            return;
        }

        reorder(); // el algoritmo ordena

        DiskRequest req = requestQueue.dequeue();
        headPosition = req.getTargetBlock();

        System.out.println("[DISK] Ejecutando: " + req);

        switch (req.getType()) {
            case Create -> handleCreate(req);
            case Read   -> handleRead(req);
            case Update -> handleUpdate(req);
            case Delete -> handleDelete(req);
        }
    }

    private void handleCreate(DiskRequest req) {
        fileSystem.addFile(
                req.getFileName(),
                req.getFileSize(),
                req.getColor(),
                req.getDir(),
                "Proceso",
                true
        );
    }

    private void handleRead(DiskRequest req) {
        System.out.println("[READ] Archivo: " + req.getFile().getName());
    }

    private void handleUpdate(DiskRequest req) {
        MyFile f = req.getFile();
        if (req.getFileName() != null) {
            System.out.println("[UPDATE] " + f.getName() + " -> " + req.getFileName());
            f.setName(req.getFileName());
        }
    }

    private void handleDelete(DiskRequest req) {
        fileSystem.deleteFile(req.getDir(), req.getFile(), "Proceso");
    }

    public int getHeadPosition() {
        return headPosition;
    }

    public Queue<DiskRequest> getQueue() {
        return requestQueue;
    }
}