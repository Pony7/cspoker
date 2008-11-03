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
package org.cspoker.client.xml.common.context;

import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import net.jcip.annotations.ThreadSafe;

import org.cspoker.client.xml.common.IDGenerator;
import org.cspoker.client.xml.common.listener.XmlServerListenerTree;
import org.cspoker.common.api.account.context.RemoteAccountContext;
import org.cspoker.common.api.cashier.context.RemoteCashierContext;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.action.ActionPerformer;
import org.cspoker.common.api.shared.action.ServerChatInterestAction;
import org.cspoker.common.api.shared.action.TableChatInterestAction;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.TableId;

@ThreadSafe
public class XmlRemoteServerContext implements RemoteServerContext {

	private final ActionPerformer performer;
	private final IDGenerator generator;
	
	private final XmlRemoteAccountContext accountContext;
	private final XmlRemoteCashierContext cashierContext;
	private final AtomicReference<RemoteChatContext> serverChatContext = new AtomicReference<RemoteChatContext>();
	private final AtomicReference<RemoteLobbyContext> lobbyContext = new AtomicReference<RemoteLobbyContext>();
	private final ConcurrentHashMap<TableId, XmlRemoteTableChatContext>  tableChatContexts = new ConcurrentHashMap<TableId, XmlRemoteTableChatContext>();
	
	private final XmlServerListenerTree serverListenerTree;

	public XmlRemoteServerContext(ActionPerformer performer, XmlServerListenerTree serverListenerTree) {
		this(performer, new IDGenerator(),serverListenerTree);
	}
	
	public XmlRemoteServerContext(ActionPerformer performer, IDGenerator generator, XmlServerListenerTree serverListenerTree) {
		this.performer = performer;
		this.generator = generator;
		this.serverListenerTree = serverListenerTree;
		this.accountContext = new XmlRemoteAccountContext(performer, generator);
		this.cashierContext = new XmlRemoteCashierContext(performer, generator);
	}
	
	public RemoteAccountContext getAccountContext() throws RemoteException {
		return accountContext;
	}

	public RemoteCashierContext getCashierContext() throws RemoteException {
		return cashierContext;
	}

	public RemoteChatContext getServerChatContext(ChatListener chatListener)
			throws RemoteException, IllegalActionException {
		XmlRemoteServerChatContext context = new XmlRemoteServerChatContext(performer,generator);
		if(serverChatContext.compareAndSet(null, context)){
			serverListenerTree.setServerChatListener(chatListener);
			performer.perform(new ServerChatInterestAction(generator.getNextID()));
			return context;
		}else{
			throw new IllegalActionException("Already registered a server chat listener");
		}
	}
	
	public RemoteChatContext getTableChatContext(ChatListener chatListener,
			TableId tableID) throws RemoteException, IllegalActionException {
		XmlRemoteTableChatContext context = new XmlRemoteTableChatContext(performer,generator,tableID);
		if(tableChatContexts.putIfAbsent(tableID, context)==null){
			serverListenerTree.setTableChatListener(tableID, chatListener);
			performer.perform(new TableChatInterestAction(generator.getNextID(),tableID));
			return context;
		}else{
			throw new IllegalActionException("Already registered a chat listener for table #"+tableID+".");
		}
	}

	public RemoteLobbyContext getLobbyContext(LobbyListener lobbyListener)
			throws RemoteException, IllegalActionException {
		XmlRemoteLobbyContext context = new XmlRemoteLobbyContext(performer,generator, serverListenerTree);
		if(lobbyContext.compareAndSet(null, context)){
			serverListenerTree.getLobbyListenerTree().setLobbyListener(lobbyListener);
			return context;
		}else{
			throw new IllegalActionException("Already registered a lobby listener");
		}
	}

}
