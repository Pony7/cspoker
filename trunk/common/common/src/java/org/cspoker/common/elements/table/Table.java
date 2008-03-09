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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.player.Player;

/**
 * An immutable class to represent a snapshot of the state of a table.
 * 
 * @author kenzo
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Table implements Serializable  {
	
	private static final long serialVersionUID = 1647960710321459407L;

	@XmlAttribute
	private TableId id;
	
	@XmlAttribute
	private String name;
	
	@XmlElementWrapper
	@XmlElement(name = "player")
	private List<Player> players;
	
	private int nbPlayers;
	
	private boolean playing;
	
	private GameProperty property;
	
	public Table(TableId id, String name, int nbPlayers, Collection<Player> players, boolean playing, GameProperty property){
		this.id = id;
		this.name = name;
		if(players==null)
			throw new IllegalArgumentException("The given list of players must be effective.");
		this.players = Collections.unmodifiableList(new ArrayList<Player>(players));
		this.nbPlayers = nbPlayers;
		this.playing = playing;
		this.property = property;
	}
	
	protected Table(){
		// no op
	}
	
	/**
	 * The id of this table.
	 * 
	 * This id is unique.
	 * 
	 * @return The id of this table.
	 */
	public TableId getId(){
		return id;
	}
	
	/**
	 * Returns the name of this table.
	 * 
	 * The name is not guaranteed unique.
	 * 
	 * @return The name of this table.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the list of players at this table.
	 * The size of the returned list equals the maximum number of players at this table.
	 * 
	 * Be aware that this list contains null-elements if the table is not full.
	 * 
	 * @return The list of players at this table.
	 */
	public List<Player> getPlayers(){
		return players;
	}
	
	/**
	 * The number of players seated at this table.
	 * 
	 * @return The number of players seated at this table.
	 */
	public int getNbPlayers(){
		return nbPlayers;
	}
	
	/**
	 * The playing status of this table.
	 * 
	 * @return True if the players are playing, false otherwise.
	 */
	public boolean isPlaying(){
		return playing;
	}
	
	/**
	 * Returns the game property of this table.
	 * 
	 * @return The game property of this table.
	 */
	public GameProperty getGameProperty(){
		return property;
	}
}
