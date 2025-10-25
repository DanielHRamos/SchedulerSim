/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Scheduler;

import EDD.Queue;
import OS.CPU;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public interface SchedulingPolicy {
    PCB selectProcess(Queue<PCB> readyQueue, CPU cpu);
}
