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
import game.cards.deck.Deck;
import game.chips.pot.Pots;
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
	
	private Pots pots;
	
	private Player dealer;
	
	private Player firstToActPlayer;
	
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
	
	public Pots getPots(){
		return pots;
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
	 * Checks whether this game is full of players
	 */
	public boolean fullOfPlayers(){
		return players.size()>=getGameProperty().getMaxNbPlayers();
	}
	/**
	 * Checks whether the given player is part of this game
	 * @param player
	 * 			the given player
	 */
	public boolean hasAsPlayer(Player player){
		return players.contains(player);
	}
	public List<Player> getPlayers(){
		return Collections.unmodifiableList(players);
	}
	
	public int getNbWaitingPlayers(){
		return players.size();
	}
	
	/**********************************************************
	 * Round manipulation.
	 **********************************************************/
	
	public void nextPlayer(){
		currentHandPlayers.next();
	}
	
	public Player getCurrentPlayer(){
		return currentHandPlayers.getCurrent();
	}
	
	public void setCurrentPlayer(Player player){
		currentHandPlayers.setCurrent(player);
	}
	
	public void removePlayerFromCurrentDeal(Player player){
		currentHandPlayers.remove(player);
		if(getFirstToActPlayer().equals(player)){
			setFirstToActPlayer(getCurrentPlayer());
		}
	}
	
	public List<Player> getCurrentHandPlayers(){
		return currentHandPlayers.getList();
	}
	
	/**
	 * Returns the number of players that
	 * can act at this moment.
	 * 
	 * @return The number of players that
	 * 			can act at this moment.
	 */
	public int getNbCurrentDealPlayers(){
		return currentHandPlayers.size();
	}
	
	
	/**
	 * Deal a new hand.
	 * 
	 */
	public void dealNewHand(){
		openCards = new ArrayList<Card>();
		deck.newDeal();
		pots = new Pots();
		currentHandPlayers = new LoopingList<Player>(players);
		setCurrentPlayer(dealer);		
		nextPlayer();
		setFirstToActPlayer(getCurrentPlayer());
	}
	
	/**********************************************************
	 * Card Logic
	 **********************************************************/	
	
	public Card drawCard(){
		return deck.drawCard();
	}
	
	public void addOpenCard(Card card){
		openCards.add(card);
	}
	
	public void addMuckCard(Card card){
		//only for formalism :)
		//it does what is says it does...
	}
	
	public List<Card> getOpenCards(){
		return Collections.unmodifiableList(openCards);
	}
	
	/**********************************************************
	 * First to act player
	 **********************************************************/	
	
	public Player getFirstToActPlayer(){
		return firstToActPlayer;
	}
	
	public void setFirstToActPlayer(Player player){
		firstToActPlayer = player;
	}
	
	/**********************************************************
	 * Dealer
	 **********************************************************/	
	
	public Player getDealer(){
		return dealer;
	}
	
	public void setDealer(Player dealer){
		this.dealer = dealer;
	}

}
