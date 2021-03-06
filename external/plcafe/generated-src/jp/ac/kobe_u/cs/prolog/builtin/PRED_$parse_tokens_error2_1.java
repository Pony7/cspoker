package jp.ac.kobe_u.cs.prolog.builtin;
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
 <code>'$parse_tokens_error2'/1</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$parse_tokens_error2_1 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol("number", 1);
    static SymbolTerm s2 = SymbolTerm.makeSymbol("atom", 1);
    static SymbolTerm s3 = SymbolTerm.makeSymbol("var", 2);
    static SymbolTerm s4 = SymbolTerm.makeSymbol("string", 1);
    static SymbolTerm s5 = SymbolTerm.makeSymbol("\"");
    static Predicate _$parse_tokens_error2_1_var = new PRED_$parse_tokens_error2_1_var();
    static Predicate _$parse_tokens_error2_1_var_1 = new PRED_$parse_tokens_error2_1_var_1();
    static Predicate _$parse_tokens_error2_1_var_2 = new PRED_$parse_tokens_error2_1_var_2();
    static Predicate _$parse_tokens_error2_1_var_3 = new PRED_$parse_tokens_error2_1_var_3();
    static Predicate _$parse_tokens_error2_1_var_4 = new PRED_$parse_tokens_error2_1_var_4();
    static Predicate _$parse_tokens_error2_1_str = new PRED_$parse_tokens_error2_1_str();
    static Predicate _$parse_tokens_error2_1_str_0 = new PRED_$parse_tokens_error2_1_str_0();
    static Predicate _$parse_tokens_error2_1_str_0_1 = new PRED_$parse_tokens_error2_1_str_0_1();
    static Predicate _$parse_tokens_error2_1_str_1 = new PRED_$parse_tokens_error2_1_str_1();
    static Predicate _$parse_tokens_error2_1_str_1_1 = new PRED_$parse_tokens_error2_1_str_1_1();
    static Predicate _$parse_tokens_error2_1_str_2 = new PRED_$parse_tokens_error2_1_str_2();
    static Predicate _$parse_tokens_error2_1_str_2_1 = new PRED_$parse_tokens_error2_1_str_2_1();
    static Predicate _$parse_tokens_error2_1_str_3 = new PRED_$parse_tokens_error2_1_str_3();
    static Predicate _$parse_tokens_error2_1_str_3_1 = new PRED_$parse_tokens_error2_1_str_3_1();
    static Predicate _$parse_tokens_error2_1_1 = new PRED_$parse_tokens_error2_1_1();
    static Predicate _$parse_tokens_error2_1_2 = new PRED_$parse_tokens_error2_1_2();
    static Predicate _$parse_tokens_error2_1_3 = new PRED_$parse_tokens_error2_1_3();
    static Predicate _$parse_tokens_error2_1_4 = new PRED_$parse_tokens_error2_1_4();
    static Predicate _$parse_tokens_error2_1_5 = new PRED_$parse_tokens_error2_1_5();
    static java.util.Hashtable<Term, Predicate> str = new java.util.Hashtable<Term, Predicate>(4);
    static {
        str.put(s1, _$parse_tokens_error2_1_str_0);
        str.put(s2, _$parse_tokens_error2_1_str_1);
        str.put(s3, _$parse_tokens_error2_1_str_2);
        str.put(s4, _$parse_tokens_error2_1_str_3);
    }

    public Term arg1;

    public PRED_$parse_tokens_error2_1(Term a1, Predicate cont) {
        arg1 = a1;
        this.cont = cont;
    }

    public PRED_$parse_tokens_error2_1(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        this.cont = cont;
    }

    public int arity() { return 1; }

    public String toString() {
        return "$parse_tokens_error2(" + arg1 + ")";
    }

    public Predicate exec(Prolog engine) {
        engine.aregs[1] = arg1;
        engine.cont = cont;
        engine.setB0();
        return engine.switch_on_term(_$parse_tokens_error2_1_var, _$parse_tokens_error2_1_5, _$parse_tokens_error2_1_5, _$parse_tokens_error2_1_5, _$parse_tokens_error2_1_str, _$parse_tokens_error2_1_5);
    }
}

class PRED_$parse_tokens_error2_1_var extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$parse_tokens_error2_1_1, _$parse_tokens_error2_1_var_1);
    }
}

class PRED_$parse_tokens_error2_1_var_1 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$parse_tokens_error2_1_2, _$parse_tokens_error2_1_var_2);
    }
}

class PRED_$parse_tokens_error2_1_var_2 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$parse_tokens_error2_1_3, _$parse_tokens_error2_1_var_3);
    }
}

class PRED_$parse_tokens_error2_1_var_3 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.retry(_$parse_tokens_error2_1_4, _$parse_tokens_error2_1_var_4);
    }
}

class PRED_$parse_tokens_error2_1_var_4 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$parse_tokens_error2_1_5);
    }
}

class PRED_$parse_tokens_error2_1_str extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.switch_on_hash(str, _$parse_tokens_error2_1_5);
    }
}

class PRED_$parse_tokens_error2_1_str_0 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$parse_tokens_error2_1_1, _$parse_tokens_error2_1_str_0_1);
    }
}

class PRED_$parse_tokens_error2_1_str_0_1 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$parse_tokens_error2_1_5);
    }
}

