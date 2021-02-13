package ControlSystem;
import java.lang.Thread.State;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class backupSystem implements Runnable {
	private Thread backupthread;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private int interval;

	
	public backupSystem(int sleepInterval) {
        interval = sleepInterval;
    }

	public void start() {
		backupthread = new Thread(this);
		backupthread.start();
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
            System.out.println("Starting Backup");
            try {
				ControlSystem.getDBConnection().backUpDatabase();
			} catch (SQLException e) {
				e.printStackTrace();
			}
         } 
    } 
	
	public State checkState() {
		return backupthread.getState();
	}
	
	  

}
