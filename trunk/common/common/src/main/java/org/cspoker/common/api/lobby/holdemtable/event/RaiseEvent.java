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

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.player.Player;

/**
 * A class to represent raise events.
 * 
 * @author Kenzo
 * 
 */
@Immutable
public class RaiseEvent extends HoldemTableEvent {

	private static final long serialVersionUID = -5634645028675762487L;

	private final Player player;

	private final int amount;

	public RaiseEvent(Player player, int amount) {
		this.player = player;
		this.amount = amount;
	}

	protected RaiseEvent() {
		player = null;
		amount = 0;
	}

	public String toString() {
		return getPlayer().getName() + " raises with " + getAmount()
				+ " chips.";
	}

	public int getAmount() {
		return amount;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void dispatch(HoldemTableListener holdemTableListener) {
		holdemTableListener.onRaise(this);
	}

}
