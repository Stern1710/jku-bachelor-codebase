package employee;

import inout.Out;

public class Manager extends Employee {
	//Times how often the monthly payment is to be payed in a year
	protected final static int NUMBER_OF_PAYMENTS = 12;
	
	private final double bonus; //Forces to set bonus on creation of instance
	
	/**
	 * Constructor for the Manager
	 * @param name Name of the manager
	 * @param salary The monthly salary of the manager
	 * @param bonus The bonus a manager gets every year
	 */
	public Manager(String name, double salary, double bonus) {
		super(name, salary);
		this.bonus = bonus;
	}
	
	//Method implementation of Employee class and overwrittes
	/**
	 * Manager does work when it is to Sign Contracts (and tells you this)
	 * For all other work a WorkException is thrown
	 */
	public void doWork(Work work) throws WorkException {
		switch (work) {
		case SignContract:
			Out.println("I, a manager, will very gladly sign this contract.");
			break;		
		default:
			throw new WorkException("As a manager I am not responsible for this kind of work: " + work.toString(), work);
		}
	}
	
	/**
	 * The annual salary of a Manager is 12 times the monthly plus the bonus
	 * @return The calculated annual salary of a manager
	 */
	@Override
	public double getAnnualSalary() {
		return getMonthlySalary() * NUMBER_OF_PAYMENTS + bonus;
	}
	
	/**
	 * Overrides the standard implementation in Employee to tell it is a manager
	 */
	@Override
	public String toString() {
		return "Manager: " + getName();
	}
}
