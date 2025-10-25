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
public class HRRN implements ClockListener {

    private Queue<PCB> readyQueue;
    private CPU cpu;

    
    private Consumer<String> logger;

    public HRRN(Queue<PCB> readyQueue, CPU cpu) {
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
        if (cpu.isIdle() && !readyQueue.isEmpty()) {
            PCB best = findBestProcess(cycle);
            readyQueue.delete(best);
            cpu.setRunningProcess(best, cycle); 
            log("Ciclo " + cycle + " → HRRN asigna " + best.getName() + " al CPU (ratio=" 
                + String.format("%.2f", responseRatio(best, cycle)) + ")");
        }
    }

    private PCB findBestProcess(long cycle) {
        SimpleNode<PCB> current = readyQueue.getpFirst();
        PCB best = current.getData();
        double bestRatio = responseRatio(best, cycle);

        while (current != null) {
            PCB candidate = current.getData();
            double ratio = responseRatio(candidate, cycle);
            if (ratio > bestRatio) {
                best = candidate;
                bestRatio = ratio;
            }
            current = current.getpNext();
        }
        return best;
    }

    private double responseRatio(PCB pcb, long currentCycle) {
        int waitingTime = (int) (currentCycle - pcb.getArrivalCycle());
        int serviceTime = pcb.getLength();
        return (double) (waitingTime + serviceTime) / serviceTime;
    }
}