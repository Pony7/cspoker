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

import org.cspoker.common.elements.player.PlayerId;

/**
 * Abstract GameState partial implementation.
 * Only methods that are a simple combination of other methods should be implemented here.
 * 
 * @author guy
 *
 */
public abstract class AbstractGameState implements GameState {

	public final int getDeficit(PlayerId playerId) {
		return getLargestBet()-getBetSize(playerId);
	}
	
	public final int getCallValue(PlayerId playerId) {
		return Math.min(getDeficit(playerId), getStack(playerId));
	}
	
	public final boolean isAllIn(PlayerId playerId) {
		return getStack(playerId)>0;
	}
	
	public final int getGamePotSize() {
		return getPreviousRoundsPotSize()+getRoundPotSize();
	}
	
}
