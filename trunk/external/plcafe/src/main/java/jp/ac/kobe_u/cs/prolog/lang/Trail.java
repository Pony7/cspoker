package jp.ac.kobe_u.cs.prolog.lang;

import java.util.ArrayList;

/**
 * Trail stack.<br>
 * The class <code>Trail</code> represents a trail stack.<br>
 * Entries pushed to this trail stack must implement the <code>Undoable</code>
 * interface.
 * 
 * @see Undoable
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class Trail extends ArrayList<Undoable> {

	private static final long serialVersionUID = 5667307060391116602L;

	/** Holds the Prolog engine that this <code>Trail</code> belongs to. */
	public final Prolog engine;

	/** Constructs a new trail stack. */
	public Trail(Prolog _engine) {
		super(512);
		engine = _engine;
	}

	/** Constructs a new trail stack with the given size. */
	public Trail(Prolog _engine, int n) {
		super(n);
		engine = _engine;
	}

	/**
	 * Returns the value of <code>maxContents</code>.
	 * 
	 * @see #maxContents
	 */
	public int max() {
		return top();
	}

	/**
	 * Returns the value of <code>top</code>.
	 * 
	 * @see #top
	 */
	public int top() {
		return size() - 1;
	}

	/** Unwinds all entries after the value of <code>i</code>. */
	public void unwind(int i) {
		int top = top();
		while (top > i) {
			remove(top--).undo();
		}
	}

	/** Shows the contents of this <code>Trail</code>. */
	public void show() {
		if (isEmpty()) {
			System.out.println("{trail stack is empty!}");
			return;
		}
		for (Undoable u : this) {
			System.out.print("trail[]: ");
			System.out.println(u);
		}
	}

}
