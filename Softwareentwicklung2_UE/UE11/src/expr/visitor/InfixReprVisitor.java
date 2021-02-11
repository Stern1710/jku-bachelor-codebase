package expr.visitor;

import expr.Add;
import expr.Lit;
import expr.Minus;
import expr.Mult;
import expr.Recip;
import expr.Var;

public class InfixReprVisitor implements ExprVisitor<String> {

	@Override
	public String visit(Add a) {
		return "(" + a.getLeft().accept(this) + " + " + a.getRight().accept(this) + ")" ;
	}

	@Override
	public String visit(Lit l) {
		return l.getVal() + "";
	}

	@Override
	public String visit(Minus m) {
		return "(-" + m.getSubExpr().accept(this) + ")" ;
	}

	@Override
	public String visit(Mult m) {
		return "(" + m.getLeft().accept(this) + " * " + m.getRight().accept(this) + ")";
	}

	@Override
	public String visit(Recip r) {
		return "(1.0/" + r.getSubExpr().accept(this) + ")";
	}

	@Override
	public String visit(Var v) {
		return v.getName();
	}

}
