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
    private Queue<PCB> newQueue = new Queue<>();   // Cola de procesos recién creados
    private Queue<PCB> readyQueue = new Queue<>(); // Cola de procesos listos

    public void addNewProcess(PCB pcb) {
        pcb.setStatus(PCB.Status.NEW);
        newQueue.insert(pcb);
        System.out.println("Proceso creado: " + pcb.getName() + " con ID " + pcb.getPid());
    }

    // Método para mover de "nuevo" a "listo"
    public void admitProcess() {
        if (!newQueue.isEmpty()) {
            PCB pcb = newQueue.pop();
            pcb.setStatus(PCB.Status.READY);
            readyQueue.insert(pcb);
            System.out.println("Proceso admitido a READY: " + pcb.getName());
        }
    }

    public Queue<PCB> getNewQueue() {
        return newQueue;
    }

    public Queue<PCB> getReadyQueue() {
        return readyQueue;
    }
}