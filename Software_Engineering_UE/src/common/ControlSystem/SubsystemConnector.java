package common.ControlSystem;

import common.Participant.ParticipantApp;
import common.Participant.Roads;
import common.RoadMaintenance.Resource;
import common.TrafficControlDetect.StreetName;

import java.util.List;
import java.util.Map;
import java.util.Set;

import Participants.Motorized;
import Participants.Unmotorized;

/**
 * This interface defines all methods which the ControlSystem has to provide for the subsystems
 * in order to make request to the ControlSystem possible.
 * @author Sternbauer
 */
public interface SubsystemConnector {

    /**
     * Loads all resources of the type human from the data system
     * @return List of all resources that are human
     */
    List<Resource> getHumanResources();

    /**
     * Loads all resources of the type vehicle from the data system
     * @return List of all resources that are vehicles
     */
    List<Resource> getVehicleResources();

    /**
     * Opens a street, passed by the enum StreetName, for traffic
     * @param street street to be opened up
     */
    void openStreet (StreetName street);

    /**
     * Closses a street, passed by the enum StreetName, for traffic
     * @param street street to be closed down
     */
    boolean closeStreet(StreetName street);

    /**
     * Tells whether service for a street was completed or not
     * @param street The street where a task is being performed
     * @param completed True/false if the job could be completed
     */
    void jobCompleted (StreetName street, boolean completed);

	public void insertStreet(StreetName name, Roads roadtype);
	
	public void deleteStreet(StreetName name);
	
	public void processData(int type);
	
	public void crashDetection(StreetName name);
	
	public Map<Integer, Motorized> getMotorizedParticipants();
	
	public Map<Integer, Unmotorized> getUnmotorizedParticipants();
	
	public void addStreetTrafficDetection();
	public void setParticipantConn(ParticipantApp app);
	
}
