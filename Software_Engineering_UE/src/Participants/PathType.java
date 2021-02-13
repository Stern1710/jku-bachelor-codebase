package Participants;

/**
 * The aim of this enum is to have the different kind of Paths we can assign to Participants. 
 * Whereas the EmergencyCar Path can only b e assigned to Emergency Vehicles, the FIXED PathType 
 * can be assigned to Busses and Subways. The Path finding on every other Participant works a bit different
 * Here we always set a Predifined Path, but if we come across e.g. road block on the way
 *  we switch to an alternative Path.
 * @author Treml
 */

public enum PathType {
	FIXED,
	PREDIFINED,
	ALTERNATIV,
	EMERGENCYCAR
}
