/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package GUI;

import EDD.Queue;
import ProcessCreation.PCB;

/**
 *
 * @author Daniel
 */
public interface Scheduler {
    PCB selectProcess(Queue<PCB> readyQueue, long cycle);
}
