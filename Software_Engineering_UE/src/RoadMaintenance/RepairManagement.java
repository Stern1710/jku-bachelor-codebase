package RoadMaintenance;

import common.RoadMaintenance.Resource;
import common.TrafficControlDetect.StreetName;
import common.ControlSystem.SubsystemConnector;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main class with the provided API-methods to the outside for the control system to access
 * Holds lists of all resources, the connection to the ControlSystem and other relevant values.
 * @author Sternbauer
 */
public class RepairManagement {
    //Connection to the ControlSystem for requests
    private final SubsystemConnector connector;

    private List<Resource> vehicles, humans; //Lists of resources separate into vehicles and humans

    private ExecutorService executor = Executors.newCachedThreadPool(); //ThreadPool to execute Jobs in

    private Object resourceLock;

    private final int spareVehicleCount = 1; //Spares
    private final int sparePersonalCount = 2;

    /**
     * Constructor to generate to RepairManagement module. Needs a ready-to-use SubsystemConnector for loading
     * resources into the subsystem
     * @param connector Connection to the COntrolSystem for requesting data
     */
    public RepairManagement (SubsystemConnector connector) {
        this.connector = connector;
        vehicles = connector.getVehicleResources();
        humans = connector.getHumanResources();
        resourceLock = new Object();
    }

    //FOR API, to be fully implemented in the future
    /**
     * Creates a maintenance task, if possible, for a street with the demand of 1 human and 1 vehicle. Returns either the
     * created job element if everything could be allocated or a null object when this failed. Scheduling a maintenance
     * task does not affect the spare vehicle count as spares are never used for maintenance tasks.
     * @param street Street where maintenance should be scheduled
     * @return A job element which can be passed into the subsystem anytime for execution
     */
    public Job scheduleMaintenance(StreetName street) {
        //assume cleanup needs 1 people and 1 vehicles
        Job j = new Job(connector, JobType.MAINTENANCE, street, 1, 1);

        Team t;
        synchronized (resourceLock) {
            long freeHR = humans.stream().filter(h -> h.isAvailable()).count();
            long freeVR = vehicles.stream().filter(v -> v.isAvailable()).count();

            if (freeHR - sparePersonalCount < j.getHumanDemand() || freeVR - spareVehicleCount < j.getVehicleDemand()) {
                return null; //Quits of not enough resources are available
            }
            t = new Team(j); //Creates team as enough resources are available

            int assigned = 0;
            for (Resource h: humans) {
                if (h.isAvailable()) {
                    assigned++;
                    t.addHumanToTeam(h);
                }
                if (assigned >= j.getHumanDemand()) {
                    break;
                }
            }

            assigned = 0;
            for (Resource v: vehicles) {
                if (v.isAvailable()) {
                    assigned++;
                    t.addVehicleToTeam(v);
                }
                if (assigned >= j.getHumanDemand()) {
                    break;
                }
            }

        }

        j.setAssignedTeam(t); //Assigned the team to the task
        return j; //Returns the new job
    }

    /**
     * Creates an cleanup task for the given street with a fixed demand for vehicles of 2 and human workers of 3. This
     * method does not return anything but it will notify the ControlSystem via the connector whether the cleanup could
     * be performed or not. If no team could be assembled or the task execution was interrupted, the according false
     * will be sent, otherwise a true via the connector. Scheduling a cleanup task does not affect the spare
     * count as spares are never used for cleanup tasks. All cleanup tasks are executed immediately.
     * @param street The street where cleanups has to be made
     */
    public void cleanup (StreetName street) {
        //assume cleanup needs 3 people and 2 vehicles
        Job j = new Job(connector, JobType.CLEANUP, street, 2, 3);
        Team t;

        synchronized (resourceLock) {
            long freeHR = humans.stream().filter(h -> h.isAvailable()).count();
            long freeVR = vehicles.stream().filter(v -> v.isAvailable()).count();

            if (freeHR - sparePersonalCount < j.getHumanDemand() || freeVR - spareVehicleCount < j.getVehicleDemand()) {
                connector.jobCompleted(street, false);
                return;
            }
            t = new Team(j);

            int assigned = 0;
            for (Resource h: humans) {
                if (h.isAvailable()) {
                    assigned++;
                    t.addHumanToTeam(h);
                }
                if (assigned >= j.getHumanDemand()) {
                    break;
                }
            }

            assigned = 0;
            for (Resource v: vehicles) {
                if (v.isAvailable()) {
                    assigned++;
                    t.addVehicleToTeam(v);
                }
                if (assigned >= j.getHumanDemand()) {
                    break;
                }
            }
        }


        j.setAssignedTeam(t);
        executeJob(j);
    }

    /**
     * Creates an emergency repair task for the given street with a fixed demand for vehicles of 1 and human workers of 2.
     * This method does not return anything but it will notify the ControlSystem via the connector whether the emergency
     * repair could be performed or not. If no team could be assembled or the task execution was interrupted,
     * the according false  will be sent, otherwise a true via the connector. Scheduling a emergency repair task does
     * affect the spare count as spares can be used for this sort of operation.
     * All emergency repair tasks are executed immediately.
     * @param street The street where cleanups has to be made
     */
    public void emergencyTask (StreetName street) {
        //assume emergency needs 2 people and 1 vehicles
        Job j = new Job(connector, JobType.EMERGENCY, street, 1, 2);
        Team t;

        synchronized (resourceLock) {
            long freeHR = humans.stream().filter(h -> h.isAvailable()).count();
            long freeVR = vehicles.stream().filter(v -> v.isAvailable()).count();

            if (freeHR < j.getHumanDemand() || freeVR < j.getVehicleDemand()) {
                connector.jobCompleted(street, false);
                return;
            }

            t = new Team(j);

            int assigned = 0;
            for (Resource h: humans) {
                if (h.isAvailable()) {
                    assigned++;
                    t.addHumanToTeam(h);
                }
                if (assigned >= j.getHumanDemand()) {
                    break;
                }
            }

            assigned = 0;
            for (Resource v: vehicles) {
                if (v.isAvailable()) {
                    assigned++;
                    t.addVehicleToTeam(v);
                }
                if (assigned >= j.getHumanDemand()) {
                    break;
                }
            }
        }

        j.setAssignedTeam(t);
        executeJob(j);
    }

    /**
     * This function executes any job give to it, either by the repair management (cleanup, emergency repair)
     * or the ControlSystem from outside (Maintenance). It submits the job, which is runnable, into a threadpool where
     * the job knows what to do autonomously.
     * @param job
     */
    public void executeJob (Job job) {
        executor.submit(job);
    }

    /**
     * Cancels an scheduled job and dissolves the team behind this job automatically
     * @param job The job to be cancelled and where the team should be dissolved
     */
    public void cancelJob (Job job) {
        Team t = job.getAssignedTeam();
        synchronized (resourceLock) {
            t.dissolveTeam();
        }
        t = null;
    }

    /**
     * Reassigns a team to another job. This method is not really implemented and only here for API completenes reasons
     * @param team The team which should get another job
     * @param nextJob The other job
     */
    private void redirectTeam(Team team, Job nextJob) {
        //Not a finished method as way more things need to be done here
        team.replaceJob(nextJob);
    }

    /**
     * Performs a shutdown on the executor in order to enable a clean finishing of the program
     */
    void killExecutor() {
        executor.shutdown();
    }
}
