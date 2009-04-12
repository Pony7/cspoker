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
package org.cspoker.client.bots.bot.search.node;

import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.bots.bot.search.action.DefaultWinnerException;
import org.cspoker.client.bots.bot.search.action.GameEndedException;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.node.leaf.ConstantLeafNode;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.Triple;

public abstract class ActionNode implements InnerGameTreeNode {

	private final static Logger logger = Logger.getLogger(ActionNode.class);

	protected final GameState gameState;
	protected final PlayerId playerId;
	protected final PlayerId botId;

	protected final NodeVisitor[] visitors;
	protected final SearchConfiguration config;
	protected final int searchId;

	public ActionNode(PlayerId playerId, PlayerId botId, GameState gameState,
			SearchConfiguration config, int searchId, NodeVisitor... visitors) {
		this.gameState = gameState;
		this.playerId = playerId;
		this.visitors = visitors;
		this.botId = botId;
		this.config = config;
		this.searchId = searchId;
	}
	
	@Override
	public GameTreeNode getChildAfter(ProbabilityAction action,
			int tokens) {
		if (action.getAction().endsInvolvementOf(botId)) {
			// bot folded
			return new ConstantLeafNode(gameState,action.getAction().gameState.getPlayer(botId)
					.getStack(),0, tokens);
		} else {
			try {
				GameState nextState = action.getAction().getStateAfterAction();
				// expand further
				PlayerId nextToAct = nextState.getNextToAct();
				if (nextToAct.equals(botId)) {
					// go to next player node
					return new BotActionNode(botId,
							nextState, config, tokens, searchId, visitors);
				} else {
					return  new OpponentActionNode(
							nextToAct, botId, nextState, config, tokens,
							searchId, visitors);
				}
			} catch (GameEndedException e) {
				// no active players left
				// go to showdown
				return config.getShowdownNodeFactory()
						.create(botId, e.lastState, tokens, config, searchId,
								visitors);
			} catch (DefaultWinnerException e) {
				if (e.winner.getPlayerId().equals(botId)) {
					// bot wins
					int stack = e.winner.getStack();
					int pots = e.foldState.getGamePotSize();
					return new ConstantLeafNode(gameState, stack + pots,0, tokens);
				} else {
					throw new IllegalStateException(
							"Bot should have folded earlier, winner can't be "
									+ e.winner);
				}
			}
		}
	}
	
	public double getUpperWinBound(){
		int sum = 0;
		int botStack = gameState.getPlayer(botId).getStack();
		//TODO check what if bot allin and 2 other players?
		Set<PlayerState> players = gameState.getAllSeatedPlayers();
		for(PlayerState p:players){
			if(p.isActivelyPlaying() && !p.getPlayerId().equals(botId)){
				sum += Math.min(botStack, p.getStack());
			}
		}
		return botStack+sum+gameState.getGamePotSize();
	}

	@Override
	public Triple<Double, Double, Double> getFoldCallRaiseProbabilities() {
		return config.getOpponentModeler().getFoldCallRaiseProbabilities(
				gameState, playerId);
	}

	@Override
	public Pair<Double, Double> getCheckBetProbabilities() {
		return config.getOpponentModeler().getCheckBetProbabilities(gameState,
				playerId);
	}

	public PlayerId getPlayerId() {
		return playerId;
	}

	@Override
	public PlayerId getBotId() {
		return botId;
	}

	public OpponentModel getOpponentModeler() {
		return config.getOpponentModeler();
	}

	@Override
	public GameState getGameState() {
		return gameState;
	}
	
	@Override
	public String toString() {
		return "Action Node";
	}

}
