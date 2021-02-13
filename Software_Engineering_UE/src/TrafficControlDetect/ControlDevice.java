package TrafficControlDetect;

import common.ControlSystem.SubsystemConnector;
import common.Participant.Roads;
import common.TrafficControlDetect.*;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * This is the core class of the Traffic Detection and Control Subsystems. Other subsystems 
 * connect to this class to get data or set new actions.
 * @author Christopher Holzweber, 11803108
 *
 */
public  class ControlDevice implements Runnable{
	private final int NRSENSORS = 15;
	private final int NRCAMERAS = 4;
	private final boolean showOutput = true;
	private boolean terminate = false;
	private DetectionNetwork detNetwork;
	private Map<StreetName, Street> streets;
	private  Thread tDetNetwork;
	private GUI gui;
	private SubsystemConnector connector;

	public ControlDevice(GUI gui,SubsystemConnector subconn) {
		this.connector = subconn;
		this.gui = gui;
		streets = new HashMap<>();
		
		//create thread for random street crashes
		new Thread(()-> {
			Random rand = new Random();
			Random rand2 = new Random();
			while(true) {
				int x = rand.nextInt(5000);
				if(x >50 && x < 100) {
					int x2 = rand2.nextInt(StreetName.values().length); //print random street crash
					subconn.crashDetection(StreetName.values()[x2]);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public void terminate() {
		terminate = true;
	}
	@Override
	public void run(){
		
		//start DetNet Server
		startDetNet();
		System.out.println("Started Detection Network Server");
		
		//Create streets in this subsystem
		for (StreetName street : StreetName.values()) { 
			connector.insertStreet(street,Roads.INNERCITYROAD);
			createStreet(street);
		}
	
		//Add streets to detection Network
		for (StreetName street : StreetName.values()) { 
			detNetwork.addStreet(street);
		}
		
		//Set up sensors and Cameras and Actors of street
		for (StreetName street : StreetName.values()) { 
			setUpStreet(street);
		}
		
		while(!terminate) {
			if(showOutput) {
				for (StreetName street : StreetName.values()) { 
					if(gui != null) {
						gui.setData(street,printDataOfStreet(street));
					}
				}
			}
			//wait some time and then print current status again
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private String printDataOfStreet(StreetName street) {
		StreetData data = detNetwork.getCurrentStreetData(street);
		StringBuilder sb = new StringBuilder();
		sb.append("ControlDevice got on "+ street +":");
		sb.append(" motor:" +data.getMotorizedParticipants());
		sb.append(" unmotor:" +data.getUnmotorizedParticipant());
		sb.append(" total:" +data.getTotalParticipants());
		sb.append(" participants at today at: ");
		sb.append(new SimpleDateFormat("hh:mm:ss").format(new Date()));
		return sb.toString();
	}
	
	/**
	 * Create Sensors/Cameras and Actors of the street
	 * @param street
	 */
	private void setUpStreet(StreetName street) {
		//create sensors
		for(int i = 0; i<NRSENSORS; i++) {
			addSensor(street);
		}
		//create cameras
		for(int i = 0; i<NRCAMERAS; i++) {
			addCamera(street);
		}		
		//create traffic lights
		//create digital warnsigns
	}
	
	/**
	 * Adds a Camera to the wanted Street
	 * @param name - id of the street
	 */
	private void addCamera(StreetName name) {
		Street tempStr = streets.get(name);
		Camera cam = new Camera(tempStr,connector);
		
		Thread t = new Thread(()->{
			try {
				cam.start(); //start new camera client
			} catch (ClassNotFoundException | IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});
		tempStr.addCamera(cam, t); //add camera to street intern list
		t.start();
	}
	
	/**
	 * Adds a Sensor to the wanted Street
	 * @param name - id of the street
	 */
	private void addSensor(StreetName name) {
		Street tempStr = streets.get(name);
		Sensor sens = new Sensor(tempStr,connector);
		
		Thread t = new Thread(()->{
			try {
				sens.start(); //start new sensor client
			} catch (ClassNotFoundException | IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});
		tempStr.addSensor(sens,t);//add sensor to street intern list
		t.start(); //while t is alive, sensor is active
	}
	/**
	 * Starts server of the detection network
	 */
	private void startDetNet() {
		detNetwork  = new DetectionNetwork();
		tDetNetwork = new Thread(() ->{
			detNetwork.start();
		});
		tDetNetwork.start();
	}
	/**
	 * Creates a new street instance and stores it in list
	 * @param name
	 */
	private void createStreet(StreetName name){
		Street s  = new Street(name);
		streets.put(name, s);
	}
	
	//-------------------------------------PUBLIC SECTION---------------------------------------//
	/**
	 * Get data of a particular street
	 * @param street
	 * @return
	 */
	public StreetData getStreetData(StreetName street) {
		return detNetwork.getCurrentStreetData(street);
	}
	
	/**
	 * Get current Dataset of all streets
	 * @return
	 */
	public Set<StreetData> getCityData(){
		Set<StreetData> data = new HashSet<>();
		for(StreetName street : StreetName.values()) {
			data.add(getStreetData(street));
		}
		return data;
	}
	
	/**
	 * Set status of a part. street
	 * @param street
	 * @param mode
	 * @return
	 */
	public boolean setStreetMode(StreetName street, StreetMode mode) {
		if(gui != null) {
			gui.setData(street,"Set new street Mode: "+mode.toString());
		}
		return streets.get(street).setStreetMode(mode);
	}
	
	/**
	 * Return current mode of the searches street
	 * @param street
	 * @return
	 */
	public StreetMode getStreetMode(StreetName street) {
		return streets.get(street).getStreetMode();
	}
	
	public boolean setWarnSigns(StreetName street, Warnsign sign) {
		if(gui != null) {
			gui.setData(street,"Set new warnsign Mode: "+sign.toString());
		}
		return  streets.get(street).setWarnsign(sign);
	}
	
	/**
	 * Return current warnsign status of the searches street
	 * @param street
	 * @return
	 */
	public Warnsign getStreetSignMode(StreetName street) {
		return streets.get(street).getWarnsignStatus();
	}
	
	/**
	 * Set status of traffic lights
	 * @param street
	 * @param mode
	 * @return
	 */
	public boolean setStreetTrafficLight(StreetName street, TrafficLight mode) {
		if(gui != null) {
			gui.setData(street,"Set new trafficlight Mode: "+mode.toString());
		}
		return streets.get(street).setTrafficlightMode(mode);
	}
	
	/**
	 * Return current mode of the traffic lights of the searchedstreet
	 * @param street
	 * @return
	 */
	public TrafficLight getStreetTrafficLightMode(StreetName street) {
		return streets.get(street).getTrafficlightMode();
	}
}
