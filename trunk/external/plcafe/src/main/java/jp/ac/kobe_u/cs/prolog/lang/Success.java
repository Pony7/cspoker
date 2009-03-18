package jp.ac.kobe_u.cs.prolog.lang;

/**
 * Initial continuation goal.<br>
 * That is to say, this <code>Success</code> will be executed every time the
 * Prolog Cafe system finds an answer.
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class Success extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3151488423400820573L;
	/** Prolog thread that this <code>Success</code> belongs to. */
	public PrologControl c;

	/** Constructs a new initial continuation goal. */
	public Success() {
	}

	/** Constructs a new initial continuation goal with given Prolog thread. */
	public Success(PrologControl c) {
		this.c = c;
	}

	/**
	 * Backtracks and returns a next clause after invoking the
	 * <code>PrologControl.success()</code>.
	 * 
	 * @param engine
	 *            Prolog engine
	 * @see PrologControl#success
	 */
	@Override
	public Predicate exec(Prolog engine) {
		c.success();
		engine.exceptionRaised = 3;
		return engine.fail();
	}

	/** Returns a string representation of this <code>Success</code>. */
	@Override
	public String toString() {
		return "Success";
	}

	/** Returns <code>0</code>. */
	@Override
	public int arity() {
		return 0;
	}
}
