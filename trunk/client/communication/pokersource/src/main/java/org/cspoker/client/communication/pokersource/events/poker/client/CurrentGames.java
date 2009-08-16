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
package org.cspoker.client.communication.pokersource.events.poker.client;

import org.cspoker.client.communication.pokersource.eventlisteners.poker.PokerEventListener;
import org.cspoker.client.communication.pokersource.events.poker.PokerEvent;


public class CurrentGames extends PokerEvent{

	public String getType() {
		return getStaticType();
	}
	
	public static String getStaticType() {
		return "PacketPokerCurrentGames";
	}
	
	private int count;
	private int[] game_ids;
	private int length;
	
	@Override
	public void signal(PokerEventListener listener) {
		listener.onCurrentGames(this);
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int[] getGame_ids() {
		return game_ids;
	}

	public void setGame_ids(int[] game_ids) {
		this.game_ids = game_ids;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	
}
