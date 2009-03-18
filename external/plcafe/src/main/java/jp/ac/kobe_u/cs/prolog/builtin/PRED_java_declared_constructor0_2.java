package jp.ac.kobe_u.cs.prolog.builtin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import jp.ac.kobe_u.cs.prolog.lang.ExistenceException;
import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
import jp.ac.kobe_u.cs.prolog.lang.JavaException;
import jp.ac.kobe_u.cs.prolog.lang.JavaObjectTerm;
import jp.ac.kobe_u.cs.prolog.lang.JavaPredicate;
import jp.ac.kobe_u.cs.prolog.lang.PInstantiationException;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;

/**
 * <code>java_declared_constructor0/2</code>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class PRED_java_declared_constructor0_2 extends JavaPredicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3121167598495928046L;
	private Term arg1, arg2;

	public PRED_java_declared_constructor0_2(Term a1, Term a2, Predicate cont) {
		arg1 = a1;
		arg2 = a2;
		this.cont = cont;
	}

	public PRED_java_declared_constructor0_2() {
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
		return "java_declared_constructor0(" + arg1 + "," + arg2 + ")";
	}

	@Override
	public Predicate exec(Prolog engine) {
		engine.setB0();
		Term a1, a2;
		a1 = arg1;
		a2 = arg2;

		Class clazz = null;
		Object instance = null;
		int arity;
		Constructor[] constrs = null;
		Term[] pArgs = null;
		Object[] jArgs = null;
		Constructor c = null;

		// 2nd. argument (unbound variable)
		a2 = a2.dereference();
		if (!a2.isVariable()) {
			throw new IllegalTypeException(this, 2, "variable", a2);
		}
		// 1st. argument (atom or callable term)
		try {
			a1 = a1.dereference();
			if (a1.isVariable()) {
				throw new PInstantiationException(this, 1);
			}
			if (!a1.isSymbol() && !a1.isStructure()) {
				throw new IllegalTypeException(this, 1, "callable", a1);
			}
			if (a1.isSymbol()) { // No argument constructor
				clazz = Class.forName(((SymbolTerm) a1).name());
				c = clazz.getDeclaredConstructor();
				if (c == null) {
					throw new ExistenceException(this, 1, "constructor", a1, "");
				}
				c.setAccessible(true);
				instance = c.newInstance();
				if (!a2.unify(toPrologTerm(instance), engine.trail)) {
					return engine.fail();
				}
				return cont;
			}
			// Parameterized constructor
			clazz = Class.forName(((StructureTerm) a1).name());
			arity = ((StructureTerm) a1).arity();
			constrs = clazz.getDeclaredConstructors();
			if (constrs.length == 0) {
				throw new ExistenceException(this, 1, "constructor", a1, "");
			}
			pArgs = ((StructureTerm) a1).args();
			jArgs = new Object[arity];
			for (int i = 0; i < arity; i++) {
				pArgs[i] = pArgs[i].dereference();
				if (!pArgs[i].isJavaObject()) {
					pArgs[i] = new JavaObjectTerm(pArgs[i]);
				}
				jArgs[i] = pArgs[i].toJava();
			}
			for (Constructor constr : constrs) {
				if (checkParameterTypes(constr.getParameterTypes(), pArgs)) {
					try {
						c = constr;
						c.setAccessible(true);
						instance = c.newInstance(jArgs);
						break; // Succeeds to create new instance
					} catch (Exception e) {
						c = null; // Back to loop
					}
				}
			}
			if (c == null) {
				throw new ExistenceException(this, 1, "constructor", a1, "");
			}
			if (!a2.unify(toPrologTerm(instance), engine.trail)) {
				return engine.fail();
			}
			return cont;
		} catch (ClassNotFoundException e) { // Class.forName(..)
			throw new JavaException(this, 1, e);
		} catch (InstantiationException e) { // Class.forName(..) or
			// Constructor.newInstance()
			throw new JavaException(this, 1, e);
		} catch (IllegalAccessException e) { // Class.forName(..) or
			// Constructor.newInstance()
			throw new JavaException(this, 1, e);
		} catch (NoSuchMethodException e) { // Class.getDeclaredConstructor()
			throw new JavaException(this, 1, e);
		} catch (SecurityException e) { // Class.getDeclaredConstructors()
			throw new JavaException(this, 1, e);
		} catch (IllegalArgumentException e) { // Constructor.newInstance()
			throw new JavaException(this, 1, e);
		} catch (InvocationTargetException e) { // Constructor.newInstance()
			throw new JavaException(this, 1, e);
		}
	}

	private Term toPrologTerm(Object obj) {
		if (Term.instanceOfTerm(obj)) {
			return (Term) obj;
		} else {
			return new JavaObjectTerm(obj);
		}
	}
}
