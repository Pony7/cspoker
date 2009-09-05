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

import javax.xml.bind.annotation.XmlAttribute;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Util;

/**
 * A class to represent call events.
 */
@Immutable
public class CallEvent
		extends HoldemTableEvent {
	
	private static final long serialVersionUID = -78379299188217626L;
	
	private final PlayerId playerId;
	
	@XmlAttribute
	private final int movedAmount;
	
	public CallEvent(PlayerId player, int movedAmount) {
		this.playerId = player;
		this.movedAmount = movedAmount;
	}
	
	protected CallEvent() {
		playerId = null;
		movedAmount = 0;
	}
	
	@Override
	public String toString() {
		return getPlayerId() + " calls with "+Util.parseDollars(movedAmount)+".";
	}
	
	public PlayerId getPlayerId() {
		return playerId;
	}
	
	public int getMovedAmount() {
		return movedAmount;
	}
	
	@Override
	public void dispatch(HoldemTableListener holdemTableListener) {
		holdemTableListener.onCall(this);
	}
}
