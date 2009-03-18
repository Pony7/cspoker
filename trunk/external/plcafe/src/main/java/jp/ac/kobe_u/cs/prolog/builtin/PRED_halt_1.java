package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>halt/1</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_halt_1 extends Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5338644777139239355L;
	private Term arg1;

	public PRED_halt_1(Term a1, Predicate cont) {
		arg1 = a1;
		this.cont = cont;
	}

	public PRED_halt_1() {
	}

	@Override
	public void setArgument(Term[] args, Predicate cont) {
		arg1 = args[0];
		this.cont = cont;
	}

	@Override
	public int arity() {
		return 1;
	}

	@Override
	public String toString() {
		return "halt(" + arg1 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1;
		a1 = arg1;

		a1 = a1.dereference();
		if (a1.isVariable()) {
			throw new PInstantiationException(this, 1);
		}
		if (!a1.isInteger()) {
			throw new IllegalTypeException(this, 1, "integer", a1);
		}
		engine.exceptionRaised = ((IntegerTerm) a1).intValue();
		return cont;
	}
}
