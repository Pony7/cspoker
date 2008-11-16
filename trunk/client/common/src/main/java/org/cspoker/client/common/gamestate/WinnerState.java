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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.Winner;

public class WinnerState extends ForwardingGameState {

	private final WinnerEvent event;

	private final Map<PlayerId, Integer> gained;
	
	public WinnerState(GameState gameState, WinnerEvent event) {
		super(gameState);
		this.event = event;
		HashMap<PlayerId, Integer> buildingGained = new HashMap<PlayerId, Integer>();
		for(Winner winner:event.getWinners()){
			buildingGained.put(winner.getPlayer().getId(), winner.getGainedAmount());
		}
		gained = Collections.unmodifiableMap(buildingGained);
	}
	
	@Override
	public int getBetSize(PlayerId playerId) {
		return 0;
	}
	
	@Override
	public int getPreviousRoundsPotSize() {
		return 0;
	}
	
	@Override
	public int getRoundPotSize() {
		return 0;
	}
	
	@Override
	public int getStack(PlayerId playerId) {
		Integer gainedValue;
		if((gainedValue = gained.get(playerId))!=null){
			return super.getStack(playerId)+gainedValue;
		}
		return super.getStack(playerId);
	}
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}

}
