package common.TrafficControlDetect;

import java.util.HashMap;
import java.util.Map;

import TrafficControlDetect.Camera;
import TrafficControlDetect.Sensor;

/**
 * A street stores is current status, and also camera IDs and Sensors IDs.
 * So connected Cameras can be maintained over this class for each street separatly.
 * @author Christopher Holzweber
 *
 */
public class Street {
	//Static ID for all Streets
	public static int StreetID;
	public static int sensorID;
	public static int cameraID;
	private StreetName ID;
	private StreetMode curMode;
	private final Map<Integer, Sensor> sensorSet; //set which stores ID of all sensors of the street
	private final Map<Integer, Thread> sensorActiveSet; //set which stores ID of all sensors of the street
	private final Map<Integer, Camera> cameraSet;//set which stores ID of all cameras of the street
	private final Map<Integer, Thread> cameraActiveSet; //set which stores ID of all sensors of the street
	private TrafficLight lights;//set which stores ID of all traffic lights of the street
	private Warnsign warningsign; //set which stores ID of all warningsigns of the street
	
	public Street(StreetName name) {
		this.ID = name;
		curMode = StreetMode.NORMAL;
		sensorSet = new HashMap<>();
		sensorActiveSet = new HashMap<>(); //stores threads of sensors
		cameraActiveSet = new HashMap<>(); //stores threads of cameras
		cameraSet = new HashMap<>();
		lights = TrafficLight.NORMAL; //normal maintain
		warningsign = Warnsign.NOSIGN; //default no special sign
	}

	/**
	 * Return ID of the given Street
	 */
	public StreetName getStreetID() {
		return ID;
	}

	/**
	 * add a new sensors to the street list
	 * @param s - sensor to be added
	 * @param t - running thread of sensor
	 * @return true if add was possible, otherwise false
	 */
	public boolean addSensor(Sensor s,Thread t) {
		sensorSet.put(sensorID, s);
		sensorActiveSet.put(sensorID++,t);
		return true;
	}
	
	/**
	 * add a new camera to the street list
	 * @param c - camera to be added
	 * @param t - runnning thread of the camera
	 * @return true if add was possible, otherwise false
	 */
	public boolean  addCamera(Camera c, Thread t) {
		cameraSet.put(cameraID,c);
		cameraActiveSet.put(sensorID++,t);
		return true;
	}

	/**
	 * get map of all sensors installed on the street
	 * @return map of sensors
	 */
	public Map<Integer, Sensor> getSensorSet() {
		return sensorSet;
	}
	
	/**
	 * get map of all cameras installed on the street
	 * @return map of all cameras
	 */
	public Map<Integer, Camera> getCameraSet() {
		return cameraSet;
	}

	/**
	 * 
	 * @return the current Warnsign Mode
	 */
	public Warnsign getWarnsignStatus() {
		return warningsign;
	}
	/**
	 * 
	 * @return the current Street Mode
	 */
	public StreetMode getStreetMode() {
		return curMode;
	}
	/**
	 * this action will reset the StreetMode
	 * @param mode
	 */
	public boolean setStreetMode(StreetMode mode) {
		if(mode == curMode) {
			return false;
		}else {
			curMode = mode; //store new mode
			return true;
		}
	}

	/**
	 * Reset current state of the warnsigns of the street
	 * @param sign to be set
	 * @return true if set was possible, otherwise false
	 */
	public boolean setWarnsign(Warnsign sign) {
		warningsign = sign;
		return true;
	}

	/**
	 * Reset current state of the trafficlights of the street
	 * @param mode to be set
	 * @return true if set was possible, otherwise false
	 */
	public boolean setTrafficlightMode(TrafficLight mode) {
		lights = mode;
		return true;
	}

	/**
	 * 
	 * @return the current TrafficLight Mode
	 */
	public TrafficLight getTrafficlightMode() {
		return lights;
	}

}
