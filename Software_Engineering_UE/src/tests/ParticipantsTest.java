package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.Application.ConnectorImpl;
import common.ControlSystem.SubsystemConnector;
import common.Participant.ParticipantApp;


/**
 * The aim of this TextClass is to check if the participants are put into the system as intended 
 * @author Magdalena Treml
 **/
class ParticipantsTest {
	private ParticipantApp participants;
	private int cntMotorized = 28;
	private int cntUnmotorized = 14;
	
	/**
	 * Before each test it's important that we setup the ParticipantApp
	 **/
	@BeforeEach
	public void setUp() throws Exception {
		 SubsystemConnector connector = new ConnectorImpl();
		 participants = new ParticipantApp();
		 new Thread(participants).start();
		 connector.setParticipantConn(participants);
	}
	
	/**
	 * In our current implementation of the system there are going to be 
	 * 6 Participants of every kind, in our ParticipantApp we split them in to
	 * Motorized and Unmotorized. If the size of the map equals our participants the 
	 * integration of the participants was successful. 
	 **/
	@Test
	void testMotorized() {
		 assertEquals(participants.getMotorized().size(), cntMotorized);
	}

	@Test
	void testUnmotorized() {	
		 assertEquals(participants.getUnmotorized().size(), cntUnmotorized); 
	}
}
