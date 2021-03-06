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
 <code>'$term_to_clause'/4</code> defined in builtins.pl<br>
 @author Mutsunori Banbara (banbara@kobe-u.ac.jp)
 @author Naoyuki Tamura (tamura@kobe-u.ac.jp)
 @version 1.0
*/
class PRED_$term_to_clause_4 extends Predicate {
    static SymbolTerm s1 = SymbolTerm.makeSymbol(":", 2);
    static SymbolTerm s2 = SymbolTerm.makeSymbol("/", 2);
    static SymbolTerm s3 = SymbolTerm.makeSymbol("user");
    static SymbolTerm s4 = SymbolTerm.makeSymbol(":-", 2);

    public Term arg1, arg2, arg3, arg4;

    public PRED_$term_to_clause_4(Term a1, Term a2, Term a3, Term a4, Predicate cont) {
        arg1 = a1;
        arg2 = a2;
        arg3 = a3;
        arg4 = a4;
        this.cont = cont;
    }

    public PRED_$term_to_clause_4(){}

    public void setArgument(Term[] args, Predicate cont) {
        arg1 = args[0];
        arg2 = args[1];
        arg3 = args[2];
        arg4 = args[3];
        this.cont = cont;
    }

    public int arity() { return 4; }

    public String toString() {
        return "$term_to_clause(" + arg1 + "," + arg2 + "," + arg3 + "," + arg4 + ")";
    }

    public Predicate exec(Prolog engine) {
    // '$term_to_clause'(A,B,C:D/E,F):-'$term_to_clause'(A,B,user,C,F),B=(G:-H),functor(G,D,E)
        engine.setB0();
        Term a1, a2, a3, a4, a5, a6, a7, a8, a9, a10;
        Predicate p1, p2;
        a1 = arg1;
        a2 = arg2;
        a3 = arg3;
        a4 = arg4;
    // '$term_to_clause'(A,B,C:D/E,F):-['$term_to_clause'(A,B,user,C,F),'$unify'(B,(G:-H)),functor(G,D,E)]
        a3 = a3.dereference();
        if (a3.isStructure()){
            if (! s1.equals(((StructureTerm)a3).functor()))
                return engine.fail();
            Term[] args = ((StructureTerm)a3).args();
            a5 = args[0];
            a6 = args[1];
        } else if (a3.isVariable()){
            a5 = new VariableTerm(engine);
            a6 = new VariableTerm(engine);
            Term[] args = {a5, a6};
            ((VariableTerm) a3).bind(new StructureTerm(s1, args), engine.trail);
        } else {
            return engine.fail();
        }
        a6 = a6.dereference();
        if (a6.isStructure()){
            if (! s2.equals(((StructureTerm)a6).functor()))
                return engine.fail();
            Term[] args = ((StructureTerm)a6).args();
            a7 = args[0];
            a8 = args[1];
        } else if (a6.isVariable()){
            a7 = new VariableTerm(engine);
            a8 = new VariableTerm(engine);
            Term[] args = {a7, a8};
            ((VariableTerm) a6).bind(new StructureTerm(s2, args), engine.trail);
        } else {
            return engine.fail();
        }
        a9 = new VariableTerm(engine);
        Term[] y1 = {a9, new VariableTerm(engine)};
        a10 = new StructureTerm(s4, y1);
        p1 = new PRED_functor_3(a9, a7, a8, cont);
        p2 = new PRED_$unify_2(a2, a10, p1);
        return new PRED_$term_to_clause_5(a1, a2, s3, a5, a4, p2);
    }
}
