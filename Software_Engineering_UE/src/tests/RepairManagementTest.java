package tests;

import ControlSystem.model.Model;
import RoadMaintenance.Job;
import RoadMaintenance.JobType;
import RoadMaintenance.RepairManagement;
import RoadMaintenance.Team;
import common.Application.ConnectorImpl;
import common.ControlSystem.SubsystemConnector;
import common.TrafficControlDetect.StreetName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RepairManagementTest {
    private final boolean testWithExecutionAwaiting = true;

    RepairManagement management;
    SubsystemConnector con;

    /**
     * Reinitializes the connector after each test
     */
    @BeforeEach
    void setup() {
        con = new ConnectorImpl();
        management = new RepairManagement(con);
    }

    /**
     * Checks if a maintenance job is created when the resources are available
     */
    @Test
    void testMaintenanceCreation() {
        Job j = management.scheduleMaintenance(StreetName.Street1);
        assertNotNull(j);
    }

    /**
     * Checks if the creation of a maintenance job fails when no more resources are available
     */
    @Test
    void testFailedMaintenanceCreation() {
        management.scheduleMaintenance(StreetName.Street1);
        management.scheduleMaintenance(StreetName.Street2);
        management.scheduleMaintenance(StreetName.Street3);
        management.scheduleMaintenance(StreetName.Street4);
        Job jFail = management.scheduleMaintenance(StreetName.Street2);
        assertNull(jFail);
    }

    /**
     * Tests if a creating a new maintenance job after executing the older is is again possible
     * if the according boolean flag is set.
     */
    @Test
    void testCreationAfterExecution() {
        Job j1 = management.scheduleMaintenance(StreetName.Street9);
        Job j2 = management.scheduleMaintenance(StreetName.Street1);
        Job j3 = management.scheduleMaintenance(StreetName.Street2);
        Job j4 = management.scheduleMaintenance(StreetName.Street3);
        Job jExec = management.scheduleMaintenance(StreetName.Street0);
        assertNull(jExec);

        if (!testWithExecutionAwaiting) return;

        management.executeJob(j1);
        try {
            Thread.sleep(2500);
            jExec = management.scheduleMaintenance(StreetName.Street0);
            assertNotNull(jExec);
        } catch (InterruptedException e) {}
    }


    /**
     * Tests the properties of a maintenance job if it can hold up with what is expected
     */
    @Test
    void testMaintenanceJob() {
        Job j = management.scheduleMaintenance(StreetName.Street0);
        assertEquals(JobType.MAINTENANCE, j.getType());
        assertNotEquals(JobType.CLEANUP, j.getType());
        assertNotEquals(JobType.EMERGENCY, j.getType());
        assertEquals(1, j.getHumanDemand());
        assertEquals(1, j.getVehicleDemand());
        assertNotNull(j.getAssignedTeam());
    }

    /**
     * Tests if all the settings placed in the team align with what is expected
     */
    @Test
    void testJobTeam() {
        Job j = management.scheduleMaintenance(StreetName.Street0);
        Team t = j.getAssignedTeam();
        assertNotNull(t);
        assertNotNull(j.getAssignedTeam());
        assertEquals(t, j.getAssignedTeam());
        assertNotNull(t.getHumans());
        assertNotNull(t.getVehicles());
        assertEquals(j.getHumanDemand(), t.getHumans().size());
        assertEquals(j.getVehicleDemand(), t.getVehicles().size());
    }

    /**
     * Tests if the returned human resources are not null
     */
    @Test
    void testGetHumanResources() {
        assertNotNull(con.getHumanResources());
    }

    /**
     * Tests if the returned vehicle resources are not null
     */
    @Test
    void testGetVehicleResources() {
        assertNotNull(con.getVehicleResources());
    }

    /**
     * Tests the closing of streets if it returns true for a certain street with the dummy connector
     */
    @Test
    void testConnectorForStreets() {
        assertTrue(con.closeStreet(StreetName.Street0));
    }
}
