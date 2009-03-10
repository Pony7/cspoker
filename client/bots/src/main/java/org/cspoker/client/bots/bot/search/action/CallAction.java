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
package org.cspoker.client.bots.bot.search.action;

import java.rmi.RemoteException;
import java.util.Set;

import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.client.common.gamestate.modifiers.CallState;
import org.cspoker.client.common.gamestate.modifiers.NextPlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;

public class CallAction extends SearchBotAction{

	public CallAction(GameState gameState, PlayerId actor) {
		super(gameState, actor);
	}

	@Override
	public void perform(RemoteHoldemPlayerContext context)
	throws RemoteException, IllegalActionException {
		context.checkOrCall();
	}

	@Override
	public GameState getStateAfterAction() throws GameEndedException {
		boolean roundEnds = true;
		Set<PlayerState> players = gameState.getAllSeatedPlayers();
		forloop:
			for(PlayerState player:players){
				if(player.isActivelyPlaying() && !player.getPlayerId().equals(actor) 
						&& gameState.getDeficit(player.getPlayerId())>0){
					roundEnds = false;
					break forloop;
				}
			}
		//what if small or big blind all-in?
		if(roundEnds 
				&& gameState.getRound().equals(Round.PREFLOP) 
				&& gameState.getPlayer(actor).isSmallBlind() 
				&& gameState.getLargestBet()<=gameState.getTableConfiguration().getBigBlind()){
			roundEnds = false;
		}

		CallState state = new CallState(gameState, new CallEvent(actor, roundEnds));
		if(roundEnds){
			return getNewRoundState(state);
		}else{
			PlayerState nextActivePlayerAfter = state.getNextActivePlayerAfter(actor);
			if(nextActivePlayerAfter==null){
				//BigBlind is all-in
				return getNewRoundState(state);
			}
			return new NextPlayerState(state,
					new NextPlayerEvent(nextActivePlayerAfter.getPlayerId()));
		}
	}

	@Override
	public String toString() {
		return "Call";
	}

}
