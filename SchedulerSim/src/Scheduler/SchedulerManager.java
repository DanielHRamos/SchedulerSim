/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Scheduler;

import EDD.Queue;
import EDD.SimpleNode;
import OS.CPU;
import OS.Clock;
import OS.ClockListener;
import ProcessCreation.PCB;
import java.util.function.Consumer;

/**
 *
 * @author Daniel
 */
public class SchedulerManager {

    private Clock clock;
    private CPU cpu;
    private Queue<PCB> readyQueue;
    private ClockListener currentScheduler;
    private Queue<PCB> blockedQueue;
    private Consumer<String> logger;

    public SchedulerManager(Clock clock, CPU cpu, Queue<PCB> readyQueue) {
        this.readyQueue = readyQueue;
        this.blockedQueue = blockedQueue;
        this.cpu = cpu;
        this.clock = clock;
    }

    public void setLogger(Consumer<String> logger) {
        this.logger = logger;
    }

    private void setScheduler(ClockListener scheduler) {
       
        if (currentScheduler != null) {
            clock.removeListener(currentScheduler);
        }

        
        cleanTerminated(readyQueue);
        cleanTerminated(blockedQueue);

        
        PCB running = cpu.getRunningProcess();
        if (running != null) {
            if (running.getStatus() == PCB.Status.TERMINATED) {
                cpu.setRunningProcess(null);
            } else {
                running.setStatus(PCB.Status.READY);
                readyQueue.insert(running);
                cpu.setRunningProcess(null);
            }
        }

        if (logger != null) {
            try {
                scheduler.getClass().getMethod("setLogger", Consumer.class)
                        .invoke(scheduler, logger);
            } catch (Exception ignored) {
            }
        }

        
        currentScheduler = scheduler;
        clock.addListener(currentScheduler);

        
        if (scheduler instanceof RoundRobin rr) {
            rr.resetQuantumCounter();
        }
        if (scheduler instanceof Feedback fb) {
            fb.resetQuantumCounter();
        }
    }

    private void cleanTerminated(Queue<PCB> queue) {
        if (queue == null) {
            return; // 🔹 evita NPE
        }
        SimpleNode<PCB> node = queue.getpFirst();
        while (node != null) {
            PCB pcb = node.getData();
            if (pcb.getStatus() == PCB.Status.TERMINATED) {
                queue.delete(pcb);
            }
            node = node.getpNext();
        }
    }

    public void useFCFS() {
        FCFS fcfs = new FCFS(readyQueue, cpu);
        if (logger != null) {
            fcfs.setLogger(logger);
        }
        setScheduler(fcfs);
    }

    public void useSJF() {
        SJF sjf = new SJF(readyQueue, cpu);
        if (logger != null) {
            sjf.setLogger(logger);
        }
        setScheduler(sjf);
    }

    public void useSRTF() {
        SRTF srtf = new SRTF(readyQueue, cpu);
        if (logger != null) {
            srtf.setLogger(logger);
        }
        setScheduler(srtf);
    }

    public void useRoundRobin(int quantum) {
        RoundRobin rr = new RoundRobin(readyQueue, cpu, quantum);
        if (logger != null) {
            rr.setLogger(logger);
        }
        setScheduler(rr);
    }

    public void useHRRN() {
        HRRN hrrn = new HRRN(readyQueue, cpu);
        if (logger != null) {
            hrrn.setLogger(logger);
        }
        setScheduler(hrrn);
    }

    public void useFeedback(int[] quantums) {
        Feedback fb = new Feedback(cpu, quantums.length, quantums, readyQueue);
        if (logger != null) {
            fb.setLogger(logger);
        }
        setScheduler(fb);
    }
}
