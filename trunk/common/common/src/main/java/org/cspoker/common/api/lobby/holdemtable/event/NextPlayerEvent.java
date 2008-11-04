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
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.player.PlayerId;

/**
 * A class to represent new player events.
 * 
 * @author Kenzo
 * 
 */
@Immutable
public class NextPlayerEvent extends HoldemTableEvent {

	private static final long serialVersionUID = -2048233796443189725L;

	@XmlAttribute
	private final PlayerId playerId;

	public NextPlayerEvent(PlayerId player) {
		this.playerId = player;
	}

	protected NextPlayerEvent() {
		playerId = null;
	}

	public NextPlayerEvent(Player player) {
		this(player.getId());
	}

	public PlayerId getPlayerId() {
		return playerId;
	}

	@Override
	public String toString() {
		return "It's " + playerId + "'s turn.";
	}
	
	@Override
	public void dispatch(HoldemTableListener holdemTableListener) {
		holdemTableListener.onNextPlayer(this);
	}

}
