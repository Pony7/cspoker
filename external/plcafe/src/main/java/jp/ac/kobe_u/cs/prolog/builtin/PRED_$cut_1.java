package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$cut'/1</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_$cut_1 extends Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4347449654924527310L;
	private Term arg1;

	public PRED_$cut_1(Term a1, Predicate cont) {
		arg1 = a1;
		this.cont = cont;
	}

	public PRED_$cut_1() {
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
		return "$cut(" + arg1 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		// engine.setB0();
		Term a1;
		a1 = arg1;
		a1 = a1.dereference();
		if (!a1.isInteger()) {
			throw new IllegalTypeException("integer", a1);
		} else {
			engine.cut(((IntegerTerm) a1).intValue());
		}
		return cont;
	}
}
