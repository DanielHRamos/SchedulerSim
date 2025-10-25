/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import EDD.Queue;
import EDD.SimpleNode;
import OS.ClockListener;
import ProcessCreation.MemoryManager;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public class LongTermScheduler implements ClockListener {
    private Queue<PCB> newQueue;
    private Queue<PCB> readyQueue;
    private MemoryManager memoryManager;

    public LongTermScheduler(Queue<PCB> newQueue, Queue<PCB> readyQueue, MemoryManager memoryManager) {
        this.newQueue = newQueue;
        this.readyQueue = readyQueue;
        this.memoryManager = memoryManager;
    }

    @Override
    public void onTick(long cycle) {
        
        if (!newQueue.isEmpty()) {
            SimpleNode<PCB> node = newQueue.getpFirst();
            while (node != null) {
                PCB pcb = node.getData();
                newQueue.delete(pcb);

                if (memoryManager.allocate(pcb)) {
                    pcb.setStatus(PCB.Status.READY);
                    readyQueue.insert(pcb);
                    
                } else {
                    pcb.setStatus(PCB.Status.SUSPENDED_READY);
                    memoryManager.getSuspendedReady().insert(pcb);
                    
                }

                node = node.getpNext();
            }
        }
    }
}
