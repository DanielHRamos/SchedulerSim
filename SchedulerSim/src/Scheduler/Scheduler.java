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
public class Scheduler implements ClockListener {

    private Queue<PCB> readyQueue;
    private CPU cpu;

    public Scheduler(Queue<PCB> readyQueue, CPU cpu) {
        this.readyQueue = readyQueue;
        this.cpu = cpu;
    }

    @Override
    public void onTick(long cycle) {
        if (cpu.isIdle() && !readyQueue.isEmpty()) {
            PCB next = readyQueue.pop();
            cpu.setRunningProcess(next);
            System.out.println("Ciclo " + cycle + " → Scheduler asigna " + next.getName() + " al CPU");
        }
    }
}
