package expr.visitor;

import expr.Add;
import expr.Expr;
import expr.Exprs;
import expr.Lit;
import expr.Minus;
import expr.Mult;
import expr.Recip;
import expr.Var;

public class SimplifyVisitor implements ExprVisitor<Expr> {

	@Override
	public Expr visit(Add a) {
		Expr left = a.getLeft().accept(this);
		Expr right = a.getRight().accept(this);
		
		if (left instanceof Lit && ((Lit) left).getVal() == 0.0) {
			return right;
		}
		if (right instanceof Lit && ((Lit) right).getVal() == 0.0) {
			return left;
		}
		
		return Exprs.add(left, right);
	}

	@Override
	public Expr visit(Lit l) {
		return l;
	}

	@Override
	public Expr visit(Minus m) {
		Expr min = m.getSubExpr().accept(this);
		
		if (min instanceof Minus) {
			return ((Minus) min).getSubExpr();
		}
		
		return Exprs.minus(min);
	}

	@Override
	public Expr visit(Mult m) {
		Expr left = m.getLeft().accept(this);
		Expr right = m.getRight().accept(this);
		
		if (left instanceof Lit) {
			if (((Lit) left).getVal() == 1.0) {
				return right;
			}
			if (((Lit) left).getVal() == 0.0)
				return left;
		}
		if (right instanceof Lit) {
			if (((Lit) right).getVal() == 1.0) {
				return left;
			}
			if (((Lit) right).getVal() == 0.0) {
				return right;
			}
		}
		
		return Exprs.mult(left, right);
	}

	@Override
	public Expr visit(Recip r) {
		Expr rec = r.getSubExpr().accept(this);
		
		if (rec instanceof Recip) {
			return ((Recip) rec).getSubExpr();
		}
		
		return Exprs.recip(rec);
	}

	@Override
	public Expr visit(Var v) {
		return v;
	}
}