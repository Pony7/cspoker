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
import org.cspoker.common.api.account.action.ChangePasswordAction;
import org.cspoker.common.api.account.action.CreateAccountAction;
import org.cspoker.common.api.account.action.GetAvatarAction;
import org.cspoker.common.api.account.action.HasPasswordAction;
import org.cspoker.common.api.account.action.SetAvatarAction;
import org.cspoker.common.api.account.context.RemoteAccountContext;
import org.cspoker.common.api.shared.action.ActionPerformer;
import org.cspoker.common.api.shared.exception.IllegalActionException;

@ThreadSafe
public class XmlRemoteAccountContext implements RemoteAccountContext {

	private ActionPerformer performer;
	private IDGenerator generator;

	public XmlRemoteAccountContext(ActionPerformer performer, IDGenerator generator) {
		this.performer = performer;
		this.generator = generator;
	}
	
	public void changePassword(String passwordHash) throws RemoteException, IllegalActionException {
		performer.perform(new ChangePasswordAction(generator.getNextID(), passwordHash));
	}

	public void createAccount(String username, String passwordHash)
			throws RemoteException, IllegalActionException {
		performer.perform(new CreateAccountAction(generator.getNextID(),username,passwordHash));
	}

	public byte[] getAvatar(long playerId) throws RemoteException, IllegalActionException {
		return performer.perform(new GetAvatarAction(generator.getNextID(),playerId));
	}

	public boolean hasPassword(String passwordHash) throws RemoteException, IllegalActionException {
		return performer.perform(new HasPasswordAction(generator.getNextID(), passwordHash));
	}

	public void setAvatar(byte[] avatar) throws RemoteException, IllegalActionException {
		performer.perform(new SetAvatarAction(generator.getNextID(), avatar));
	}

}
