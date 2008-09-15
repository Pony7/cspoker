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

package org.cspoker.common.api.lobby.holdemtable.event;

import java.rmi.RemoteException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.player.SeatedPlayer;

/**
 * A class to represent new round events.
 * 
 * @author Kenzo
 * 
 */
@XmlRootElement
public class NewRoundEvent extends HoldemTableEvent {

	private static final long serialVersionUID = 5282936949568835084L;

	private String roundName;

	private SeatedPlayer player;

	public NewRoundEvent(String roundName, SeatedPlayer player) {
		this.roundName = roundName;
		this.player = player;
	}

	public NewRoundEvent(String roundName) {
		this.roundName = roundName;
	}

	protected NewRoundEvent() {
		// no op
	}

	public SeatedPlayer getPlayer() {
		return player;
	}

	public String toString() {
		if (player == null) {
			return roundName + ":";
		}
		return roundName + ": " + player.getName() + " can begin to act.";
	}

	public String getRoundName() {
		return roundName;
	}

	public SeatedPlayer getInitialPlayer() {
		return player;
	}

	public void dispatch(RemoteAllEventsListener listener)
			throws RemoteException {
		listener.onNewRoundEvent(this);
	}

}
