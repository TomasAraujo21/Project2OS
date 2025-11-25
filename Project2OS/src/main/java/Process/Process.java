/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Process;

import java.util.concurrent.Semaphore;
import SYS.MyFile;

/**
 *
 * @author Gabriel Flores
 */
public class Process extends Thread {
    
    
    public enum OperationType {
        Create,
        Read,
        Update,
        Delete
    }
    
    public enum Status {
        Running,
        Blocked,
        Ready,
        Exit
    }
    
    private Integer ID;
    private String processName;
    private OperationType operation;
    private int diskPosition = 0;
    private MyFile targetFile;
//    private Integer cyclesToExcept;
//    private Integer cyclesToCompleteRequest;
    private Status status;


//    public Process(Integer ID, String processName, Integer instructionCount, OperationType operation, String path, Integer cyclesToExcept, Integer cyclesToCompleteRequest, Status status) {
//        this.ID = ID;
//        this.processName = processName;
//        this.instructionCount = instructionCount;
//        this.operation = operation;
//        this.path = path;
//        this.cyclesToExcept = cyclesToExcept;
//        this.cyclesToCompleteRequest = cyclesToCompleteRequest;
//        this.status = status.Ready;
//    }
    
    public Process(Integer ID, String processName, OperationType operation, int diskPosition, MyFile targetFile, Status status) {
        this.ID = ID;
        this.processName = processName;
        this.operation = operation;
        this.diskPosition = diskPosition;
        this.targetFile = targetFile;
        this.status = status.Ready;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

//    public Integer getCyclesToExcept() {
//        return cyclesToExcept;
//    }
//
//    public void setCyclesToExcept(Integer cyclesToExcept) {
//        this.cyclesToExcept = cyclesToExcept;
//    }
//
//    public Integer getCyclesToCompleteRequest() {
//        return cyclesToCompleteRequest;
//    }
//
//    public void setCyclesToCompleteRequest(Integer cyclesToCompleteRequest) {
//        this.cyclesToCompleteRequest = cyclesToCompleteRequest;
//    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }

    public int getDiskPosition() {
        return diskPosition;
    }

    public void setDiskPosition(int diskPosition) {
        this.diskPosition = diskPosition;
    }
    
    
    public void printProcessDetail(){
        System.out.println("Process ID:" + ID);
        System.out.println("Name:" + processName);
//        System.out.println("# Cycles for exception:" + cyclesToExcept);
//        System.out.println("# Cycles to complete the request:" + cyclesToCompleteRequest);
        System.out.println("Status:" + status);

    }

    @Override
    public String toString() {
        return "Process{" + "processName=" + processName + ", instructionCount=" + '}';
    }
    
    
}
