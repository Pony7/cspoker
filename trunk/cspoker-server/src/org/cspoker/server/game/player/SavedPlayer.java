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

package org.cspoker.server.game.player;

import java.util.Collections;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.cspoker.server.game.PlayerId;
import org.cspoker.server.game.elements.cards.Card;
import org.cspoker.server.game.elements.chips.Chips;

@Immutable
public class SavedPlayer {
	
	/**
	 * The variable containing the id of the player.
	 */
	private final PlayerId id;

	/**
	 * The name of the player.
	 */
	private final String name;

	/**
	 * The stack of this player.
	 */
	private final Chips chips;

	/**
	 * The chips the player has bet in this round.
	 *
	 */
	private final Chips bettedChips;

	/**
	 * The hidden cards.
	 */
	private final List<Card> pocketCards;
	
	public SavedPlayer(Player player){
		id = player.getId();
		name = player.getName();
		chips = player.getStack().getCopy();
		bettedChips = player.getBettedChips().getCopy();
		pocketCards = Collections.unmodifiableList(player.getPocketCards());
	}

}
