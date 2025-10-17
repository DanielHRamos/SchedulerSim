/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import EDD.Queue;
import OS.IOManager;
import OS.CPU;
import OS.Clock;
import ProcessCreation.PCB;
import Scheduler.RoundRobinScheduler;
import Scheduler.Scheduler;

/**
 *
 * @author Daniel
 */
public class Main {
    public static void main(String[] args) {
        Queue<PCB> readyQueue = new Queue<>();
        Queue<PCB> blockedQueue = new Queue<>();
        CPU cpu = new CPU();
        Clock clock = new Clock(1000); // 1 segundo por ciclo
        RoundRobinScheduler scheduler = new RoundRobinScheduler(readyQueue, cpu, 3);
        IOManager ioManager = new IOManager(cpu, blockedQueue, readyQueue);

        clock.addListener(cpu);
        clock.addListener(scheduler);
        clock.addListener(ioManager);

        Thread clockThread = new Thread(clock);
        clockThread.start();

        // Proceso CPU bound
        PCB p1 = new PCB("CPU1", 8, true, 0, 0, 64);
        // Proceso I/O bound: se bloquea en ciclo 3, espera 2 ciclos
        PCB p2 = new PCB("IO1", 10, false, 3, 2, 64);

        readyQueue.insert(p1);
        readyQueue.insert(p2);

        System.out.println("Procesos cargados en READY");
    }
}