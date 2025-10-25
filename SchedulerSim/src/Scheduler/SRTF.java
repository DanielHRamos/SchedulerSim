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
public class SRTF implements ClockListener {
    private Queue<PCB> readyQueue;
    private CPU cpu;

    
    private Consumer<String> logger;

    public SRTF(Queue<PCB> readyQueue, CPU cpu) {
        this.readyQueue = readyQueue;
        this.cpu = cpu;
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
        PCB running = cpu.getRunningProcess();

        if (running == null && !readyQueue.isEmpty()) {
            assignShortest(cycle);
        } else if (running != null && !readyQueue.isEmpty()) {
            PCB shortest = findShortestRemaining();
            int remainingRunning = running.getLength() - running.getExecuted();
            int remainingShortest = shortest.getLength() - shortest.getExecuted();

            if (remainingShortest < remainingRunning) {
                // Preempt
                running.setStatus(PCB.Status.READY);
                readyQueue.insert(running);
                cpu.setRunningProcess(null);
                log("Ciclo " + cycle + " → SRTF desaloja " + running.getName() +
                    " y asigna " + shortest.getName() + " al CPU");
                assignShortest(cycle);
            }
        }
    }

    private void assignShortest(long cycle) {
        PCB shortest = findShortestRemaining();
        readyQueue.delete(shortest);
        cpu.setRunningProcess(shortest, cycle); 
        log("Ciclo " + cycle + " → SRTF asigna " + shortest.getName() + " al CPU");
    }

    private PCB findShortestRemaining() {
        SimpleNode<PCB> current = readyQueue.getpFirst();
        PCB shortest = current.getData();
        while (current != null) {
            int remaining = current.getData().getLength() - current.getData().getExecuted();
            int shortestRemaining = shortest.getLength() - shortest.getExecuted();
            if (remaining < shortestRemaining) {
                shortest = current.getData();
            }
            current = current.getpNext();
        }
        return shortest;
    }
    
}
