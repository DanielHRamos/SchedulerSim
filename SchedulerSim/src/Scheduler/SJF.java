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
public class SJF implements ClockListener {
    private Queue<PCB> readyQueue;
    private CPU cpu;

    
    private Consumer<String> logger;

    public SJF(Queue<PCB> readyQueue, CPU cpu) {
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
           
            SimpleNode<PCB> current = readyQueue.getpFirst();
            PCB shortest = current.getData();
            while (current != null) {
                if (current.getData().getLength() < shortest.getLength()) {
                    shortest = current.getData();
                }
                current = current.getpNext();
            }

            
            readyQueue.delete(shortest);
            cpu.setRunningProcess(shortest, cycle);

          
            log("Ciclo " + cycle + " → SJF asigna " + shortest.getName() + " al CPU");
        }
    }
}