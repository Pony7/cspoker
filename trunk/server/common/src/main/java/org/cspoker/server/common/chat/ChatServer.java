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
package org.cspoker.server.common.chat;

import java.util.concurrent.ConcurrentHashMap;

import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.elements.player.Player;
import org.cspoker.server.common.chat.room.ServerChatRoom;
import org.cspoker.server.common.chat.room.TableChatRoom;
import org.cspoker.server.common.elements.id.PlayerId;
import org.cspoker.server.common.elements.id.TableId;
import org.cspoker.server.common.gamecontrol.PokerTable;

public class ChatServer {
	/**
	 * Singleton chatserver
	 */
	private static ChatServer chatServer = new ChatServer();
	
	private ServerChatRoom serverChatRoom=new ServerChatRoom();
	private ConcurrentHashMap<TableId, TableChatRoom> tables = new ConcurrentHashMap<TableId, TableChatRoom>();
	
	public static ChatServer getInstance(){
		return chatServer;
	}

	public void sendServerMessage(Player player, String message) {
		serverChatRoom.sendMessage(player, message);
	}

	public void sendTableMessage(long tableId,Player player, String message) {
		TableChatRoom chatroom = tables.get(new Long(tableId));
		chatroom.sendMessage(player, message);
	}
	public TableChatRoom addTableChatRoom(PokerTable table){
		return tables.put(table.getTableId(),new TableChatRoom(table));
	}
	public TableChatRoom getTableChatRoom(long tableId){
		return tables.get(tableId);
	}
	public ServerChatRoom getServerChatRoom(){
		return serverChatRoom;
	}

	public void subscribe(ChatListener chatlistener) {
		serverChatRoom.subscribe(chatlistener);
	}

	public void unsubscribe(ChatListener chatlistener) {
		serverChatRoom.unSubscribe(chatlistener);
	}

	public void subscribe(ChatListener chatlistener, TableId id, PlayerId id2) {
		if(id==null)
			throw new IllegalArgumentException("TableId isn't effective!");
		TableChatRoom room=tables.get(id);
		if(room==null)
			throw new IllegalArgumentException("There exists no table with the given id!");
		if(room.getTable().hasAsJoinedPlayer(id2)){
			// only players who joined the table, are allowed to subscribe their listener
			room.subscribe(chatlistener);
		}
	}
	
	public void unsubscribe(ChatListener chatlistener, TableId id, PlayerId id2) {
		if(id==null)
			throw new IllegalArgumentException("TableId isn't effective!");
		TableChatRoom room=tables.get(id);
		if(room==null)
			throw new IllegalArgumentException("There exists no table with the given id!");
		if(room.getTable().hasAsJoinedPlayer(id2)){
			// only players who joined the table, are allowed to unsubscribe their listener
			room.unSubscribe(chatlistener);
		}
	}
}
