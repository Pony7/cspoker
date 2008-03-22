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
package org.cspoker.common.xml.events.invocation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.player.SeatedPlayer;
import org.cspoker.common.xml.actions.PlayerCommunicationAction;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IllegalActionEvent extends InvocationEvent {

	private static final long serialVersionUID = 8350435427841245148L;

	@XmlTransient
	private IllegalActionException e;

	private String msg;

	private SeatedPlayer player;

	private PlayerCommunicationAction<?> action;

	public IllegalActionEvent(IllegalActionException e,
			PlayerCommunicationAction<?> a) {
		this.e = e;
		msg = e.getMessage();
		player = e.getPlayer();
		action = a;
	}

	protected IllegalActionEvent() {
		e = null;
	}

	public IllegalActionException getException() {
		if (e == null) {
			e = new IllegalActionException(msg);
		}
		return e;
	}

	public String getMsg() {
		return msg;
	}

	public SeatedPlayer getPlayer() {
		return player;
	}

	public PlayerCommunicationAction<?> getAction() {
		return action;
	}
}
