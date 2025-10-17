/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import EDD.Queue;
import OS.CPU;
import OS.ClockListener;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public class RoundRobinScheduler implements ClockListener {
    private Queue<PCB> readyQueue;
    private CPU cpu;
    private int quantum;
    private int quantumCounter;

    public RoundRobinScheduler(Queue<PCB> readyQueue, CPU cpu, int quantum) {
        this.readyQueue = readyQueue;
        this.cpu = cpu;
        this.quantum = quantum;
        this.quantumCounter = 0;
    }

    @Override
    public void onTick(long cycle) {
        // Si no hay proceso en CPU, asignar uno
        if (cpu.isIdle() && !readyQueue.isEmpty()) {
            PCB next = readyQueue.pop();
            cpu.setRunningProcess(next);
            quantumCounter = 0;
            System.out.println("Ciclo " + cycle + " → RR asigna " + next.getName() + " al CPU");
            return;
        }

        // Si hay proceso en CPU, verificar quantum
        if (!cpu.isIdle()) {
            quantumCounter++;

            PCB current = cpu.getRunningProcess();

            // Si terminó
            if (current.getExecuted() >= current.getLength()) {
                current.setStatus(PCB.Status.TERMINATED);
                System.out.println("Ciclo " + cycle + " → Proceso terminado: " + current.getName());
                cpu.setRunningProcess(null);
                quantumCounter = 0;
            }
            // Si agotó el quantum
            else if (quantumCounter >= quantum) {
                current.setStatus(PCB.Status.READY);
                readyQueue.insert(current);
                System.out.println("Ciclo " + cycle + " → Quantum agotado, proceso " + current.getName() + " vuelve a READY");
                cpu.setRunningProcess(null);
                quantumCounter = 0;
            }
        }
    }
}
