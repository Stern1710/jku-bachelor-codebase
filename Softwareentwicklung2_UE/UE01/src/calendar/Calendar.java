package calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Calendar class that manages all the calls to and from the list 
 * @author Simon Sternbauer
 */
public final class Calendar {
	
	/** The list of all appointments (nodes) */
	private final AppointmentList list;
	/**  the last and therefore currently selected or worked-on node */
	private AppointmentNode current;
	
	/**
	 * Standard constructor for calendar, initializes the list and the current node
	 */
	public Calendar() {
		list = new AppointmentList();
		current = null;
	}
	
	/**
	 * Adds an passed appointment into the list
	 * @param app Appointment which should be added
	 * @return the appointment if added successfully or null if not
	 */
	public Appointment addAppointment(Appointment app) {
		current = list.insertAppointment(app);
		if (current != null) {
			return current.getAppointment();
		}
		return null;	
	}
	
	/**
	 * Gets first appointment at of after given date and time
	 * @param dateTime The date and time after at which the first appointment should be taken
	 * @return first appointment or null if no suitable appointment is found
	 */
	public Appointment appointmentByDateTime (LocalDateTime dateTime) {
		current = list.findAppointmentByDateTime(dateTime);
		if (current != null) {
			return current.getAppointment();
		}
		return null;	
	}
	
	/**
	 * Gets the first appointment of a given day
	 * @param day The day of where the first appointment is wanted
	 * @return either the first appointment or null if on that day no appointments take place
	 */
	public Appointment firstAppointmentOfDay (LocalDate day) {
		current = list.findFirstAppointmentForDay(day);
		if (current != null) {
			return current.getAppointment();
		}
		return null;
	}
	
	/**
	 * gets the next appointment after the last selected one
	 * @return Either the next appointment after the currently selected one or null if there aren't any
	 */
	public Appointment nextAppointment () {
		if (current != null) {
			current = current.getNext();
			
			if (current != null) {
				return current.getAppointment();
			}
		}
		return null;
	}
	
	/**
	 * Removes an appointment from the list, identified by its id
	 * @param id The id of the appointment
	 * @return Either true for a successful remove or false if it failed
	 */
	public boolean removeAppointment (int id) {
		return list.removeAppointment(id);
	}
	
	/**
	 * Prints all appointments in the calendar on the screen
	 * This method was not required, but added by the author (Simon Sternbauer) himself as it seems very practical to test out the calendar
	 */
	public void printCalendar() {
		list.printAllAppointments();
	}

}