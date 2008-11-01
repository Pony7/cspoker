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

import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.player.Player;

/**
 * A class to represent small blind events.
 * 
 * @author Kenzo
 * 
 */
public class SmallBlindEvent extends HoldemTableEvent {

	private static final long serialVersionUID = 8729210630280570765L;

	private Player player;

	private int amount;

	public SmallBlindEvent(Player player, int amount) {
		this.player = player;
		this.amount = amount;
	}

	protected SmallBlindEvent() {
		// no op
	}

	public String toString() {
		return getPlayer().getName() + " bets the small blind of "
				+ getAmount() + " chips.";
	}

	public int getAmount() {
		return amount;
	}

	public Player getPlayer() {
		return player;
	}

	public void dispatch(HoldemTableListener holdemTableListener) {
		holdemTableListener.onSmallBlind(this);
	}

}
