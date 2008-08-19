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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.xml.eventlisteners.invocation.AllInvocationEventsListener;
import org.cspoker.common.xml.events.invocation.SuccessfulInvocationEvent;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class PlayerCommunicationAction<T> implements Serializable {

	private static final long serialVersionUID = -7188969396903443467L;
	
	@XmlAttribute
	private long id;

	public PlayerCommunicationAction(long id) {
		this.id = id;
	}

	protected PlayerCommunicationAction() {
		// no op
	}

	public abstract void perform(PlayerCommunication pc,
			AllInvocationEventsListener listener);

	protected void dispatchResult(T result, AllInvocationEventsListener listener) {
		listener.onSuccessfullInvokation(new SuccessfulInvocationEvent<T>(this,
				result));
	}

	public long getID() {
		return id;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PlayerCommunicationAction)) {
			return false;
		}
		final PlayerCommunicationAction<?> other = (PlayerCommunicationAction<?>) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
