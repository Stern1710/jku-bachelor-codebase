package calendar.app;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import calendar.Appointment;
import calendar.Calendar;
import inout.In;
import inout.Out;

public class CalendarApplication {
	
	/** Formatter for reading date objects */
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"); 
	/** Formatter for reading time objects */
	private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH mm"); 
	
	/**
	 * Main method starting the application.
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		
		Calendar calendar = new Calendar();
		
		In.open("appointments.txt"); //Change this to the linux file if working on linux systems
		Appointment app = readAppointment();
		while (app != null) {
			calendar.addAppointment(app); 
			app = readAppointment();
		}
		In.close();
		
		Appointment current = null;

		// Put out operation codes
		//Student (Simon Sternbauer): I have added a method to show all appointments by typing in "p"
		Out.println("Terminalender ");
		Out.println("============== ");
		Out.println("Folgende Operationen stehen zur Verfügung: ");
		Out.println("  a - Anfügen eines Termins ");
		Out.println("  z - Zugriff auf einen Eintrag zu einem Zeitpunkt");
		Out.println("  e - erster Termin eines Tages ");
		Out.println("  n - nächsten Termin ");
		Out.println("  l - Löschen eines Termins");
		Out.println("  p - Zeigt alle Termine an");
		Out.println("  x - Programm verlassen");

		// Read operation code and perform operation
		char op = readOperation();
		while (op != 'x') {
			switch (op) {
			
			case 'a': // insert a new appointment
				Out.print("   Bitte Beschreibung eingeben: ");
				String descr = In.readLine();
				LocalDateTime start = readDateTimeFromConsole();
				Duration duration = readDurationFromConsole();
				current = calendar.addAppointment(new Appointment(descr, start, duration));
				if (current != null) {
					Out.println("Einfügen erfolgreich"); 
					printAppointment(current);
				} else {
					Out.println("Einfügen nicht erfolgreich"); 
				}
				break;
				
			case 'z': // get appointment
				LocalDateTime time = readDateTimeFromConsole();
				current = calendar.appointmentByDateTime(time);
				printAppointment(current);
				break;
				
			case 'e': // get first of day
				LocalDate day = readDateFromConsole(); 
				current = calendar.firstAppointmentOfDay(day);
				printAppointment(current);
				break;
				
			case 'n': // get next 
				if (current != null) {
					current = calendar.nextAppointment(); 
					printAppointment(current);
				} else {
					Out.println("--> Kein aktueller Termin ausgewählt!"); 
				}
				break;
				
			case 'l': // remove with id
				int id = readIdFromConsole();
				boolean success = calendar.removeAppointment(id);
				if (success) {
					Out.println("Löschen erfolgreich"); 
				} else {
					Out.println("Löschen nicht erfolgreich"); 
				}
				break;
			
			//Student (Simon Sternbauer): I have added a method to show all appointments by typing in "p"
			case 'p': //Print all appointments
				calendar.printCalendar();
				break;
			}
			
			op = readOperation();
		}

		Out.println();
		Out.println("Ciao! Auf ein Wiedersehen freut sich Dein Terminkalender!");

	}
	
	/**
	 * Read an id from the user console 
	 * @return the id read
	 */
	private static int readIdFromConsole() {
		boolean validInput = false; 
		int id = -1; 
		while (! validInput) {
			try {
				Out.print("   Bitte Id eingeben: ");
				id = Integer.parseInt(In.readLine());
				if (id > 0) validInput = true; 
				else Out.print("   Falsche Eingabe!");
			} catch (Exception e) {
				Out.print("   Falsche Eingabe!");
			}
		}
		return id; 
	}

	/**
	 * Read and parse date
	 * @return the parsed date object 
	 * @throws Exception thrown if reading or parsing not possible 
	 */
	private static LocalDate readDate() throws Exception {
		return LocalDate.parse(In.readLine(), dateFormatter);
	}

