package jp.ac.kobe_u.cs.prolog.lang;

import java.util.HashMap;

/**
 * <code>Hashtable&lt;Term,Term&gt;</code>.<br>
 * <font color="red">This document is under construction.</font>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.0
 */
public class HashMapOfTerm extends HashMap<Term, Term> {

	private static final long serialVersionUID = 8762581634035674684L;

	public HashMapOfTerm() {
		super();
	}

	public HashMapOfTerm(int initialCapacity) {
		super(initialCapacity);
	}

	public HashMapOfTerm(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
}