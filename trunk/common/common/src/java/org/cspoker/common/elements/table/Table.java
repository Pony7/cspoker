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
	
	private boolean playing;
	
	private GameProperty property;
	
	public Table(TableId id, String name, Collection<Player> players, boolean playing, GameProperty property){
		this.id = id;
		this.name = name;
		if(players==null)
			throw new IllegalArgumentException("The given list of players must be effective.");
		this.players = Collections.unmodifiableList(new ArrayList<Player>(players));
		this.playing = playing;
		this.property = property;
	}
	
	protected Table(){
		// no op
	}
	
	public TableId getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	
	public List<Player> getPlayers(){
		return players;
	}
	
	public int getNbPlayers(){
		return players.size();
	}
	
	public boolean isPlaying(){
		return playing;
	}
	
	public GameProperty getGameProperty(){
		return property;
	}
}
