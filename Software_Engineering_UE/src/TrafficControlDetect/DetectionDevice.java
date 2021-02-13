package TrafficControlDetect;
/**
 * Abstract Definition of an Dection Device like cameras and sensors
 * @author Christopher Holzweber
 */
public abstract class DetectionDevice {

	private int id = -1; //starting ID of a new device, gets reset later on
	
	private boolean avail = true; //available flag
	
	/**
	 * Reset the ID of this detection Device
	 * @param ID
	 */
	public void setID(int ID) {
		this.id = ID;
	}

	/**
	 * reset the available flag
	 */
	public void setAvilable() {
		avail = !avail;
	}
	/**
	 * 
	 * @return a boolean value, if the device got defined.
	 */
	public boolean getAvailable() {
		return id>0; //if we never got an id, we cant exist
	}
	public int getID() {
		return id;
	}
	public abstract DetectionType getType();
}
