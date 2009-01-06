/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.bots.bot.search.opponentmodel.prolog;

import java.util.ArrayDeque;
import java.util.Deque;

import jp.ac.kobe_u.cs.prolog.builtin.PRED_assert_1;
import jp.ac.kobe_u.cs.prolog.builtin.PRED_retract_1;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;

import org.apache.log4j.Logger;

public class AssertRetractVisitor extends ToPrologTermVisitor {
	
	private final static Logger logger = Logger.getLogger(AssertRetractVisitor.class);

	private Deque<Term> stack = new ArrayDeque<Term>();
	
	public AssertRetractVisitor() {
		super();
	}
	
	public AssertRetractVisitor(LoggingVisitor root) {
		super(root);
	}

	@Override
	protected void addTerm(StructureTerm term) {
		stack.push(term);
	}

	public String wrapGoal(String goal) {
		for(Term term:stack){
			Predicate assertPredicate = new PRED_assert_1(term, null);
			Predicate retractPredicate = new PRED_retract_1(term, null);
			goal = assertPredicate.toString()+", "+goal+", "+retractPredicate.toString();
		}
		return goal;
	}
	
}
