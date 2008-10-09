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
package org.cspoker.common.api.shared.action;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

import org.cspoker.common.api.shared.context.StaticServerContext;
import org.cspoker.common.api.shared.event.ActionEvent;
import org.cspoker.common.api.shared.event.ActionFailedEvent;
import org.cspoker.common.api.shared.event.ActionPerformedEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public abstract class DispatchableAction<T> implements Action {

	private static final long serialVersionUID = -7188969396903443467L;
	
	@XmlAttribute
	private long id;

	public DispatchableAction(long id) {
		this.id = id;
	}

	protected DispatchableAction() {
		// no op
	}
	
	public ActionEvent<T> wrappedPerform(StaticServerContext serverContext){
		try {
			return new ActionPerformedEvent<T>(this, perform(serverContext));
		} catch (IllegalActionException exception) {
			return new ActionFailedEvent<T>(this,exception);
		}
	}

	public abstract T perform(StaticServerContext serverContext) throws IllegalActionException;

	public long getID() {
		return id;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

}
