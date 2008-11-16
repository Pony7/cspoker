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

import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.player.PlayerId;

public class AllInState extends ForwardingGameState {

	private final AllInEvent event;
	
	private final int newPotSize;

	private final int raise;

	private final int newBetSize;

	public AllInState(GameState gameState, AllInEvent event) {
		super(gameState);
		this.event = event;
		this.newPotSize = super.getRoundPotSize()+event.getAmount();
		this.newBetSize = super.getBetSize(event.getPlayerId())+event.getAmount();
		int buildingRaise = newBetSize-super.getLargestBet();
		if(buildingRaise<0){
			buildingRaise=0;
		}
		raise = buildingRaise;
	}

	@Override
	public int getBetSize(PlayerId playerId) {
		if(event.getPlayerId().equals(playerId)){
			return newBetSize;
		}
		return super.getBetSize(playerId);
	}

	@Override
	public int getLargestBet() {
		return raise>0 ? newBetSize : super.getLargestBet();
	}

	@Override
	public int getStack(PlayerId playerId) {		
		if(event.getPlayerId().equals(playerId)){
			return 0;
		}
		return super.getStack(playerId);
	}

	@Override
	public int getMinNextRaise() {
		return Math.max(raise, super.getMinNextRaise());
	}

	@Override
	public int getRoundPotSize() {
		return newPotSize;
	}
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}
	
	@Override
	public boolean hasFolded(PlayerId playerId) {
		if(event.getPlayerId().equals(playerId)){
			return false;
		}
		return super.hasFolded(playerId);
	}
	
	@Override
	public PlayerId getLastBettor() {
		return raise>0 ? event.getPlayerId():super.getLastBettor();
	}

}
