/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Scheduler;

import DS.*;
import Storage.DiskRequest;
/**
 *
 * @author Gabriel Flores
 */
public interface DiskSchedulingAlgorithm {
    void reorder(Queue<DiskRequest> queue, int headPos);
}
