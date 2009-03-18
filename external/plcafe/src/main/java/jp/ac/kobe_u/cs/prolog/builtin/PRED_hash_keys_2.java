package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.ExistenceException;
import jp.ac.kobe_u.cs.prolog.lang.HashMapOfTerm;
import jp.ac.kobe_u.cs.prolog.lang.IllegalDomainException;
import jp.ac.kobe_u.cs.prolog.lang.InternalException;
import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.ListTerm;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>hash_keys/2</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class PRED_hash_keys_2 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2889209528074235249L;
	private final static SymbolTerm SYM_NIL = SymbolTerm.makeSymbol("[]");
	private Term arg1, arg2;

	public PRED_hash_keys_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_hash_keys_2() {
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
		return "hash_keys(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;

		Object hash = null;

		a1 = a1.dereference();
		if (a1.isVariable()) {
			throw new PInstantiationException(this, 1);
		} else if (a1.isSymbol()) {
			if (!engine.getHashManager().containsKey(a1)) {
				throw new ExistenceException(this, 1, "hash", a1, "");
			}
			hash = ((JavaObjectTerm) engine.getHashManager().get(a1)).object();
		} else if (a1.isJavaObject()) {
			hash = ((JavaObjectTerm) a1).object();
		} else {
			throw new IllegalDomainException(this, 1, "hash_or_alias", a1);
		}
		if (!(hash instanceof HashMapOfTerm)) {
			throw new InternalException(this + ": Hash is not HashtableOfTerm");
		}
		Term keys = SYM_NIL;
		for (Term term : ((HashMapOfTerm) hash).keySet()) {
			keys = new ListTerm(term, keys);
		}
		if (!a2.unify(keys, engine.trail)) {
			return engine.fail();
		}
		return cont;
	}
}
