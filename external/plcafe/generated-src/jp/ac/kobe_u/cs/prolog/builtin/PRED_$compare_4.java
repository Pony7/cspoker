package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.Term;
import jp.ac.kobe_u.cs.prolog.lang.VariableTerm;
/*
 This file is generated by Prolog Cafe.
 PLEASE DO NOT EDIT!
*/
/**
 <code>'$compare'/4</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$compare_4 extends Predicate {
    static IntegerTerm si1 = new IntegerTerm(0);
    static Predicate _$compare_4_var = new PRED_$compare_4_var();
    static Predicate _$compare_4_var_1 = new PRED_$compare_4_var_1();
    static Predicate _$compare_4_1 = new PRED_$compare_4_1();
    static Predicate _$compare_4_2 = new PRED_$compare_4_2();

    public Term arg1, arg2, arg3, arg4;

    public PRED_$compare_4(Term a1, Term a2, Term a3, Term a4, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        arg4 = a4;
        this.cont = cont;
    }

    public PRED_$compare_4(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        arg3 = args[2];
        arg4 = args[3];
        this.cont = cont;
    }

    public int arity() { return 4; }

    public String toString() {
        return "$compare(" + arg1 + "," + arg2 + "," + arg3 + "," + arg4 + ")";
    }

    public Predicate exec(Prolog engine) {
        engine.aregs[1] = arg1;
        engine.aregs[2] = arg2;
        engine.aregs[3] = arg3;
        engine.aregs[4] = arg4;
        engine.cont = cont;
        engine.setB0();
        return engine.switch_on_term(_$compare_4_var, _$compare_4_var, _$compare_4_2, _$compare_4_2, _$compare_4_2, _$compare_4_2);
    }
}

class PRED_$compare_4_var extends PRED_$compare_4 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$compare_4_1, _$compare_4_var_1);
    }
}

class PRED_$compare_4_var_1 extends PRED_$compare_4 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$compare_4_2);
    }
}

class PRED_$compare_4_1 extends PRED_$compare_4 {
    public Predicate exec(Prolog engine) {
    // '$compare'(0,A,B,C):-!,compare(C,A,B)
        Term a1, a2, a3, a4;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        a4 = engine.aregs[4];
        cont = engine.cont;
    // '$compare'(0,A,B,C):-['$neck_cut',compare(C,A,B)]
        a1 = a1.dereference();
        if (a1.isInteger()){
            if (((IntegerTerm) a1).intValue() != 0)
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(si1, engine.trail);
        } else {
            return engine.fail();
        }
        //START inline expansion of $neck_cut
        engine.neckCut();
        //END inline expansion
        return new PRED_compare_3(a4, a2, a3, cont);
    }
}

class PRED_$compare_4_2 extends PRED_$compare_4 {
    public Predicate exec(Prolog engine) {
    // '$compare'(A,B,C,D):-arg(A,B,E),arg(A,C,F),compare(D,E,F)
        Term a1, a2, a3, a4, a5, a6;
        Predicate p1, p2;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        a4 = engine.aregs[4];
        cont = engine.cont;
    // '$compare'(A,B,C,D):-[arg(A,B,E),arg(A,C,F),compare(D,E,F)]
        a5 = new VariableTerm(engine);
        a6 = new VariableTerm(engine);
        p1 = new PRED_compare_3(a4, a5, a6, cont);
        p2 = new PRED_arg_3(a1, a3, a6, p1);
        return new PRED_arg_3(a1, a2, a5, p2);
    }
}
