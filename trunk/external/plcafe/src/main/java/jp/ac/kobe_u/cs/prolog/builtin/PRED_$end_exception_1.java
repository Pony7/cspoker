package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.OutOfScope;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.SystemException;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$end_exception'/1<code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$end_exception_1 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3593594865830516422L;
	Term arg1;

	public PRED_$end_exception_1(Term a1, Predicate cont) {
		arg1 = a1;
		this.cont = cont;
	}

	public PRED_$end_exception_1() {
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
		return "$end_exception(" + arg1 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1;
		a1 = arg1;

		a1 = a1.dereference();
		if (!a1.isJavaObject()) {
			throw new IllegalTypeException(this, 1, "java", a1);
		}
		Object obj = ((JavaObjectTerm) a1).object();
		if (!(obj instanceof PRED_$begin_exception_1)) {
			throw new SystemException(
					"a1 must be an object of PRED_$begin_exception_1: "
							+ this.toString());
		}
		PRED_$begin_exception_1 p = (PRED_$begin_exception_1) obj;
		p.outOfScope = true;
		engine.trail.add(new OutOfScope(p));
		return cont;
	}
}
