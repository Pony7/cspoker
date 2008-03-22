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

import org.cspoker.common.player.Player;
import org.cspoker.common.player.PlayerId;
import org.cspoker.server.common.game.elements.chips.Chips;
import org.cspoker.server.common.game.elements.chips.IllegalValueException;

public class GamePlayer {

	private final PlayerId id;

	private final String name;

	private final Chips stack;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	/**
	 * Construct a new player with given id, name and initial number of chips.
	 * 
	 * @post The chips pile is effective and the value of chips is the same as
	 *       the given initial value.
	 *       
	 * @throws IllegalValueException
	 *         The given initial value is not valid.
	 */
	GamePlayer(PlayerId id, String name, int initialNbChips)
			throws IllegalValueException {
		this.id = id;
		this.name = name;
		stack = new Chips(initialNbChips);
	}
	
	public PlayerId getId() {
		return id;
	}
	
	public String getName(){
		return name;
	}
		
	public Chips getStack(){
		return stack;
	}
	
	/**
	 * Returns a snapshot of this game player.
	 */
	public Player getMemento(){
		return new Player(id, name, stack.getValue());
	}
}
