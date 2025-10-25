/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import ProcessCreation.PCB;
/**
 *
 * @author Daniel
 */
public class MetricsManager {
    private long totalTurnaround = 0;
    private long totalResponse = 0;
    private long totalWaiting = 0;
    private int finishedCount = 0;

    private long totalCycles = 0;
    private long busyCycles = 0;

    
    public void onProcessFinished(PCB pcb, long cycle) {
        pcb.setFinishCycle(cycle);

        totalTurnaround += pcb.getTurnaround();
        totalResponse += pcb.getResponseTime();
        totalWaiting += pcb.getWaitingTime();
        finishedCount++;
    }

    
    public void onCycle(boolean cpuBusy) {
        totalCycles++;
        if (cpuBusy) busyCycles++;
    }

    public void printReport() {
        if (finishedCount == 0 || totalCycles == 0) {
            return;
        }

        double avgTurnaround = (double) totalTurnaround / finishedCount;
        double avgResponse = (double) totalResponse / finishedCount;
        double avgWaiting = (double) totalWaiting / finishedCount;

        double throughput = (double) finishedCount / totalCycles;
        double cpuUtil = (double) busyCycles / totalCycles * 100;

    }
}