package expr;

import expr.visitor.ExprVisitor;

/**
 * Interface for expressions. 
 * Expressions should allow visitors.  
 */
public interface Expr {
	public <T> T accept(ExprVisitor<T> visitor);
}
