package common.Participant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import Participants.Bicycle;
import Participants.Bus;
import Participants.Car;
import Participants.EmergencyVehicle;
import Participants.Motorized;
import Participants.Pedestrian;
import Participants.Subway;
import Participants.Unmotorized;
import common.TrafficControlDetect.StreetName;

/**
 * The ParticipantApp class will be used in other subsystems. The aim of this class is to 
 * create a fixed number of Participants in our system, divided in maps of unmotorized and motorized Participants. 
 * In the run method we use the move method we implemented in the unmotorized.java and motorized.java to create movement of participants. 
 * @author Magdalena Treml
 */
public class ParticipantApp implements Runnable{
	private volatile boolean terminate;
	private Map<Integer, Motorized> motorized;
	private Map<Integer, Unmotorized> unmotorized;
	
	public ParticipantApp() {
		motorized = new ConcurrentHashMap<>();
		unmotorized = new ConcurrentHashMap<>();
		int motorcnt = 0;
		int unmotorcnt = 0;
		for(int i = 0; i <= 6; i++) {
			motorized.put(motorcnt++, new Car(i, StreetName.Street0));
			motorized.put(motorcnt++, new EmergencyVehicle(i, StreetName.Street0));
			motorized.put(motorcnt++, new Bus(i, StreetName.Street0));
			motorized.put(motorcnt++, new Subway(i, StreetName.Street0));
			unmotorized.put(unmotorcnt++, new Pedestrian(i, StreetName.Street0));
			unmotorized.put(unmotorcnt++, new Bicycle(i, StreetName.Street0));
		}
		
	}
	
	public void terminate() {
		terminate = true;	
	}
	
	@Override
	public void run() {
		terminate = false;
		
		System.out.println("created test participant, travelling trough the streets random");
		while(!terminate) {
			for(Motorized m : motorized.values()) {
				m.move();
			}
			for(Unmotorized m : unmotorized.values()) {
				m.move();
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
		System.out.println("terminated");
	}	
	
	public Map<Integer, Motorized> getMotorized() {
		return motorized;
	}
	
	public Map<Integer, Unmotorized> getUnmotorized() {
		return unmotorized;
	}
}
