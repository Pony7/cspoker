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
package org.cspoker.common.api.shared.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cspoker.common.api.shared.action.Action;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.player.SeatedPlayer;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IllegalActionEvent extends ActionEvent {

	private static final long serialVersionUID = 8350435427841245148L;

	@XmlTransient
	private IllegalActionException exception;

	private String msg;

	private SeatedPlayer player;

	public IllegalActionEvent(IllegalActionException exception,
			Action<?> action) {
		super(action);
		this.exception = exception;
		this.msg = exception.getMessage();
		this.player = exception.getPlayer();
	}

	protected IllegalActionEvent() {
		exception = null;
	}

	public IllegalActionException getException() {
		if (exception == null) {
			exception = new IllegalActionException(msg);
		}
		return exception;
	}

	public String getMsg() {
		return msg;
	}

	public SeatedPlayer getPlayer() {
		return player;
	}
}
