/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProcessCreation;

import EDD.Queue;
import EDD.SimpleNode;

/**
 *
 * @author Daniel
 */
public class MemoryManager {

    private int totalMemory;
    private int usedMemory;

    private Queue<PCB> suspendedReady = new Queue<>();
    private Queue<PCB> suspendedBlocked = new Queue<>();

    public MemoryManager(int totalMemory) {
        this.totalMemory = totalMemory;
        this.usedMemory = 0;
    }

    public boolean allocate(PCB pcb) {
        if (pcb.getMemorySize() + usedMemory <= totalMemory) {
            usedMemory += pcb.getMemorySize();
            return true;
        } else {
            suspend(pcb);
            return false;
        }
    }

    public void free(PCB pcb) {
        usedMemory -= pcb.getMemorySize();
        if (usedMemory < 0) {
            usedMemory = 0;
        }
    }

    private void suspend(PCB pcb) {
        if (pcb.getStatus() == PCB.Status.READY) {
            pcb.setStatus(PCB.Status.SUSPENDED_READY);
            suspendedReady.insert(pcb);
            
        } else if (pcb.getStatus() == PCB.Status.BLOCKED) {
            pcb.setStatus(PCB.Status.SUSPENDED_BLOCKED);
            suspendedBlocked.insert(pcb);
            
        }
    }

    public void tryReactivate(Queue<PCB> readyQueue, Queue<PCB> blockedQueue) {
        
        reactivateFrom(suspendedReady, PCB.Status.READY, readyQueue);

        reactivateFrom(suspendedBlocked, PCB.Status.BLOCKED, blockedQueue);
    }

    private void reactivateFrom(Queue<PCB> source, PCB.Status targetStatus, Queue<PCB> destination) {
        SimpleNode<PCB> node = source.getpFirst();
        while (node != null) {
            PCB pcb = node.getData();
            if (pcb.getMemorySize() + usedMemory <= totalMemory) {
                source.delete(pcb);
                pcb.setStatus(targetStatus);
                destination.insert(pcb);
                usedMemory += pcb.getMemorySize();
                
            }
            node = node.getpNext();
        }
    }

    public int getUsedMemory() {
        return usedMemory;
    }

    public int getTotalMemory() {
        return totalMemory;
    }

    public Queue<PCB> getSuspendedReady() {
        return suspendedReady;
    }

    public Queue<PCB> getSuspendedBlocked() {
        return suspendedBlocked;
    }

}
