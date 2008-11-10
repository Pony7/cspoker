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

/**
 * A class to represent big blind events.
 * 
 */
@Immutable
public class BigBlindEvent extends HoldemTableEvent {

	private static final long serialVersionUID = -7908022421547219241L;

	@XmlAttribute
	private final PlayerId playerId;

	@XmlAttribute
	private final int amount;

	public BigBlindEvent(PlayerId player, int amount) {
		this.playerId = player;
		this.amount = amount;
	}

	protected BigBlindEvent() {
		playerId = null;
		amount = 0;
	}

	@Override
	public String toString() {
		return getPlayerId() + " bets the big blind of " + getAmount()
				+ " chips.";
	}

	public int getAmount() {
		return amount;
	}

	public PlayerId getPlayerId() {
		return playerId;
	}

	@Override
	public void dispatch(HoldemTableListener holdemTableListener) {
		holdemTableListener.onBigBlind(this);
	}
}
