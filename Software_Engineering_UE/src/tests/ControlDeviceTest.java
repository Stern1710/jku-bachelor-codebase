package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import TrafficControlDetect.ControlDevice;
import common.Application.ConnectorImpl;
import common.ControlSystem.SubsystemConnector;
import common.Participant.ParticipantApp;
import common.TrafficControlDetect.StreetData;
import common.TrafficControlDetect.StreetMode;
import common.TrafficControlDetect.StreetName;
import common.TrafficControlDetect.TrafficLight;
import common.TrafficControlDetect.Warnsign;

/**
 * 
 * @author Christopher Holzweber
 * This TestClass tests the core class of the Traffic Detection and Control Subsystem
 * At init Time this cases need to wait 8000 milisec. before starting the testcases, because 
 * the testing server needs some time to connect all clients and get first data packages.
 **/
class ControlDeviceTest {
	private static ControlDevice trafficDetandContr;
	@BeforeAll
	static void init() {
		//-----------------------------
		//---SubsystemConnector Interface
		//-----------------------------
		SubsystemConnector connector = new ConnectorImpl();
        
		//-----------------------------
		//---Participants
		//-----------------------------
	  ParticipantApp participants = new ParticipantApp();
	  new Thread(participants).start();
	  connector.setParticipantConn(participants);
		//-----------------------------
		//---TrafficDetection and Control
		//-----------------------------
	   trafficDetandContr = new ControlDevice(null,connector);
	   System.out.println("Starting Server - wait 5 seconds");
	   new Thread(trafficDetandContr).start();
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This testcase checks if the whole city has moving participants, so non of the total integer fields should be zero
	 */
	@Test
	void testGetCityData() {
		int checkMotor = 0;
		int checkUnotor = 0;
		int checkTotal = 0;
		//iterate over whole city
		for(StreetData datapoint : trafficDetandContr.getCityData()) {
			checkMotor += datapoint.getMotorizedParticipants();
			checkUnotor += datapoint.getUnmotorizedParticipant();
			checkTotal += datapoint.getTotalParticipants();
		}
		//check if fields are set
		assertNotEquals(-1, checkMotor);
		assertNotEquals(-1, checkUnotor);
		assertNotEquals(-1, checkTotal);
	}
	
	/**
	 * Test method for {@link TrafficControlDetect.ControlDevice#getStreetData(common.TrafficControlDetect.StreetName)}.
	 * Test if you get the correct StreetData of a certain Street. 
	 */
	@Test
	void testGetStreetData() {
		assertNotEquals(-1, trafficDetandContr.getStreetData(StreetName.Street0).getUnmotorizedParticipant());
	}

	/**
	 * Test method for {@link TrafficControlDetect.ControlDevice#setStreetMode(common.TrafficControlDetect.StreetName, common.TrafficControlDetect.StreetMode)}.
	 * This Method is testing the method, if it is possible to reset the Mode of a street and then checks if this
	 * manipulation is consistent.
	 */
	@Test
	void testSetStreetMode() {
		/**
		 * Set all StreetMode of all init Streets to BLOCK
		 */
		for(StreetName name : StreetName.values()) {
			assertTrue(trafficDetandContr.setStreetMode(name, StreetMode.BLOCK));
		}
		/**
		 * Now check if last set was stored correctly
		 */
		for(StreetName name : StreetName.values()) {
			assertEquals(StreetMode.BLOCK,trafficDetandContr.getStreetMode(name));
		}
	}

	/**
	 * Test method for {@link TrafficControlDetect.ControlDevice#getStreetMode(common.TrafficControlDetect.StreetName)}.
	 * This method test the given method, if returns the correct Mode and also if a change happens,
	 *  if the updated Mode is returned.
	 */
	@Test
	void testGetStreetMode() {
		/*
		 * After Initialization
		 */
		for(StreetName name : StreetName.values()) {
			assertEquals(StreetMode.NORMAL,trafficDetandContr.getStreetMode(name));
		}
		
		/*
		 * Now change the StreetMode of a Street
		 */
		trafficDetandContr.setStreetMode(StreetName.Street0, StreetMode.BLOCK);
		assertEquals(StreetMode.BLOCK,trafficDetandContr.getStreetMode(StreetName.Street0));

		/*
		 * Now change the StreetMode of a Street
		 */
		trafficDetandContr.setStreetMode(StreetName.Street0, StreetMode.STANDBY);
		assertEquals(StreetMode.STANDBY,trafficDetandContr.getStreetMode(StreetName.Street0));
	}

	/**
	 * Test method for {@link TrafficControlDetect.ControlDevice#setWarnSigns(common.TrafficControlDetect.StreetName, common.TrafficControlDetect.Warnsign)}.
	 * This Method is testing the method, if it is possible to reset the Mode of a street and then checks if this
	 * manipulation is consistent.
	 */
	@Test
	void testSetWarnSigns() {
		/**
		 * Set all WarningsSigns of all init Streets to WET
		 */
		for(StreetName name : StreetName.values()) {
			assertTrue(trafficDetandContr.setWarnSigns(name, Warnsign.WET));
		}
		/**
		 * Now check if last set was stored correctly
		 */
		for(StreetName name : StreetName.values()) {
			assertEquals(Warnsign.WET,trafficDetandContr.getStreetSignMode(name));
		}
	}

	/**
	 * Test method for {@link TrafficControlDetect.ControlDevice#getStreetSignMode(common.TrafficControlDetect.StreetName)}.
	 * This method test the given method, if returns the correct Mode and also if a change happens,
	 *  if the updated Mode is returned.
	 */
	@Test
	void testGetStreetSignMode() {
		/*
		 * After Initialization
		 */
		for(StreetName name : StreetName.values()) {
			assertEquals(Warnsign.NOSIGN,trafficDetandContr.getStreetSignMode(name));
		}
		
		/*
		 * Now change the WarningSign of a Street
		 */
		trafficDetandContr.setWarnSigns(StreetName.Street0, Warnsign.NOSIGN);
		assertEquals(Warnsign.NOSIGN,trafficDetandContr.getStreetSignMode(StreetName.Street0));

		/*
		 * Now change the WarningSign of a Street
		 */
		trafficDetandContr.setWarnSigns(StreetName.Street0, Warnsign.WET);
		assertEquals(Warnsign.WET,trafficDetandContr.getStreetSignMode(StreetName.Street0));
		
	}

	/**
	 * Test method for {@link TrafficControlDetect.ControlDevice#setStreetTrafficLight(common.TrafficControlDetect.StreetName, common.TrafficControlDetect.TrafficLight)}.
	 * This Method is testing the method, if it is possible to reset the Mode of a street and then checks if this
	 * manipulation is consistent.
	 */
	@Test
	void testSetStreetTrafficLight() {
		/**
		 * Set all TrafficLights of all init Streets to RED
		 */
		for(StreetName name : StreetName.values()) {
			assertTrue(trafficDetandContr.setStreetTrafficLight(name, TrafficLight.RED));
		}
		/**
		 * Now check if set was stored correctly
		 */
		for(StreetName name : StreetName.values()) {
			assertEquals(TrafficLight.RED,trafficDetandContr.getStreetTrafficLightMode(name));
		}
	}

	/**
	 * Test method for {@link TrafficControlDetect.ControlDevice#getStreetTrafficLightMode(common.TrafficControlDetect.StreetName)}.
	 * This method test the given method, if returns the correct Mode and also if a change happens,
	 *  if the updated Mode is returned.
	 */
	@Test
	void testGetStreetTrafficLightMode() {
		/*
		 * After Initialization
		 */
		for(StreetName name : StreetName.values()) {
			assertEquals(TrafficLight.NORMAL,trafficDetandContr.getStreetTrafficLightMode(name));
		}
		
		/*
		 * Now change a TrafficLightMode to Mode RED
		 */
		trafficDetandContr.setStreetTrafficLight(StreetName.Street0, TrafficLight.RED);
		assertEquals(TrafficLight.RED,trafficDetandContr.getStreetTrafficLightMode(StreetName.Street0));
		/*
		 * Now change a TrafficLightMode to Mode WARNING
		 */
		trafficDetandContr.setStreetTrafficLight(StreetName.Street0, TrafficLight.WARNING);
		assertEquals(TrafficLight.WARNING,trafficDetandContr.getStreetTrafficLightMode(StreetName.Street0));
		
		
	}

}
