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

import org.cspoker.client.bots.bot.search.action.CallAction;
import org.cspoker.client.bots.bot.search.action.FoldAction;
import org.cspoker.client.bots.bot.search.action.RaiseAction;
import org.cspoker.client.bots.bot.search.action.SimulatedOpponentAction;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public abstract class OpponentBetNode extends OpponentActionNode{

	public OpponentBetNode(PlayerId botId, PlayerId opponentId, GameState gameState) {
		super(botId,opponentId,gameState);
	}
	
	public void expand(){
		//TODO figure out weights
		expandAction(new SimulatedOpponentAction(
				new FoldAction(),
				0.1));
		expandAction(new SimulatedOpponentAction(
					new CallAction(),
					0.6));	
		if(Math.random()<0.01){
			expandAction(new SimulatedOpponentAction(
					new RaiseAction(gameState.getLowerRaiseBound(playerId)),
					0.2));
			}
		if(Math.random()<0.005){
			expandAction(new SimulatedOpponentAction(
					new RaiseAction(Math.min(5*gameState.getLowerRaiseBound(playerId),gameState.getUpperRaiseBound(playerId))),
					0.1));
		}

	}
		
}
