package jp.ac.kobe_u.cs.prolog.lang;

/**
 * Floating point number. The class <code>DoubleTerm</code> wraps a value of
 * primitive type <code>double</code>.
 * 
 * <pre>
 * Term t = new DoubleTerm(3.3333);
 * double d = ((DoubleTerm) t).doubleValue();
 * </pre>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class DoubleTerm extends NumberTerm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1967983685420691435L;
	/**
	 * Holds a <code>double</code> value that this <code>DoubleTerm</code>
	 * represents.
	 */
	protected final double val;

	/**
	 * Constructs a new Prolog floating point number that represents the
	 * specified <code>double</code> value.
	 */
	public DoubleTerm(double i) {
		val = i;
	}

	/**
	 * Returns the value of <code>val</code>.
	 * 
	 * @see #val
	 */
	public double value() {
		return val;
	}

	/* Term */
	@Override
	public boolean unify(Term t, Trail trail) {
		if (t.isVariable()) {
			return ((VariableTerm) t).unify(this, trail);
		}
		if (!t.isDouble()) {
			return false;
		}
		return val == ((DoubleTerm) t).value();
	}

	/**
	 * @return the <code>boolean</code> whose value is
	 *         <code>convertible(Double.class, type)</code>.
	 * @see Term#convertible(Class, Class)
	 */
	@Override
	public boolean convertible(Class type) {
		return convertible(Double.class, type);
	}

	// protected Term copy(Prolog engine) { return new DoubleTerm(val); }

	/**
	 * Returns a <code>java.lang.Double</code> corresponds to this
	 * <code>DoubleTerm</code> according to
	 * <em>Prolog Cafe interoperability with Java</em>.
	 * 
	 * @return a <code>java.lang.Double</code> object equivalent to this
	 *         <code>DoubleTerm</code>.
	 */
	@Override
	public Object toJava() {
		return new Double(val);
	}

	/* Object */
	/** Returns a string representation of this <code>DoubleTerm</code>. */
	@Override
	public String toString() {
		return Double.toString(val);
	}

	/**
	 * Checks <em>term equality</em> of two terms. The result is
	 * <code>true</code> if and only if the argument is an instance of
	 * <code>DoubleTerm</code> and has the same <code>double</code> value as
	 * this object.
	 * 
	 * @param obj
	 *            the object to compare with. This must be dereferenced.
	 * @return <code>true</code> if the given object represents a Prolog
	 *         floating point number equivalent to this <code>DoubleTerm</code>,
	 *         false otherwise.
	 * @see #compareTo
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DoubleTerm)) {
			return false;
		}
		return Double.doubleToLongBits(val) == Double
				.doubleToLongBits(((DoubleTerm) obj).val);
	}

	@Override
	public int hashCode() {
		long bits = Double.doubleToLongBits(val);
		return (int) (bits ^ bits >>> 32);
	}

	/* Comparable */
	/**
	 * Compares two terms in <em>Prolog standard order of terms</em>.<br>
	 * It is noted that <code>t1.compareTo(t2) == 0</code> has the same
	 * <code>boolean</code> value as <code>t1.equals(t2)</code>.
	 * 
	 * @param anotherTerm
	 *            the term to compared with. It must be dereferenced.
	 * @return the value <code>0</code> if two terms are identical; a value less
	 *         than <code>0</code> if this term is <em>before</em> the
	 *         <code>anotherTerm</code>; and a value greater than <code>0</code>
	 *         if this term is <em>after</em> the <code>anotherTerm</code>.
	 */
	public int compareTo(Term anotherTerm) { // anotherTerm must be dereferenced
		if (anotherTerm.isVariable()) {
			return AFTER;
		}
		if (!anotherTerm.isDouble()) {
			return BEFORE;
		}
		return Double.compare(val, ((DoubleTerm) anotherTerm).value());
	}

	/* NumberTerm */
	@Override
	public int intValue() {
		return (int) val;
	}

	@Override
	public long longValue() {
		return (long) val;
	}

	@Override
	public double doubleValue() {
		return val;
	}

	@Override
	public int arithCompareTo(NumberTerm t) {
		return Double.compare(val, t.doubleValue());
	}

	@Override
	public NumberTerm abs() {
		return new DoubleTerm(Math.abs(val));
	}

	@Override
	public NumberTerm acos() {
		return new DoubleTerm(Math.acos(val));
	}

	@Override
	public NumberTerm add(NumberTerm t) {
		return new DoubleTerm(val + t.doubleValue());
	}

	/**
	 * Throws a <code>type_error</code>.
	 * 
	 * @exception IllegalTypeException
	 */
	@Override
	public NumberTerm and(NumberTerm t) {
		throw new IllegalTypeException("integer", this);
	}

	// public NumberTerm and(NumberTerm t) { return new
	// IntegerTerm(this.intValue() & t.intValue()); }

	@Override
	public NumberTerm asin() {
		return new DoubleTerm(Math.asin(val));
	}

	@Override
	public NumberTerm atan() {
		return new DoubleTerm(Math.atan(val));
	}

	@Override
	public NumberTerm ceil() {
		return new IntegerTerm((int) Math.ceil(val));
	}

	@Override
	public NumberTerm cos() {
		return new DoubleTerm(Math.cos(val));
	}

	/**
	 * @exception EvaluationException
	 *                if the given argument <code>NumberTerm</code> represents
	 *                <coe>0</code>.
	 */
	@Override
	public NumberTerm divide(NumberTerm t) {
		if (t.doubleValue() == 0) {
			throw new EvaluationException("zero_divisor");
		}
		return new DoubleTerm(val / t.doubleValue());
	}

	@Override
	public NumberTerm exp() {
		return new DoubleTerm(Math.exp(val));
	}

	@Override
	public NumberTerm floatIntPart() {
		return new DoubleTerm(Math.signum(val) * Math.floor(Math.abs(val)));
	}

	@Override
	public NumberTerm floatFractPart() {
		return new DoubleTerm(val - Math.signum(val)
				* Math.floor(Math.abs(val)));
	}

	@Override
	public NumberTerm floor() {
		return new IntegerTerm((int) Math.floor(val));
	}

	/**
	 * Throws a <code>type_error</code>.
	 * 
	 * @exception IllegalTypeException
	 */
	@Override
	public NumberTerm intDivide(NumberTerm t) {
		throw new IllegalTypeException("integer", this);
	}

	// public NumberTerm intDivide(NumberTerm t) { return new
	// IntegerTerm((int)(this.intValue() / t.intValue())); }

	/**
	 * @exception EvaluationException
	 *                if this object represents <coe>0</code>.
	 */
	@Override
	public NumberTerm log() {
		if (val == 0) {
			throw new EvaluationException("undefined");
		}
		return new DoubleTerm(Math.log(val));
	}

	@Override
	public NumberTerm max(NumberTerm t) {
		return new DoubleTerm(Math.max(val, t.doubleValue()));
	}

	@Override
	public NumberTerm min(NumberTerm t) {
		return new DoubleTerm(Math.min(val, t.doubleValue()));
	}

	/**
	 * Throws a <code>type_error</code>.
	 * 
	 * @exception IllegalTypeException
	 */
	@Override
	public NumberTerm mod(NumberTerm t) {
		throw new IllegalTypeException("integer", this);
	}

	// public NumberTerm mod(NumberTerm t) { return new
	// IntegerTerm(this.intValue() % t.intValue()); }

	@Override
	public NumberTerm multiply(NumberTerm t) {
		return new DoubleTerm(val * t.doubleValue());
	}

	@Override
	public NumberTerm negate() {
		return new DoubleTerm(-val);
	}

	/**
	 * Throws a <code>type_error</code>.
	 * 
	 * @exception IllegalTypeException
	 */
	@Override
	public NumberTerm not() {
		throw new IllegalTypeException("integer", this);
	}

	// public NumberTerm not() { return new IntegerTerm(~ this.intValue()); }

	/**
	 * Throws a <code>type_error</code>.
	 * 
	 * @exception IllegalTypeException
	 */
	@Override
	public NumberTerm or(NumberTerm t) {
		throw new IllegalTypeException("integer", this);
	}

	// public NumberTerm or(NumberTerm t) { return new
	// IntegerTerm(this.intValue() | t.intValue()); }

	@Override
	public NumberTerm pow(NumberTerm t) {
		return new DoubleTerm(Math.pow(val, t.doubleValue()));
	}

	@Override
	public NumberTerm rint() {
		return new DoubleTerm(Math.rint(val));
	}

	@Override
	public NumberTerm round() {
		return new IntegerTerm((int) Math.round(val));
	}

	/**
	 * Throws a <code>type_error</code>.
	 * 
	 * @exception IllegalTypeException
	 */
	@Override
	public NumberTerm shiftLeft(NumberTerm t) {
		throw new IllegalTypeException("integer", this);
	}

	/**
	 * Throws a <code>type_error</code>.
	 * 
	 * @exception IllegalTypeException
	 */
	@Override
	public NumberTerm shiftRight(NumberTerm t) {
		throw new IllegalTypeException("integer", this);
	}

	@Override
	public NumberTerm signum() {
		return new DoubleTerm(Math.signum(val));
	}

	@Override
	public NumberTerm sin() {
		return new DoubleTerm(Math.sin(val));
	}

	/**
	 * @exception EvaluationException
	 *                if this object represents a floating point number less
	 *                than <coe>0</code>.
	 */
	@Override
	public NumberTerm sqrt() {
		if (val < 0) {
			throw new EvaluationException("undefined");
		}
		return new DoubleTerm(Math.sqrt(val));
	}

	@Override
	public NumberTerm subtract(NumberTerm t) {
		return new DoubleTerm(val - t.doubleValue());
	}

	@Override
	public NumberTerm tan() {
		return new DoubleTerm(Math.tan(val));
	}

	@Override
	public NumberTerm toDegrees() {
		return new DoubleTerm(Math.toDegrees(val));
	}

	@Override
	public NumberTerm toFloat() {
		return this;
	}

	@Override
	public NumberTerm toRadians() {
		return new DoubleTerm(Math.toRadians(val));
	}

	@Override
	public NumberTerm truncate() {
		if (val >= 0) {
			return new IntegerTerm((int) Math.floor(val));
		} else {
			return new IntegerTerm((int) (-1 * Math.floor(Math.abs(val))));
		}
	}

	/**
	 * Throws a <code>type_error</code>.
	 * 
	 * @exception IllegalTypeException
	 */
	@Override
	public NumberTerm xor(NumberTerm t) {
		throw new IllegalTypeException("integer", this);
	}

	@Override
	public boolean isClosure() {
		return false;
	}

	@Override
	public boolean isDouble() {
		return true;
	}

	@Override
	public boolean isInteger() {
		return false;
	}

	@Override
	public boolean isJavaObject() {
		return false;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public boolean isSymbol() {
		return false;
	}

	@Override
	public boolean isVariable() {
		return false;
	}
}
