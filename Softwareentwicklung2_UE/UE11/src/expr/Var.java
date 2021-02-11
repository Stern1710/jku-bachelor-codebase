package expr;

import expr.visitor.ExprVisitor;

/**
 * The class for variable expressions. 
 * Variable expression have a name and a double value. 
 * The double value can be set.  
 */
public class Var implements Expr {
	
	/** The name of the variable */ 
	private final String name; 
	/** The current value of this variable */
	private double value; 

	/**
	 * Constructor setting the name of the variable. 
	 * @param name the name of the variable
	 */
	Var(String name) {
		super();
		this.name = name;
		this.value = 0.0; 
	}

	/**
	 * Constructor setting the name of the variable and 
	 * the initial value of the variable . 
	 * @param name the name of the variable
	 * @param value the initial value of the variable 
	 */
	Var(String name, double value) {
		super();
		this.name = name;
		this.value = value; 
	}

	/**
	 * The name of this variable. 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the current value of this variable. 
	 * @return the current value of this variable
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets the value of this variable 
	 * @param value the new value 
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Lets visitor visit this object and returns generic value
	 * @param visitor Visitor that wants to perform actions on this object
	 * @return T - Generic that matches with given generic from Visitor
	 */
	@Override
	public <T> T accept(ExprVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
