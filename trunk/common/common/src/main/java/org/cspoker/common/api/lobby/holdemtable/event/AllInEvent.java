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

@Immutable
public class AllInEvent
		extends HoldemTableEvent {
	
	private static final long serialVersionUID = 2029273959014493873L;
	
	@XmlAttribute
	private final PlayerId playerId;
	
	@XmlAttribute
	private final boolean endsRound;
	
	private final int movedAmount;
	
	/**
	 * 
	 * @param player
	 * @param movedAmount
	 * 		  The amount of chips moved in addition to the existing bet by the player.
	 * @param endsRound
	 */
	public AllInEvent(PlayerId player, int movedAmount, boolean endsRound) {
		this.playerId = player;
		this.movedAmount = movedAmount;
		this.endsRound = endsRound;
	}
	
	protected AllInEvent() {
		playerId = null;
		movedAmount = 0;
		endsRound = false;
	}
	
	public PlayerId getPlayerId() {
		return playerId;
	}
	
	public int getMovedAmount() {
		return movedAmount;
	}
	
	@Override
	public void dispatch(HoldemTableListener holdemTableListener) {
		holdemTableListener.onAllIn(this);
	}
	
	@Override
	public String toString() {
		return playerId + " is all-in with " + movedAmount + ".";
	}
	
	public boolean endsRound() {
		return endsRound;
	}
	
}
