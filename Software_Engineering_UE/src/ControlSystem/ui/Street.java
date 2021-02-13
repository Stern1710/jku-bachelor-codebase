package ControlSystem.ui;

import common.Participant.Roads;
import common.TrafficControlDetect.*;


public class Street {
	private final String name;
	private final StreetMode mode;
	private final TrafficLight light;
	private final Warnsign sign;
	private final Roads road;
		
	public Street(String name, StreetMode mode, TrafficLight light, Warnsign sign, Roads road) {
		super();
		this.name = name;
		this.mode = mode;
		this.light = light;
		this.sign = sign;
		this.road = road;
	}
	

	public String getName() {
		return name;
	}
	public StreetMode getMode() {
		return mode;
	}
	public TrafficLight getLight() {
		return light;
	}
	public Warnsign getWarnsign() {
		return sign;
	}
	public Roads getRoad() {
		return road;
	}
}