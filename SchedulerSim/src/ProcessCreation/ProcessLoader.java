/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProcessCreation;

import EDD.Queue;
import GUI.SimulationGUI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Daniel
 */
public class ProcessLoader {

    private Queue<PCB> readyQueue;
    private MemoryManager memoryManager;
    private SimulationGUI gui; 

    public ProcessLoader(Queue<PCB> readyQueue, MemoryManager memoryManager, SimulationGUI gui) {
        this.readyQueue = readyQueue;
        this.memoryManager = memoryManager;
        this.gui = gui;
    }

    public void loadFromCSV(String filePath, long currentCycle) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 7) {
                    continue;
                }

                String name = parts[0];
                int length = Integer.parseInt(parts[1]);
                boolean cpuBound = Boolean.parseBoolean(parts[2]);
                int ioTrigger = Integer.parseInt(parts[3]);
                int ioService = Integer.parseInt(parts[4]);
                int memorySize = Integer.parseInt(parts[5]);
                int priority = Integer.parseInt(parts[6]);

                PCB pcb = new PCB(name, length, cpuBound, ioTrigger, ioService, memorySize);
                pcb.setArrivalCycle((int) currentCycle);
                pcb.setPriority(priority);

                
                if (memoryManager.allocate(pcb)) {
                    pcb.setStatus(PCB.Status.READY);
                    readyQueue.insert(pcb);
                    gui.logEvent("→ Proceso cargado en READY: " + name);
                } else {
                    pcb.setStatus(PCB.Status.SUSPENDED_READY);
                    gui.logEvent("→ Proceso suspendido (sin memoria): " + name);
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }
}
