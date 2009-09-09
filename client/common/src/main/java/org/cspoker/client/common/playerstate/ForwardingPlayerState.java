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
package org.cspoker.client.common.playerstate;

import java.util.EnumSet;
import java.util.List;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.SeatId;

public abstract class ForwardingPlayerState
		extends AbstractPlayerState {
	
	private final PlayerState playerState;
	
	public ForwardingPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}
	
	public int getBet() {
		return playerState.getBet();
	}
	
	@Override
	public String getName() {
		return playerState.getName();
	}
	
	@Override
	public int getTotalInvestment() {
		return playerState.getTotalInvestment();
	}
	
	public EnumSet<Card> getCards() {
		return playerState.getCards();
	}
	
	public int getStack() {
		return playerState.getStack();
	}
	
	public boolean hasFolded() {
		return playerState.hasFolded();
	}
	
	public SeatId getSeatId() {
		return playerState.getSeatId();
	}
	
	public PlayerId getPlayerId() {
		return playerState.getPlayerId();
	}
	
	@Override
	public boolean hasChecked() {
		return playerState.hasChecked();
	}
	
	@Override
	public List<Integer> getBetProgression() {
		return playerState.getBetProgression();
	}
	
}
