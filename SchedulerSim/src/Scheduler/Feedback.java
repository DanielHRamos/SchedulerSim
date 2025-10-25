/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import EDD.Queue;
import EDD.SimpleNode;
import OS.CPU;
import OS.ClockListener;
import ProcessCreation.PCB;
import java.util.function.Consumer;

/**
 *
 * @author Daniel
 */
public class Feedback implements ClockListener {

    private final Queue<PCB>[] queues;    
    private final CPU cpu;
    private final int[] quantums;         
    private int quantumCounter;

    private Consumer<String> logger;

   
    private final Queue<PCB> readyQueueGlobal;

    @SuppressWarnings("unchecked")
    public Feedback(CPU cpu, int levels, int[] quantums, Queue<PCB> readyQueueGlobal) {
        this.cpu = cpu;
        this.quantums = quantums;
        this.queues = new Queue[levels];
        for (int i = 0; i < levels; i++) {
            queues[i] = new Queue<>();
        }
        this.quantumCounter = 0;
        this.readyQueueGlobal = readyQueueGlobal;
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

    
    public void addProcess(PCB pcb) {
        pcb.setPriority(0);
        queues[0].insert(pcb);
        log("Proceso " + pcb.getName() + " ingresó a nivel 0");
    }

    
    public void resetQuantumCounter() {
        this.quantumCounter = 0;
    }

    @Override
    public void onTick(long cycle) {
        
        drainGlobalReadyToLevel0();

        
        if (cpu.isIdle()) {
            PCB next = getNextProcess();
            if (next != null) {
                cpu.setRunningProcess(next, cycle);
                quantumCounter = 0;
                log("Ciclo " + cycle + " → Feedback asigna " + next.getName()
                        + " al CPU (nivel " + next.getPriority() + ")");
            }
            
            return;
        }

        
        quantumCounter++;
        PCB running = cpu.getRunningProcess();

        
        if (running.getExecuted() >= running.getLength()) {
            running.setStatus(PCB.Status.TERMINATED);
            log("Ciclo " + cycle + " → Proceso terminado: " + running.getName());
            cpu.setRunningProcess(null);
            quantumCounter = 0;
            
            cleanTerminatedInternal();
            return;
        }

        
        int q = getQuantumOf(running);
        if (quantumCounter >= q) {
            int level = running.getPriority();
            if (level < queues.length - 1) {
                running.setPriority(level + 1);
            }
            queues[running.getPriority()].insert(running);
            log("Ciclo " + cycle + " → Quantum agotado, " + running.getName()
                    + " baja a nivel " + running.getPriority());
            cpu.setRunningProcess(null);
            quantumCounter = 0;
        }
    }

    
    private PCB getNextProcess() {
        for (int i = 0; i < queues.length; i++) {
            if (!queues[i].isEmpty()) {
                PCB next = queues[i].pop();
                
                next.setPriority(i);
                return next;
            }
        }
        return null;
    }

    private int getQuantumOf(PCB pcb) {
        int level = pcb.getPriority();
        
        if (level < 0) {
            level = 0;
        }
        if (level >= quantums.length) {
            level = quantums.length - 1;
        }
        return quantums[level];
    }

    
    private void drainGlobalReadyToLevel0() {
        
        SimpleNode<PCB> node = readyQueueGlobal.getpFirst();
        
        java.util.ArrayList<PCB> toMove = new java.util.ArrayList<>();
        while (node != null) {
            PCB pcb = node.getData();
            if (pcb.getStatus() == PCB.Status.READY) {
                toMove.add(pcb);
            }
            node = node.getpNext();
        }
        for (PCB pcb : toMove) {
            
            readyQueueGlobal.delete(pcb);
            pcb.setPriority(0);
            queues[0].insert(pcb);
            log("Ingreso desde READY global → " + pcb.getName() + " a nivel 0");
        }
    }

    
    private void cleanTerminatedInternal() {
        for (Queue<PCB> q : queues) {
            SimpleNode<PCB> node = q.getpFirst();
            while (node != null) {
                PCB pcb = node.getData();
                if (pcb.getStatus() == PCB.Status.TERMINATED) {
                    q.delete(pcb);
                }
                node = node.getpNext();
            }
        }
    }
}
