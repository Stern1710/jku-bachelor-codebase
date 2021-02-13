package common.RoadMaintenance;

/**
 * A resource is a unit used by the road maintenance subsystem to keep streets and devices repaired. A resource has a
 * type which specifies this as either a Vehicle or a Human. The resource can be available or not, depending if it
 * is assigned to a team or not.
 */
public class Resource {

    private String name;
    private ResourceType type;
    private boolean available;
    private Object lockObject; //Lock object for set/get of available

    /**
     * Constructor which takes the name of the resource and its type
     * @param name Name of the resource
     * @param type The type of the resource, either a vehicle or a human
     */
    public Resource (String name, ResourceType type) {
        this.name = name;
        this.type = type;
        available = true;
        lockObject = new Object();
    }

     /* -------------------
    Getter
    ------------------- */

    /**
     * Gets the name of the resource
     * @return String with the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type (Vehicle/Human) of the resource
     * @return The ResourceType of the resource
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Synchronized method that checks if the resource is available or not.
     * @return boolean value for available (true) or not (false)
     */
    public boolean isAvailable() {
        synchronized (lockObject) {
            return available;
        }
    }

    /**
     * Sets the availability of a resource. This should be usually done by the team as resources get assigned
     * or taken away (example: Team is dissolved --> set resource available)
     * @param available The availability status of the resource which is either true or false
     */
    public void setAvailable (boolean available) {
        synchronized (lockObject) {
            this.available = available;
        }
    }

     /* -------------------
    Overwritten methods
    ------------------- */

    /**
     * Builds a string with name, type and availability of the resource
     * @return conateneted information about the resource
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Name: ");
        sb.append(name);
        sb.append("; Type: ");
        sb.append(type);
        sb.append("; Available: ");
        sb.append(available);

        return sb.toString();
    }
}
