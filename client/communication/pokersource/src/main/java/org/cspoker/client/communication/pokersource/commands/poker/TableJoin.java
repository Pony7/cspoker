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

public class TableJoin extends PokerCommand{
	
	public TableJoin(int serial, int game_id) {
		this.serial = serial;
		this.game_id = game_id;
	}
	
	private final int serial;
	private final int game_id;
	
	public int getSerial() {
		return serial;
	}

	public int getGame_id() {
		return game_id;
	}

	public String getType() {
		return "PacketPokerTableJoin";
	}
	
}
