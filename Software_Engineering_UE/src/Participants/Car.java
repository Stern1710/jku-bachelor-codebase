package Participants;

import common.Participant.Roads;
import common.TrafficControlDetect.StreetName;

public class Car extends Motorized {

	Car(int id, int start, int destination){
		super(id, start, destination);
	}
	
	public Car(int id, StreetName currStreet) {
		super(id, currStreet);
	}
	
	@Override
	public PathType getPathType() {
		if(!switchToAlternativePath()) {
			return PathType.PREDIFINED;
		} else {
			return PathType.ALTERNATIV;
		}
	}

	@Override
	public Roads[] getRoadRestricition() {
		Roads[] roads = new Roads[] {Roads.HIGHWAY, Roads.INNERCITYROAD, Roads.OUTERCITYROAD};
		return roads;
	}

	
	public boolean switchToAlternativePath() {
		return true;
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
