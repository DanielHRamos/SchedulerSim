/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProcessCreation;

import EDD.Queue;

/**
 *
 * @author Daniel
 */
public class ProcessManager {
    private Queue<PCB> newQueue = new Queue<>();   
    private Queue<PCB> readyQueue = new Queue<>(); 

    public void addNewProcess(PCB pcb) {
        pcb.setStatus(PCB.Status.NEW);
        newQueue.insert(pcb);
        
    }

    
    public void admitProcess() {
        if (!newQueue.isEmpty()) {
            PCB pcb = newQueue.pop();
            pcb.setStatus(PCB.Status.READY);
            readyQueue.insert(pcb);
            
        }
    }

    public Queue<PCB> getNewQueue() {
        return newQueue;
    }

    public Queue<PCB> getReadyQueue() {
        return readyQueue;
    }   
}