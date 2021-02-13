package ControlSystem;

import java.lang.Thread.State;
import java.util.concurrent.atomic.AtomicBoolean;

public class securitySystem implements Runnable{

	private Thread idsthread;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private int interval;

	
	public securitySystem(int sleepInterval) {
        interval = sleepInterval;
    }

	public void start() {
		idsthread = new Thread(this);
		idsthread.start();
	}

	public void stop() {
		running.set(false);
	}

	public void run() { 
        running.set(true);
        while (running.get()) {
            try { 
                Thread.sleep(interval); 
            } catch (InterruptedException e){ 
                Thread.currentThread().interrupt();
                System.out.println(
                  "Thread was interrupted, Failed to complete operation");
            }
            System.out.println("IDS Running - no intruders found");
         } 
    } 
	
	public State checkState() {
		return idsthread.getState();
	}
}
