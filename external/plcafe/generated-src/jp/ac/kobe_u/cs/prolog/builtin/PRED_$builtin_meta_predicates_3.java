package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.ListTerm;
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
 <code>'$builtin_meta_predicates'/3</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$builtin_meta_predicates_3 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol("^");
    static IntegerTerm si2 = new IntegerTerm(2);
    static SymbolTerm s3 = SymbolTerm.makeSymbol("?");
    static SymbolTerm s4 = SymbolTerm.makeSymbol(":");
    static SymbolTerm s5 = SymbolTerm.makeSymbol("[]");
    static ListTerm s6 = new ListTerm(s4, s5);
    static ListTerm s7 = new ListTerm(s3, s6);
    static SymbolTerm s8 = SymbolTerm.makeSymbol("call");
    static IntegerTerm si9 = new IntegerTerm(1);
    static SymbolTerm s10 = SymbolTerm.makeSymbol("once");
    static SymbolTerm s11 = SymbolTerm.makeSymbol("\\+");
    static SymbolTerm s12 = SymbolTerm.makeSymbol("findall");
    static IntegerTerm si13 = new IntegerTerm(3);
    static ListTerm s14 = new ListTerm(s3, s5);
    static ListTerm s15 = new ListTerm(s4, s14);
    static ListTerm s16 = new ListTerm(s3, s15);
    static SymbolTerm s17 = SymbolTerm.makeSymbol("setof");
    static SymbolTerm s18 = SymbolTerm.makeSymbol("bagof");
    static SymbolTerm s19 = SymbolTerm.makeSymbol("on_exception");
    static ListTerm s20 = new ListTerm(s4, s6);
    static ListTerm s21 = new ListTerm(s3, s20);
    static SymbolTerm s22 = SymbolTerm.makeSymbol("catch");
    static ListTerm s23 = new ListTerm(s4, s7);
    static SymbolTerm s24 = SymbolTerm.makeSymbol("freeze");
    static Predicate _fail_0 = new PRED_fail_0();
    static Predicate _$builtin_meta_predicates_3_var = new PRED_$builtin_meta_predicates_3_var();
    static Predicate _$builtin_meta_predicates_3_var_1 = new PRED_$builtin_meta_predicates_3_var_1();
    static Predicate _$builtin_meta_predicates_3_var_2 = new PRED_$builtin_meta_predicates_3_var_2();
    static Predicate _$builtin_meta_predicates_3_var_3 = new PRED_$builtin_meta_predicates_3_var_3();
    static Predicate _$builtin_meta_predicates_3_var_4 = new PRED_$builtin_meta_predicates_3_var_4();
    static Predicate _$builtin_meta_predicates_3_var_5 = new PRED_$builtin_meta_predicates_3_var_5();
    static Predicate _$builtin_meta_predicates_3_var_6 = new PRED_$builtin_meta_predicates_3_var_6();
    static Predicate _$builtin_meta_predicates_3_var_7 = new PRED_$builtin_meta_predicates_3_var_7();
    static Predicate _$builtin_meta_predicates_3_var_8 = new PRED_$builtin_meta_predicates_3_var_8();
    static Predicate _$builtin_meta_predicates_3_var_9 = new PRED_$builtin_meta_predicates_3_var_9();
    static Predicate _$builtin_meta_predicates_3_con = new PRED_$builtin_meta_predicates_3_con();
    static Predicate _$builtin_meta_predicates_3_1 = new PRED_$builtin_meta_predicates_3_1();
    static Predicate _$builtin_meta_predicates_3_2 = new PRED_$builtin_meta_predicates_3_2();
    static Predicate _$builtin_meta_predicates_3_3 = new PRED_$builtin_meta_predicates_3_3();
    static Predicate _$builtin_meta_predicates_3_4 = new PRED_$builtin_meta_predicates_3_4();
    static Predicate _$builtin_meta_predicates_3_5 = new PRED_$builtin_meta_predicates_3_5();
    static Predicate _$builtin_meta_predicates_3_6 = new PRED_$builtin_meta_predicates_3_6();
    static Predicate _$builtin_meta_predicates_3_7 = new PRED_$builtin_meta_predicates_3_7();
    static Predicate _$builtin_meta_predicates_3_8 = new PRED_$builtin_meta_predicates_3_8();
    static Predicate _$builtin_meta_predicates_3_9 = new PRED_$builtin_meta_predicates_3_9();
    static Predicate _$builtin_meta_predicates_3_10 = new PRED_$builtin_meta_predicates_3_10();
    static java.util.Hashtable<Term, Predicate> con = new java.util.Hashtable<Term, Predicate>(10);
    static {
        con.put(s1, _$builtin_meta_predicates_3_1);
        con.put(s8, _$builtin_meta_predicates_3_2);
        con.put(s10, _$builtin_meta_predicates_3_3);
        con.put(s11, _$builtin_meta_predicates_3_4);
        con.put(s12, _$builtin_meta_predicates_3_5);
        con.put(s17, _$builtin_meta_predicates_3_6);
        con.put(s18, _$builtin_meta_predicates_3_7);
        con.put(s19, _$builtin_meta_predicates_3_8);
        con.put(s22, _$builtin_meta_predicates_3_9);
        con.put(s24, _$builtin_meta_predicates_3_10);
    }

    public Term arg1, arg2, arg3;

    public PRED_$builtin_meta_predicates_3(Term a1, Term a2, Term a3, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        this.cont = cont;
    }

    public PRED_$builtin_meta_predicates_3(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        arg3 = args[2];
        this.cont = cont;
    }

    public int arity() { return 3; }

    public String toString() {
        return "$builtin_meta_predicates(" + arg1 + "," + arg2 + "," + arg3 + ")";
    }

    public Predicate exec(Prolog engine) {
        engine.aregs[1] = arg1;
        engine.aregs[2] = arg2;
        engine.aregs[3] = arg3;
        engine.cont = cont;
        engine.setB0();
        return engine.switch_on_term(_$builtin_meta_predicates_3_var, _fail_0, _fail_0, _$builtin_meta_predicates_3_con, _fail_0, _fail_0);
    }
}

