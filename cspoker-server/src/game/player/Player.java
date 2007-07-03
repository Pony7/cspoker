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
 **/

package game.player;

import game.cards.PrivateCards;
import game.chips.Chips;

/**
 * A class to represent players: bots or humans.
 * 
 * @author Kenzo
 *
 */
public class Player {
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
	/**
	 * This id should be guarenteed unique upon creation
	 * of a player.
	 */
	private final long id;
	
	/**
	 * The name of the player.
	 */
	private final String name;
	
	/**
	 * The available chips of this player.
	 */
	private Chips chips;
	
	/**
	 * The hidden cards.
	 */
	private PrivateCards privateCards;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	public Player(long id, String name){
		this.id = id;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	
	/**
	 * State pattern welke acties de player mag doen als een state modelleren?
	 */
	
	public void fold(){
		
	}
	
	public long getId(){
		return 0;
	}
	
	public long getPrivateKey(){
		return 0;
	}

}
