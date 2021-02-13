package Participants;

import common.Participant.Roads;
import common.TrafficControlDetect.StreetName;

public class EmergencyVehicle extends Motorized{
	private boolean priority;
	EmergencyVehicle(int id, int start, int destination){
		super(id, start, destination);
		priority = false;
	}
	
	public EmergencyVehicle(int id, StreetName currStreet) {
		super(id, currStreet);
	}
	
	@Override
	public Roads[] getRoadRestricition() {
		Roads[] roads = new Roads[] {Roads.HIGHWAY, Roads.INNERCITYROAD, Roads.OUTERCITYROAD};
		return roads;
	}

	@Override
	public PathType getPathType() {
		if(!switchToAlternativePath()) {
			return PathType.PREDIFINED;
		} else {
			return PathType.ALTERNATIV;
		}
	}
	
	public boolean switchToAlternativePath() {
		return true;
	}
	
	public boolean prioritySwitch() {
		if(priority) {
			return !priority;
		} else {
			return priority;
		}
	}
	
	public boolean setParked(boolean parked) {
		return parked;
	}
	
	public boolean isParked() {
		if(setParked(true)) {
			return true;
		} else {
			return false;
		}
	}
}
