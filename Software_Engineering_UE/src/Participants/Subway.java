package Participants;

import common.Participant.Roads;
import common.TrafficControlDetect.StreetName;

public class Subway extends PublicTransport{
	Subway(int id, int start, int destination, double shiftDuration){
		super(id, start,destination, shiftDuration);
	}
	
	public Subway(int id, StreetName currStreet) {
		super(id, currStreet);
	}
	
	@Override
	public Roads[] getRoadRestricition() {
		Roads[] roads = new Roads[] {Roads.SUBWAYLANE};
		return roads;
	}
}
