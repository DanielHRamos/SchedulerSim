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
public class FCFS implements ClockListener {

    private Queue<PCB> readyQueue;
    private CPU cpu;

   
    private Consumer<String> logger;

    public FCFS(Queue<PCB> readyQueue, CPU cpu) {
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
            PCB next = readyQueue.pop();
            cpu.setRunningProcess(next, cycle); // 🔹 usar versión con ciclo
            log("Ciclo " + cycle + " → FCFS asigna " + next.getName() + " al CPU");
        }
    }
}
