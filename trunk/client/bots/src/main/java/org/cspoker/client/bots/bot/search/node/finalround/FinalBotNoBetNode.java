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
package org.cspoker.client.bots.bot.search.node.finalround;


import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.search.node.ActionNode;
import org.cspoker.client.bots.bot.search.node.BotNoBetNode;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.elements.player.PlayerId;

public class FinalBotNoBetNode extends BotNoBetNode{
	
	private final static Logger logger = Logger.getLogger(FinalBotNoBetNode.class);

	public FinalBotNoBetNode(PlayerId botId, GameState gameState) {
		super(botId,gameState);
	}

	protected double doNextPlayer(GameState newGameState, PlayerState nextToAct) {
		logger.trace("Moving on the next opponent: "+nextToAct.getPlayerId());
		ActionNode nextNode;
		if(newGameState.hasBet()){
			nextNode = new FinalOpponentBetNode(playerId, nextToAct.getPlayerId(),newGameState);
		}else{
			nextNode = new FinalOpponentNoBetNode(playerId, nextToAct.getPlayerId(),newGameState);
		}
		nextNode.expand();
		return nextNode.getEV();
	}

	protected double doRoundEnd(GameState newGameState) {
		logger.trace("Ending round with showdown");
		//round is over
		ShowdownNode showdownNode = new ShowdownNode(playerId, newGameState);
		showdownNode.expand();
		return showdownNode.getEV();
	}
	
}
