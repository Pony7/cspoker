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

import net.jcip.annotations.ThreadSafe;

import org.cspoker.client.xml.common.IDGenerator;
import org.cspoker.common.api.chat.action.SendTableMessageAction;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.shared.action.ActionPerformer;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.TableId;

@ThreadSafe
public class XmlRemoteTableChatContext implements RemoteChatContext {

	protected ActionPerformer performer;
	protected IDGenerator generator;
	private TableId tableID;

	public XmlRemoteTableChatContext(ActionPerformer performer, IDGenerator generator, TableId tableID) {
		this.performer = performer;
		this.generator = generator;
		this.tableID = tableID;
	}
	
	public void sendMessage(String message) throws RemoteException,
			IllegalActionException {
		performer.perform(new SendTableMessageAction(generator.getNextID(),message,tableID));
	}

}
