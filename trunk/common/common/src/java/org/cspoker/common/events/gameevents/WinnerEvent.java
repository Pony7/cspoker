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

package org.cspoker.common.events.gameevents;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.player.Winner;

/**
 * A class to represent winner events.
 * 
 * @author Kenzo
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WinnerEvent extends GameEvent {

	private static final long serialVersionUID = -2384964708734525969L;

	private Set<Winner> winners;

	public WinnerEvent(Set<Winner> winners) {
		this.winners = Collections.unmodifiableSet(winners);
	}

	protected WinnerEvent() {
		// no op
	}

	public Set<Winner> getWinners() {
		return winners;
	}

	public String toString() {
		String toReturn = "Winners: ";
		for (Winner winner : winners) {
			String winnerString = winner.toString();
			toReturn += winnerString.substring(0, winnerString.length() - 1);
			toReturn += ", ";
		}
		return toReturn.substring(0, toReturn.length() - 2) + ".";
	}

	public void dispatch(RemoteAllEventsListener listener)
			throws RemoteException {
		listener.onWinnerEvent(this);
	}

}
