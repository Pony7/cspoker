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
package org.cspoker.common.elements.table;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.cspoker.common.elements.player.SeatedPlayer;

/**
 * An immutable class to represent a snapshot of the state of a table.
 * 
 */
public class DetailedHoldemTable extends Table {

	private static final long serialVersionUID = 1647960710321459407L;

	@XmlElementWrapper
	@XmlElement(name = "player")
	private List<SeatedPlayer> players;

	private boolean playing;

	private TableConfiguration property;

	public DetailedHoldemTable(long id, String name, List<SeatedPlayer> players,
			boolean playing, TableConfiguration property) {
		super(id,name);
		if (players == null) {
			throw new IllegalArgumentException(
					"The given list of players must be effective.");
		}
		this.players = Collections.unmodifiableList(players);
		this.playing = playing;
		this.property = property;
	}

	protected DetailedHoldemTable() {
		// no op
	}

	/**
	 * Returns the list of players at this table.
	 * 
	 * @return The list of players at this table.
	 */
	public List<SeatedPlayer> getPlayers() {
		return players;
	}

	/**
	 * The number of players seated at this table.
	 * 
	 * @return The number of players seated at this table.
	 */
	public int getNbPlayers() {
		return players.size();
	}

	/**
	 * The playing status of this table.
	 * 
	 * @return True if the players are playing, false otherwise.
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * Returns the game property of this table.
	 * 
	 * @return The game property of this table.
	 */
	public TableConfiguration getGameProperty() {
		return property;
	}

}
