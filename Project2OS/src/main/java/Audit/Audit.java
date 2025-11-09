/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Audit;
import DS.LinkedList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 *
 * @author Gabriel Flores
 */
public class Audit {
    private LinkedList<String> logs;

    public Audit(LinkedList<String> logs) {
        this.logs = new LinkedList<>();
    }
    
    public void registerOperation(String user, String operation){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String log = String.format("[%s] - Usuario: %s - Acción: %s", timestamp, user, operation);
        logs.add(log);
        System.out.println(log);
    }
    
    public LinkedList<String> getrLogs() {
        return logs;
    }
    
}
