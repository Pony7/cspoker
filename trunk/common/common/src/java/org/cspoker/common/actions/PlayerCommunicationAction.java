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

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.apache.log4j.Logger;
import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.eventlisteners.invokation.RemoteAllInvokationEventsListener;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class PlayerCommunicationAction implements Serializable {

	private final static Logger logger = Logger
			.getLogger(PlayerCommunicationAction.class);

	@XmlAttribute
	private long id;

	public PlayerCommunicationAction(long id) {
		this.id = id;
	}

	protected PlayerCommunicationAction() {
		// no op
	}

	public void perform(PlayerCommunication pc,
			RemoteAllInvokationEventsListener listener) {
		try {
			performRemote(pc, listener);
		} catch (RemoteException e) {
			logger.error(e);
			// TODO kill?
			pc.kill();
		}
	}

	protected abstract void performRemote(PlayerCommunication pc,
			RemoteAllInvokationEventsListener listener) throws RemoteException;

	public long getID() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PlayerCommunicationAction other = (PlayerCommunicationAction) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
