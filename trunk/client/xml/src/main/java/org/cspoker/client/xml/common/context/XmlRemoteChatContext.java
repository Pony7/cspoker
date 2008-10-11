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

import org.cspoker.client.xml.common.IDGenerator;
import org.cspoker.common.api.chat.action.SendServerMessageAction;
import org.cspoker.common.api.chat.action.SendTableMessageAction;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.shared.action.ActionPerformer;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public class XmlRemoteChatContext implements RemoteChatContext {

	private ActionPerformer performer;
	private IDGenerator generator;

	public XmlRemoteChatContext(ActionPerformer performer, IDGenerator generator) {
		this.performer = performer;
		this.generator = generator;
	}
	
	public void sendServerMessage(String message) throws RemoteException, IllegalActionException {
		performer.perform(new SendServerMessageAction(generator.getNextID(),message));
	}

	public void sendTableMessage(long tableId, String message)
			throws RemoteException, IllegalActionException {
		performer.perform(new SendTableMessageAction(generator.getNextID(),tableId,message));
	}

}