	/**
	 * Read and parse date object from user console 
	 * @return the parsed date object 
	 */
	private static LocalDate readDateFromConsole() {
		boolean validInput = false; 
		LocalDate date = null; 
		while (! validInput) {
			try {
				Out.print("   Bitte Datum eingeben (tt.mm.jjjj): ");
				date = readDate();
				validInput = true; 
			} catch (Exception e) {
				Out.print("   Falsche Datumseingabe!");
			}
		}
		return date; 
	} 

	/**
	 * Read and parse time 
	 * @return the parsed time object 
	 * @throws Exception thrown if reading or parsing not possible 
	 */
	private static LocalTime readTime() {
		LocalTime time;
		time = LocalTime.parse(In.readLine(), timeFormatter);
		return time;
	} 

	/**
	 * Read and parse time object from user console 
	 * @return the parsed date object 
	 */
	private static LocalTime readTimeFromConsole() {
		boolean validInput = false; 
		LocalTime time = null; 
		while (! validInput) {
			try {
				Out.print("   Bitte Zeit eingeben (HH mm): ");
				time = readTime();
				validInput = true; 
			} catch (Exception e) {
				Out.print("   Falsche Zeiteingabe!");
			}
		}
		return time; 
	}

	/**
	 * Read and parse date and time object from user console 
	 * @return the parsed date and time object 
	 */
	private static LocalDateTime readDateTimeFromConsole() {
		LocalDate date = readDateFromConsole();
		LocalTime time = readTimeFromConsole();
		return date.atTime(time); 
	} 

	/**
	 * Read an duration  
	 * @return the read duration object 
	 * @throws Exception thrown if reading not possible 
	 */
	private static Duration readDuration() {
		Duration duration;
		duration = Duration.ofMinutes(Integer.parseInt(In.readLine()));
		return duration;
	}

	/**
	 * Read duration from user console 
	 * @return the parsed duration object 
	 */
	private static Duration readDurationFromConsole() {
		boolean validInput = false; 
		Duration duration = null; 
		while (! validInput) {
			try {
				Out.print("   Bitte Dauer in Minuten eingeben: ");
				duration = readDuration();
				validInput = true; 
			} catch (Exception e) {
				Out.print("   Falsche Eingabe der Dauer!");
			}
		}
		return duration;
	}

/**
	 * Reads the operation code.
	 * @return the character specifying the next operation.
	 */
	private static char readOperation() {
		Out.println();
		Out.print("Bitte Operation auswaehlen: (a, z, e, n, l, p, x): ");

		String line = In.readLine().trim();
		//Student (Simon Sternbauer): I have added a method to show all appointments by typing in "p"
		while (line.length() == 0 || ! line.startsWith("a") && ! line.startsWith("z") && 
				! line.startsWith("e") && ! line.startsWith("n") && 
				! line.startsWith("l") && ! line.startsWith("x") &&
				! line.startsWith("p")) {
			// wrong operation code, repeat input of operation code
			Out.println();
			Out.print("   Falsche Eingabe! Bitte Eingabe wiederholen: (a, z, e, n, l, x): ");
			 
			line = In.readLine().trim();
		}
		return line.charAt(0);
	} 

	/** 
	 * Prints and appointment
	 * @param a the appointment
	 */
	private static void printAppointment(Appointment a) {
		if (a != null) {
			Out.println(a.toString());
		} else {
			Out.println("NICHT VORHANDEN");
	
		}
	} 

	/**
	 * Reads an appointment
	 * Should be used for file input.  
	 * @return the appointment read. 
	 * @throws IOException if reading fails. 
	 */
	private static Appointment readAppointment() throws Exception {
		String descr = In.readLine(); 
		if (! In.done()) return null; 
		LocalDate date = readDate(); 
		LocalTime time = readTime(); 
		Duration duration = readDuration(); 
		return new Appointment(descr, LocalDateTime.of(date, time), duration);
	}

}
