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
package org.cspoker.ai.bots.bot.gametree.action;

import java.rmi.RemoteException;

import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.modifiers.AllInState;
import org.cspoker.client.common.gamestate.modifiers.BetState;
import org.cspoker.client.common.gamestate.modifiers.NextPlayerState;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Util;

public class BetAction extends SearchBotAction {

	public final int amount;

	public BetAction(GameState gameState, PlayerId actor, int amount) {
		super(gameState, actor);
		this.amount = amount;
	}

	@Override
	public void perform(RemoteHoldemPlayerContext context)
			throws RemoteException, IllegalActionException {
		context.betOrRaise(amount);
	}

	@Override
	public GameState getUnwrappedStateAfterAction() {
		GameState betState;
		int stack = gameState.getPlayer(actor).getStack();
		if (stack == amount) {
			betState = new AllInState(gameState, new AllInEvent(actor, amount));
		} else if (stack > amount) {
			betState = new BetState(gameState, new BetEvent(actor, amount));
		} else throw new IllegalStateException("Can't bet amount: "+amount+", with stack: " + stack);
		return betState;
	}

	@Override
	public GameState getStateAfterAction() {
		GameState betState = getUnwrappedStateAfterAction();
		PlayerState nextToAct = betState.getNextActivePlayerAfter(actor);
		if (nextToAct != null) {
			return new NextPlayerState(betState, new NextPlayerEvent(nextToAct
					.getPlayerId()));
		}
		throw new IllegalStateException("Round can't be over after a bet.");
	}

	@Override
	public String toString() {
		if(gameState.getPlayer(actor).getStack() == amount){
			return "Bet " + Util.parseDollars(amount)+ " (all-in)";
		}else{
			return "Bet " + Util.parseDollars(amount);
		}
	}
}
