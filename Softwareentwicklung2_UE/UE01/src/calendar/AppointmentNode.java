package calendar;

/**
 * This class implements the nodes for the linked linear list of calendar entries
 */
final class AppointmentNode {
    
    /** The value behind the node in the data class */
    private Appointment app;

    /** Pointer to the next node */
    private AppointmentNode next;

    /**
     * Constructor for setting the appointment
     * @param app The value for the appointment
     */
    public AppointmentNode (Appointment app) {
    	this.app = app;
    	next = null;
    }
    
    /**
     * Returns the appointment the node is linking to
     * @return The Appointment the Node links to
     */
    public Appointment getAppointment() {
    	return app;
    }
    
    /**
     * Returns the next node in the linear list or null if there is no next node
     * @return either the next node or null
     */
    public AppointmentNode getNext() {
    	return next;
    }
    
    /**
     * Sets the next node, needed when deleting a appointment and its node from the list
     * @param next
     */
    public void setNext (AppointmentNode next) {
    	this.next = next;
    }
}