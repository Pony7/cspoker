package jp.ac.kobe_u.cs.prolog.lang;

import java.io.Serializable;

/**
 * Prolog thread.<br>
 * The <code>PrologControl</code> class is an implementation of
 * <em>Prolog Box Control Flow Model</em>.<br>
 * This <code>PrologControl</code> provides methods for both sequential and
 * parallel execution.
 * 
 * <pre>
 * // An example of sequential execution
 * // calls a goal &lt;code&gt;father(abraham, X)&lt;/code&gt; and get all solutions.
 * PrologControl p = new PrologControl();
 * Predicate code = new PRED_father_2();
 * Term a1 = SymbolTerm.makeSymbol(&quot;abraham&quot;);
 * Term a2 = new VariableTerm();
 * Term[] args = { a1, a2 };
 * p.setPredicate(code, args);
 * for (boolean r = p.call(); r; r = p.redo()) {
 * 	System.out.println(a2.toString());
 * }
 * </pre>
 * 
 * <pre>
 * // To get only one solution.
 * PrologControl p = new PrologControl();
 * Predicate code = new PRED_father_2();
 * Term a1 = SymbolTerm.makeSymbol(&quot;abraham&quot;);
 * Term a2 = new VariableTerm();
 * Term[] args = { a1, a2 };
 * if (p.execute(code, args))
 * 	System.out.println(a2.toString());
 * else
 * 	System.out.println(&quot;fail&quot;);
 * </pre>
 * 
 * <pre>
 * // An example of parallel execution
 * // calls &lt;code&gt;queens(4,X)&lt;/code&gt; and &lt;code&gt;queens(8,Y)&lt;/code&gt; in parallel.
 * // Usage:
 * //   % plcafe -cp queens.jar T
 * // 
 * import jp.ac.kobe_u.cs.prolog.lang.*;
 * 
 * public class T {
 * 	public static void main(String args[]) {
 * 		long t = System.currentTimeMillis();
 * 		boolean r1 = true;
 * 		boolean r2 = true;
 * 		Term a1[] = { new IntegerTerm(4), new VariableTerm() };
 * 		Term a2[] = { new IntegerTerm(8), new VariableTerm() };
 * 		PrologControl e1 = new PrologControl();
 * 		PrologControl e2 = new PrologControl();
 * 		Term v1 = new VariableTerm();
 * 		Term v2 = new VariableTerm();
 * 		e1.setPredicate(new PRED_queens_2(), a1);
 * 		e2.setPredicate(new PRED_queens_2(), a2);
 * 		System.out.println(&quot;Start&quot;);
 * 		e1.start();
 * 		e2.start();
 * 		while (r1 || r2) {
 * 			try {
 * 				Thread.sleep(10);
 * 			} catch (InterruptedException e) {
 * 			}
 * 			if (r1 &amp;&amp; e1.ready()) {
 * 				r1 = e1.next();
 * 				if (r1) {
 * 					System.out.println(&quot;Success1 = &quot; + a1[1]);
 * 					e1.cont();
 * 				} else {
 * 					System.out.println(&quot;Fail1&quot;);
 * 				}
 * 			} else if (r2 &amp;&amp; e2.ready()) {
 * 				r2 = e2.next();
 * 				if (r2) {
 * 					System.out.println(&quot;Success2 = &quot; + a2[1]);
 * 					e2.cont();
 * 				} else {
 * 					System.out.println(&quot;Fail2&quot;);
 * 				}
 * 			} else {
 * 				System.out.println(&quot;Waiting&quot;);
 * 				try {
 * 					Thread.sleep(100);
 * 				} catch (InterruptedException e) {
 * 				}
 * 			}
 * 		}
 * 		System.out.println(&quot;End&quot;);
 * 		long t1 = System.currentTimeMillis();
 * 		long t2 = t1 - t;
 * 		System.out.println(&quot;time = &quot; + t2 + &quot;msec.&quot;);
 * 	}
 * }
 * </pre>
 * 
 * @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 * @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 * @version 1.2
 */
public class PrologControl implements Runnable, Serializable {

	private static final long serialVersionUID = -2735621623554316312L;

	/** Holds a Prolog engine. */
	public Prolog engine;

	/** Holds a Prolog goal to be executed. */
	public Predicate code;

	/**
	 * A flag that indicates whether the result of goal is <code>true</code> or
	 * <code>false</code>.
	 */
	public boolean result;

	/** Constructs a new <code>PrologControl</code>. */
	public PrologControl() {
		engine = new Prolog(this);
		code = null;
		result = false;
	}

	/**
	 * Sets a goal and its arguments to this Prolog thread. An initial
	 * continuation goal (a <code>Success</code> object) is set to the
	 * <code>cont</code> field of goal <code>p</code> as continuation.
	 */
	public void setPredicate(Predicate p, Term[] args) {
		code = p;
		code.setArgument(args, new Success(this));
	}

