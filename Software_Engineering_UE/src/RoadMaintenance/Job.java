package RoadMaintenance;

import common.ControlSystem.SubsystemConnector;
import common.TrafficControlDetect.StreetName;

/**
 * Jobs are the core element created by the RepairManagement. They are runnable and therefore can and will be executed
 * inside a executor once they are ready for this (Emergency and Cleanup tasks immediately, maintenance some point later).
 * They hold all relevant information about the task as well as a connection to the SubsystemConnector form whom they use
 * certain methods to close streets and provide feedback about the street status (repaired or not).
 * @author Sternbauer
 */
public class Job implements Runnable {
    private final SubsystemConnector connector;
    private final JobType type;
    private final int vehicleDemand, humanDemand;
    private final StreetName place;
    private Team assignedTeam;

    /**
     * Initializes the Job with all its relevant information
     * @param connector The SubsystemConnector where certain actions can be requested
     * @param type The type of job to be performed (relevant for rescheduling tasks)
     * @param place Street where a job needs to be performed
     * @param vehicleDemand The amount of vehicles needed
     * @param humanDemand The amount of human workers needed
     */
    public Job (SubsystemConnector connector, JobType type, StreetName place, int vehicleDemand, int humanDemand) {
        this.connector = connector;
        this.type = type;
        this.vehicleDemand = vehicleDemand;
        this.humanDemand = humanDemand;
        this.place = place;
    }

     /* -------------------
    Getter
    ------------------- */

    /**
     * Gets the type of job to be performed
     * @return
     */
    public JobType getType() {
        return type;
    }

    /**
     * Number of vehicle needed to perform the task
     * @return Integer with the amount
     */
    public int getVehicleDemand() {
        return vehicleDemand;
    }

    /**
     * Number of human workers needed to perform the task
     * @return Integer with the amount
     */
    public int getHumanDemand() {
        return humanDemand;
    }

    /**
     * Gets the street where actions need to be performed
     * @return StreetName enum
     */
    public StreetName getPlace() {
        return place;
    }

    /**
     * Gets the assigned team for the task
     * @return Team object with the team
     */
    public Team getAssignedTeam() {
        return assignedTeam;
    }

    /**
     * Sets the team assigned for this task
     * @param assignedTeam team which will perform this task
     */
    public void setAssignedTeam(Team assignedTeam) {
        this.assignedTeam = assignedTeam;
    }

    /* -------------------
    provided methods
    ------------------- */

    /**
     * Executes the task with closing of the street, performing the action and returning a signal over the connector.
     * After a task, the street will be opened again and the team be dissolved.
     */
    @Override
    public void run() {
        connector.closeStreet(place);
        //Delay here for some time;
        try {
            Thread.sleep(place.repairTime);
        } catch (InterruptedException e) {
            connector.jobCompleted(place, false);
            connector.openStreet(place);
            return;
        }

        connector.jobCompleted(place, true);
        connector.openStreet(place);
        assignedTeam.dissolveTeam();
        assignedTeam = null;
    }


    /* -------------------
    Overwritten methods
    ------------------- */

    /**
     * Concatenates all information about the job into a single string over multiple lines
     * @return String with information about the job
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Job Type: ");
        sb.append(type);
        sb.append("; Vehicle demand: ");
        sb.append(vehicleDemand);
        sb.append("; Human demand: ");
        sb.append(humanDemand);
        sb.append("; Place: ");
        sb.append(place);

        return sb.toString();
    }

}
