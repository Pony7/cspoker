package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$get_prolog_impl_flag'/2</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
class PRED_$get_prolog_impl_flag_2 extends Predicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7104696345415927316L;
	private final static SymbolTerm TRUE = SymbolTerm.makeSymbol("true");
	private final static SymbolTerm FALSE = SymbolTerm.makeSymbol("false");
	private final static SymbolTerm BOUNDED = SymbolTerm.makeSymbol("bounded");
	private final static SymbolTerm MAX_INTEGER = SymbolTerm
			.makeSymbol("max_integer");
	private final static SymbolTerm MIN_INTEGER = SymbolTerm
			.makeSymbol("min_integer");
	private final static SymbolTerm INTEGER_ROUNDING_FUNCTION = SymbolTerm
			.makeSymbol("integer_rounding_function");
	private final static SymbolTerm CHAR_CONVERSION = SymbolTerm
			.makeSymbol("char_conversion");
	private final static SymbolTerm DEBUG = SymbolTerm.makeSymbol("debug");
	private final static SymbolTerm MAX_ARITY = SymbolTerm
			.makeSymbol("max_arity");
	private final static SymbolTerm UNKNOWN = SymbolTerm.makeSymbol("unknown");
	private final static SymbolTerm DOUBLE_QUOTES = SymbolTerm
			.makeSymbol("double_quotes");
	private final static SymbolTerm PRINT_STACK_TRACE = SymbolTerm
			.makeSymbol("print_stack_trace");

	private Term arg1, arg2;

	public PRED_$get_prolog_impl_flag_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_$get_prolog_impl_flag_2() {
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
		return "$get_prolog_impl_flag(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;
		a1 = a1.dereference();
		a2 = a2.dereference();

		if (a1.equals(BOUNDED)) {
			if (engine.isBounded()) {
				if (!a2.unify(TRUE, engine.trail)) {
					return engine.fail();
				}
			} else {
				if (!a2.unify(FALSE, engine.trail)) {
					return engine.fail();
				}
			}
		} else if (a1.equals(MAX_INTEGER)) {
			if (!a2
					.unify(new IntegerTerm(engine.getMaxInteger()),
							engine.trail)) {
				return engine.fail();
			}
		} else if (a1.equals(MIN_INTEGER)) {
			if (!a2
					.unify(new IntegerTerm(engine.getMinInteger()),
							engine.trail)) {
				return engine.fail();
			}
		} else if (a1.equals(INTEGER_ROUNDING_FUNCTION)) {
			if (!a2.unify(SymbolTerm.makeSymbol(engine
					.getIntegerRoundingFunction()), engine.trail)) {
				return engine.fail();
			}
		} else if (a1.equals(CHAR_CONVERSION)) {
			if (!a2.unify(SymbolTerm.makeSymbol(engine.getCharConversion()),
					engine.trail)) {
				return engine.fail();
			}
		} else if (a1.equals(DEBUG)) {
			if (!a2.unify(SymbolTerm.makeSymbol(engine.getDebug()),
					engine.trail)) {
				return engine.fail();
			}
		} else if (a1.equals(MAX_ARITY)) {
			if (!a2.unify(new IntegerTerm(engine.getMaxArity()), engine.trail)) {
				return engine.fail();
			}
		} else if (a1.equals(UNKNOWN)) {
			if (!a2.unify(SymbolTerm.makeSymbol(engine.getUnknown()),
					engine.trail)) {
				return engine.fail();
			}
		} else if (a1.equals(DOUBLE_QUOTES)) {
			if (!a2.unify(SymbolTerm.makeSymbol(engine.getDoubleQuotes()),
					engine.trail)) {
				return engine.fail();
			}
		} else if (a1.equals(PRINT_STACK_TRACE)) {
			if (!a2.unify(SymbolTerm.makeSymbol(engine.getPrintStackTrace()),
					engine.trail)) {
				return engine.fail();
			}
		} else {
			return engine.fail();
		}
		return cont;
	}
}
