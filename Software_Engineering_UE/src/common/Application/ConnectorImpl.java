package common.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ControlSystem.DBManager;
import ControlSystem.dataProcessing;
import ControlSystem.model.Item;
import ControlSystem.model.Model;
import ControlSystem.ui.Street;
import Participants.Motorized;
import Participants.Unmotorized;
import common.ControlSystem.SubsystemConnector;
import common.Participant.ParticipantApp;
import common.Participant.Roads;
import common.RoadMaintenance.Resource;
import common.RoadMaintenance.ResourceType;
import common.TrafficControlDetect.StreetMode;
import common.TrafficControlDetect.StreetName;
import common.TrafficControlDetect.TrafficLight;
import common.TrafficControlDetect.Warnsign;

/**
 *  The ConnectorImpl defines the API used by all subsystems for communication with each other
 * @author Christopher Holzweber
 *
 */
public class ConnectorImpl implements SubsystemConnector{
	private DBManager dbm;
	private Model model;
	private ParticipantApp participant; 
	public ConnectorImpl(Model model) {
		this.dbm = model.getDBManager();
		this.model = model;
	}
	
	public ConnectorImpl() {

	}

	@Override
    public List<Resource> getHumanResources() {
        List<Resource> humans = new ArrayList<>();
        for (int i=0; i < 10; i++) {
            humans.add(new Resource("Worker " + i, ResourceType.HUMAN));
        }
        return humans;
    }

    @Override
    public List<Resource> getVehicleResources() {
        List<Resource> vehicles = new ArrayList<>();
        for (int i=0; i < 5; i++) {
            vehicles.add(new Resource("Worker " + i, ResourceType.VEHICLE));
        }
        return vehicles;
    }

    @Override
    public void openStreet(StreetName street) {
        //Do nothing important as not relevant atm
        System.out.println("Open street " + street.name());
    }

    @Override
    public boolean closeStreet(StreetName street) {
        //Do nothing important as not relevant atm
        System.out.println("Close street " + street.name());
        return true;
    }

    @Override
    public void jobCompleted (StreetName street, boolean completed) {
        System.out.println("Job for street " + street + " was completed: " + completed);
    }

	@Override
	public void insertStreet(StreetName name, Roads roadtype) {
		if(dbm != null) {
			int x = dbm.addItem(name.toString(), StreetMode.NORMAL.getValue(), TrafficLight.NORMAL.getValue(), Warnsign.NOSIGN.getValue(), roadtype.getValue());
			Item item = new Item(x,new Street(name.toString(), StreetMode.NORMAL, TrafficLight.NORMAL, Warnsign.NOSIGN, roadtype));
			model.invoiceItems.add(item);
		}
	}

	@Override
	public void deleteStreet(StreetName name) {
		dbm.deleteItem(name.toString());
	}

	@Override
	public void processData(int type) {
		switch (type){
		case 0: dataProcessing.calculateFastestPath();break;
		case 1: dataProcessing.calculatePathAvoidance();break;
		case 2: dataProcessing.calculateShortestPath();;break;
		case 3: dataProcessing.calculateStatistics();break;
		case 4: dataProcessing.trafficPrediction();break;
		default: System.out.println("Invalid operation");
		}
	}

	@Override
	public void crashDetection(StreetName name) {
		System.out.println("Crash detected on Street "+name.toString());
	}

	@Override
	public Map<Integer, Motorized> getMotorizedParticipants() {
		return participant.getMotorized();
	}

	@Override
	public Map<Integer, Unmotorized> getUnmotorizedParticipants() {
		return participant.getUnmotorized();
	}

	@Override
	public void addStreetTrafficDetection() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setParticipantConn(ParticipantApp app) {
		participant = app;
	}
}
