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
package org.cspoker.common.elements.player;

import org.cspoker.common.elements.chips.Chips;

public class MutablePlayer {
	
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
	 * @throws IllegalArgumentException The given initial value is not valid.
	 */
	public MutablePlayer(PlayerId id, String name, int initialNbChips) {
		this.id = id;
		this.name = name;
		stack = new Chips(initialNbChips);
	}
	
	public MutablePlayer(Player player) {
		this(player.getId(), player.getName(), 0);
	}
	
	public MutablePlayer(SeatedPlayer player) {
		this(player.getId(), player.getName(), player.getStackValue());
	}
	
	public PlayerId getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Chips getStack() {
		return stack;
	}
	
	@Override
	public String toString() {
		return getName() + " (#" + getId() + ")";
	}
	
	/**
	 * Returns a snapshot of this game player.
	 */
	public Player getMemento() {
		return new Player(getId(), getName());
	}
}
