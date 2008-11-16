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

import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.player.PlayerId;

public class BigBlindState extends ForwardingGameState {

	private final BigBlindEvent event;
	private final int newStack;
	private final int newPot;

	public BigBlindState(GameState gameState, BigBlindEvent event) {
		super(gameState);
		this.event = event;
		this.newStack = super.getStack(event.getPlayerId())-event.getAmount();
		this.newPot = super.getRoundPotSize()+event.getAmount();
	}

	@Override
	public int getBetSize(PlayerId playerId) {
		if(event.getPlayerId().equals(playerId)){
			return event.getAmount();
		}
		return super.getBetSize(playerId);
	}

	@Override
	public int getLargestBet() {
		return event.getAmount();
	}

	@Override
	public int getStack(PlayerId playerId) {		
		if(event.getPlayerId().equals(playerId)){
			return newStack;
		}
		return super.getStack(playerId);
	}

	@Override
	public int getMinNextRaise() {
		return event.getAmount();
	}

	@Override
	public int getRoundPotSize() {
		return newPot;
	}
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}
	
	@Override
	public PlayerId getLastBettor() {
		return event.getPlayerId();
	}

}
