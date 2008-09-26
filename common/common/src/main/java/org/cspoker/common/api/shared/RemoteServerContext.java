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
package org.cspoker.common.api.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cspoker.common.api.account.RemoteAccountContext;
import org.cspoker.common.api.cashier.RemoteCashierContext;
import org.cspoker.common.api.chat.RemoteChatContext;
import org.cspoker.common.api.lobby.RemoteLobbyContext;
import org.cspoker.common.api.shared.event.RemoteServerListener;

public interface RemoteServerContext extends Remote{

	RemoteAccountContext getAccountContext() throws RemoteException;
	
	RemoteCashierContext getCashierContext() throws RemoteException;
	
	RemoteChatContext getChatContext() throws RemoteException;

	RemoteLobbyContext getLobbyContext() throws RemoteException;
	
	void subscribe(RemoteServerListener serverListener) throws RemoteException;
	
	void unSubscribe(RemoteServerListener serverListener) throws RemoteException;
	
	void die();
}
