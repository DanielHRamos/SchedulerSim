/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProcessCreation;

import java.util.function.Consumer;

/**
 *
 * @author Daniel
 */
public class PCB {

    public enum Status {
        NEW,
        READY,
        RUNNING,
        BLOCKED,
        TERMINATED,
        SUSPENDED_READY,
        SUSPENDED_BLOCKED
    }

    private static int nextId = 1;
    private final int pid;
    private String name;
    private Status status;
    private int pc = 0;
    private int mar = 0;
    private int length;
    private boolean cpuBound;
    private int ioTriggerCycles;
    private int ioServiceCycles;
    private int executed = 0;
    private long arrivalTime;
    private int memorySize;
    private int ioTrigger;
    private int ioService;
    private long ioStartCycle;
    private int arrivalCycle;
    private long startCycle = -1;
    private long finishCycle;
    private boolean ioTriggered = false;
    private int priority;

    private static Consumer<String> logger;

    public static void setLogger(Consumer<String> logConsumer) {
        logger = logConsumer;
    }

    public PCB(String name, int length, boolean cpuBound, int ioTriggerCycles, int ioServiceCycles, int memorySize) {
        this.pid = nextId++;
        this.name = name;
        this.length = length;
        this.cpuBound = cpuBound;
        this.ioTriggerCycles = ioTriggerCycles;
        this.ioServiceCycles = ioServiceCycles;
        this.status = Status.NEW;
        this.arrivalTime = System.currentTimeMillis();
        this.memorySize = memorySize;
    }

    public void changeStatus(Status newStatus, long cycle, String detail) {
        Status oldStatus = this.status;
        this.status = newStatus;

        if (logger != null) {
            String msg = "Proceso " + name + " pasó de " + oldStatus + " a " + newStatus
                    + (detail != null ? " (" + detail + ")" : "")
                    + " en ciclo " + cycle;
            logger.accept(msg);
        }
    }

    public static int getNextId() {
        return nextId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStartCycle(long cycle) {
        if (this.startCycle == -1) {
            this.startCycle = cycle;
        }
    }

    public void setFinishCycle(long cycle) {
        this.finishCycle = cycle;
    }

    public long getTurnaround() {
        return finishCycle - arrivalCycle;
    }

    public long getResponseTime() {
        return startCycle - arrivalCycle;
    }

    public long getWaitingTime() {
        return getTurnaround() - executed;
    }

    public boolean isIoTriggered() {
        return ioTriggered;
    }

    public void setIoTriggered(boolean ioTriggered) {
        this.ioTriggered = ioTriggered;
    }

    public static void setNextId(int nextId) {
        PCB.nextId = nextId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getMar() {
        return mar;
    }

    public void setMar(int mar) {
        this.mar = mar;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isCpuBound() {
        return cpuBound;
    }

    public void setCpuBound(boolean cpuBound) {
        this.cpuBound = cpuBound;
    }

    public int getIoTriggerCycles() {
        return ioTriggerCycles;
    }

    public void setIoTriggerCycles(int ioTriggerCycles) {
        this.ioTriggerCycles = ioTriggerCycles;
    }

    public int getIoServiceCycles() {
        return ioServiceCycles;
    }

    public void setIoServiceCycles(int ioServiceCycles) {
        this.ioServiceCycles = ioServiceCycles;
    }

    public int getExecuted() {
        return executed;
    }

    public void setExecuted(int executed) {
        this.executed = executed;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public int getPid() {
        return pid;
    }

    public boolean needsIO() {
        if (cpuBound) {
            return false;
        }
        return executed == ioTrigger; 
    }

    public void startIO(long cycle) {
        this.ioStartCycle = cycle;
    }

    public boolean ioCompleted(long cycle) {
        if (ioStartCycle == -1) {
            return false;
        }
        return (cycle - ioStartCycle) >= ioService;
    }

    public int getArrivalCycle() {
        return arrivalCycle;
    }

    public void setArrivalCycle(int arrivalCycle) {
        this.arrivalCycle = arrivalCycle;
    }
}
