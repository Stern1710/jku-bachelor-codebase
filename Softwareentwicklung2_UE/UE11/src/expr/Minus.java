package expr;

import expr.visitor.ExprVisitor;

/**
 * Class for minus unary expressions. 
 */
public class Minus extends UnExpr {

	/**
	 * Constructor setting the subexpression. 
	 * @param expr the subexpression
	 */
	Minus(Expr expr) {
		super(expr);
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
