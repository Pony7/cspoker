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
package org.cspoker.server.embedded.chat.room;

import org.cspoker.common.elements.player.MutablePlayer;
import org.cspoker.server.embedded.gamecontrol.PokerTable;

public class TableChatRoom extends ChatRoom {

	private final PokerTable table;
	
	public TableChatRoom(PokerTable table) {
		super();
		if(table==null)
			throw new IllegalArgumentException("Given table isn't effective!");
		
		this.table=table;
	}

	public PokerTable getTable(){
		return table;
	}
	@Override
	public boolean canSubscribeListener(MutablePlayer player) {
		//check if the player is actually seated at the table of this chat room
		return table.hasAsJoinedPlayer(player);
	}
}
