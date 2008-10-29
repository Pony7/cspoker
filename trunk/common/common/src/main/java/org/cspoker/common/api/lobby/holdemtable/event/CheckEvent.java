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
 * A class to represent check events.
 * 
 * @author Kenzo
 * 
 */
public class CheckEvent extends HoldemTableEvent {

	private static final long serialVersionUID = -1128439307716720778L;

	private Player player;

	public CheckEvent(Player player) {
		this.player = player;
	}

	protected CheckEvent() {
		// no op
	}

	public String toString() {
		return getPlayer().getName() + " checks.";
	}

	public Player getPlayer() {
		return player;
	}

	public void dispatch(HoldemTableListener holdemTableListener) {
		holdemTableListener.onCheck(this);
	}
}
