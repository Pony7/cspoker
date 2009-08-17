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
package org.cspoker.client.communication.pokersource.commands.poker;

import org.cspoker.client.communication.pokersource.PokerPacket;
import org.cspoker.client.communication.pokersource.eventlisteners.poker.PokerEventListener;

public class Poll extends PokerPacket{
	
	public Poll(int game_id, int tourney_serial) {
		this.game_id = game_id;
		this.tourney_serial = tourney_serial;
	}
	
	public String getType() {
		return "PacketPokerPoll";
	}
	
	private int game_id;
	private int tourney_serial;
	
	@Override
	public void signal(PokerEventListener listener) {
		throw new IllegalStateException("This is not an event");
	}
	
	public int getGame_id() {
		return game_id;
	}
	
	public int getTourney_serial() {
		return tourney_serial;
	}
	
}
