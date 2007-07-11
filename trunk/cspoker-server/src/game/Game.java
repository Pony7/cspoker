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
import game.chips.IllegalValueException;
import game.chips.pot.Pot;
import game.chips.pot.SidePots;
import game.deck.Deck;
import game.player.AllInPlayer;
import game.player.Player;
import game.utilities.LoopingList;

import java.util.ArrayList;
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
	
	private final GameProperty gameProperty;
	
	private final List<Player> players = new CopyOnWriteArrayList<Player>();
	
	private LoopingList<Player> currentHandPlayers;
	
	private final Deck deck;
	
	private List<Card> openCards;
	
	private SidePots sidePots;
	
	private Pot pot;
	
	private Player dealer;
	
	private Player firstToActPlayer;
	
	private final List<Player> allInPlayers = new ArrayList<Player>();
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	/**
	 * Construct a new game with given game property.
	 * 
	 */
	public Game(GameProperty gameProperty){
		deck = new Deck();
		this.gameProperty = gameProperty;
		//set initial dealer-button holder
	}
	
	/**********************************************************
	 * Getters
	 **********************************************************/
	
	public GameProperty getGameProperty(){
		return gameProperty;
	}
	
	public Player getCurrentPlayer(){
		return currentHandPlayers.getCurrent();
	}
	
	public List<Player> getPlayers(){
		return Collections.unmodifiableList(players);
	}
	
	/**********************************************************
	 * Add/Remove Players to/from the game.
	 **********************************************************/
	
	/**
	 * Removes the given player from this game
	 * @throws	IllegalArgumentException
	 * 			if the given player isn't part of this game
	 * 			| !hasAsPlayer(player)
	 * @post	The given player isn't part of this game anymore
	 * 			| !new.hasAsPlayer(player)
	 */
	public void removePlayer(Player player){
		if(!hasAsPlayer(player))
			throw new IllegalArgumentException();
		players.remove(player);
	}
	/**
	 * Adds the given player to this game
	 * @param player
	 * 			the given player
	 * @throws PlayerListFullException
	 * 			if this game is full of players
	 * 			| fullOfPlayers()
	 */
	public void addPlayer(Player player) throws PlayerListFullException{
		if(fullOfPlayers())
			throw new PlayerListFullException();
		players.add(player);
	}
	/**
	 * Checks wether this game is full of players
	 */
	public boolean fullOfPlayers(){
		return players.size()>=getGameProperty().getMaxNbPlayers();
	}
	/**
	 * Checks wether the given player is part of this game
	 * @param player
	 * 			the given player
	 */
	public boolean hasAsPlayer(Player player){
		return players.contains(player);
	}
	/**********************************************************
	 * Round manipulation.
	 **********************************************************/
	
	public void nextPlayer(){
		currentHandPlayers.next();
	}
	
	public void setCurrentPlayer(Player player){
		currentHandPlayers.setCurrent(player);
	}
	
	public void removePlayerFromCurrentDeal(Player player){
		currentHandPlayers.remove(player);
	}
	
	public void dealNewHand(){
		deck.newDeal();
		currentHandPlayers = new LoopingList<Player>(players);
		setCurrentPlayer(dealer);
		nextPlayer();
		try {
			collectSmallBlind(getCurrentPlayer());
		} catch (IllegalValueException e) {
			
			//All-in logic
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nextPlayer();
		try {
			collectBigBlind(getCurrentPlayer());
		} catch (IllegalValueException e) {
			// TODO Auto-generated catch block
			//All-in logic
			e.printStackTrace();
		}
		
		nextPlayer();

		//special case with 2 players
		//and if the small blind player
		//is all-in, other player can call
		//the all-in bet.
	}
	
	/**
	 * Collect small blind from given player.
	 * 
	 * @param 	player
	 * 			The player to collect the small blind from.
	 * @throws IllegalValueException
	 */
	private void collectSmallBlind(Player player) throws IllegalValueException{
		player.transferAmountToBettedPile(gameProperty.getSmallBlind());
	}
	
	/**
	 * Collect big blind from given player.
	 * 
	 * @param 	player
	 * 			The player to collect the big blind from.
	 * @throws IllegalValueException
	 */
	private void collectBigBlind(Player player) throws IllegalValueException{
		player.transferAmountToBettedPile(gameProperty.getBigBlind());
	}
	
	/**********************************************************
	 * All-In Logic
	 **********************************************************/
	
	
	private void allIn(Player player){
		allInPlayers.add(player);
		removePlayerFromCurrentDeal(player);
	}
	
	public List<AllInPlayer> getAllInPlayers(){
		return null;
	}
	
	public void newRound(){
		
	}

}