class PRED_$parse_tokens_error2_1_str_1 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$parse_tokens_error2_1_2, _$parse_tokens_error2_1_str_1_1);
    }
}

class PRED_$parse_tokens_error2_1_str_1_1 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$parse_tokens_error2_1_5);
    }
}

class PRED_$parse_tokens_error2_1_str_2 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$parse_tokens_error2_1_3, _$parse_tokens_error2_1_str_2_1);
    }
}

class PRED_$parse_tokens_error2_1_str_2_1 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$parse_tokens_error2_1_5);
    }
}

class PRED_$parse_tokens_error2_1_str_3 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.jtry(_$parse_tokens_error2_1_4, _$parse_tokens_error2_1_str_3_1);
    }
}

class PRED_$parse_tokens_error2_1_str_3_1 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
        return engine.trust(_$parse_tokens_error2_1_5);
    }
}

class PRED_$parse_tokens_error2_1_1 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_error2'(number(A)):-!,write(A)
        Term a1, a2;
        Predicate cont;
        a1 = engine.aregs[1];
        cont = engine.cont;
    // '$parse_tokens_error2'(number(A)):-['$neck_cut',write(A)]
        a1 = a1.dereference();
        if (a1.isStructure()){
            if (! s1.equals(((StructureTerm)a1).functor()))
                return engine.fail();
            Term[] args = ((StructureTerm)a1).args();
            a2 = args[0];
        } else if (a1.isVariable()){
            a2 = new VariableTerm(engine);
            Term[] args = {a2};
            ((VariableTerm) a1).bind(new StructureTerm(s1, args), engine.trail);
        } else {
            return engine.fail();
        }
        //START inline expansion of $neck_cut
        engine.neckCut();
        //END inline expansion
        return new PRED_write_1(a2, cont);
    }
}

class PRED_$parse_tokens_error2_1_2 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_error2'(atom(A)):-!,writeq(A)
        Term a1, a2;
        Predicate cont;
        a1 = engine.aregs[1];
        cont = engine.cont;
    // '$parse_tokens_error2'(atom(A)):-['$neck_cut',writeq(A)]
        a1 = a1.dereference();
        if (a1.isStructure()){
            if (! s2.equals(((StructureTerm)a1).functor()))
                return engine.fail();
            Term[] args = ((StructureTerm)a1).args();
            a2 = args[0];
        } else if (a1.isVariable()){
            a2 = new VariableTerm(engine);
            Term[] args = {a2};
            ((VariableTerm) a1).bind(new StructureTerm(s2, args), engine.trail);
        } else {
            return engine.fail();
        }
        //START inline expansion of $neck_cut
        engine.neckCut();
        //END inline expansion
        return new PRED_writeq_1(a2, cont);
    }
}

class PRED_$parse_tokens_error2_1_3 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_error2'(var(A,B)):-!,write(A)
        Term a1, a2;
        Predicate cont;
        a1 = engine.aregs[1];
        cont = engine.cont;
    // '$parse_tokens_error2'(var(A,B)):-['$neck_cut',write(A)]
        a1 = a1.dereference();
        if (a1.isStructure()){
            if (! s3.equals(((StructureTerm)a1).functor()))
                return engine.fail();
            Term[] args = ((StructureTerm)a1).args();
            a2 = args[0];
        } else if (a1.isVariable()){
            a2 = new VariableTerm(engine);
            Term[] args = {a2, new VariableTerm(engine)};
            ((VariableTerm) a1).bind(new StructureTerm(s3, args), engine.trail);
        } else {
            return engine.fail();
        }
        //START inline expansion of $neck_cut
        engine.neckCut();
        //END inline expansion
        return new PRED_write_1(a2, cont);
    }
}

class PRED_$parse_tokens_error2_1_4 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_error2'(string(A)):-!,write('"'),'$parse_tokens_write_string'(A),write('"')
        Term a1, a2;
        Predicate p1, p2;
        Predicate cont;
        a1 = engine.aregs[1];
        cont = engine.cont;
    // '$parse_tokens_error2'(string(A)):-['$neck_cut',write('"'),'$parse_tokens_write_string'(A),write('"')]
        a1 = a1.dereference();
        if (a1.isStructure()){
            if (! s4.equals(((StructureTerm)a1).functor()))
                return engine.fail();
            Term[] args = ((StructureTerm)a1).args();
            a2 = args[0];
        } else if (a1.isVariable()){
            a2 = new VariableTerm(engine);
            Term[] args = {a2};
            ((VariableTerm) a1).bind(new StructureTerm(s4, args), engine.trail);
        } else {
            return engine.fail();
        }
        //START inline expansion of $neck_cut
        engine.neckCut();
        //END inline expansion
        p1 = new PRED_write_1(s5, cont);
        p2 = new PRED_$parse_tokens_write_string_1(a2, p1);
        return new PRED_write_1(s5, p2);
    }
}

class PRED_$parse_tokens_error2_1_5 extends PRED_$parse_tokens_error2_1 {
    public Predicate exec(Prolog engine) {
    // '$parse_tokens_error2'(A):-write(A)
        Term a1;
        Predicate cont;
        a1 = engine.aregs[1];
        cont = engine.cont;
    // '$parse_tokens_error2'(A):-[write(A)]
        return new PRED_write_1(a1, cont);
    }
}
