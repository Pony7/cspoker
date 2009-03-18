package jp.ac.kobe_u.cs.prolog.builtin;

import jp.ac.kobe_u.cs.prolog.lang.BlockPredicate;
import jp.ac.kobe_u.cs.prolog.lang.JavaException;
import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.OutOfLoop;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.PrologException;
import jp.ac.kobe_u.cs.prolog.lang.SystemException;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>'$begin_exception'/1</code><br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.2
 */
class PRED_$begin_exception_1 extends BlockPredicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1479739045309859209L;
	Term arg1;

	public PRED_$begin_exception_1(Term a1, Predicate cont) {
		arg1 = a1;
		this.cont = cont;
	}

	public PRED_$begin_exception_1() {
	}

	@Override
	public void setArgument(Term[] args, Predicate cont) {
		arg1 = args[0];
		this.cont = cont;
	}

	@Override
	public int arity() {
		return 1;
	}

	@Override
	public String toString() {
		return "$begin_exception(" + arg1 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1;
		a1 = arg1;

		if (!a1.unify(new JavaObjectTerm(this), engine.trail)) {
			return engine.fail();
		}
		Predicate code = cont;
		int B = engine.stack.top();
		outOfScope = false;
		outOfLoop = false;
		engine.trail.add(new OutOfLoop(this));

		try {
			main_loop: while (true) {
				while (engine.exceptionRaised == 0) {
					// TODO check if this is correctly removed?
					// if (engine.control.thread == null)
					// break main_loop;
					if (outOfLoop) {
						break main_loop;
					}
					code = code.exec(engine);
				}
				switch (engine.exceptionRaised) {
				case 1: // halt/0
					break main_loop;
				case 2: // freeze/2
					throw new SystemException("freeze/2 is not supported yet");
					// Do something here
					// engine.exceptionRaised = 0 ;
					// break
				default:
					break main_loop;
				}
			}
		} catch (PrologException e) {
			if (outOfScope) {
				throw e;
			}
			engine.setException(engine.copy(e.getMessageTerm()));
			engine.cut(B);
			return engine.fail();
		} catch (Exception e) {
			if (outOfScope) {
				throw new JavaException(e);
			}
			engine.setException(new JavaObjectTerm(e));
			engine.cut(B);
			return engine.fail();
		}
		return code;
	}
}
