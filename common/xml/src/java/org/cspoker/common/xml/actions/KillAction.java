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
package org.cspoker.common.xml.actions;

import java.rmi.RemoteException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.xml.eventlisteners.invocation.RemoteAllInvocationEventsListener;
import org.cspoker.common.xml.events.invocation.SuccessfulInvokationEvent;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class KillAction extends PlayerCommunicationAction {

	private static final long serialVersionUID = -7677068438295920901L;

	public KillAction(long id) {
		super(id);
	}

	protected KillAction() {
		// no op
	}

	@Override
	public void performRemote(PlayerCommunication pc,
			RemoteAllInvocationEventsListener listener) throws RemoteException {
		pc.kill();
		listener.onSuccessfullInvokation(new SuccessfulInvokationEvent<Void>(
				this, null));
	}

}
