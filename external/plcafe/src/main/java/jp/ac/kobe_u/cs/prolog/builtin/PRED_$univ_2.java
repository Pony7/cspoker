package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.IllegalDomainException;
import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
import jp.ac.kobe_u.cs.prolog.lang.ListTerm;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$univ'/2</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_$univ_2 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5471407918613159981L;
	private final static SymbolTerm SYM_DOT = SymbolTerm.makeSymbol(".");
	private final static SymbolTerm SYM_NIL = SymbolTerm.makeSymbol("[]");
	private Term arg1, arg2;

	public PRED_$univ_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_$univ_2() {
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
		return "=..(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;

		a1 = a1.dereference();
		if (a1.isSymbol() || a1.isNumber() || a1.isJavaObject()
				|| a1.isClosure()) {
			if (!a2.unify(new ListTerm(a1, SYM_NIL), engine.trail)) {
				return engine.fail();
			}
		} else if (a1.isList()) {
			Term t = new ListTerm(((ListTerm) a1).cdr(), SYM_NIL);
			t = new ListTerm(((ListTerm) a1).car(), t);
			t = new ListTerm(SYM_DOT, t);
			if (!a2.unify(t, engine.trail)) {
				return engine.fail();
			}
		} else if (a1.isStructure()) {
			SymbolTerm sym = SymbolTerm.makeSymbol(((StructureTerm) a1)
					.functor().name());
			Term[] args = ((StructureTerm) a1).args();
			Term t = SYM_NIL;
			for (int i = args.length; i > 0; i--) {
				t = new ListTerm(args[i - 1], t);
			}
			if (!a2.unify(new ListTerm(sym, t), engine.trail)) {
				return engine.fail();
			}
		} else if (a1.isVariable()) {
			a2 = a2.dereference();
			if (a2.isVariable()) {
				throw new PInstantiationException(this, 2);
			} else if (a2.equals(SYM_NIL)) {
				throw new IllegalDomainException(this, 2, "non_empty_list", a2);
			} else if (!a2.isList()) {
				throw new IllegalTypeException(this, 2, "list", a2);
			}
			Term head = ((ListTerm) a2).car().dereference();
			Term tail = ((ListTerm) a2).cdr().dereference();
			if (head.isVariable()) {
				throw new PInstantiationException(this, 2);
			}
			if (tail.equals(SYM_NIL)) {
				if (head.isSymbol() || head.isNumber() || head.isJavaObject()
						|| head.isClosure()) {
					if (!a1.unify(head, engine.trail)) {
						return engine.fail();
					}
					return cont;
				} else {
					throw new IllegalTypeException(this, 2, "atomic", head);
				}
			}
			if (!head.isSymbol()) {
				throw new IllegalTypeException(this, 2, "atom", head);
			}
			Term x = tail;
			while (!x.equals(SYM_NIL)) {
				if (x.isVariable()) {
					throw new PInstantiationException(this, 2);
				}
				if (!x.isList()) {
					throw new IllegalTypeException(this, 2, "list", a2);
				}
				x = ((ListTerm) x).cdr().dereference();
			}
			int n = ((ListTerm) a2).length() - 1;
			SymbolTerm sym = SymbolTerm.makeSymbol(((SymbolTerm) head).name(),
					n);
			Term[] args = new Term[n];
			for (int i = 0; i < n; i++) {
				args[i] = ((ListTerm) tail).car().dereference();
				tail = ((ListTerm) tail).cdr().dereference();
			}
			if (!a1.unify(new StructureTerm(sym, args), engine.trail)) {
				return engine.fail();
			}
		} else {
			return engine.fail();
		}
		return cont;
	}
}
