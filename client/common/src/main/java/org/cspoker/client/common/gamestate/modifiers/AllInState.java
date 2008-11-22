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
package org.cspoker.client.common.gamestate.modifiers;

import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.ForwardingPlayerState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.player.PlayerId;

public class AllInState extends ForwardingGameState {

	private final AllInEvent event;
	
	private final int newPotSize;

	private final int raise;
	
	private final PlayerState playerState;

	private final int newBetSize;

	public AllInState(GameState gameState, AllInEvent event) {
		super(gameState);
		this.event = event;
		
		PlayerState player = super.getPlayer(event.getPlayerId());
		this.newPotSize = super.getRoundPotSize()+event.getAmount();
		
		this.newBetSize = player.getBet()+event.getAmount();
		int buildingRaise = newBetSize-super.getLargestBet();
		if(buildingRaise<0){
			buildingRaise=0;
		}
		raise = buildingRaise;
		this.playerState = new ForwardingPlayerState(player){
			
			@Override
			public int getBet() {
				return AllInState.this.newBetSize;
			}
			
			@Override
			public int getStack() {
				return 0;
			}
			
			@Override
			public PlayerId getPlayerId() {
				return AllInState.this.event.getPlayerId();
			}
			
			@Override
			public boolean hasFolded() {
				return false;
			}
			
			@Override
			public boolean sitsIn() {
				return true;
			}
			
		};
	}
	
	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		if(event.getPlayerId().equals(playerId)){
			return playerState;
		}
		return super.getPlayer(playerId);
	}

	@Override
	public int getLargestBet() {
		return raise>0 ? newBetSize : super.getLargestBet();
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
	public PlayerId getLastBettor() {
		return raise>0 ? event.getPlayerId():super.getLastBettor();
	}
	
	@Override
	public int getNbRaises() {
		int prevNbRaises = super.getNbRaises();
		if(raise>0){
			return prevNbRaises+1;
		}
		return prevNbRaises;
	}

	public int getRaise() {
		return raise;
	}

}
