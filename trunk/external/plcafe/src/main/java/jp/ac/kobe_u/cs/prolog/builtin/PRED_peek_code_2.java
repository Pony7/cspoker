package jp.ac.kobe_u.cs.prolog.builtin;

import java.io.IOException;
import java.io.PushbackReader;

import jp.ac.kobe_u.cs.prolog.lang.ExistenceException;
import jp.ac.kobe_u.cs.prolog.lang.IllegalDomainException;
import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.PermissionException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.RepresentationException;
import jp.ac.kobe_u.cs.prolog.lang.Term;
import jp.ac.kobe_u.cs.prolog.lang.TermException;

/**
 * <code>peek_code/2</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_peek_code_2 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5579565438521482713L;
	private final static IntegerTerm INT_EOF = new IntegerTerm(-1);
	private Term arg1, arg2;

	public PRED_peek_code_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_peek_code_2() {
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
		return "peek_code(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;
		Object stream = null;

		// Char
		a2 = a2.dereference();
		if (!a2.isVariable()) {
			if (!a2.isInteger()) {
				throw new IllegalTypeException(this, 2, "integer", a2);
			}
			int n = ((IntegerTerm) a2).intValue();
			if (n != -1 && !Character.isDefined(n)) {
				throw new RepresentationException(this, 2, "in_character_code");
			}
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
		// read single character
		try {
			int c = ((PushbackReader) stream).read();
			if (c < 0) { // EOF
				if (!a2.unify(INT_EOF, engine.trail)) {
					return engine.fail();
				}
				return cont;
			}
			if (!Character.isDefined(c)) {
				throw new RepresentationException(this, 0, "character");
			}
			((PushbackReader) stream).unread(c);
			if (!a2.unify(new IntegerTerm(c), engine.trail)) {
				return engine.fail();
			}
			return cont;
		} catch (IOException e) {
			throw new TermException(new JavaObjectTerm(e));
		}
	}
}
