package common.TrafficControlDetect;

import java.util.Set;

/**
 * Some subsystem need to exchange current StreetData. This Data will be represented as a StreetData Object.
 * @author Christopher Holzweber
 *
 */
public class StreetData {
	private int total;
	private int motorized;
	private int unmotorized;
	private Set<Integer> broken;

	public StreetData(int total, int motor, int unmotor, Set<Integer> brokenDevices){
		this.total = total;
		this.motorized = motor;
		this.unmotorized = unmotor;
		this.broken = brokenDevices;
	}
	
	public int getTotalParticipants(){
		return total;
	}

	public int getMotorizedParticipants() {
		return motorized;
	}

	public int getUnmotorizedParticipant() {
		return unmotorized;
	}

	Set<Integer> getBrokenDevices(){
		return broken;
	}
}
