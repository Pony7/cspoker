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

import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.player.PlayerId;

public class CallState extends ForwardingGameState {

	private final CallEvent callEvent;
	private final int newBetSize;
	private final int newStack;
	private final int newPotSize;

	public CallState(GameState gameState, CallEvent callEvent) {
		super(gameState);
		this.callEvent = callEvent;
		this.newBetSize = super.getLargestBet();
		int chipsMoved = newBetSize-super.getBetSize(callEvent.getPlayerId());
		this.newStack = super.getStack(callEvent.getPlayerId())-chipsMoved;
		this.newPotSize = super.getRoundPotSize()+chipsMoved;
	}

	@Override
	public int getBetSize(PlayerId playerId) {
		if(callEvent.getPlayerId().equals(playerId)){
			return newBetSize;
		}
		return super.getBetSize(playerId);
	}

	@Override
	public int getStack(PlayerId playerId) {		
		if(callEvent.getPlayerId().equals(playerId)){
			return newStack;
		}
		return super.getStack(playerId);
	}

	@Override
	public int getRoundPotSize() {
		return newPotSize;
	}
	
	public HoldemTableEvent getLastEvent() {
		return callEvent;
	}
	
}
