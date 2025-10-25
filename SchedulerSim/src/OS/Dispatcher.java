/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS;

import EDD.Queue;
import GUI.Scheduler;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public class Dispatcher implements ClockListener {

    private CPU cpu;
    private Queue<PCB> readyQueue;
    private Scheduler scheduler;

    public Dispatcher(CPU cpu, Queue<PCB> readyQueue, Scheduler scheduler) {
        this.cpu = cpu;
        this.readyQueue = readyQueue;
        this.scheduler = scheduler;
    }

    @Override
    public void onTick(long cycle) {
        
        if (cpu.isIdle() && !readyQueue.isEmpty()) {
            PCB next = scheduler.selectProcess(readyQueue, cycle);
            if (next != null) {
                readyQueue.delete(next);
                cpu.setRunningProcess(next, cycle);               
            }
        }
    }
}

