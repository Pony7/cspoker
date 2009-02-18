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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.BetAction;
import org.cspoker.client.bots.bot.search.action.CallAction;
import org.cspoker.client.bots.bot.search.action.CheckAction;
import org.cspoker.client.bots.bot.search.action.FoldAction;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.action.RaiseAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public abstract class AbstractPrologModel implements AllPlayersModel {

	private final static Logger logger = Logger.getLogger(AbstractPrologModel.class);

	public final static SymbolTerm fold = SymbolTerm.makeSymbol("fold");
	public final static SymbolTerm call = SymbolTerm.makeSymbol("call");
	public final static SymbolTerm bet = SymbolTerm.makeSymbol("bet");
	public final static SymbolTerm check = SymbolTerm.makeSymbol("check");

	private final ToPrologTermVisitor assertingVisitor;
	private final PlayerId botId;

	private final Deque<TermListVisitor> visitors = new ArrayDeque<TermListVisitor>();

	public AbstractPrologModel(PlayerId botId) {
		this.botId = botId;
		this.assertingVisitor = new ToPrologTermVisitor(){
			@Override
			protected void addTerm(StructureTerm term) {
				assertTerm(term);
			}
		};
	}

	protected abstract void assertTerm(jp.ac.kobe_u.cs.prolog.lang.StructureTerm term);

	protected abstract void retractTerm(jp.ac.kobe_u.cs.prolog.lang.StructureTerm term);

	protected abstract double priorActionProbability(SymbolTerm action, PlayerId playerId);

	@Override
	public void signalNextAction(GameState gameState) {
		if(logger.isDebugEnabled()){
			logger.debug("Signalling "+gameState);
		}
		assertingVisitor.readHistory(gameState);
	}

	@Override
	public OpponentModel getModelFor(final PlayerId playerId, GameState gameState) {
		return new OpponentModel(){
			@Override
			public Set<SearchBotAction> getAllPossibleActions(GameState gameState) {
				HashSet<SearchBotAction> possibleActions = new LinkedHashSet<SearchBotAction>();
				Set<ProbabilityAction> probActions = getProbabilityActions(gameState);
				for(ProbabilityAction action:probActions){
					possibleActions.add(action.getAction());
				}
				return possibleActions;
			}

			@Override
			public Set<ProbabilityAction> getProbabilityActions(GameState gameState) {
				HashSet<ProbabilityAction> actions = new LinkedHashSet<ProbabilityAction>();
				double betProb = priorActionProbability(bet, playerId);
				double totalProb = 0;

				if(gameState.hasBet()){
					//call, raise or fold

					double callProb = priorActionProbability(call, playerId);
					actions.add(new ProbabilityAction(
							new CallAction(gameState, playerId), 
							callProb));
					totalProb += callProb;

					double foldProb = priorActionProbability(fold, playerId);
					actions.add(new ProbabilityAction(
							new FoldAction(gameState, playerId),
							foldProb));
					totalProb += foldProb;

					if(logger.isDebugEnabled()){
						logger.debug(callProb+" | "+foldProb);
					}
					if(!gameState.getPlayer(botId).isAllIn() && gameState.isAllowedToRaise(playerId)){
						int lowerRaiseBound = gameState.getLowerRaiseBound(playerId);
						int upperRaiseBound = gameState.getUpperRaiseBound(playerId);

						actions.add(new ProbabilityAction(
								new RaiseAction(gameState, playerId, lowerRaiseBound),
								//raise is no target, bet is! 
								//TODO fix in prolog
								betProb));
						totalProb += betProb;

						if(upperRaiseBound>lowerRaiseBound){
							actions.add(new ProbabilityAction(
									new RaiseAction(gameState, playerId, Math.min(5*lowerRaiseBound, upperRaiseBound)),
									betProb));
							totalProb += betProb;
						}
					}
				}else{
					//check or bet
					double checkprob = priorActionProbability(check, playerId);
					actions.add(new ProbabilityAction(
							new CheckAction(gameState, playerId),
							checkprob));
					totalProb += checkprob;

					if(!gameState.getPlayer(botId).isAllIn() && gameState.isAllowedToRaise(playerId)){
						int lowerRaiseBound = gameState.getLowerRaiseBound(playerId);
						int upperRaiseBound = gameState.getUpperRaiseBound(playerId);

						actions.add(new ProbabilityAction(
								new BetAction(gameState, playerId, lowerRaiseBound),
								betProb));
						totalProb += betProb;

						if(upperRaiseBound>lowerRaiseBound){
							actions.add(new ProbabilityAction(
									new BetAction(gameState, playerId, Math.min(5*lowerRaiseBound, upperRaiseBound)),
									betProb));
							totalProb += betProb;
						}
					}
				}

				HashSet<ProbabilityAction> normalizedActions = new HashSet<ProbabilityAction>();
				for(ProbabilityAction action:actions){
					normalizedActions.add(new ProbabilityAction(action.getActionWrapper(), action.getProbability()/totalProb));
				}	

				return normalizedActions;
			}

		};
	}

	@Override
	public void assume(GameState gameState) {
		if(logger.isDebugEnabled()){
			logger.debug("Assuming "+gameState);
		}
		LoggingVisitor root = visitors.isEmpty()?assertingVisitor:visitors.peek();
		TermListVisitor visitor = new TermListVisitor(root);
		visitor.readHistory(gameState);
		List<jp.ac.kobe_u.cs.prolog.lang.StructureTerm> terms = visitor.getTerms();
		for(jp.ac.kobe_u.cs.prolog.lang.StructureTerm term:terms){
			assertTerm(term);
		}
		visitors.push(visitor);
	}

	@Override
	public void forgetAssumption() {
		TermListVisitor visitor = visitors.pop();
		if(logger.isDebugEnabled()){
			logger.debug("Forgetting "+visitor);
		}
		List<jp.ac.kobe_u.cs.prolog.lang.StructureTerm> terms = visitor.getTerms();
		for(int i=terms.size()-1;i>=0;i--){
			retractTerm(terms.get(i));
		}
	}
	
	protected TermListVisitor getTopVisitor(){
		return visitors.peek();
	}
}
