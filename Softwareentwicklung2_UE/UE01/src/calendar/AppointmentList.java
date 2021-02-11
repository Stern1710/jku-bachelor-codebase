package calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;

import inout.Out;

/**
 * Class which implements a linked linear list for the calendar entries.
 * Entries are stored in order by date and time
 */
final class AppointmentList {
    
    /**
     * The first element in the list
    */
    private AppointmentNode head;

    /**
     * Standard constructor for AppointmentList
     */
    public AppointmentList () {
        head = null;
    }
    
    /**
     * Inserts a new appointment to position sorted by daten and time if there are no conflicts with existing appointments
     * @param app the new appointment that should be in the calendar
     * @return Either the new AppointmentNode or null if there was a time-related conflict
     */
    public AppointmentNode insertAppointment (Appointment app) {
    	AppointmentNode pred = null;
    	AppointmentNode curr = head;
    	
    	//First find the correct position to insert the new appointment
    	//This must be before the first occurrence of a date that is after the new appointments date
    	while (curr != null && curr.getAppointment().getDateTime().isBefore(app.getDateTime()) ) {
    		pred = curr;
    		curr = curr.getNext();
    	}
    	
    	//Temporary variables to hold the appointments. Makes the if-clause more readable
    	Appointment currApp = null;
    	Appointment predApp = null;
    	
    	if (curr != null) {
    		currApp = curr.getAppointment();
    	}
    	if (pred != null) {
    		predApp = pred.getAppointment();
    	}
    	
    	if (curr != null &&
    			(currApp.getDateTime().isEqual(app.getDateTime()) ||
    			app.getDateTime().plus(app.getDuration()).isAfter(currApp.getDateTime())) ||
    		(pred != null &&
    			predApp.getDateTime().plus(predApp.getDuration()).isAfter(app.getDateTime()))) {
    		/* This checks two blocks (for curr and pred) of time related problem sources.
    		 * If either on of these is true, the appointment will not be added and "null" be returned
    		 * Curr-block: This is only checked if curr has a value (otherwise we are at the end of at the very beginning of the list)
    		 *		1) The currently selected appointment has the exact same Date and Time as the new one
    		 *		2) the new appointment has not finished when the next appointment starts
    		 * Pred-block: This is only checked if curr has a value (so there is a appointment previous to the on we want to insert)
    		 * 		1) the pred-appointment has not ended by the time the new appointment should start
    		*/
    		
    		//Decrease globalID as the new appointment is not used (frees some IDs for later usage)
    		Appointment.decreasteGlobalID();
    		
    		return null;
    	} else {
    		//If there is no time conflict, insert it with changing the next references
    		AppointmentNode node = new AppointmentNode(app);
    		
    		if (pred == null) {
    			//If no previous node before the current node is here, new appointment is the new head
    			head = node;
    		} else {
    			//Otherwise the previous appointment now 
    			pred.setNext(node);
    		}
    		node.setNext(curr);
    		
    		return node;
    	}
    }
    
    /**
     * Returns an AppointmentNode where the Appointment is next to the date and time
     * @param dateTime the date and time from where the next AppointmentNode should be returned
     * @return the next AppointmentNode from dateTime or null if no one is found
     */
    public AppointmentNode findAppointmentByDateTime (LocalDateTime dateTime) {
    	AppointmentNode selApp = head;
    	
    	//Iterates as long as the wanted point in time is earlier than the dateTime of the currently selected Appointment
    	while (selApp != null && selApp.getAppointment().getDateTime().isBefore(dateTime)) {
    		selApp = selApp.getNext();
    	}
    	
    	return selApp;
    }
    
    /**
     * Searches for the first appointment for a given day
     * @param day The day where the first appointment should be found
     * @return Either the first appoinment for the day or null if no appointment is on the given day
     */
    public AppointmentNode findFirstAppointmentForDay (LocalDate day) {
    	AppointmentNode selApp = head;
    	
    	//Iterates as long as there is no appointment on the same day as wanted
    	//Always gets the first appointment as the list is sorted by time too and we start from the head
    	while (selApp != null && (!selApp.getAppointment().getDate().isEqual(day))) {
    		selApp = selApp.getNext();
    	}
    	
    	return selApp;
    }
    
    /**
     * Removes a single appointment identified by its id
     * @param id The id of the apppointment which should be removed
     * @return True of False, according to if deletion was successfull or not
     */
    public boolean removeAppointment (int id) {
    	AppointmentNode pred = null;
    	AppointmentNode selApp = head;
    	
    	//First get the appointment with the wanted id
    	while (selApp != null && selApp.getAppointment().getID() != id) {
    		pred = selApp;
    		selApp = selApp.getNext();
    	}
    	
    	//If an appointment is found and there is no pred available, it must be the head, therefore link the next one as new head
    	//Otherwise link the previous one to the next one to exclude the wanted appointment from the list
    	if (selApp != null) {
    		if (pred == null) {
    			head = selApp.getNext();
    		} else {
    			pred.setNext(selApp.getNext());
    		}
    		
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Prints all Appointments to the standard output
     * This method was not required, but added by the author (Simon Sternbauer) himself as it seems very practical to test out the calendar
     */
    public void printAllAppointments() {
    	AppointmentNode curr = head;
    	
    	while (curr != null) {
    		Out.println(curr.getAppointment().toString());
    		curr = curr.getNext();
    	}
    }
}