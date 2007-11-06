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

package org.cspoker.common.game.player;

import java.io.Serializable;


/**
 * A class of immutable saved players.
 *
 *
 * @author Kenzo
 *
 */
public class Player implements Serializable{

    private static final long serialVersionUID = -9200622390366978194L;

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
    private final int stackValue;

    /**
     * The chips the player has bet in this round.
     *
     */
    private final int betChipsValue;


    public Player(PlayerId id, String name, int stackValue, int betChipsValue) {
	this.id = id;
	this.name = name;
	this.stackValue = stackValue;
	this.betChipsValue = betChipsValue;
    }

    /**
     * Returns the id of this saved player.
     *
     * @return The id of this saved player.
     */
    public PlayerId getId() {
	return id;
    }

    /**
     * Returns the name of this saved player.
     *
     * @return The name of this saved player.
     */
    public String getName() {
	return name;
    }

    /**
     * Returns the stack value of this saved player.
     *
     * @return The stack value of this saved player.
     */
    public int getStackValue() {
	return stackValue;
    }

    /**
     * Returns the bet chips value of this saved player.
     *
     * @return The bet chips value of this saved player.
     */
    public int getBetChipsValue() {
	return betChipsValue;
    }

}
