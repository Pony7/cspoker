package jp.ac.kobe_u.cs.prolog.lang;

/**
 * Initial backtrak point.<br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.2
 */
public class Failure extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3729935990018453335L;
	/** Prolog thread that this <code>Failure</code> belongs to. */
	public PrologControl c;

	/** Constructs a new initial backtrak point. */
	public Failure() {
	}

	/** Constructs a new initial backtrak point with given Prolog thread. */
	public Failure(PrologControl c) {
		this.c = c;
	}

	@Override
	public Predicate exec(Prolog engine) {
		c.fail();
		engine.exceptionRaised = 1; // halt
		return null;
	}

	/** Returns a string representation of this <code>Failure</code>. */
	@Override
	public String toString() {
		return "Failure";
	}

	/** Returns <code>0</code>. */
	@Override
	public int arity() {
		return 0;
	}
}
