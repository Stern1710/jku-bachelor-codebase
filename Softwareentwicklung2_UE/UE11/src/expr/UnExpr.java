package expr;

/**
 * Abstract base class for unary expressions. 
 * Unary expressions have a subexpression. 
 */
abstract class UnExpr implements Expr {
	
	/** The subexpression of this unary expression */
	private final Expr subExpr; 

	/**
	 * The constructor initializing the subexpression.
	 * @param subExpr the subexpression 
	 */
	UnExpr(Expr subExpr) {
		super();
		this.subExpr = subExpr;
	}

	
	/**
	 * Gets the subexpression of this unary expression
	 * @return the subexpression 
	 */
	public Expr getSubExpr() {
		return subExpr;
	}

}
