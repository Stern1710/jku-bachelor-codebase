package Participants;

import common.Participant.Roads;

/**
 * Every Participant is built on an hierachical concept. The interface Participant predefines methods every Participant class has to implement.
 * For better understanding i will present the hierachical concept:
 * Interface Participant -> Abstract Motorized.java, Abstract Unmotorized.java
 * Abstract Motorized -> Car.java, EmergencyVehicle.java, Abstract PublicTransport.java
 * Abstract Public Transport -> Subway.java, Bus.java
 * Abstract Unmotorized -> Pedestrian.java, Bycycle.java
 * 
 * Note: Every method is only explained once, so if one can't seem to find a description look in a super class 
 * @author Magdalena Treml
 */

public interface Participant {

	/**
	 * The PathType is defined in the Enum PathType.
	 * @return the Type of Path the Participant is allowed to take
	 */
	PathType getPathType();
	
	/**
	 * The Roads are defined in the Enum Roads in the common.Participant package.
	 * @return array of allowed Roads the Participant can drive on
	 */
	Roads[] getRoadRestricition();
	
	/**
	 * Getter of the Start point of a Participant
	 * @return start
	 */
	int getStart();
	
	/**
	 * Getter of the End point of a Participant
	 * @return destination
	 */
	int getDestination();
	
	/**
	 * Method that makes the Participant move randomly to 
	 * another street
	 */
	void move();
	
	/**
	 * Getter for the speed limit of a Road
	 * @return speed limit
	 */
	default int getRoadSpeedLimit(Roads road) {
		return road.getValue();
	}	
}
