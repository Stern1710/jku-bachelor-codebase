package Participants;
import common.Participant.Roads;
import common.TrafficControlDetect.StreetName;
public class Bus extends PublicTransport{

	Bus(int id, int start, int destination, double shiftDuration){
		super(id, start, destination, shiftDuration);
	}
	
	public Bus(int id, StreetName currStreet) {
		super(id, currStreet);
	}
	
	@Override
	public Roads[] getRoadRestricition() {
		Roads[] roads = new Roads[] {Roads.BUSLANE};
		return roads;
	}
}
