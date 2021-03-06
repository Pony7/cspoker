package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;
import jp.ac.kobe_u.cs.prolog.lang.VariableTerm;
/*
 This file is generated by Prolog Cafe.
 PLEASE DO NOT EDIT!
*/
/**
 <code>'$set_prolog_flag0'/2</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$set_prolog_flag0_2 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol("changeable", 1);
    static SymbolTerm s2 = SymbolTerm.makeSymbol("domain", 2);
    static SymbolTerm s3 = SymbolTerm.makeSymbol("atom");
    static SymbolTerm s4 = SymbolTerm.makeSymbol("prolog_flag");
    static Term[] s5 = {s3, s4};
    static StructureTerm s6 = new StructureTerm(s2, s5);
    static SymbolTerm s7 = SymbolTerm.makeSymbol("set_prolog_flag", 2);
    static IntegerTerm si8 = new IntegerTerm(1);
    static Predicate _$set_prolog_flag0_2_sub_1 = new PRED_$set_prolog_flag0_2_sub_1();
    static Predicate _$set_prolog_flag0_2_1 = new PRED_$set_prolog_flag0_2_1();
    static Predicate _$set_prolog_flag0_2_2 = new PRED_$set_prolog_flag0_2_2();

    public Term arg1, arg2;

    public PRED_$set_prolog_flag0_2(Term a1, Term a2, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    public PRED_$set_prolog_flag0_2(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        this.cont = cont;
    }

    public int arity() { return 2; }

    public String toString() {
        return "$set_prolog_flag0(" + arg1 + "," + arg2 + ")";
    }

    public Predicate exec(Prolog engine) {
        engine.aregs[1] = arg1;
        engine.aregs[2] = arg2;
        engine.cont = cont;
        engine.setB0();
        return engine.jtry(_$set_prolog_flag0_2_1, _$set_prolog_flag0_2_sub_1);
    }
}

class PRED_$set_prolog_flag0_2_sub_1 extends PRED_$set_prolog_flag0_2 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$set_prolog_flag0_2_2);
    }
}

class PRED_$set_prolog_flag0_2_1 extends PRED_$set_prolog_flag0_2 {
    public Predicate exec(Prolog engine) {
    // '$set_prolog_flag0'(A,B):-'$prolog_impl_flag'(A,C,changeable(D)),!,'$set_prolog_flag0'(D,A,B,C)
        Term a1, a2, a3, a4, a5, a6;
        Predicate p1, p2;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        cont = engine.cont;
    // '$set_prolog_flag0'(A,B):-['$get_level'(C),'$prolog_impl_flag'(A,D,changeable(E)),'$cut'(C),'$set_prolog_flag0'(E,A,B,D)]
        a3 = new VariableTerm(engine);
        //START inline expansion of $get_level(a(3))
        if (! a3.unify(new IntegerTerm(engine.B0), engine.trail)) {
            return engine.fail();
        }
        //END inline expansion
        a4 = new VariableTerm(engine);
        a5 = new VariableTerm(engine);
        Term[] y1 = {a5};
        a6 = new StructureTerm(s1, y1);
        p1 = new PRED_$set_prolog_flag0_4(a5, a1, a2, a4, cont);
        p2 = new PRED_$cut_1(a3, p1);
        return new PRED_$prolog_impl_flag_3(a1, a4, a6, p2);
    }
}

class PRED_$set_prolog_flag0_2_2 extends PRED_$set_prolog_flag0_2 {
    public Predicate exec(Prolog engine) {
    // '$set_prolog_flag0'(A,B):-illarg(domain(atom,prolog_flag),set_prolog_flag(A,B),1)
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        cont = engine.cont;
    // '$set_prolog_flag0'(A,B):-[illarg(domain(atom,prolog_flag),set_prolog_flag(A,B),1)]
        Term[] y1 = {a1, a2};
        a3 = new StructureTerm(s7, y1);
        return new PRED_illarg_3(s6, a3, si8, cont);
    }
}
