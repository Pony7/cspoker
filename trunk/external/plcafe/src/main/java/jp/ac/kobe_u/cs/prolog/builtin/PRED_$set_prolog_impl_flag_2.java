package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$set_prolog_impl_flag'/2</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$set_prolog_impl_flag_2 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8346566972830188056L;
	private final static SymbolTerm CHAR_CONVERSION = SymbolTerm
			.makeSymbol("char_conversion");
	private final static SymbolTerm DEBUG = SymbolTerm.makeSymbol("debug");
	private final static SymbolTerm UNKNOWN = SymbolTerm.makeSymbol("unknown");
	private final static SymbolTerm DOUBLE_QUOTES = SymbolTerm
			.makeSymbol("double_quotes");
	private final static SymbolTerm PRINT_STACK_TRACE = SymbolTerm
			.makeSymbol("print_stack_trace");

	public Term arg1, arg2;

	public PRED_$set_prolog_impl_flag_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_$set_prolog_impl_flag_2() {
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
		return "$set_prolog_impl_flag(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;
		a1 = a1.dereference();
		a2 = a2.dereference();

		if (a1.equals(CHAR_CONVERSION)) {
			if (!a2.isSymbol()) {
				return engine.fail();
			}
			engine.setCharConversion(((SymbolTerm) a2).name());
		} else if (a1.equals(DEBUG)) {
			if (!a2.isSymbol()) {
				return engine.fail();
			}
			engine.setDebug(((SymbolTerm) a2).name());
		} else if (a1.equals(UNKNOWN)) {
			if (!a2.isSymbol()) {
				return engine.fail();
			}
			engine.setUnknown(((SymbolTerm) a2).name());
		} else if (a1.equals(DOUBLE_QUOTES)) {
			if (!a2.isSymbol()) {
				return engine.fail();
			}
			engine.setDoubleQuotes(((SymbolTerm) a2).name());
		} else if (a1.equals(PRINT_STACK_TRACE)) {
			if (!a2.isSymbol()) {
				return engine.fail();
			}
			engine.setPrintStackTrace(((SymbolTerm) a2).name());
		} else {
			return engine.fail();
		}
		return cont;
	}
}
