package jp.ac.kobe_u.cs.prolog.builtin;

import java.io.IOException;
import java.io.PushbackReader;

import jp.ac.kobe_u.cs.prolog.lang.Arithmetic;
import jp.ac.kobe_u.cs.prolog.lang.BuiltinException;
import jp.ac.kobe_u.cs.prolog.lang.ExistenceException;
import jp.ac.kobe_u.cs.prolog.lang.IllegalDomainException;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.NumberTerm;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.PermissionException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.RepresentationException;
import jp.ac.kobe_u.cs.prolog.lang.Term;
import jp.ac.kobe_u.cs.prolog.lang.TermException;

/**
 * <code>skip/2</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_skip_2 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3123006796750242051L;
	private final static IntegerTerm INT_EOF = new IntegerTerm(-1);
	private Term arg1, arg2;

	public PRED_skip_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_skip_2() {
	}

	@Override
	public void setArgument(Term[] args, Predicate cont) {
		arg1 = args[0];
		arg2 = args[1];
		this.cont = cont;
	}

	@Override
	public int arity() {
		return 2;
	}

	@Override
	public String toString() {
		return "skip(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;
		int n;
		Object stream = null;

		// Char
		a2 = a2.dereference();
		if (a2.isVariable()) {
			throw new PInstantiationException(this, 2);
		}
		if (!a2.isInteger()) {
			try {
				a2 = Arithmetic.evaluate(a2);
			} catch (BuiltinException e) {
				e.goal = this;
				e.argNo = 2;
				throw e;
			}
		}
		n = ((NumberTerm) a2).intValue();
		if (!Character.isDefined(n)) {
			throw new RepresentationException(this, 2, "character_code");
		}
		// S_or_a
		a1 = a1.dereference();
		if (a1.isVariable()) {
			throw new PInstantiationException(this, 1);
		} else if (a1.isSymbol()) {
			if (!engine.getStreamManager().containsKey(a1)) {
				throw new ExistenceException(this, 1, "stream", a1, "");
			}
			stream = ((JavaObjectTerm) engine.getStreamManager().get(a1))
					.object();
		} else if (a1.isJavaObject()) {
			stream = ((JavaObjectTerm) a1).object();
		} else {
			throw new IllegalDomainException(this, 1, "stream_or_alias", a1);
		}
		if (!(stream instanceof PushbackReader)) {
			throw new PermissionException(this, "input", "stream", a1, "");
		}
		// skip
		try {
			PushbackReader in = (PushbackReader) stream;
			int c = in.read();
			while (c != n) {
				c = in.read();
				if (c == -1) {
					return cont;
				}
				if (!Character.isDefined(c)) {
					throw new RepresentationException(this, 0, "character");
				}
			}
			return cont;
		} catch (IOException e) {
			throw new TermException(new JavaObjectTerm(e));
		}
	}
}
