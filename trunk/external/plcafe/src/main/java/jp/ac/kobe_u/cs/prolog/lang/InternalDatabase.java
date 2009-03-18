package jp.ac.kobe_u.cs.prolog.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal database for dynamic predicates.<br>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.1
 */
public class InternalDatabase extends ArrayList<Term> {

	private static final long serialVersionUID = 3684635244195934590L;

	/* For GC */
	/** A list of reusable entry indices. */
	protected final List<Integer> reusableIndices = new ArrayList<Integer>();

	/** Constructs a new internal database. */
	public InternalDatabase() {
		super(512);
	}

	/** Constructs a new internal database with the given size. */
	public InternalDatabase(int n) {
		super(n);
	}

	@Override
	public void clear() {
		reusableIndices.clear();
		super.clear();
	}

	/** Inserts an entry to this <code>InternalDatabase</code>. */
	public int insert(Term t) {
		if (reusableIndices.isEmpty()) {
			add(t);
			return top();
		}
		int i = reusableIndices.remove(reusableIndices.size() - 1);
		add(i, t);
		return i;
	}

	/**
	 * Erases an entry with the given index from this
	 * <code>InternalDatabase</code>.
	 */
	public Term erase(int i) {
		Term term = get(i);
		reusableIndices.add(i);
		set(i, null);
		return term;
	}

	/**
	 * Returns the value of <code>top</code>.
	 * 
	 * @see #top
	 */
	public int top() {
		return size() - 1;
	}

	/** Shows the contents of this <code>InternalDatabase</code>. */
	public void show() {
		if (isEmpty()) {
			System.out.println("{internal database is empty!}");
		}
		System.out.println("{reusable indices: " + reusableIndices.toString()
				+ "}");
		for (Term term : this) {
			System.out.print("internal database[]: ");
			System.out.println(term);
		}
	}
}
