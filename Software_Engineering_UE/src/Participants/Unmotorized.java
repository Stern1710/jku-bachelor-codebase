package Participants;

import java.util.Random;

import common.TrafficControlDetect.StreetName;

/**
 * The abstract class Unmotorized implements the mehthods that are the same
 * in every unmotorized Participant. 
 * 
 * Note: Every method is only explained once, so if one can't seem to find a description look in a super class 
 * @author Magdalena Treml
 */

public abstract class Unmotorized implements Participant{
	private final int id;
	private final int start;
	private final int destination;
	private volatile StreetName currStreet;
	private Random random;
	
	 /**
     * Constructor of a unmotorized Participant with a start and end point
     * @param a id to ensure the uniqueness of each Participant
     * @param an integer start, which tells us where on the map he start from
     * @param an integer start, which tells us where on the map he wants to go
     */
	Unmotorized(int id, int start, int destination){
		this.id = id;
		this.start = start;
		this.destination = destination;
		random = new Random();
	}
	
	 /**
     * Constructor of a unmotorized Participant which tells us on which street it currently is
     * @param a id to ensure the uniqueness of each Participant
     * @param an StreetName currStreet which tells us where it is right now
     */
	public Unmotorized(int id, StreetName currStreet) {
		this.id = id;
		start = 0;
		destination = 0;
		this.currStreet = currStreet;
		random = new Random();
	}
	
	/**
     * No unmotorized Participant is has the permission to park.
     */
	public boolean parkingAuthorization() {
		return false;
	}
	
	/**
	 * Getter for the id of a Participant
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter for the current Road
	 * @return current Road
	 */
	public StreetName getCurrStreet() {
		return currStreet;
	}
	
	
	@Override
	public void move() {
		int pick = random.nextInt(StreetName.values().length);
	    currStreet = StreetName.values()[pick];	
	}
	
	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getDestination() {
		return destination;
	}

}
