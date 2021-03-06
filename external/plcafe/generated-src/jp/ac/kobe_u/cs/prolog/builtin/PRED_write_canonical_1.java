package jp.ac.kobe_u.cs.prolog.builtin;
import jp.ac.kobe_u.cs.prolog.lang.ListTerm;
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
 <code>write_canonical/1</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
public class PRED_write_canonical_1 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol("quoted", 1);
    static SymbolTerm s2 = SymbolTerm.makeSymbol("true");
    static Term[] s3 = {s2};
    static StructureTerm s4 = new StructureTerm(s1, s3);
    static SymbolTerm s5 = SymbolTerm.makeSymbol("ignore_ops", 1);
    static StructureTerm s6 = new StructureTerm(s5, s3);
    static SymbolTerm s7 = SymbolTerm.makeSymbol("[]");
    static ListTerm s8 = new ListTerm(s6, s7);
    static ListTerm s9 = new ListTerm(s4, s8);

    public Term arg1;

    public PRED_write_canonical_1(Term a1, Predicate cont) {
        arg1 = a1;
        this.cont = cont;
    }

    public PRED_write_canonical_1(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        this.cont = cont;
    }

    public int arity() { return 1; }

    public String toString() {
        return "write_canonical(" + arg1 + ")";
    }

    public Predicate exec(Prolog engine) {
    // write_canonical(A):-current_output(B),write_term(B,A,[quoted(true),ignore_ops(true)])
        engine.setB0();
        Term a1, a2;
        Predicate p1;
        a1 = arg1;
    // write_canonical(A):-[current_output(B),write_term(B,A,[quoted(true),ignore_ops(true)])]
        a2 = new VariableTerm(engine);
        p1 = new PRED_write_term_3(a2, a1, s9, cont);
        return new PRED_current_output_1(a2, p1);
    }
}
