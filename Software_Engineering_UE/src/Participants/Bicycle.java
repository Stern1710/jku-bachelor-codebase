package Participants;

import common.Participant.Roads;
import common.TrafficControlDetect.StreetName;

public class Bicycle extends Unmotorized{

	Bicycle(int id, int start, int destination){
		super(id, start,destination);
	}
	public Bicycle(int id, StreetName currStreet) {
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
		Roads[] roads = new Roads[] {Roads.INNERCITYROAD, Roads.BUSLANE,Roads.OUTERCITYROAD};
		return roads;
	}

	public boolean switchToAlternativePath() {
		return true;
	}
}
