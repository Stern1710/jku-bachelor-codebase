package ControlSystem;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ControlSystem.model.Model;
import RoadMaintenance.RepairManagement;
import TrafficControlDetect.ControlDevice;
import TrafficControlDetect.GUI;
import common.Application.ConnectorImpl;
import common.ControlSystem.SubsystemConnector;
import common.Participant.ParticipantApp;

public class ControlSystem {
	private static DBManager dbm = null;
	public backupSystem bs;
	public securitySystem ss;
	private SubsystemConnector connector;
	private RepairManagement repMan;
	public static void setDBConnection(DBManager dbm) {
		ControlSystem.dbm = dbm;
	}
	
	public static DBManager getDBConnection() {
		return ControlSystem.dbm;
	}
	
	// initialize backups and security scans 
	public ControlSystem(int interval) {
		bs = new backupSystem(interval);
		ss = new securitySystem(interval);
		
		startBackupRoutine();
		startIDSRoutine();
	}
	
	public void startBackupRoutine() {
		bs.start();
	}
	
	public void stopBackupRoutine() {
		bs.stop();
	}
	
	public void startIDSRoutine() {
		ss.start();
	}
		
	public void stopIDSRoutine() {
		ss.stop();
	}

	public void startSubsystems(Model model) {
		//-----------------------------
		//---SubsystemConnector Interface
		//-----------------------------
		connector = new ConnectorImpl(model);
		//-----------------------------
		//---RepairManagement
		//-----------------------------
        repMan = new RepairManagement(connector);
        //-----------------------------
		//---Participants
		//-----------------------------
	  ParticipantApp participants = new ParticipantApp();
	  new Thread(participants).start();
	  connector.setParticipantConn(participants);
	  	//-----------------------------
		//---TrafficDetection and Control
		//-----------------------------
		 try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		   
		 //GUI of the traffic det. and ctrl. system
		  GUI thegui = new GUI();
		  
		  ControlDevice trafficDetandContr = new ControlDevice(thegui,connector);

		  new Thread(trafficDetandContr).start();
	}
}
