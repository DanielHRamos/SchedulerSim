/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import EDD.Queue;
import GUI.MetricsManager;
import GUI.SimulationGUI;
import OS.CPU;
import OS.Clock;
import OS.IOManager;
import ProcessCreation.MemoryManager;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public class Main {

    public static void main(String[] args) {
        
        Queue<PCB> newQueue = new Queue<>();        
        Queue<PCB> readyQueue = new Queue<>();     
        Queue<PCB> blockedQueue = new Queue<>();    
        Queue<PCB> terminatedQueue = new Queue<>(); 

        
        MemoryManager memoryManager = new MemoryManager(10000); 
        MetricsManager metricsManager = new MetricsManager();

        
        CPU cpu = new CPU(terminatedQueue, readyQueue, blockedQueue, memoryManager, metricsManager);

        
        Clock clock = new Clock(1000, cpu, metricsManager);

        
        SimulationGUI gui = new SimulationGUI(clock, cpu, readyQueue, blockedQueue, terminatedQueue, memoryManager, newQueue);
        gui.setVisible(true);

        
        clock.addListener(gui);

        
        IOManager ioManager = new IOManager(cpu, blockedQueue, readyQueue, memoryManager);
        clock.addListener(cpu);
        clock.addListener(ioManager);
        
        Thread clockThread = new Thread(clock);
        clockThread.start();
    }
}