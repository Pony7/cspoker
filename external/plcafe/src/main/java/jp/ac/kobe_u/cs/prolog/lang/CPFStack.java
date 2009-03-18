package jp.ac.kobe_u.cs.prolog.lang;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Choice point frame stack.<br>
 * The <code>CPFStack</code> class represents a stack of choice point frames.<br>
 * Each choice point frame has the following fields:
 * <ul>
 * <li><em>arguments</em>
 * <li><em>continuation goal</em>
 * <li><em>next clause</em>
 * <li><em>trail pointer</em>
 * <li><em>cut point</em>
 * <li><em>time stamp</em>
 * </ul>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class CPFStack extends ArrayList<CPFStack.CPFEntry> {

	private static final long serialVersionUID = 3998065220451465985L;

	private int top = -1;

	/** Constructs a new choice point frame stack. */
	public CPFStack(Prolog _engine) {
		super(512);
	}

	/** Constructs a new choice point frame stack with the given size. */
	public CPFStack(Prolog _engine, int n) {
		super(n);
	}

	/**
	 * Create a new choice point frame.
	 * 
	 * @param args
	 *            <em>arguments</em>
	 * @param p
	 *            a <em>continuation goal</em>
	 */
	public void create(Term[] args, Predicate p) {
		add(new CPFEntry(args, p));
	}

	@Override
	public boolean add(CPFEntry e) {
		++top;
		return super.add(e);
	}

	@Override
	public void clear() {
		super.clear();
		top = -1;
	}

	public void removeLast() {
		remove(top--);
	}

	/** Discards all choice points after the value of <code>i</code>. */
	public void cut(int i) {
		while (top > i) {
			removeLast();
		}
	}

	/**
	 * Returns the value of <code>top</code>.
	 * 
	 * @see #top
	 */
	public final int top() {
		return top;
	}

	/**
	 * Returns the value of <code>maxContents</code>.
	 * 
	 * @see #maxContents
	 */
	public int max() {
		return top();
	}

	/** Returns the <em>arguments</em> of current choice point frame. */
	public Term[] getArgs() {
		return get(top).args;
	}

	/** Returns the <em>continuation goal</em> of current choice point frame. */
	public Predicate getCont() {
		return get(top).cont;
	}

	/** Returns the <em>time stamp</em> of current choice point frame. */
	public final long getTimeStamp() {
		return get(top).timeStamp;
	}

	/** Sets the <em>time stamp</em> of current choice point frame. */
	public void setTimeStamp(long t) {
		get(top).timeStamp = t;
	}

	/** Returns the <em>next clause</em> of current choice point frame. */
	public Predicate getBP() {
		return get(top).bp;
	}

	/** Sets the <em>next clause</em> of current choice point frame. */
	public void setBP(Predicate p) {
		get(top).bp = p;
	}

	/** Returns the <em>trail pointer</em> of current choice point frame. */
	public int getTR() {
		return get(top).tr;
	}

	/** Sets the <em>trail pointer</em> of current choice point frame. */
	public void setTR(int i) {
		get(top).tr = i;
	}

	/** Returns the <em>cut point</em> of current choice point frame. */
	public int getB0() {
		return get(top).b0;
	}

	/** Sets the <em>cut point</em> of current choice point frame. */
	public void setB0(int i) {
		get(top).b0 = i;
	}

	/** Shows the contents of this <code>CPFStack</code>. */
	public void show() {
		if (isEmpty()) {
			System.out.println("{choice point stack is empty!}");
			return;
		}
		for (CPFEntry entry : this) {
			System.out.print("stack[]: ");
			System.out.println(entry);
		}
	}

	/**
	 * Choice point frame.<br>
	 * 
	 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
	 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
	 * @version 1.0
	 */
	public class CPFEntry implements Serializable {

		private static final long serialVersionUID = -3761557812958612952L;

		public long timeStamp;
		public final Term[] args; // argument register
		public final Predicate cont; // continuation goal
		public Predicate bp; // next cluase
		public int tr; // trail pointer
		public int b0; // cut point

		public CPFEntry(Term[] args, Predicate cont) {
			this.args = args;
			this.cont = cont;
		}

		@Override
		public String toString() {
			String t = " time:" + timeStamp + "\n";
			t = t + "args:";
			for (Term arg : args) {
				t = t + arg + " ";
			}
			t = t + "\n";
			t = t + " cont:" + cont + "\n";
			t = t + " bp:" + bp + "\n";
			t = t + " tr:" + tr + "\n";
			t = t + " b0:" + b0 + "\n";
			return t;
		}
	}

}
