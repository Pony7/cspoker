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

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.elements.player.PlayerId;

public class SitOutState extends ForwardingGameState {

	private final SitOutEvent event;

	public SitOutState(GameState gameState, SitOutEvent event) {
		super(gameState);
		this.event = event;
	}
	
	@Override
	public boolean sitsIn(PlayerId playerId) {
		if(event.getPlayerId().equals(playerId)){
			return false;
		}
		return super.sitsIn(playerId);
	}
	
	public HoldemTableEvent getLastEvent() {
		return event;
	}
	
}
