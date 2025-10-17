/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS;

import EDD.SimpleList;
import EDD.SimpleNode;

/**
 *
 * @author Daniel
 */
public class Clock implements Runnable {
    private volatile int cycleDurationMs;        
    private volatile long globalCycle;           
    private volatile boolean running;            
    private SimpleList<ClockListener> listeners; 

    public Clock(int cycleDurationMs) {
        this.cycleDurationMs = cycleDurationMs;
        this.globalCycle = 0;
        this.running = true;
        this.listeners = new SimpleList<>();
    }

    
    public void addListener(ClockListener listener) {
        listeners.addAtTheEnd(listener);
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

    
    public void stopClock() { running = false; }
    public void setCycleDurationMs(int ms) { this.cycleDurationMs = ms; }
    public long getGlobalCycle() { return globalCycle; }
}
