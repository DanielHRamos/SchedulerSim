/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS;

import EDD.SimpleList;
import EDD.SimpleNode;
import GUI.MetricsManager;

/**
 *
 * @author Daniel
 */
public class Clock implements Runnable {

    private volatile int cycleDurationMs;
    private volatile long globalCycle;
    private volatile boolean running;
    private SimpleList<ClockListener> listeners;

    private CPU cpu;
    private MetricsManager metricsManager;

    public Clock(int cycleDurationMs, CPU cpu, MetricsManager metricsManager) {
        this.cycleDurationMs = cycleDurationMs;
        this.globalCycle = 0;
        this.running = true;
        this.listeners = new SimpleList<>();
        this.cpu = cpu;
        this.metricsManager = metricsManager;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(cycleDurationMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            globalCycle++;

            if (metricsManager != null && cpu != null) {
                metricsManager.onCycle(!cpu.isIdle());
            }

            notifyListeners();
        }
    }

    private void notifyListeners() {
        SimpleNode<ClockListener> current = listeners.getpFirst();
        while (current != null) {
            current.getData().onTick(globalCycle);
            current = current.getpNext();
        }
    }

    public void stopClock() {
        running = false;
    }

    public void setCycleDurationMs(int ms) {
        this.cycleDurationMs = ms;
    }

    public long getGlobalCycle() {
        return globalCycle;
    }

    public void tick(long cycle) {
        SimpleNode<ClockListener> node = listeners.getpFirst();
        while (node != null) {
            node.getData().onTick(cycle);
            node = node.getpNext();
        }
    }

    public void addListener(ClockListener l) {
        if (l != null) {
            listeners.addAtTheEnd(l);
        }
    }

    public void removeListener(ClockListener l) {
        if (l != null) {
            listeners.delete(l);
        }
    }
}
