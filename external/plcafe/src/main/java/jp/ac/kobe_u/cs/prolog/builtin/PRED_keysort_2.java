package jp.ac.kobe_u.cs.prolog.builtin;

import java.util.Arrays;

import jp.ac.kobe_u.cs.prolog.lang.BuiltinException;
import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
import jp.ac.kobe_u.cs.prolog.lang.JavaException;
import jp.ac.kobe_u.cs.prolog.lang.ListTerm;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>keysort/2</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PRED_keysort_2 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4716980178240583223L;
	private final static SymbolTerm SYM_HYPHEN_2 = SymbolTerm
			.makeSymbol("-", 2);
	private final static SymbolTerm Nil = SymbolTerm.makeSymbol("[]");
	Term arg1, arg2;

	public PRED_keysort_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_keysort_2() {
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
		return "keysort(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;
		int len;
		Term tmp;
		Term[] list;

		a1 = a1.dereference();
		if (a1.isVariable()) {
			throw new PInstantiationException(this, 1);
		} else if (a1.equals(Nil)) {
			if (!a2.unify(Nil, engine.trail)) {
				return engine.fail();
			}
			return cont;
		} else if (!a1.isList()) {
			throw new IllegalTypeException(this, 1, "list", a1);
		}
		len = ((ListTerm) a1).length();
		list = new Term[len];
		tmp = a1;
		for (int i = 0; i < len; i++) {
			if (!tmp.isList()) {
				throw new IllegalTypeException(this, 1, "list", a1);
			}
			list[i] = ((ListTerm) tmp).car().dereference();
			if (list[i].isVariable()) {
				throw new PInstantiationException(this, 1);
			}
			if (!list[i].isStructure()) {
				throw new IllegalTypeException(this, 1, "key_value_pair", a1);
			}
			if (!((StructureTerm) list[i]).functor().equals(SYM_HYPHEN_2)) {
				throw new IllegalTypeException(this, 1, "key_value_pair", a1);
			}
			tmp = ((ListTerm) tmp).cdr().dereference();
		}
		if (!tmp.equals(Nil)) {
			throw new PInstantiationException(this, 1);
		}
		try {
			Arrays.sort(list, new KeySortComparator());
		} catch (BuiltinException e) {
			e.goal = this;
			e.argNo = 1;
			throw e;
		} catch (ClassCastException e1) {
			throw new JavaException(this, 1, e1);
		}
		tmp = Nil;
		for (int i = list.length - 1; i >= 0; i--) {
			tmp = new ListTerm(list[i], tmp);
		}
		if (!a2.unify(tmp, engine.trail)) {
			return engine.fail();
		}
		return cont;
	}
}

class KeySortComparator implements java.util.Comparator<Term> {
	public int compare(Term t1, Term t2) {
		Term arg1 = ((StructureTerm) t1).args()[0].dereference();
		Term arg2 = ((StructureTerm) t2).args()[0].dereference();
		return arg1.compareTo(arg2);
	}
}
