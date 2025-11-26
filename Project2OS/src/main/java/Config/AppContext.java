/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;
import DS.*;
import Audit.Audit;
import Scheduler.*;
import SYS.FileSystem;

/**
 *
 * @author tomasaraujo
 */
public class AppContext {
    public static final LinkedList<String> LOG_LIST = new LinkedList<>();
    public static final Audit AUDIT = new Audit(LOG_LIST);
    public static final FileSystem FILE_SYSTEM = new FileSystem(100, AUDIT);
    public static final DiskScheduler DISK_SCHEDULER = new DiskScheduler(FILE_SYSTEM, new FIFO());

    private AppContext() {
        
    }
    
}
