package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.ClosureTerm;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$call_closure'/1</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$call_closure_1 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5789556048420994521L;
	private Term arg1;
	private Predicate cont;

	public PRED_$call_closure_1(Term a1, Predicate cont) {
		arg1 = a1;
		this.cont = cont;
	}

	public PRED_$call_closure_1() {
	}

	@Override
	public void setArgument(Term args[], Predicate cont) {
		arg1 = args[0];
		this.cont = cont;
	}

	@Override
	public int arity() {
		return 1;
	}

	@Override
	public String toString() {
		return "$call_closure(" + arg1 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1;
		Predicate code;

		// a1 must be closure
		a1 = arg1.dereference();

		if (!a1.isClosure()) {
			return engine.fail();
		}
		code = ((ClosureTerm) a1).getCode();
		code.cont = cont;
		return code;
	}
}
