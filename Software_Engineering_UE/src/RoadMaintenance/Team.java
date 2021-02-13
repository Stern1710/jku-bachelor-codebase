package RoadMaintenance;

import common.RoadMaintenance.Resource;
import common.RoadMaintenance.ResourceType;

import java.util.*;

/**
 * Represents an assembled team consisting of human and vehicle resources in a certain amount determined by the job.
 * Each team has a name for better identification and an assigned job that it should complete, therefore the resources
 * are bound to this job for the time being.
 */
public class Team {
    private String teamName;
    private List<Resource> vehicles, humans;
    private Job job;

    /* -------------------
    Constructors
    ------------------- */

    /**
     * Empty constructor which sets a default name (to be not null) and initializes synchronized lists
     * to enable parallel thread access to the resources if needed
     */
    public Team () {
        this.teamName = "Default";
        vehicles = Collections.synchronizedList(new ArrayList<>());
        humans = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Does the same as the standard constructor but overwrites the name with the passed one
     * @param teamName Name for the newly formed team
     */
    public Team (String teamName) {
        this();
        this.teamName = teamName;
    }

    /**
     * Initializes the team with the empty constructor and attaches a job to it
     * @param job Job assigned to the team
     */
    public Team (Job job) {
        this();
        this.job = job;
    }

    /**
     * Initializes the team with a name and sets the job
     * @param teamName Name of the team
     * @param job Job assigned to the team
     */
    public Team (String teamName, Job job) {
        this(teamName);
        this.job = job;
    }

     /* -------------------
    Getter
    ------------------- */

    /**
     * Returns the name of the team
     * @return String with the name
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Returns an unmodifiable collection of all vehicle resources
     * @return List of generic type resource
     */
    public List<Resource> getVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    /**
     * Returns an unmodifiable collection of all human resources
     * @return List of generic type resource
     */
    public List<Resource> getHumans() {
        return Collections.unmodifiableList(humans);
    }

    /**
     * Gets the assigned job of the team
     * @return the Job object
     */
    public Job getJob() {
        return job;
    }

    /* -------------------
    Resource management
    ------------------- */

    /**
     * Assignes a new job to the team and forgets the old one
     * @param job the new job for the team
     */
    void replaceJob (Job job) {
        this.job = job;
    }

    /**
     * Removes all resources from the team and sets those available. Uses the remove functionality of the class
     */
    void dissolveTeam() {
        getHumans().forEach(h -> h.setAvailable(true));
        humans = Collections.synchronizedList(new ArrayList<>());
        getVehicles().forEach(v -> v.setAvailable(true));
        vehicles = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Adds a human resource to the team. The resource is only added if the ResourceType is okay and the passed resource
     * is also available, therefore not already associated with another team
     * @param human the human that should be a part of the team
     * @return boolean to tell if the assignment was possible or not
     */
    boolean addHumanToTeam (Resource human) {
        if (human.getType() == ResourceType.HUMAN && human.isAvailable()) {
            humans.add(human);
            human.setAvailable(false);
            return true;
        }

        return false;
    }

    /**
     * Adds a vehicle resource to the team. The resource is only added if the ResourceType is okay and the passed resource
     * is also available, therefore not already associated with another team
     * @param vehicle the vehicle that should be a part of the team
     * @return boolean to tell if the assignment was possible or not
     */
    boolean addVehicleToTeam (Resource vehicle) {
        if (vehicle.getType() == ResourceType.VEHICLE && vehicle.isAvailable()) {
            vehicles.add(vehicle);
            vehicle.setAvailable(false);
            return true;
        }
        return false;
    }

    /**
     * Removes a human resource from the team. The resource is only remove if the ResourceType matches and the passed
     * resource is not null, therefore avoiding null restraints.
     * @param human the human that should no longer be a part of the team
     * @return True or false if removing it from the team was possible (i.e removed from the list)
     * or not (not in the list / not a human)
     */
    boolean removeHumanFromTeam (Resource human) {
        if (human != null && human.getType() == ResourceType.HUMAN) {
            return humans.remove(human);
        }
        return false;
    }

    /**
     * Removes a vehicle resource from the team. The resource is only remove if the ResourceType matches and the passed
     * resource is not null, therefore avoiding null restraints.
     * @param vehicle the vehicle that should no longer be a part of the team
     * @return True or false if removing it from the team was possible (i.e removed from the list)
     * or not (not in the list / not a vehicle)
     */
    boolean removeVehicleFromTeam (Resource vehicle) {
        if (vehicle != null && vehicle.getType() == ResourceType.VEHICLE) {
            return vehicles.remove(vehicle);
        }
        return false;
    }

    /* -------------------
    Overwritten methods
    ------------------- */

    /**
     *  Builds a string with name, attached resources and the assigned job of the team
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("; Team Name: ");
        sb.append(teamName);

        sb.append("\n  Vehicle resources");
        vehicles.forEach(v -> {
            sb.append("\n    ");
            sb.append(v.toString());
        });

        sb.append("\n  Human resources");
        humans.forEach(h -> {
            sb.append("\n    ");
            sb.append(h.toString());
        });

        sb.append("  Job: ");
        sb.append(job != null ? job.toString() : "None");

        return sb.toString();
    }
}
