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
import java.util.Collections;
import java.util.List;

import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.client.common.gamestate.modifiers.NewRoundState;
import org.cspoker.client.common.gamestate.modifiers.NextPlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.chips.Pot;
import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;

public abstract class SearchBotAction implements ActionWrapper{

	protected final GameState gameState;
	protected final PlayerId actor;

	public SearchBotAction(GameState gameState, PlayerId actor) {
		this.gameState = gameState;
		this.actor = actor;
	}

	public PlayerId getActor() {
		return actor;
	}

	public GameState getGameState() {
		return gameState;
	}

	@Override
	public SearchBotAction getAction() {
		return this;
	}

	public abstract void perform(RemoteHoldemPlayerContext context) throws RemoteException, IllegalActionException;

	public abstract GameState getStateAfterAction();
	
	protected GameState getNewRoundState(GameState lastState){
		Round nextRound = lastState.getRound().getNextRound();
		if(nextRound==null){
			return lastState;
		}
		List<Pot> pots = Collections.emptyList();
		NewRoundState newRoundState = new NewRoundState(lastState, new NewRoundEvent(nextRound, new Pots(pots, lastState.getGamePotSize())));
		PlayerState firstToAct = newRoundState.previewNextToAct();
		if(firstToAct==null){
			return getNewRoundState(newRoundState);
		}
		return new NextPlayerState(newRoundState, new NextPlayerEvent(firstToAct.getPlayerId(), 
				gameState.getCallValue(firstToAct.getPlayerId())));
	}

}
