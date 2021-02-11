package expr;

/**
 * Class with static factory methods for creating expressions of 
 * various kinds. 
 */
public class Exprs {
	
	/**
	 * Creates an literal expression. 
	 * @param value the value of the literal
	 * @return the literal expression 
	 */
	public static Lit lit(double value) {
		return new Lit(value); 
	}
	
	/** 
	 * Creates an variable expression with name. 
	 * Initial value of the variable is 0.0. 
	 * @param name the name of the variable
	 * @return the variable expression
	 */
	public static Var var(String name) {
		return new Var(name); 
	}
	
	/**
	 * Creates an variable expression with name and initial value. 
	 * @param name the name of the variable
	 * @param value the initial value of the variable 
	 * @return the variable expression
	 */
	public static Var var(String name, double value) {
		return new Var(name, value); 
	}
	
	/**
	 * Creates an add expression with left and right subexpressions. 
	 * @param left the left subexpression
	 * @param right the right subexpression
	 * @return the add expression 
	 */
	public static Add add(Expr left, Expr right) {
		return new Add(left, right); 
	}
	
	/**
	 * Creates an multiplication expression with left and right subexpressions. 
	 * @param left the left subexpression
	 * @param right the right subexpression
	 * @return the multiplication expression 
	 */
	public static Mult mult(Expr left, Expr right) {
		return new Mult(left, right); 
	}
	
	/**
	 * Creates an minus expression with one subexpressions. 
	 * @param expr the subexpression
	 * @return the minus expression 
	 */
	public static Minus minus(Expr expr) {
		return new Minus(expr); 
	}
	
	/**
	 * Creates an reciprocal expression with one subexpressions. 
	 * @param expr the subexpression
	 * @return the reciprocal expression 
	 */
	public static Recip recip(Expr expr) {
		return new Recip(expr); 
	}

}
