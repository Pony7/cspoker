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

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.modifiers.FoldState;
import org.cspoker.client.common.gamestate.modifiers.NextPlayerState;
import org.cspoker.common.elements.player.PlayerId;

public abstract class ActionNode implements InnerGameTreeNode{

	private final static Logger logger = Logger.getLogger(ActionNode.class);

	protected final GameState gameState;
	protected final PlayerId playerId;
	protected final String prefix;
	protected final AllPlayersModel opponentModeler;
	protected final PlayerId botId;

	public ActionNode(PlayerId playerId, PlayerId botId, GameState gameState, AllPlayersModel opponentModeler, String prefix) {
		this.gameState = gameState;
		this.playerId = playerId;
		this.prefix =  prefix;
		this.opponentModeler = opponentModeler;
		this.botId= botId;
	}

	public <A extends ActionWrapper> EvaluatedAction<A> expandWith(A action){
		if(logger.isDebugEnabled()){
			logger.debug(prefix+"---o "+action+" in "+this);
		}
		EvaluatedAction<A> result;
		GameState nextState = action.getAction().getStateAfterAction();
		if(nextState instanceof FoldState){
			result = getFoldEVForBot(action, nextState);
		}else if(nextState instanceof NextPlayerState){
			PlayerId nextToAct = nextState.getNextToAct();
			if(nextToAct.equals(botId)){
				//go to next player node
				BotActionNode botActionNode = new BotActionNode(botId, nextState, opponentModeler, prefix+"   |");
				result = new EvaluatedAction<A>(action, botActionNode.getEV());
			}else{	
				OpponentActionNode opponentActionNode = new OpponentActionNode(nextToAct, botId, nextState, opponentModeler, prefix+"   |");
				result = new EvaluatedAction<A>(action, opponentActionNode.getEV());
			}
		}else{
			//no active players left
			//go to showdown
			ShowdownNode showdownNode = new ShowdownNode(botId, nextState);
			result = new EvaluatedAction<A>(action, showdownNode.getEV());
		}
		if(logger.isDebugEnabled()){
			logger.debug(prefix+"   `"+result);
			//logger.trace(prefix);
		}
		return result;
	}

	protected abstract <A extends ActionWrapper> EvaluatedAction<A> getFoldEVForBot(A action,GameState nextState);

	public PlayerId getPlayerId() {
		return playerId;
	}

	public AllPlayersModel getOpponentModeler() {
		return opponentModeler;
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
