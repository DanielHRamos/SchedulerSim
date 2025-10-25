/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS;

import EDD.Queue;
import EDD.SimpleNode;
import ProcessCreation.MemoryManager;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public class IOManager implements ClockListener {

    private CPU cpu;
    private Queue<PCB> blockedQueue;
    private Queue<PCB> readyQueue;
    private MemoryManager memoryManager;

    public IOManager(CPU cpu, Queue<PCB> blockedQueue, Queue<PCB> readyQueue, MemoryManager memoryManager) {
        this.cpu = cpu;
        this.blockedQueue = blockedQueue;
        this.readyQueue = readyQueue;
        this.memoryManager = memoryManager;
    }

    @Override
    public void onTick(long cycle) {
        PCB running = cpu.getRunningProcess();

       
        if (running != null && running.needsIO()) {
            cpu.setRunningProcess(null);
            running.startIO(cycle);

            if (memoryManager.allocate(running)) {
                running.setStatus(PCB.Status.BLOCKED);
                blockedQueue.insert(running);
                
            } else {
                running.setStatus(PCB.Status.SUSPENDED_BLOCKED);
                memoryManager.getSuspendedBlocked().insert(running);
                
            }

            
            memoryManager.tryReactivate(readyQueue, blockedQueue);
        }

        
        if (!blockedQueue.isEmpty()) {
            SimpleNode<PCB> node = blockedQueue.getpFirst();
            while (node != null) {
                PCB pcb = node.getData();
                if (pcb.ioCompleted(cycle)) {
                    blockedQueue.delete(pcb);

                    if (memoryManager.allocate(pcb)) {
                        pcb.setStatus(PCB.Status.READY);
                        readyQueue.insert(pcb);
                        
                    } else {
                        pcb.setStatus(PCB.Status.SUSPENDED_READY);
                        memoryManager.getSuspendedReady().insert(pcb);
                        
                    }

                    
                    memoryManager.tryReactivate(readyQueue, blockedQueue);
                }
                node = node.getpNext();
            }
        }
    }
}
