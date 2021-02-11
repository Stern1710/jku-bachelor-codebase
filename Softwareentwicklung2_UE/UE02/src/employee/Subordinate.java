package employee;

public abstract class Subordinate extends Employee {
	//Times how often the monthly payment is to be payed in a year
	protected static final int NUMBER_OF_PAYMENTS = 14;
	
	//Local fields
	private final Employee manager;
	
	//Constructors
	/**
	 * Constructs a extended class of Subordinate by calling super and setting the manager for the employee
	 * @param name Name of the worker
	 * @param monSalary Monthly salary of the employee
	 * @param manager The manager of the employee
	 */
	protected Subordinate(String name, double monSalary, Employee manager) {
		super(name, monSalary);
		this.manager = manager;
	}
	
	/**
	 * Returns the annual earnings of the employee (14 times monthly)
	 * @return 14 times the monthly salary
	 */
	public double getAnnualSalary() {
		return this.getMonthlySalary() * NUMBER_OF_PAYMENTS;
	}
	
	//Getter
	/**
	 * Returns the Manager-Employee
	 * @return Employee that is the manager
	 */
	protected Employee getManager() {
		return manager;
	}
}
