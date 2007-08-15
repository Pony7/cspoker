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

package org.cspoker.server.game.events.playerActionEvents;

import org.cspoker.server.game.events.GameEvent;
import org.cspoker.server.game.player.SavedPlayer;

/**
 * A class to represent big blind events.
 *
 * @author Kenzo
 *
 */
public class BigBlindEvent extends GameEvent{

	private final SavedPlayer player;

	private final int amount;

	public BigBlindEvent(SavedPlayer player, int amount){
		this.player = player;
		this.amount = amount;
	}

	@Override
	public String[] getAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(){
		return player.getName()+" bets the big blind of "+amount+" chips.";
	}

}
