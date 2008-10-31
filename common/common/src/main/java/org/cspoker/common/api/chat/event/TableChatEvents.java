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
package org.cspoker.common.api.chat.event;

import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.shared.event.ServerEvent;
import org.cspoker.common.api.shared.listener.ServerListenerTree;

@XmlRootElement
public class TableChatEvents extends ServerEvent {

	private static final long serialVersionUID = 557148706756328395L;

	private long tableID;

	private ChatEvent chatEvent;
	
	public TableChatEvents() {
		// no op
	}
	
	public TableChatEvents(long tableID, ChatEvent chatEvent){
		this.tableID = tableID;
		this.chatEvent = chatEvent;
	}
	
	public void dispatch(ServerListenerTree serverListenerTree) {
		ChatListener listenerTree = serverListenerTree.getTableChatListener(tableID);
		chatEvent.dispatch(listenerTree);
	}
	
	@Override
	public String toString() {
		return "Table #"+tableID+" | "+chatEvent.toString();
	}

}
