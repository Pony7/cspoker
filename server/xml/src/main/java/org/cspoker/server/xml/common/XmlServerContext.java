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
package org.cspoker.server.xml.common;

import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.lobby.context.StaticLobbyContext;
import org.cspoker.common.api.shared.context.ForwardingServerContext;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.common.api.shared.context.StaticServerContext;
import org.cspoker.common.api.shared.listener.UniversalServerListener;

public class XmlServerContext extends ForwardingServerContext implements StaticServerContext {

	private UniversalServerListener listener;
	private StaticLobbyContext lobbyContext;

	public XmlServerContext(ServerContext context, UniversalServerListener listener) {
		super(context);
		this.listener = listener;
	}
	
	public ChatContext getChatContext() {
		return super.getChatContext(listener);
	}

	public synchronized StaticLobbyContext getLobbyContext() {
		if(this.lobbyContext ==null){
			this.lobbyContext = new XmlLobbyContext(super.getLobbyContext(listener),listener);
		}
		return this.lobbyContext;
	}

}
