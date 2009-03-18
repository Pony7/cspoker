package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.IllegalDomainException;
import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;
import jp.ac.kobe_u.cs.prolog.lang.VariableTerm;

/**
 * <code>current_input/1</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_current_input_1 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9011169850941382559L;
	private Term arg1;

	public PRED_current_input_1(Term a1, Predicate cont) {
		arg1 = a1;
		this.cont = cont;
	}

	public PRED_current_input_1() {
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
		return "current_input(" + arg1 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1;
		a1 = arg1;
		a1 = a1.dereference();
		if (a1.isVariable()) {
			((VariableTerm) a1).bind(new JavaObjectTerm(engine
					.getCurrentInput()), engine.trail);
		} else if (a1.isJavaObject()) {
			if (!a1.unify(new JavaObjectTerm(engine.getCurrentInput()),
					engine.trail)) {
				return engine.fail();
			}
		} else {
			throw new IllegalDomainException(this, 1, "stream", a1);
		}
		return cont;
	}
}
