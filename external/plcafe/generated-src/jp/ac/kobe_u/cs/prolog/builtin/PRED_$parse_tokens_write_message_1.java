package jp.ac.kobe_u.cs.prolog.builtin;
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
 <code>'$parse_tokens_write_message'/1</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$parse_tokens_write_message_1 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol("[]");
    static SymbolTerm s2 = SymbolTerm.makeSymbol(" ");
    static Predicate _fail_0 = new PRED_fail_0();
    static Predicate _$parse_tokens_write_message_1_var = new PRED_$parse_tokens_write_message_1_var();
    static Predicate _$parse_tokens_write_message_1_var_1 = new PRED_$parse_tokens_write_message_1_var_1();
    static Predicate _$parse_tokens_write_message_1_1 = new PRED_$parse_tokens_write_message_1_1();
    static Predicate _$parse_tokens_write_message_1_2 = new PRED_$parse_tokens_write_message_1_2();

    public Term arg1;

    public PRED_$parse_tokens_write_message_1(Term a1, Predicate cont) {
        arg1 = a1;
        this.cont = cont;
    }

    public PRED_$parse_tokens_write_message_1(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        this.cont = cont;
    }

    public int arity() { return 1; }

    public String toString() {
        return "$parse_tokens_write_message(" + arg1 + ")";
    }

    public Predicate exec(Prolog engine) {
        engine.aregs[1] = arg1;
        engine.cont = cont;
        engine.setB0();
        return engine.switch_on_term(_$parse_tokens_write_message_1_var, _fail_0, _fail_0, _$parse_tokens_write_message_1_1, _fail_0, _$parse_tokens_write_message_1_2);
    }
}

class PRED_$parse_tokens_write_message_1_var extends PRED_$parse_tokens_write_message_1 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$parse_tokens_write_message_1_1, _$parse_tokens_write_message_1_var_1);
    }
}

class PRED_$parse_tokens_write_message_1_var_1 extends PRED_$parse_tokens_write_message_1 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$parse_tokens_write_message_1_2);
    }
}

class PRED_$parse_tokens_write_message_1_1 extends PRED_$parse_tokens_write_message_1 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_write_message'([]):-true
        Term a1;
        Predicate cont;
        a1 = engine.aregs[1];
        cont = engine.cont;
    // '$parse_tokens_write_message'([]):-[]
        a1 = a1.dereference();
        if (a1.isSymbol()){
            if (! a1.equals(s1))
                return engine.fail();
        } else if (a1.isVariable()){
            ((VariableTerm) a1).bind(s1, engine.trail);
        } else {
            return engine.fail();
        }
        return cont;
    }
}

class PRED_$parse_tokens_write_message_1_2 extends PRED_$parse_tokens_write_message_1 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_write_message'([A|B]):-write(A),write(' '),'$parse_tokens_write_message'(B)
        Term a1, a2, a3;
        Predicate p1, p2;
        Predicate cont;
        a1 = engine.aregs[1];
        cont = engine.cont;
    // '$parse_tokens_write_message'([A|B]):-[write(A),write(' '),'$parse_tokens_write_message'(B)]
        a1 = a1.dereference();
        if (a1.isList()){
            Term[] args = {((ListTerm)a1).car(), ((ListTerm)a1).cdr()};
            a2 = args[0];
            a3 = args[1];
        } else if (a1.isVariable()){
            a2 = new VariableTerm(engine);
            a3 = new VariableTerm(engine);
            ((VariableTerm) a1).bind(new ListTerm(a2, a3), engine.trail);
        } else {
            return engine.fail();
        }
        p1 = new PRED_$parse_tokens_write_message_1(a3, cont);
        p2 = new PRED_write_1(s2, p1);
        return new PRED_write_1(a2, p2);
    }
}
