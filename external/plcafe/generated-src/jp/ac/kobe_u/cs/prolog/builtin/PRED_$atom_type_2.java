package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.Prolog;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;
import jp.ac.kobe_u.cs.prolog.lang.VariableTerm;
/*
 This file is generated by Prolog Cafe.
 PLEASE DO NOT EDIT!
*/
/**
 <code>'$atom_type'/2</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$atom_type_2 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol("alpha");
    static IntegerTerm si2 = new IntegerTerm(0);
    static SymbolTerm s3 = SymbolTerm.makeSymbol("symbol");
    static IntegerTerm si4 = new IntegerTerm(1);
    static SymbolTerm s5 = SymbolTerm.makeSymbol("punct");
    static IntegerTerm si6 = new IntegerTerm(2);
    static SymbolTerm s7 = SymbolTerm.makeSymbol("other");
    static IntegerTerm si8 = new IntegerTerm(3);
    static Predicate _$atom_type_2_sub_1 = new PRED_$atom_type_2_sub_1();
    static Predicate _$atom_type_2_sub_2 = new PRED_$atom_type_2_sub_2();
    static Predicate _$atom_type_2_sub_3 = new PRED_$atom_type_2_sub_3();
    static Predicate _$atom_type_2_1 = new PRED_$atom_type_2_1();
    static Predicate _$atom_type_2_2 = new PRED_$atom_type_2_2();
    static Predicate _$atom_type_2_3 = new PRED_$atom_type_2_3();
    static Predicate _$atom_type_2_4 = new PRED_$atom_type_2_4();

    public Term arg1, arg2;

    public PRED_$atom_type_2(Term a1, Term a2, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    public PRED_$atom_type_2(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        this.cont = cont;
    }

    public int arity() { return 2; }

    public String toString() {
        return "$atom_type(" + arg1 + "," + arg2 + ")";
    }

    public Predicate exec(Prolog engine) {
        engine.aregs[1] = arg1;
        engine.aregs[2] = arg2;
        engine.cont = cont;
        engine.setB0();
        return engine.jtry(_$atom_type_2_1, _$atom_type_2_sub_1);
    }
}

class PRED_$atom_type_2_sub_1 extends PRED_$atom_type_2 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$atom_type_2_2, _$atom_type_2_sub_2);
    }
}

class PRED_$atom_type_2_sub_2 extends PRED_$atom_type_2 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$atom_type_2_3, _$atom_type_2_sub_3);
    }
}

class PRED_$atom_type_2_sub_3 extends PRED_$atom_type_2 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$atom_type_2_4);
    }
}

class PRED_$atom_type_2_1 extends PRED_$atom_type_2 {
    public Predicate exec(Prolog engine) {
    // '$atom_type'(A,alpha):-'$atom_type0'(A,0),!
        Term a1, a2, a3;
        Predicate p1;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        cont = engine.cont;
    // '$atom_type'(A,alpha):-['$get_level'(B),'$atom_type0'(A,0),'$cut'(B)]
        a2 = a2.dereference();
        if (a2.isSymbol()){
            if (! a2.equals(s1))
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(s1, engine.trail);
        } else {
            return engine.fail();
        }
        a3 = new VariableTerm(engine);
        //START inline expansion of $get_level(a(3))
        if (! a3.unify(new IntegerTerm(engine.B0), engine.trail)) {
            return engine.fail();
        }
        //END inline expansion
        p1 = new PRED_$cut_1(a3, cont);
        return new PRED_$atom_type0_2(a1, si2, p1);
    }
}

class PRED_$atom_type_2_2 extends PRED_$atom_type_2 {
    public Predicate exec(Prolog engine) {
    // '$atom_type'(A,symbol):-'$atom_type0'(A,1),!
        Term a1, a2, a3;
        Predicate p1;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        cont = engine.cont;
    // '$atom_type'(A,symbol):-['$get_level'(B),'$atom_type0'(A,1),'$cut'(B)]
        a2 = a2.dereference();
        if (a2.isSymbol()){
            if (! a2.equals(s3))
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(s3, engine.trail);
        } else {
            return engine.fail();
        }
        a3 = new VariableTerm(engine);
        //START inline expansion of $get_level(a(3))
        if (! a3.unify(new IntegerTerm(engine.B0), engine.trail)) {
            return engine.fail();
        }
        //END inline expansion
        p1 = new PRED_$cut_1(a3, cont);
        return new PRED_$atom_type0_2(a1, si4, p1);
    }
}

class PRED_$atom_type_2_3 extends PRED_$atom_type_2 {
    public Predicate exec(Prolog engine) {
    // '$atom_type'(A,punct):-'$atom_type0'(A,2),!
        Term a1, a2, a3;
        Predicate p1;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        cont = engine.cont;
    // '$atom_type'(A,punct):-['$get_level'(B),'$atom_type0'(A,2),'$cut'(B)]
        a2 = a2.dereference();
        if (a2.isSymbol()){
            if (! a2.equals(s5))
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(s5, engine.trail);
        } else {
            return engine.fail();
        }
        a3 = new VariableTerm(engine);
        //START inline expansion of $get_level(a(3))
        if (! a3.unify(new IntegerTerm(engine.B0), engine.trail)) {
            return engine.fail();
        }
        //END inline expansion
        p1 = new PRED_$cut_1(a3, cont);
        return new PRED_$atom_type0_2(a1, si6, p1);
    }
}

class PRED_$atom_type_2_4 extends PRED_$atom_type_2 {
    public Predicate exec(Prolog engine) {
    // '$atom_type'(A,other):-'$atom_type0'(A,3),!
        Term a1, a2, a3;
        Predicate p1;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        cont = engine.cont;
    // '$atom_type'(A,other):-['$get_level'(B),'$atom_type0'(A,3),'$cut'(B)]
        a2 = a2.dereference();
        if (a2.isSymbol()){
            if (! a2.equals(s7))
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(s7, engine.trail);
        } else {
            return engine.fail();
        }
        a3 = new VariableTerm(engine);
        //START inline expansion of $get_level(a(3))
        if (! a3.unify(new IntegerTerm(engine.B0), engine.trail)) {
            return engine.fail();
        }
        //END inline expansion
        p1 = new PRED_$cut_1(a3, cont);
        return new PRED_$atom_type0_2(a1, si8, p1);
    }
}
