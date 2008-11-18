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
package org.cspoker.client.common;

import org.cspoker.client.common.gamestate.modifiers.NewPocketCardsState;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.ForwardingHoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.elements.player.PlayerId;

public class SmartHoldemPlayerListener extends ForwardingHoldemPlayerListener {

	private final TableState tableState;
	private final PlayerId playerId;

	public SmartHoldemPlayerListener(HoldemPlayerListener holdemPlayerListener,TableState tableState, PlayerId playerId) {
		super(holdemPlayerListener);
		this.tableState = tableState;
		this.playerId = playerId;
	}

	@Override
	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {
		synchronized(this){
			tableState.setGameState(new NewPocketCardsState(tableState.getGameState(),playerId,newPocketCardsEvent));
		}
		super.onNewPocketCards(newPocketCardsEvent);
	}

}
