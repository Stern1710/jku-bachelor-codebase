package expr;

/**
 * Abstract base class for binary expressions. 
 * Binary expressions have a left and right subexpression. 
 */
abstract class BinExpr implements Expr {
	
	/** Left and right subexpressions */
	private final Expr left, right; 

	/** 
	 * Constructor with left and right subexpressions. 
	 * @param left the left subexpression
	 * @param right the right subexpression 
	 */
	BinExpr(Expr left, Expr right) {
		super();
		this.left = left;
		this.right = right;
	}

	/** 
	 * Gets the left subexpression.
	 * @return the left subexpression
	 */
	public Expr getLeft() {
		return left;
	}

	/**
	 * Gets the right subexpression.
	 * @return the right subexpression
	 */
	public Expr getRight() {
		return right;
	}
}
