package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>current_engine/1</code>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_current_engine_1 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4515273983441740035L;
	private Term arg1;

	public PRED_current_engine_1(Term a1, Predicate cont) {
		arg1 = a1;
		this.cont = cont;
	}

	public PRED_current_engine_1() {
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
		return "current_engine(" + arg1 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1;
		a1 = arg1;

		a1 = a1.dereference();
		if (!a1.unify(new JavaObjectTerm(engine), engine.trail)) {
			return engine.fail();
		}
		return cont;
	}
}
