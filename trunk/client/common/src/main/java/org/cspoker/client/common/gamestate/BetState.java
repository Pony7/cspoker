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
package org.cspoker.client.common.gamestate;

import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.player.PlayerId;

public class BetState extends ForwardingGameState {

	private final BetEvent betEvent;
	
	private final int newStack;
	private final int newPotSize;

	public BetState(GameState gameState, BetEvent betEvent) {
		super(gameState);
		this.betEvent = betEvent;
		this.newStack = super.getStack(betEvent.getPlayerId())-betEvent.getAmount();;
		this.newPotSize = super.getPotSize()+betEvent.getAmount();;
	}

	@Override
	public int getBetSize(PlayerId playerId) {
		if(betEvent.getPlayerId().equals(playerId)){
			return betEvent.getAmount();
		}
		return super.getBetSize(playerId);
	}

	@Override
	public int getLargestBet() {
		return betEvent.getAmount();
	}

	@Override
	public int getStack(PlayerId playerId) {		
		if(betEvent.getPlayerId().equals(playerId)){
			return newStack;
		}
		return super.getStack(playerId);
	}

	@Override
	public int getMinNextRaise() {
		return betEvent.getAmount();
	}

	@Override
	public int getPotSize() {
		return newPotSize;
	}
	
	public HoldemTableEvent getLastEvent() {
		return betEvent;
	}

}
