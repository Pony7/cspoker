package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.ListTerm;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.RepresentationException;
import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$get_instances'/2</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
class PRED_$get_instances_2 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3085864832865167853L;
	private Term arg1, arg2;
	private final static SymbolTerm COMMA = SymbolTerm.makeSymbol(",", 2);

	public PRED_$get_instances_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_$get_instances_2() {
	}

	@Override
	public void setArgument(Term[] args, Predicate cont) {
		arg1 = args[0];
		arg2 = args[1];
		this.cont = cont;
	}

	@Override
	public int arity() {
		return 2;
	}

	@Override
	public String toString() {
		return "$get_instances(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;
		a1 = a1.dereference();
		if (a1.isNil()) {
			return engine.fail();
		}
		if (!a1.isList()) {
			throw new IllegalTypeException(this, 1, "list", a1);
		}
		Term x = Prolog.Nil;
		Term tmp = a1;
		while (!tmp.isNil()) {
			if (!tmp.isList()) {
				throw new IllegalTypeException(this, 1, "list", a1);
			}
			Term car = ((ListTerm) tmp).car().dereference();
			if (car.isVariable()) {
				throw new PInstantiationException(this, 1);
			}
			if (!car.isInteger()) {
				throw new RepresentationException(this, 1, "integer");
			}
			// car is an integer
			int i = ((IntegerTerm) car).intValue();
			Term e = engine.internalDB.get(i);
			if (e != null) {
				Term[] arg = { e, car };
				x = new ListTerm(new StructureTerm(COMMA, arg), x);
			}
			// else {
			// System.out.println("index " + i + " is deleted.");
			// }

			// if (e == null)
			// throw new SystemException("invalid index");
			// Term[] arg = {e, car};
			// x = new ListTerm(new StructureTerm(COMMA, arg), x);
			tmp = ((ListTerm) tmp).cdr().dereference();
		}
		if (!a2.unify(x, engine.trail)) {
			return engine.fail();
		}
		return cont;
	}
}
