/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS;

import OS.ClockListener;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public class CPU implements ClockListener {
    private PCB runningProcess;

    @Override
    public void onTick(long cycle) {
        if (runningProcess != null) {
            runningProcess.setPc(runningProcess.getPc() + 1);
            runningProcess.setMar(runningProcess.getMar() + 1);
            runningProcess.setExecuted(runningProcess.getExecuted() + 1);

            System.out.println("Ciclo " + cycle + " ejecutando: " + runningProcess.getName());
        }
    }

    public void setRunningProcess(PCB pcb) {
        this.runningProcess = pcb;
        if (pcb != null) {
            pcb.setStatus(PCB.Status.RUNNING);
        }
    }

    public PCB getRunningProcess() {
        return runningProcess;
    }

    public boolean isIdle() {
        return runningProcess == null;
    }
}