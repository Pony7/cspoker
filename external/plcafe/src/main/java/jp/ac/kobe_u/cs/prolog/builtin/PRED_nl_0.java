package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>nl/0</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_nl_0 extends Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -670783863646643364L;

	public PRED_nl_0(Predicate cont) {
		this.cont = cont;
	}

	public PRED_nl_0() {
	}

	@Override
	public void setArgument(Term[] args, Predicate cont) {
		this.cont = cont;
	}

	@Override
	public int arity() {
		return 0;
	}

	@Override
	public String toString() {
		return "nl";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		engine.getCurrentOutput().println();
		return cont;
	}
}
