package expr.visitor;

import expr.Add;
import expr.Lit;
import expr.Minus;
import expr.Mult;
import expr.Recip;
import expr.Var;

public class EvalVisitor implements ExprVisitor<Double> {

	@Override
	public Double visit(Add a) {
		return a.getLeft().accept(this) + a.getRight().accept(this);
	}

	@Override
	public Double visit(Lit l) {
		return l.getVal();
	}

	@Override
	public Double visit(Minus m) {
		return -1.0 * m.getSubExpr().accept(this);
	}

	@Override
	public Double visit(Mult m) {
		return m.getLeft().accept(this) * m.getRight().accept(this);
	}

	@Override
	public Double visit(Recip r) {
		return 1 / r.getSubExpr().accept(this);
	}

	@Override
	public Double visit(Var v) {
		return v.getValue();
	}

}
