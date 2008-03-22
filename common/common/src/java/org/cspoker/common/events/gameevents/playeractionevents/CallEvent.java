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

package org.cspoker.common.events.gameevents.playeractionevents;

import java.rmi.RemoteException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.elements.pots.Pots;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.player.SeatedPlayer;

/**
 * A class to represent call events.
 * 
 * @author Kenzo
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CallEvent extends ActionChangedPotEvent {

	private static final long serialVersionUID = -78379299188217626L;

	private SeatedPlayer player;

	public CallEvent(SeatedPlayer player, Pots pots) {
		super(pots);
		this.player = player;
	}

	protected CallEvent() {
		// no op
	}

	public String toString() {
		return getPlayer().getName() + " calls.";
	}

	public SeatedPlayer getPlayer() {
		return player;
	}

	public void dispatch(RemoteAllEventsListener listener)
			throws RemoteException {
		listener.onCallEvent(this);
	}
}
