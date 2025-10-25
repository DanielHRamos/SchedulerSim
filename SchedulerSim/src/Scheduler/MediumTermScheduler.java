/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import EDD.Queue;
import OS.ClockListener;
import ProcessCreation.MemoryManager;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public class MediumTermScheduler implements ClockListener {
    private MemoryManager memoryManager;
    private Queue<PCB> readyQueue;
    private Queue<PCB> blockedQueue;

    public MediumTermScheduler(MemoryManager memoryManager, Queue<PCB> readyQueue, Queue<PCB> blockedQueue) {
        this.memoryManager = memoryManager;
        this.readyQueue = readyQueue;
        this.blockedQueue = blockedQueue;
    }

    @Override
    public void onTick(long cycle) {
  
        memoryManager.tryReactivate(readyQueue, blockedQueue);
    }
}
