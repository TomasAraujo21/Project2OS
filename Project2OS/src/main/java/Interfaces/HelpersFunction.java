/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;
import Scheduler.*;
import DS.Queue;
import Clock.ClockManager;
import java.util.concurrent.Semaphore;
import javax.swing.JOptionPane;

/**
 *
 * @author Gabriel Flores
 */
public class HelpersFunction {
//    /**
//     * 
//     * @param planningAlgorithm
//     * @param readyQueue
//     * @param clockManager
//     * @return Aplica el algoritmo de planificación seleccionado
//     */
//    public DiskScheduler updateSchedulerAlgorithm(String planningAlgorithm, Queue readyQueue, ClockManager clockManager, Semaphore readyLock) {
////        String selected = (String) planningAlgorithm.getSelectedItem();
//        String selected = planningAlgorithm; 
//
//        switch (selected) {
//            case "FIFO":
//                System.out.println("Algoritmo cambiado a FIFO");
//                JOptionPane.showMessageDialog(null, "✅ Configuración guardada exitosamente.");
//                return new DiskScheduler(new FIFO(readyQueue), readyQueue);
//                
//            case "SSTF":
//                System.out.println("Algoritmo cambiado a SSTF");
//                JOptionPane.showMessageDialog(null, "✅ Configuración guardada exitosamente.");
//                //return new Scheduler(new SSTF(readyQueue), readyQueue);
//
//            
//            default:
//                System.out.println("Algoritmo no reconocido.");
//                break;
//        }
//        return new DiskScheduler(new FIFO(readyQueue), readyQueue);
//    }
}
