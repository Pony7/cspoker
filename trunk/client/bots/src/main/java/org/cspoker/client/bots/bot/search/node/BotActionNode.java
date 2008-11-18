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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.action.BotActionEvaluation;
import org.cspoker.client.bots.bot.search.action.SimulatedBotAction;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.client.common.gamestate.modifiers.NextPlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;

public abstract class BotActionNode extends ActionNode{

	private final static Logger logger = Logger.getLogger(BotActionNode.class);
	
	protected final List<BotActionEvaluation> actions = new ArrayList<BotActionEvaluation>();

	public BotActionNode(PlayerId playerId, GameState gameState,int depth) {
		super(playerId,gameState,depth);
	}

	public abstract void expand();

	protected void expandAction(SimulatedBotAction action) {
		StringBuilder spaces = new StringBuilder("");
		for(int i=0;i<depth;i++){
			spaces.append("   ");
		}
		if(logger.isTraceEnabled()){
			System.out.println(spaces+"BotAction: "+action);
		}
		double EV;
		if(action.hasSubTree()){
			GameState newGameState = action.getNextState(gameState, playerId);
			PlayerState nextToAct;
			if((nextToAct=newGameState.previewNextToAct())==null){
				EV = doRoundEnd(newGameState);
			}else{
				newGameState = new NextPlayerState(newGameState,new NextPlayerEvent(nextToAct.getPlayerId()));
				EV = doNextPlayer(newGameState, nextToAct);
			}
		}else{
			EV = gameState.getPlayer(playerId).getStack();
		}
		actions.add(new BotActionEvaluation(action,EV));

		if(logger.isTraceEnabled()){
			System.out.println(spaces+"EV="+EV);
		}
	}

	protected abstract double doNextPlayer(GameState newGameState, PlayerState nextToAct) ;

	protected abstract double doRoundEnd(GameState newGameState);

	@Override
	public double getEV() {
		double maxEv=0;
		for(BotActionEvaluation eval : actions){
			if(eval.getEV()>maxEv){
				maxEv = eval.getEV();
			}
		}
		return maxEv;
	}

	public void performbestAction(RemoteHoldemPlayerContext context) throws RemoteException,
	IllegalActionException {
		double maxEv=Double.NEGATIVE_INFINITY;
		SimulatedBotAction action = null;
		for(BotActionEvaluation eval : actions){
			logger.trace("Considering: "+eval.toString());
			if(eval.getEV()>maxEv){
				maxEv = eval.getEV();
				action = eval.getAction();
			}
		}
		action.perform(context);
	}

}
