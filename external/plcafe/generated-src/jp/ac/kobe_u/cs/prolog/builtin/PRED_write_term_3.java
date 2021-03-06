package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;
/*
 This file is generated by Prolog Cafe.
 PLEASE DO NOT EDIT!
*/
/**
 <code>write_term/3</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
public class PRED_write_term_3 extends Predicate {
    static Predicate _write_term_3_sub_1 = new PRED_write_term_3_sub_1();
    static Predicate _write_term_3_1 = new PRED_write_term_3_1();
    static Predicate _write_term_3_2 = new PRED_write_term_3_2();

    public Term arg1, arg2, arg3;

    public PRED_write_term_3(Term a1, Term a2, Term a3, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
    }

    public PRED_write_term_3(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        arg3 = args[2];
        this.cont = cont;
    }

    public int arity() { return 3; }

    public String toString() {
        return "write_term(" + arg1 + "," + arg2 + "," + arg3 + ")";
    }

    public Predicate exec(Prolog engine) {
        engine.aregs[1] = arg1;
        engine.aregs[2] = arg2;
        engine.aregs[3] = arg3;
        engine.cont = cont;
        engine.setB0();
        return engine.jtry(_write_term_3_1, _write_term_3_sub_1);
    }
}

class PRED_write_term_3_sub_1 extends PRED_write_term_3 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_write_term_3_2);
    }
}

class PRED_write_term_3_1 extends PRED_write_term_3 {
    public Predicate exec(Prolog engine) {
    // write_term(A,B,C):-'$write_term'(A,B,C),fail
        Term a1, a2, a3;
        Predicate p1;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // write_term(A,B,C):-['$write_term'(A,B,C),fail]
        p1 = new PRED_fail_0(cont);
        return new PRED_$write_term_3(a1, a2, a3, p1);
    }
}

class PRED_write_term_3_2 extends PRED_write_term_3 {
    public Predicate exec(Prolog engine) {
    // write_term(A,B,C):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // write_term(A,B,C):-[]
        return cont;
    }
}
