package employee;

public abstract class Employee {

	//Local Variables
	private final String name;
	private double monSalary;	//Not final as salary can change over time
	
	//Constructors
	/**
	 * Constructor for a new Employee
	 * @param name Name of the employee which is used for comparisons
	 * @param monSalary Monthly salary of the employee
	 */
	protected Employee (String name, double monSalary) {
		this.name = name;
		this.monSalary = monSalary;
	}
	
	//Getter and Setter
	/**
	 * Returns name of the employee
	 * @return String with the name 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the montly earnings of the employee
	 * @return double value with monthly salary
	 */
	public double getMonthlySalary() {
		return monSalary;
	}
	
	/**
	 * Sets the monthly salary of a Employee when that person gets payed more (hopefully not less)
	 * @param monSalary
	 */
	public void setMonthlySalary(double monSalary) {
		this.monSalary = monSalary;
	}
	
	//Abstract methods
	public abstract void doWork(Work work) throws WorkException;
	
	public abstract double getAnnualSalary();
	
	//Other methods
	/**
	 * Overwrites the standard implementation of equals
	 * Returns true, if name of Employee 1 is the same as Employee 2
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof Employee))
			return false;

		Employee other = (Employee)obj;
		return name.equals(other.name);
	}
	
	/**
	 * Returns the name of the employee
	 * @return Name of the employee
	 */
	@Override
	public String toString() {
		return "Emploeyee: " + name;
	}
}
