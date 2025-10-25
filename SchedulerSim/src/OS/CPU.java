/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS;

import EDD.Queue;
import GUI.MetricsManager;
import ProcessCreation.MemoryManager;
import ProcessCreation.PCB;
import java.util.function.Consumer;

/**
 *
 * @author Daniel
 */
public class CPU implements ClockListener {

    private PCB runningProcess;
    private Queue<PCB> terminatedQueue;
    private Queue<PCB> readyQueue;
    private Queue<PCB> blockedQueue;
    private MemoryManager memoryManager;
    private MetricsManager metricsManager;

    
    private Consumer<String> logger;

    public CPU(Queue<PCB> terminatedQueue, Queue<PCB> readyQueue,
            Queue<PCB> blockedQueue, MemoryManager memoryManager,
            MetricsManager metricsManager) {
        this.terminatedQueue = terminatedQueue;
        this.readyQueue = readyQueue;
        this.blockedQueue = blockedQueue;
        this.memoryManager = memoryManager;
        this.metricsManager = metricsManager;
    }

    
    public void setLogger(Consumer<String> logger) {
        this.logger = logger;
    }

    
    private void log(String msg) {
        if (logger != null) {
            logger.accept(msg);
        } else {
            System.out.println(msg); 
        }
    }

    @Override
    public void onTick(long cycle) {
        if (runningProcess != null) {
            runningProcess.setPc(runningProcess.getPc() + 1);
            runningProcess.setMar(runningProcess.getMar() + 1);
            runningProcess.setExecuted(runningProcess.getExecuted() + 1);

            log("Ciclo " + cycle + " ejecutando: " + runningProcess.getName());

            
            if (runningProcess.getExecuted() >= runningProcess.getLength()) {
                runningProcess.setStatus(PCB.Status.TERMINATED);

                
                memoryManager.free(runningProcess);

              
                metricsManager.onProcessFinished(runningProcess, cycle);

                
                terminatedQueue.insert(runningProcess);

                
                memoryManager.tryReactivate(readyQueue, blockedQueue);

                log("Ciclo " + cycle + " → Proceso terminado: " + runningProcess.getName());

               
                runningProcess = null;
            }
        }
    }

    public void setRunningProcess(PCB pcb) {
        this.runningProcess = pcb;
        if (pcb != null) {
            pcb.setStatus(PCB.Status.RUNNING);
        }
    }

    public void setRunningProcess(PCB pcb, long cycle) {
        this.runningProcess = pcb;
        if (pcb != null) {
            pcb.setStatus(PCB.Status.RUNNING);
            pcb.setStartCycle(cycle);
        }
    }

    public PCB getRunningProcess() {
        return runningProcess;
    }

    public boolean isIdle() {
        return runningProcess == null;
    }
}
