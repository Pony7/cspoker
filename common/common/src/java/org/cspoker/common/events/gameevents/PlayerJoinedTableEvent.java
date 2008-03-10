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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.player.Player;

/**
 * A class to represent player joining games events.
 * 
 * @author Kenzo
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerJoinedTableEvent extends GameEvent {

	private static final long serialVersionUID = 3276571712883586966L;

	private Player player;
	
	private SeatId seatId;

	public PlayerJoinedTableEvent(SeatId seatId, Player player) {
		this.seatId = seatId;
		this.player = player;
	}

	protected PlayerJoinedTableEvent() {
		// no op
	}

	@Override
	public String toString() {
		return player.getName() + " joined the table.";
	}
	
	public SeatId getSeatId(){
		return seatId;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void dispatch(RemoteAllEventsListener listener)
			throws RemoteException {
		listener.onPlayerJoinedGameEvent(this);
	}

}
