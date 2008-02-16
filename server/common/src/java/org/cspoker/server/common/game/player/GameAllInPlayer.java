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

package org.cspoker.server.common.game.player;

import org.cspoker.server.common.elements.chips.IllegalValueException;
import org.cspoker.server.common.elements.chips.pot.GamePot;

/**
 * A class to represent all-in players.
 * 
 * @author Kenzo
 * 
 */
public class GameAllInPlayer implements Comparable<GameAllInPlayer> {

    private final GamePlayer player;

    public GameAllInPlayer(GamePlayer player) {
	this.player = player;
    }

    public GamePlayer getPlayer() {
	return player;
    }

    public int getBetValue() {
	return player.getBetChips().getValue();
    }

    public void transferAllChipsTo(GamePot pot) {
	try {
	    transferAmountTo(player.getBetChips().getValue(), pot);
	} catch (IllegalValueException e) {
	    assert false;
	}
    }

    public void transferAmountTo(int amount, GamePot pot)
	    throws IllegalValueException {
	player.getBetChips().transferAmountTo(amount, pot.getChips());
    }

    public int compareTo(GameAllInPlayer o) {
	return getBetValue() - o.getBetValue();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final GameAllInPlayer other = (GameAllInPlayer) obj;
	if (player == null) {
	    if (other.player != null)
		return false;
	} else if (getBetValue() != other.getBetValue())
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return String.valueOf(getBetValue());
    }

}