	/**
	 * Sets a goal <code>call(t)</code> to this Prolog thread. An initial
	 * continuation goal (a <code>Success</code> object) is set to the
	 * <code>cont</code> field of <code>call(t)</code> as continuation.
	 */
	public void setPredicate(Term t) {
		try {
			Class clazz = engine.pcl.loadPredicateClass(
					"jp.ac.kobe_u.cs.prolog.builtin", "call", 1, true);
			Term[] args = { engine.copy(t) };
			code = (Predicate) clazz.newInstance();
			code.setArgument(args, new Success(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns <code>true</code> if the system succeeds to find a first solution
	 * of the given goal, <code>false</code> otherwise.<br>
	 * 
	 * This method is useful to find only one solution.<br>
	 * 
	 * This method first initilizes the Prolog engine by invoking
	 * <code>engine.init()</code>, allocates a new <code>Thread</code> object,
	 * and start the execution of the given goal. And then it stops the thread
	 * and returns <code>true</code> if the goal succeeds, <code>false</code>
	 * otherwise.
	 * 
	 * @see #run
	 */
	public boolean execute(Predicate p, Term[] args) {
		engine.init();
		code = p;
		code.setArgument(args, new Success(this));
		run();
		return result;
	}

	/**
	 * Returns <code>true</code> if the system succeeds to find a first solution
	 * of the goal, <code>false</code> otherwise.<br>
	 * 
	 * This method first invokes the <code>start()</code> method that initilizes
	 * the Prolog engine, allocates a new <code>Thread</code> object, and start
	 * the execution. And then it returns the <code>boolean</code> whose value
	 * is <code>next()</code>.
	 * 
	 * @see #start
	 * @see #next
	 */
	public boolean call() {
		start();
		return result;
	}

	/**
	 * Returns <code>true</code> if the system succeeds to find a next solution
	 * of the goal, <code>false</code> otherwise.<br>
	 * 
	 * This method first invokes the <code>cont()</code> method that sets the
	 * <code>resultReady</code> to <code>false</code> and wakes up all threads
	 * that are waiting on this object's monitor. And then it returns the
	 * <code>boolean</code> whose value is <code>next()</code>.
	 * 
	 * @see #cont
	 * @see #next
	 */
	public boolean redo() {
		cont();
		return result;
	}

	/**
	 * Is invoked when the system succeeds to find a solution.<br>
	 * 
	 * This method is invoked from the initial continuation goal (a
	 * <code>Success</code> object).<br>
	 * 
	 * This method first sets the <code>resultReady</code> and
	 * <code>result</code> to <code>true</code>. And then it wakes up all
	 * threads that are waiting by <code>notifyAll()</code>. Finally, while the
	 * <code>thread</code> is not <code>null</code> and the
	 * <code>resultReady</code> is <code>true</code>, it waits until another
	 * thread invokes the <code>notify()</code> method or the
	 * <code>notifyAll()</code> method for this object.
	 * 
	 * @see #resultReady
	 * @see #result
	 * @see #thread
	 */
	protected void success() {
		result = true;
	}

	/**
	 * Is invoked after failure of all trials.<br>
	 * 
	 * This method is invoked from the <code>run</code> method.<br>
	 * 
	 * This method first sets the <code>resultReady</code> and
	 * <code>result</code> to <code>true</code> and <code>false</code>
	 * respectively. And then it wakes up all threads that are waiting by
	 * <code>notifyAll()</code>. Finally, while the <code>thread</code> is not
	 * <code>null</code> and the <code>resultReady</code> is <code>true</code>,
	 * it waits until another thread invokes the <code>notify()</code> method or
	 * the <code>notifyAll()</code> method for this object.
	 * 
	 * @see #resultReady
	 * @see #result
	 * @see #thread
	 */
	protected void fail() {
		result = false;
	}

	/**
	 * Forces the thread to start the execution.<br>
	 * 
	 * This method initilizes the Prolog engine by invoking
	 * <code>engine.init()</code>, allocates a new <code>Thread</code> object,
	 * and start the execution. The Java Virtual Machine calls the
	 * <code>run</code> method of this thread.
	 * 
	 * @see #run
	 */
	public void start() {
		engine.init();
	}

	/**
	 * Forces the thread to continue the execution.<br>
	 * 
	 * This method sets the <code>resultReady</code> to <code>false</code>, and
	 * then wakes up all threads that are waiting by <code>notifyAll()</code>.
	 * 
	 * @see #resultReady
	 */
	public void cont() {
		engine.exceptionRaised = 0;
		run();
	}

	/**
	 * Returns <code>true</code> if the result of goal is ready and true,
	 * otherwise <code>false</code>.
	 * 
	 * @return a <code>boolean</code> whose value is
	 *         <code>(ready() &amp;&amp; result)</code>.
	 * @see #ready
	 * @see #result
	 */
	public boolean in_success() {
		return result;
	}

	/**
	 * Returns <code>true</code> if the result of goal is ready and false,
	 * otherwise <code>false</code>.
	 * 
	 * @return a <code>boolean</code> whose value is
	 *         <code>(ready() &amp;&amp; !result)</code>.
	 * @see #ready
	 * @see #result
	 */
	public boolean in_failure() {
		return !result;
	}

	/**
	 * Executes the goal.<br>
	 * 
	 * Every time finding a solution, the <code>success</code> method is
	 * invoked. And then the <code>fail</code> method is invoked after failure
	 * of all trials. Finally, the <code>stop</code> method is invoked at the
	 * end of this <code>run</code>.
	 * 
	 * @see #success
	 * @see #fail
	 * @see #stop
	 */
	public void run() {
		try {
			main_loop: while (true) {
				while (engine.exceptionRaised == 0) {
					code = code.exec(engine);
				}
				switch (engine.exceptionRaised) {
				case 1: // halt/0
					break main_loop;
				case 2: // freeze/2
					throw new SystemException("freeze/2 is not supported yet");
					// Do something here
					// engine.exceptionRaised = 0 ;
					// break;
				case 3: // success
					break main_loop;
				default:
					throw new SystemException(
							"Invalid value of exceptionRaised");
				}
			}
		} catch (PrologException e) {
			if (engine.getPrintStackTrace().equals("on")) {
				e.printStackTrace();
			} else {
				System.out.println(e.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
