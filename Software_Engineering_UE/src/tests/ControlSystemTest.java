package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ControlSystem.model.Model;
import common.Application.ConnectorImpl;
import common.Participant.Roads;
import common.TrafficControlDetect.StreetMode;
import common.TrafficControlDetect.TrafficLight;
import common.TrafficControlDetect.Warnsign;


class ControlSystemTest {
	private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	static Model m;
    
	/**
     * Setup a data model before all tests
     */
	@BeforeAll
	static void setup() throws InterruptedException {
		m = new Model();
		Thread.sleep(5000); // Time for starting the server 
	}
	
	/**
     * Test insertion of a database item
     */
	@Test
	void testdbInsert() {
		int currItems = m.getDBManager().countItems();
		m.getDBManager().addItem("Electric Avenue", StreetMode.BLOCK.getValue(), TrafficLight.NORMAL.getValue(), Warnsign.NOSIGN.getValue(), Roads.INNERCITYROAD.getValue());
		assertEquals(currItems+1, m.getDBManager().countItems());
	}
	
	/**
     * Test deletion of a database item
     */
	@Test
	void testdbDelete() {
		int currItems = m.getDBManager().countItems();
		m.getDBManager().deleteItem(1);
		assertEquals(currItems-1, m.getDBManager().countItems());
	}	
	
	/**
     * Test backup functionality
     */
	@Test
	void testdbBackup() {
		try {
			m.getDBManager().backUpDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String backupdirectory = "c:/mybackups/" + todaysDate.format((java.util.Calendar.getInstance()).getTime());
		assertTrue(new File(backupdirectory).exists());
		assertTrue(new File(backupdirectory+"/demoDB").exists());
		assertTrue(new File(backupdirectory+"/demoDB/seg0").exists());
	}
	
	/**
     * Test availability of the backup thread
     */
	@Test
	void testBackupThreadAvailable() {
		// Thread already started in setup()
		System.out.println(m.cs.bs.checkState());
		assertTrue(Thread.State.TIMED_WAITING == m.cs.bs.checkState() || Thread.State.RUNNABLE == m.cs.bs.checkState());
	}
	
	/**
     * Test availability of the intrusion detection thread
     */
	@Test
	void testIDSThreadAvailable() {
		// Thread already started in setup()
		System.out.println(m.cs.ss.checkState());
		assertTrue(Thread.State.TIMED_WAITING == m.cs.ss.checkState() || Thread.State.RUNNABLE == m.cs.ss.checkState());
	}

	/**
     * test data processing result
     */
	@Test
	void testDataProcessing() {
		System.setOut(new PrintStream(outputStreamCaptor));
		String expected;
		ConnectorImpl ci = new ConnectorImpl(m);
		ci.processData(0);
		ci.processData(1);
		ci.processData(2);
		ci.processData(3);
		ci.processData(4);
		expected = "Fastest path calculated\r\n" + "Alternative path calculated\r\n" + "Shortest Path calculated\r\n"
				+ "Statistics calculated\r\n" + "Traffic prediction calculated";
		assertEquals(expected, outputStreamCaptor.toString().trim());
	    System.setOut(standardOut);
	}
}
