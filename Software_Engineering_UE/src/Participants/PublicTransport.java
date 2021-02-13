package Participants;

import common.TrafficControlDetect.StreetName;

abstract class PublicTransport extends Motorized{
	private double shiftDuration;
	
	PublicTransport(int id, int start, int destination, double shiftDuration) {
		super(id, start, destination);
		this.shiftDuration = shiftDuration;
	}
	
	public PublicTransport(int id, StreetName currStreet) {
		super(id, currStreet);
	}

	public double getShiftDuration() {
		return shiftDuration;
	}
	
	@Override
	public PathType getPathType() {
		return PathType.FIXED;
	}
	
	@Override
	public boolean parkingAuthorization() {
		return false;
	}
	
	public boolean shiftisActive() {
		while(shiftDuration > 0) {
			shiftDuration = shiftDuration-0.5;
		}
		return false;
	}
}
