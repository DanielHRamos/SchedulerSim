/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS;

import EDD.Queue;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public class IOManager implements ClockListener {

    private CPU cpu;
    private Queue<PCB> blockedQueue;
    private Queue<PCB> readyQueue;

    public IOManager(CPU cpu, Queue<PCB> blockedQueue, Queue<PCB> readyQueue) {
        this.cpu = cpu;
        this.blockedQueue = blockedQueue;
        this.readyQueue = readyQueue;
    }

    @Override
    public void onTick(long cycle) {
        PCB current = cpu.getRunningProcess();

        if (current != null && !current.isCpuBound()) {
            // Si el proceso alcanzó el ciclo de disparo de I/O
            if (current.getExecuted() == current.getIoTriggerCycles() && !current.isIoTriggered()) {
                current.setIoTriggered(true); // marcar que ya se bloqueó
                current.setStatus(PCB.Status.BLOCKED);
                blockedQueue.insert(current);
                cpu.setRunningProcess(null);

                System.out.println("Ciclo " + cycle + " → Proceso " + current.getName() + " se bloquea por I/O");

                new Thread(() -> {
                    try {
                        Thread.sleep(current.getIoServiceCycles() * 1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                    current.setStatus(PCB.Status.READY);
                    readyQueue.insert(current);
                    blockedQueue.pop();

                    System.out.println("→ Proceso " + current.getName() + " regresa a READY tras I/O");
                }).start();
            }
        }
    }
}
