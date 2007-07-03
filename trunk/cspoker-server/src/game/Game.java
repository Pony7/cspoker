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

package game;

import game.player.Player;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * This class contains all game elements,
 * such as the players, table,...
 * 
 * @author Kenzo
 *
 */
public class Game {
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
	private GameProperty gameProperty;
	
	private Table table;
	
	private List<Player> players = new ArrayList<Player>();
	
	private Player currentPlayer;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	public Game(GameProperty gameProperty){
		this.gameProperty = gameProperty;
		this.table = new Table();
	}
	
	/**********************************************************
	 * Getters
	 **********************************************************/
	
	public GameProperty GetGameProperty(){
		return gameProperty;
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	
	public List<Player> getPlayers(){
		return players;
	}

}
