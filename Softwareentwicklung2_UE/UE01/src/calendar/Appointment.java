package calendar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Class for all appointments in the calendar. An entry consist of an unique id, daten and time, timespan, description
 */
public class Appointment {
    /** globalID counter for all appointments */
    private static int globalID = 0;
    
    //Local fields for all data to be stored in, final because they don't need to be modified after setting them initially
    /** Unique ID for the appointment */
    private final int id;
    /** Description of the appointment */
    private final String description;
    /** Date and Time of the appointment */
    private final LocalDateTime dateTime;
    /** How long (in minutes) the appointment will taske */
    private final Duration duration;

    /**
     * Constructor for initializing a new Appointment
     * @param description A description of the appointment
     * @param dateTime Date and time of appointment
     * @param duration How long the appointment will be
     */
    public Appointment (String description, LocalDateTime dateTime, Duration duration) {
    	id = globalID;
        this.description = description;
        this.dateTime = dateTime;
        this.duration = duration;
        
        /** Increase global ID so that next appointment gets another unique ID*/
        globalID++;
    }
    
    //Getter and Setter   
    /**
     * Returns the uniqie id of the appointment
     * @return integer value that is the id
     */
    public int getID() {
    	return id;
    }
    
    /**
     * Returns date and time of the appointment
     * @return dateTime variable that holds date and time
     */
    public LocalDateTime getDateTime() {
    	return dateTime;
    }
    
    
    /**
     * Returns the duration of the appointment (in minutes)
     * @return the duration (in minutes)
     */
    public Duration getDuration() {
    	return duration;
    }
    
    //Not directly a classic getter, but it gives back the Date from the dateTime variable
    /**
     * Returns the date of the appointment without the time
     * @return returns a local Date (format yyyy-mm-dd)
     */
    public LocalDate getDate() {
    	return dateTime.toLocalDate();
    }
    
    //Other Methods
    /**
     * Returns a string containing date, time, duration and description
     * @return String with date, duration and description
     */
    public String toString() {
    	return "[" + id + "] " + description + ": am " + dateTime.toLocalDate().toString() + ", von " + dateTime.toLocalTime().toString() + " bis " + dateTime.toLocalTime().plus(duration).toString();
    }
    
    /**
     * Decreases the GlobalID, a static int variable  in Appointment, by one.
     * It should only, and only then, if the latest Appointment created is dismissed an will NEVER be used again
     * Typically should only be called from AppointmentList when it is detected that there is a time collision
     */
    static void decreasteGlobalID() {
    	globalID--;
    }
}
