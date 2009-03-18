package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$print_stack_trace'/1</code>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$print_stack_trace_1 extends Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2780124848421377831L;
	private Term arg1;

	public PRED_$print_stack_trace_1(Term a1, Predicate cont) {
		arg1 = a1;
		this.cont = cont;
	}

	public PRED_$print_stack_trace_1() {
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
		return "$print_stack_trace(" + arg1 + ")";
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
		if (!a1.isJavaObject()) {
			throw new IllegalTypeException(this, 1, "java", a1);
		}
		Object obj = ((JavaObjectTerm) a1).object();
		if (obj instanceof InterruptedException) {
			System.exit(1);
		}
		if (engine.getPrintStackTrace().equals("on")) {
			((Exception) obj).printStackTrace();
		}
		return cont;
	}
}
