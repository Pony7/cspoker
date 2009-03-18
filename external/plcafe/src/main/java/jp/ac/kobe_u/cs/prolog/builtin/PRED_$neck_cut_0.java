package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$neck_cut'/0</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_$neck_cut_0 extends Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2717809774157417718L;

	public PRED_$neck_cut_0(Predicate cont) {
		this.cont = cont;
	}

	public PRED_$neck_cut_0() {
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
		return "$neck_cut";
	}

	@Override
	public Predicate exec(Prolog engine) {
		// engine.setB0();
		engine.neckCut();
		return cont;
	}
}
