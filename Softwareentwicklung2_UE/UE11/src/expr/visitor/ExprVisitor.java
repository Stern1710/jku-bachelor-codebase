package expr.visitor;

import expr.Add;
import expr.Lit;
import expr.Minus;
import expr.Mult;
import expr.Recip;
import expr.Var;

/**
 * Visitor interface for visiting expressions. 
 * Visit methods have return values which are generic. 
 * 
 * @param <T> the generic return type for visit methods 
 */
public interface ExprVisitor<T> {	
	public T visit(Add a);
	public T visit(Lit l);
	public T visit(Minus m);
	public T visit(Mult m);
	public T visit(Recip r);
	public T visit(Var v);	
}
