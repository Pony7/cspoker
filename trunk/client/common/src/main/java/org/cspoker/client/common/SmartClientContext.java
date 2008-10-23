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

import org.cspoker.common.api.chat.listener.ForwardingChatListener;
import org.cspoker.common.api.lobby.listener.ForwardingLobbyListener;
import org.cspoker.common.api.shared.context.ForwardingRemoteServerContext;
import org.cspoker.common.api.shared.context.RemoteServerContext;

public class SmartClientContext extends ForwardingRemoteServerContext{

	private ForwardingChatListener chatListener;
	private SmartChatContext smartChatContext;
	private Object chatLock = new Object();
	
	private ForwardingLobbyListener lobbyListener;
	private SmartChatContext smartLobbyContext;
	private Object lobbyLock = new Object();
	
	public SmartClientContext(RemoteServerContext serverContext) {
		super(serverContext);
	}

	/*@Override
	public RemoteChatContext getServerChatContext(ChatListener externalChatListener)
			throws RemoteException, IllegalActionException {
		synchronized (chatLock) {
			if(chatListener!=null){
				chatListener.subscribe(externalChatListener);
			}else{
				chatListener=new ForwardingChatListener(externalChatListener);
				smartChatContext = new SmartChatContext(super.getChatContext(chatListener), chatListener);
			}
			return smartChatContext;
		}
	}
	
	@Override
	public synchronized RemoteLobbyContext getLobbyContext(LobbyListener externalLobbyListener)
			throws RemoteException, IllegalActionException {
		synchronized (lobbyLock) {
			if(lobbyListener!=null){
				chatListener.subscribe(externalChatListener);
			}else{
				chatListener=new ForwardingChatListener(externalChatListener);
				smartChatContext = new SmartChatContext(super.getChatContext(chatListener), chatListener);
			}
			return smartChatContext;
		}
	}*/
	
}
