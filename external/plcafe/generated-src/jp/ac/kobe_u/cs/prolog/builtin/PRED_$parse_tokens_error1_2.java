package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.IllegalTypeException;
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
 <code>'$parse_tokens_error1'/2</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$parse_tokens_error1_2 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol("[]");
    static SymbolTerm s2 = SymbolTerm.makeSymbol("** here **");
    static Predicate _$parse_tokens_error1_2_var = new PRED_$parse_tokens_error1_2_var();
    static Predicate _$parse_tokens_error1_2_var_1 = new PRED_$parse_tokens_error1_2_var_1();
    static Predicate _$parse_tokens_error1_2_var_2 = new PRED_$parse_tokens_error1_2_var_2();
    static Predicate _$parse_tokens_error1_2_con = new PRED_$parse_tokens_error1_2_con();
    static Predicate _$parse_tokens_error1_2_con_1 = new PRED_$parse_tokens_error1_2_con_1();
    static Predicate _$parse_tokens_error1_2_lis = new PRED_$parse_tokens_error1_2_lis();
    static Predicate _$parse_tokens_error1_2_lis_1 = new PRED_$parse_tokens_error1_2_lis_1();
    static Predicate _$parse_tokens_error1_2_1 = new PRED_$parse_tokens_error1_2_1();
    static Predicate _$parse_tokens_error1_2_2 = new PRED_$parse_tokens_error1_2_2();
    static Predicate _$parse_tokens_error1_2_3 = new PRED_$parse_tokens_error1_2_3();

    public Term arg1, arg2;

    public PRED_$parse_tokens_error1_2(Term a1, Term a2, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        this.cont = cont;
    }

    public PRED_$parse_tokens_error1_2(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        this.cont = cont;
    }

    public int arity() { return 2; }

    public String toString() {
        return "$parse_tokens_error1(" + arg1 + "," + arg2 + ")";
    }

    public Predicate exec(Prolog engine) {
        engine.aregs[1] = arg1;
        engine.aregs[2] = arg2;
        engine.cont = cont;
        engine.setB0();
        return engine.switch_on_term(_$parse_tokens_error1_2_var, _$parse_tokens_error1_2_2, _$parse_tokens_error1_2_2, _$parse_tokens_error1_2_con, _$parse_tokens_error1_2_2, _$parse_tokens_error1_2_lis);
    }
}

class PRED_$parse_tokens_error1_2_var extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$parse_tokens_error1_2_1, _$parse_tokens_error1_2_var_1);
    }
}

class PRED_$parse_tokens_error1_2_var_1 extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$parse_tokens_error1_2_2, _$parse_tokens_error1_2_var_2);
    }
}

class PRED_$parse_tokens_error1_2_var_2 extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$parse_tokens_error1_2_3);
    }
}

class PRED_$parse_tokens_error1_2_con extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$parse_tokens_error1_2_1, _$parse_tokens_error1_2_con_1);
    }
}

class PRED_$parse_tokens_error1_2_con_1 extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$parse_tokens_error1_2_2);
    }
}

class PRED_$parse_tokens_error1_2_lis extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$parse_tokens_error1_2_2, _$parse_tokens_error1_2_lis_1);
    }
}

class PRED_$parse_tokens_error1_2_lis_1 extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$parse_tokens_error1_2_3);
    }
}

class PRED_$parse_tokens_error1_2_1 extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_error1'([],A):-!
        Term a1, a2;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        cont = engine.cont;
    // '$parse_tokens_error1'([],A):-['$neck_cut']
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s1))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s1, engine.trail);
        } else {
            return engine.fail();
        }
        //START inline expansion of $neck_cut
        engine.neckCut();
        //END inline expansion
        return cont;
    }
}

class PRED_$parse_tokens_error1_2_2 extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_error1'(A,B):-A==B,!,nl,write('** here **'),nl,'$parse_tokens_error1'(A,[]),nl
        Term a1, a2, a3;
        Predicate p1, p2, p3, p4;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        cont = engine.cont;
    // '$parse_tokens_error1'(A,B):-['$get_level'(C),'$equality_of_term'(A,B),'$cut'(C),nl,write('** here **'),nl,'$parse_tokens_error1'(A,[]),nl]
        a3 = new VariableTerm(engine);
        //START inline expansion of $get_level(a(3))
        if (! a3.unify(new IntegerTerm(engine.B0), engine.trail)) {
            return engine.fail();
        }
        //END inline expansion
        //START inline expansion of $equality_of_term(a(1),a(2))
        a1 = a1.dereference();
        a2 = a2.dereference();
        if (! a1.equals(a2)) {
            return engine.fail();
        }
        //END inline expansion
        //START inline expansion of $cut(a(3))
        a3 = a3.dereference();
        if (! a3.isInteger()) {
            throw new IllegalTypeException("integer", a3);
        } else {
            engine.cut(((IntegerTerm) a3).intValue());
        }
        //END inline expansion
        p1 = new PRED_nl_0(cont);
        p2 = new PRED_$parse_tokens_error1_2(a1, s1, p1);
        p3 = new PRED_nl_0(p2);
        p4 = new PRED_write_1(s2, p3);
        return new PRED_nl_0(p4);
    }
}

class PRED_$parse_tokens_error1_2_3 extends PRED_$parse_tokens_error1_2 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_error1'([A|B],C):-'$parse_tokens_error2'(A),'$parse_tokens_error1'(B,C)
        Term a1, a2, a3, a4;
        Predicate p1;
        Predicate cont;
        a1 = engine.aregs[1];
        a2 = engine.aregs[2];
        cont = engine.cont;
    // '$parse_tokens_error1'([A|B],C):-['$parse_tokens_error2'(A),'$parse_tokens_error1'(B,C)]
        a1 = a1.dereference();
        if (a1.isList()){
            Term[] args = {((ListTerm)a1).car(), ((ListTerm)a1).cdr()};
            a3 = args[0];
            a4 = args[1];
        } else if (a1.isVariable()){
            a3 = new VariableTerm(engine);
            a4 = new VariableTerm(engine);
            ((VariableTerm) a1).bind(new ListTerm(a3, a4), engine.trail);
        } else {
            return engine.fail();
        }
        p1 = new PRED_$parse_tokens_error1_2(a4, a2, cont);
        return new PRED_$parse_tokens_error2_1(a3, p1);
    }
}
