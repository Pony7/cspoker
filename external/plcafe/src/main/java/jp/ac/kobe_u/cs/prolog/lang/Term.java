package jp.ac.kobe_u.cs.prolog.lang;

import java.io.Serializable;

/**
 * The superclass of classes for term structures. The subclasses of
 * <code>Term</code> must override the <code>unify</code> method.
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public abstract class Term implements Serializable, Comparable<Term> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3688351765250076694L;
	/** Holds an integer value <code>0</code>. */
	public final static int EQUAL = 0;
	/** Holds an integer value <code>1</code>. */
	public final static int AFTER = 1;
	/** Holds an integer value <code>-1</code>. */
	public final static int BEFORE = -1;

	/**
	 * Checks whether the argument term is unified with this one.
	 * 
	 * @param t
	 *            the term to be unified with.
	 * @param trail
	 *            Trail Stack.
	 * @return <code>true</code> if succeeds, otherwise <code>false</code>.
	 */
	abstract public boolean unify(Term t, Trail trail);

	/**
	 * Check whether this term is a logical variable.
	 * 
	 * @return <code>true</code> if <code>this instanceof VariableTerm</code>,
	 *         otherwise <code>false</code>.
	 * @see VariableTerm
	 */
	public abstract boolean isVariable();

	/**
	 * Check whether this term is an integer.
	 * 
	 * @return <code>true</code> if <code>this instanceof IntegerTerm</code>,
	 *         otherwise <code>false</code>.
	 * @see IntegerTerm
	 */
	public abstract boolean isInteger();

	/**
	 * Check whether this term is a float.
	 * 
	 * @return <code>true</code> if <code>this instanceof DoubleTerm</code>,
	 *         otherwise <code>false</code>.
	 * @see DoubleTerm
	 */
	public abstract boolean isDouble();

	/**
	 * Check whether this term is a number.
	 * 
	 * @return <code>true</code> if
	 *         <code>this instanceof IntegerTerm || this instanceof DoubleTerm</code>
	 *         , otherwise <code>false</code>.
	 * @see IntegerTerm
	 * @see DoubleTerm
	 */
	public abstract boolean isNumber();

	/**
	 * Check whether this term is an atom.
	 * 
	 * @return <code>true</code> if <code>this instanceof SymbolTerm</code>,
	 *         otherwise <code>false</code>.
	 * @see SymbolTerm
	 */
	public abstract boolean isSymbol();

	/** Check whether this term is an empty list. */
	public boolean isNil() {
		return Prolog.Nil.equals(this);
	}

	/**
	 * Check whether this term is a list structure.
	 * 
	 * @return <code>true</code> if <code>this instanceof ListTerm</code>,
	 *         otherwise <code>false</code>.
	 * @see ListTerm
	 */
	public abstract boolean isList();

	/**
	 * Check whether this term is a compound term.
	 * 
	 * @return <code>true</code> if <code>this instanceof StructureTerm</code>,
	 *         otherwise <code>false</code>.
	 * @see StructureTerm
	 */
	public abstract boolean isStructure();

	/**
	 * Check whether this term is a java term.
	 * 
	 * @return <code>true</code> if <code>this instanceof JavaObjectTerm</code>,
	 *         otherwise <code>false</code>.
	 * @see JavaObjectTerm
	 */
	public abstract boolean isJavaObject();

	/**
	 * Check whether this term is a closure term.
	 * 
	 * @return <code>true</code> if <code>this instanceof ClosureTerm</code>,
	 *         otherwise <code>false</code>.
	 * @see ClosureTerm
	 */
	public abstract boolean isClosure();

	/**
	 * Check whether this object is convertible with the given Java class type.
	 * 
	 * @param type
	 *            the Java class type to compare with.
	 * @return <code>true</code> if this is convertible with <code>type</code>.
	 *         Otherwise <code>false</code>.
	 * @see #convertible(Class, Class)
	 */
	public boolean convertible(Class type) {
		return convertible(getClass(), type);
	}

	/** Returns a copy of this object. */
	protected Term copy(Prolog engine) {
		return this;
	}

	/** Returns the dereference value of this term. */
	public Term dereference() {
		return this;
	}

	/**
	 * Check whether this term is a ground term.
	 * 
	 * @return <code>true</code> if ground, otherwise <code>false</code>.
	 */
	public boolean isGround() {
		return true;
	}

	/**
	 * Returns a Java object that corresponds to this term if defined in
	 * <em>Prolog Cafe interoperability with Java</em>. Otherwise, returns
	 * <code>this</code>.
	 * 
	 * @return a Java object if defined in
	 *         <em>Prolog Cafe interoperability with Java</em>, otherwise
	 *         <code>this</code>.
	 */
	public Object toJava() {
		return this;
	}

	/** Returns a quoted string representation of this term. */
	public String toQuotedString() {
		return this.toString();
	}

	/**
	 * Check whether there is a widening conversion from <code>from</code> to
	 * <code>to</code>.
	 */
	protected static boolean convertible(Class from, Class<?> to) {
		if (from == null) {
			return !to.isPrimitive();
		}
		if (to.isAssignableFrom(from)) {
			return true;
		} else if (to.isPrimitive()) {
			if (from.equals(Boolean.class)) {
				return to.equals(Boolean.TYPE);
			} else if (from.equals(Byte.class)) {
				return to.equals(Byte.TYPE) || to.equals(Short.TYPE)
						|| to.equals(Integer.TYPE) || to.equals(Long.TYPE)
						|| to.equals(Float.TYPE) || to.equals(Double.TYPE);
			} else if (from.equals(Short.class)) {
				return to.equals(Short.TYPE) || to.equals(Integer.TYPE)
						|| to.equals(Long.TYPE) || to.equals(Float.TYPE)
						|| to.equals(Double.TYPE);
			} else if (from.equals(Character.class)) {
				return to.equals(Character.TYPE) || to.equals(Integer.TYPE)
						|| to.equals(Long.TYPE) || to.equals(Float.TYPE)
						|| to.equals(Double.TYPE);
			} else if (from.equals(Integer.class)) {
				return to.equals(Integer.TYPE) || to.equals(Long.TYPE)
						|| to.equals(Float.TYPE) || to.equals(Double.TYPE);
			} else if (from.equals(Long.class)) {
				return to.equals(Long.TYPE) || to.equals(Float.TYPE)
						|| to.equals(Double.TYPE);
			} else if (from.equals(Float.class)) {
				return to.equals(Float.TYPE) || to.equals(Double.TYPE);
			} else if (from.equals(Double.class)) {
				return to.equals(Double.TYPE);
			}
		}
		return false;
	}

	/** Checks whether a given object is an instance of Prolog term. */
	public static boolean instanceOfTerm(Object obj) {
		return obj instanceof VariableTerm || obj instanceof IntegerTerm
				|| obj instanceof DoubleTerm || obj instanceof SymbolTerm
				|| obj instanceof ListTerm || obj instanceof StructureTerm
				|| obj instanceof JavaObjectTerm || obj instanceof ClosureTerm;
	}
}
