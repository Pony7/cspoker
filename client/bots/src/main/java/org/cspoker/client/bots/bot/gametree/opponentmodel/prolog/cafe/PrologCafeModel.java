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
package org.cspoker.client.bots.bot.gametree.opponentmodel.prolog.cafe;

import jp.ac.kobe_u.cs.prolog.builtin.PRED_assert_1;
import jp.ac.kobe_u.cs.prolog.builtin.PRED_clause_2;
import jp.ac.kobe_u.cs.prolog.builtin.PRED_prior_action_probability_6;
import jp.ac.kobe_u.cs.prolog.builtin.PRED_retract_1;
import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.PrologControl;
import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;
import jp.ac.kobe_u.cs.prolog.lang.VariableTerm;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.gametree.opponentmodel.prolog.AbstractPrologModel;
import org.cspoker.client.bots.bot.gametree.opponentmodel.prolog.ToPrologTermVisitor;
import org.cspoker.common.elements.player.PlayerId;

public class PrologCafeModel extends AbstractPrologModel {

	private final static Logger logger = Logger
			.getLogger(PrologCafeModel.class);

	public final static SymbolTerm delimiter = SymbolTerm.makeSymbol(":", 2);
	public final static SymbolTerm packageName = SymbolTerm
			.makeSymbol("jp.ac.kobe_u.cs.prolog.builtin");

	private final PrologControl prolog;

	public PrologCafeModel(PrologControl prolog) {
		this.prolog = prolog;
	}

	@Override
	protected void assertTerm(StructureTerm term) {
		if (logger.isDebugEnabled()) {
			logger.debug("+" + term);
		}
		// Term termWithPackage = term;//new StructureTerm(delimiter,new
		// Term[]{packageName, term});
		if (!executeGoal(new PRED_assert_1(), term)) {
			throw new IllegalStateException("Failed to assert " + term);
		}
	}

	private boolean executeGoal(Predicate predicate, Term... terms) {
		long startTime = 0;
		boolean traceEnabled = logger.isTraceEnabled();
		if (traceEnabled) {
			startTime = System.nanoTime();
		}
		boolean success = prolog.execute(predicate, terms);
		if (traceEnabled) {
			logger.trace("Executing " + predicate + " took "
					+ (System.nanoTime() - startTime) / 1000000.0 + " ms");
		}
		return success;
	}

	@Override
	protected void retractTerm(StructureTerm term) {
		if (logger.isDebugEnabled()) {
			logger.debug("-" + term);
		}
		term = new StructureTerm(delimiter, new Term[] { packageName, term });
		if (!executeGoal(new PRED_retract_1(), term)) {
			throw new IllegalStateException("Failed to retract " + term);
		}
	}

	protected boolean isAsserted(StructureTerm term) {
		Term termWithPackage = term;// new StructureTerm(delimiter,new
									// Term[]{packageName, term});
		VariableTerm dontcare = new VariableTerm();
		return prolog.execute(new PRED_clause_2(), new Term[] {
				termWithPackage, dontcare });
	}

	@Override
	protected double priorActionProbability(SymbolTerm action, PlayerId playerId) {
		ToPrologTermVisitor visitor = getTopVisitor();
		Predicate prior_action_probability = new PRED_prior_action_probability_6();
		VariableTerm p = new VariableTerm();
		IntegerTerm gameId = new IntegerTerm(visitor.getGameId());
		IntegerTerm actionId = new IntegerTerm(visitor.getActionId() + 1);
		SymbolTerm playerName = SymbolTerm.makeSymbol("player_" + playerId.getId());
		Term[] args = { gameId, actionId, playerName, action, visitor.getRound(), p };
		if (logger.isDebugEnabled()) {
			logger.debug(new PRED_prior_action_probability_6(gameId, actionId, playerName, action, visitor.getRound(), p, null));
		}
		if (!executeGoal(prior_action_probability, args)) {
			throw new IllegalStateException("Failed to call "
					+ prior_action_probability);
		}
		return (Double) p.toJava();
	}
}
