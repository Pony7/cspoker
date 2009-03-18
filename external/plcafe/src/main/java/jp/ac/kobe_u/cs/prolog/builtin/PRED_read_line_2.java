package jp.ac.kobe_u.cs.prolog.builtin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PushbackReader;

import jp.ac.kobe_u.cs.prolog.lang.ExistenceException;
import jp.ac.kobe_u.cs.prolog.lang.IllegalDomainException;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.ListTerm;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.PermissionException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.RepresentationException;
import jp.ac.kobe_u.cs.prolog.lang.Term;
import jp.ac.kobe_u.cs.prolog.lang.TermException;

/**
 * <code>read_line/2</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_read_line_2 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7795452279957891103L;
	private Term arg1, arg2;

	public PRED_read_line_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_read_line_2() {
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
		return "read_line(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;
		Object stream = null;
		String line;
		char[] chars;
		Term t;

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
		// read line
		try {
			line = new BufferedReader((PushbackReader) stream).readLine();
			if (line == null) { // end_of_stream
				if (!a2.unify(new IntegerTerm(-1), engine.trail)) {
					return engine.fail();
				}
				return cont;
			}
			chars = line.toCharArray();
			t = Prolog.Nil;
			for (int i = chars.length; i > 0; i--) {
				if (!Character.isDefined((int) chars[i - 1])) {
					throw new RepresentationException(this, 0, "character");
				}
				t = new ListTerm(new IntegerTerm(chars[i - 1]), t);
			}
			if (!a2.unify(t, engine.trail)) {
				return engine.fail();
			}
			return cont;
		} catch (IOException e) {
			throw new TermException(new JavaObjectTerm(e));
		}
	}
}
