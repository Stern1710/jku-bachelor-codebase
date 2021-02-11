package expr;

import expr.visitor.ExprVisitor;

/**
 * Class for multiplication expressions. 
 */
public class Mult extends BinExpr {
	
	/**
	 * Constructor for the multiplication expression with left and right subexpressions. 
	 * @param left the left subexpression
	 * @param right the right subexpression
	 */
	Mult(Expr left, Expr right) {
		super(left, right);
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
