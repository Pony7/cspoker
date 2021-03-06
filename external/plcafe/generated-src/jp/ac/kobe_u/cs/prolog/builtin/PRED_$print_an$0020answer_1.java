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
 <code>'$print_an answer'/1</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$print_an$0020answer_1 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol("=", 2);
    static SymbolTerm s2 = SymbolTerm.makeSymbol(" = ");

    public Term arg1;

    public PRED_$print_an$0020answer_1(Term a1, Predicate cont) {
        arg1 = a1;
        this.cont = cont;
    }

    public PRED_$print_an$0020answer_1(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        this.cont = cont;
    }

    public int arity() { return 1; }

    public String toString() {
        return "$print_an answer(" + arg1 + ")";
    }

    public Predicate exec(Prolog engine) {
    // '$print_an answer'(A=B):-write(A),'$fast_write'(' = '),writeq(B)
        engine.setB0();
        Term a1, a2, a3;
        Predicate p1, p2;
        a1 = arg1;
    // '$print_an answer'(A=B):-[write(A),'$fast_write'(' = '),writeq(B)]
        a1 = a1.dereference();
        if (a1.isStructure()){
            if (! s1.equals(((StructureTerm)a1).functor()))
                return engine.fail();
            Term[] args = ((StructureTerm)a1).args();
            a2 = args[0];
            a3 = args[1];
        } else if (a1.isVariable()){
            a2 = new VariableTerm(engine);
            a3 = new VariableTerm(engine);
            Term[] args = {a2, a3};
            ((VariableTerm) a1).bind(new StructureTerm(s1, args), engine.trail);
        } else {
            return engine.fail();
        }
        p1 = new PRED_writeq_1(a3, cont);
        p2 = new PRED_$fast_write_1(s2, p1);
        return new PRED_write_1(a2, p2);
    }
}
