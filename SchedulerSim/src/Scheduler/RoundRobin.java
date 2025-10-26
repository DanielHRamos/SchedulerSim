/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import EDD.Queue;
import OS.CPU;
import OS.ClockListener;
import ProcessCreation.PCB;
import java.util.function.Consumer;

/**
 *
 * @author Daniel
 */
public class RoundRobin implements ClockListener {

    private Queue<PCB> readyQueue;
    private Queue<PCB> blockedQueue;
    private CPU cpu;
    private int quantum;
    private int quantumCounter;

    private Consumer<String> logger;

    public RoundRobin(Queue<PCB> readyQueue, CPU cpu, int quantum) {
        this.readyQueue = readyQueue;
        this.blockedQueue = blockedQueue;
        this.cpu = cpu;
        this.quantum = quantum;
        this.quantumCounter = 0;
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
        
        if (cpu.isIdle() && !readyQueue.isEmpty()) {
            PCB next = readyQueue.pop();
            cpu.setRunningProcess(next, cycle);
            quantumCounter = 0;
            log("Ciclo " + cycle + " → asigna " + next.getName() + " al CPU");
            return;
        }

        
        if (!cpu.isIdle()) {
            quantumCounter++;
            PCB current = cpu.getRunningProcess();

            
            if (current.getExecuted() >= current.getLength()) {
                current.setStatus(PCB.Status.TERMINATED);
                log("Ciclo " + cycle + " → Proceso terminado: " + current.getName());
                cpu.setRunningProcess(null);
                quantumCounter = 0;
            } 
            else if (current.needsIO(cycle)) {   
                current.setStatus(PCB.Status.BLOCKED);
                blockedQueue.insert(current);
                log("Ciclo " + cycle + " → " + current.getName() + " pasa a BLOQUEADO");
                cpu.setRunningProcess(null);
                quantumCounter = 0;
                
            } 
            else if (quantumCounter >= quantum) {
                current.setStatus(PCB.Status.READY);
                readyQueue.insert(current);
                log("Ciclo " + cycle + " → Quantum agotado, proceso " + current.getName() + " vuelve a READY");
                cpu.setRunningProcess(null);
                quantumCounter = 0;
            }
        }
    }

    public void resetQuantumCounter() {
        this.quantumCounter = 0;
    }

}
