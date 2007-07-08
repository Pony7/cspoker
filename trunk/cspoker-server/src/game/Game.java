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

import game.cards.Card;
import game.chips.Pot;
import game.chips.SidePots;
import game.deck.Deck;
import game.player.Player;
import game.utilities.LoopingList;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 
 * This class contains all game elements,
 * such as the players, open cards, pots,...
 * 
 * @author Kenzo
 *
 */
public class Game {
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
	private GameProperty gameProperty;
	
	private List<Player> players = new CopyOnWriteArrayList<Player>();
	
	private LoopingList<Player> currentHandPlayers;
	
	private Player currentPlayer;
	
	private Deck deck;
	
	private List<Card> openCards;
	
	private SidePots sidePots;
	
	private Pot pot;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	public Game(GameProperty gameProperty){
		this.gameProperty = gameProperty;
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
		return Collections.unmodifiableList(players);
	}
	
	/**********************************************************
	 * Add/Remove Players to/from the game.
	 **********************************************************/
	
	public void removePlayer(Player player){
		players.remove(player);
	}
	
	public void addPlayer(Player player){
		players.add(player);
	}

}
