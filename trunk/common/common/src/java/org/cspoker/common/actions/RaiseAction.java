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
package org.cspoker.common.actions;

import java.rmi.RemoteException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.eventlisteners.invokation.RemoteAllInvokationEventsListener;
import org.cspoker.common.events.invokation.IllegalActionEvent;
import org.cspoker.common.events.invokation.SuccessfulInvokationEvent;
import org.cspoker.common.exceptions.IllegalActionException;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RaiseAction extends PlayerCommunicationAction {

	private static final long serialVersionUID = -2573069313463411772L;

	@XmlAttribute
	private int amount;

	public RaiseAction(long id, int amount) {
		super(id);
		this.amount = amount;
	}

	protected RaiseAction() {
		// no op
	}

	@Override
	public void performRemote(PlayerCommunication pc,
			RemoteAllInvokationEventsListener listener) throws RemoteException {
		try {
			pc.raise(amount);
			listener
					.onSuccessfullInvokation(new SuccessfulInvokationEvent<Void>(
							this, null));
		} catch (IllegalActionException e) {
			listener.onIllegalAction(new IllegalActionEvent(e, this));
		}
	}

}