class PRED_$builtin_meta_predicates_3_var extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$builtin_meta_predicates_3_1, _$builtin_meta_predicates_3_var_1);
    }
}

class PRED_$builtin_meta_predicates_3_var_1 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$builtin_meta_predicates_3_2, _$builtin_meta_predicates_3_var_2);
    }
}

class PRED_$builtin_meta_predicates_3_var_2 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$builtin_meta_predicates_3_3, _$builtin_meta_predicates_3_var_3);
    }
}

class PRED_$builtin_meta_predicates_3_var_3 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$builtin_meta_predicates_3_4, _$builtin_meta_predicates_3_var_4);
    }
}

class PRED_$builtin_meta_predicates_3_var_4 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$builtin_meta_predicates_3_5, _$builtin_meta_predicates_3_var_5);
    }
}

class PRED_$builtin_meta_predicates_3_var_5 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$builtin_meta_predicates_3_6, _$builtin_meta_predicates_3_var_6);
    }
}

class PRED_$builtin_meta_predicates_3_var_6 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$builtin_meta_predicates_3_7, _$builtin_meta_predicates_3_var_7);
    }
}

class PRED_$builtin_meta_predicates_3_var_7 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$builtin_meta_predicates_3_8, _$builtin_meta_predicates_3_var_8);
    }
}

class PRED_$builtin_meta_predicates_3_var_8 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$builtin_meta_predicates_3_9, _$builtin_meta_predicates_3_var_9);
    }
}

class PRED_$builtin_meta_predicates_3_var_9 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$builtin_meta_predicates_3_10);
    }
}

class PRED_$builtin_meta_predicates_3_con extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
        return engine.switch_on_hash(con, _fail_0);
    }
}

class PRED_$builtin_meta_predicates_3_1 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(^,2,[?,:]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(^,2,[?,:]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s1))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s1, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 2)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si2, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s7.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}

class PRED_$builtin_meta_predicates_3_2 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(call,1,[:]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(call,1,[:]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s8))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s8, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 1)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si9, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s6.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}

class PRED_$builtin_meta_predicates_3_3 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(once,1,[:]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(once,1,[:]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s10))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s10, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 1)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si9, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s6.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}

class PRED_$builtin_meta_predicates_3_4 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(\+,1,[:]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(\+,1,[:]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s11))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s11, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 1)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si9, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s6.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}

class PRED_$builtin_meta_predicates_3_5 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(findall,3,[?,:,?]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(findall,3,[?,:,?]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s12))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s12, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 3)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si13, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s16.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}

class PRED_$builtin_meta_predicates_3_6 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(setof,3,[?,:,?]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(setof,3,[?,:,?]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s17))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s17, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 3)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si13, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s16.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}

class PRED_$builtin_meta_predicates_3_7 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(bagof,3,[?,:,?]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(bagof,3,[?,:,?]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s18))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s18, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 3)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si13, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s16.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}

class PRED_$builtin_meta_predicates_3_8 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(on_exception,3,[?,:,:]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(on_exception,3,[?,:,:]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s19))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s19, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 3)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si13, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s21.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}

class PRED_$builtin_meta_predicates_3_9 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(catch,3,[:,?,:]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(catch,3,[:,?,:]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s22))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s22, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 3)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si13, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s23.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}

class PRED_$builtin_meta_predicates_3_10 extends PRED_$builtin_meta_predicates_3 {
    public Predicate exec(Prolog engine) {
    // '$builtin_meta_predicates'(freeze,2,[?,:]):-true
        Term a1, a2, a3;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        a3 = engine.aregs[3];
        cont = engine.cont;
    // '$builtin_meta_predicates'(freeze,2,[?,:]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s24))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s24, engine.trail);
        } else {
            return engine.fail();
        }
        a2 = a2.dereference();
        if (a2.isInteger()){
            if (((IntegerTerm) a2).intValue() != 2)
                return engine.fail();
        } else if (a2.isVariable()){
            ((VariableTerm) a2).bind(si2, engine.trail);
        } else {
            return engine.fail();
        }
        if (! s7.unify(a3, engine.trail))
            return engine.fail();
        return cont;
    }
}
