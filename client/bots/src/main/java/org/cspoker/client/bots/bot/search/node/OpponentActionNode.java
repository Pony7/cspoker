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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.OpponentModel;
import org.cspoker.client.bots.bot.search.action.OpponentActionEvaluation;
import org.cspoker.client.bots.bot.search.action.SimulatedOpponentAction;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.client.common.gamestate.modifiers.NextPlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.elements.player.PlayerId;

public abstract class OpponentActionNode extends ActionNode{

	private final static Logger logger = Logger.getLogger(OpponentActionNode.class);
	
	protected final PlayerId botId;
	protected final List<OpponentActionEvaluation> actions = new ArrayList<OpponentActionEvaluation>();

	public OpponentActionNode(PlayerId botId, PlayerId opponentId, GameState gameState, Map<PlayerId,OpponentModel> opponentModel, int depth) {
		super(opponentId,gameState, opponentModel, depth);
		this.botId = botId;
	}
	
	public abstract void expand();

	public void expandAction(SimulatedOpponentAction action) {
		StringBuilder spaces = new StringBuilder("");
		for(int i=0;i<depth;i++){
			spaces.append("   ");
		}
		if(logger.isTraceEnabled()){
			System.out.println(spaces+"OpponentAction: "+action);
		}
		double EV;
		if(action.getAction().hasSubTree()){
			GameState newGameState = action.getAction().getNextState(gameState, playerId);
			PlayerState nextToAct;
			if((nextToAct=newGameState.previewNextToAct())==null){
				EV = doRoundEnd(newGameState);
			}else{
				newGameState = new NextPlayerState(newGameState,new NextPlayerEvent(nextToAct.getPlayerId()));
				EV = doNextPlayer(newGameState, nextToAct);
			}
		}else{
			EV = gameState.getPlayer(botId).getStack();
		}
		actions.add(new OpponentActionEvaluation(action,EV));

		if(logger.isTraceEnabled()){
			System.out.println(spaces+"EV="+EV);
		}
	}

	protected abstract double doNextPlayer(GameState newGameState, PlayerState nextToAct) ;

	protected abstract double doRoundEnd(GameState newGameState);

	@Override
	public double getEV() {
		double average = 0;
		double totalProbability = 0;
		for(OpponentActionEvaluation eval : actions){
			double probability = eval.getOpponentAction().getProbability();
			totalProbability+=probability;
			average += probability*eval.getEV(); 
		}
		return average/totalProbability;
	}
	
}
