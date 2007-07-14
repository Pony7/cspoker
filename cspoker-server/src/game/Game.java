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
import java.util.Random;


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
	
	private final Table table;
	
	private final GameProperty gameProperty;
	
	private LoopingList<Player> currentHandPlayers;
	
	private final Deck deck;
	
	private List<Card> openCards;
	
	private Pots pots;
	
	/**
	 * This variable contains the dealer of this game.
	 */
	private Player dealer;
	
	/**
	 * This variable contains the firstToActPlayer of this game.
	 */
	private Player firstToActPlayer;
	
	/**
	 * This variable contains the nextDealer of this game.
	 */
	private Player nextDealer;

	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	/**
	 * Construct a new game with given table.
	 * 
	 */
	public Game(Table table){
		this.table = table;
		table.setPlaying(true);
		gameProperty = table.getGameProperty();
		currentHandPlayers = new LoopingList<Player>(table.getPlayers());
		deck = new Deck();
		openCards = new ArrayList<Card>();
		pots = new Pots();
		setDealer(currentHandPlayers.getList().get(new Random().nextInt(currentHandPlayers.size())));
		setCurrentPlayer(getDealer());
		nextPlayer();
		setFirstToActPlayer(currentHandPlayers.getCurrent());
	}
	
	/**********************************************************
	 * Getters
	 **********************************************************/
	
	/**
	 * Returns the game property of this game.
	 * 
	 * @return	The game property of this game.
	 */
	public GameProperty getGameProperty(){
		return gameProperty;
	}
	
	/**
	 * Returns the pots of this game.
	 * 
	 * @return The pots of this game.
	 */
	public Pots getPots(){
		return pots;
	}
	
	/**********************************************************
	 * Table
	 **********************************************************/
	
	/**
	 * Returns the table of this game.
	 * 
	 * @return The table of this game.
	 * 
	 */
	public Table getTable(){
		return table;
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
	
	public List<Player> getCurrentDealPlayers(){
		return currentHandPlayers.getList();
	}
	
	/**
	 * Returns the number of players that
	 * can act at in this deal.
	 * 
	 * @return The number of players that
	 * 			can act in the current deal.
	 */
	public int getNbCurrentDealPlayers(){
		return currentHandPlayers.size();
	}
	
	public boolean hasAsActivePlayer(Player player){
		return currentHandPlayers.contains(player);
	}
	
	/**
	 * Deal a new hand.
	 * 
	 */
	public void dealNewHand(){
		openCards = new ArrayList<Card>();
		deck.newDeal();
		pots = new Pots();
		currentHandPlayers = new LoopingList<Player>(getTable().getPlayers());
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
	
	/**
	 * Return the firstToActPlayer of this game.
	 *
	 */
	public Player getFirstToActPlayer() {
		return firstToActPlayer;
	}

	/**
	 * Check whether this game can have the given firstToActPlayer
	 * as their firstToActPlayer.
	 *  
	 * @param	firstToActPlayer
	 * 			The firstToActPlayer to check.
	 * @return	True if the firstToActPlayer is effective
	 * 			and if the given player is part of this game.
	 * 			| result == firstToActPlayer!=null && hasAsActivePlayer(firstToActPlayer)
	 */
	public boolean canHaveAsFirstToActPlayer(Player firstToActPlayer) {
		return (firstToActPlayer!=null) && hasAsActivePlayer(firstToActPlayer);
	}

	/**
	 * Set the firstToActPlayer of this game to the given firstToActPlayer.
	 * 
	 * @param	firstToActPlayer
	 * 			The new firstToActPlayer for this game.
	 * @pre    	This game must be able to have the given firstToActPlayer
	 * 			as its firstToActPlayer.
	 * 			| canHaveAsFirstToActPlayer(firstToActPlayer)
	 * @post	The firstToActPlayer of this game is set to the given
	 * 			firstToActPlayer.
	 * 			| new.getFirstToActPlayer() == firstToActPlayer
	 */
	public void setFirstToActPlayer(Player firstToActPlayer) {
		this.firstToActPlayer = firstToActPlayer;
	}
	
	/**********************************************************
	 * Dealer
	 **********************************************************/
	
	/**
	 * Return the dealer of this game.
	 * 
	 * @return The dealer of this game.
	 *
	 */
	public Player getDealer() {
		return dealer;
	}

	/**
	 * Check whether this game can have the given dealer
	 * as their dealer.
	 *  
	 * @param	dealer
	 * 			The dealer to check.
	 * @return	True if the dealer is effective
	 * 			and if the given player is seated at this table.
	 * 			| result == (dealer!=null) && getTable().hasAsPlayer(dealer)
	 */
	public boolean canHaveAsDealer(Player dealer) {
		return (dealer!=null) && getTable().hasAsPlayer(dealer);
	}

	/**
	 * Set the dealer of this game to the given dealer.
	 * 
	 * @param	dealer
	 * 			The new dealer for this game.
	 * @pre    	This game must be able to have the given dealer
	 * 			as its dealer.
	 * 			| canHaveAsDealer(dealer)
	 * @post	The dealer of this game is set to the given
	 * 			dealer.
	 * 			| new.getDealer() == dealer
	 */
	public void setDealer(Player dealer) {
		this.dealer = dealer;
	}
	
	/**********************************************************
	 * Next Dealer
	 **********************************************************/	
	
	/**
	 * Return the next dealer of this game.
	 * 
	 * @return The next dealer of this game.
	 *
	 */
	public Player getNextDealer() {
		return nextDealer;
	}

	/**
	 * Check whether this game can have the given next dealer
	 * as their next dealer.
	 *  
	 * @param	nextDealer
	 * 			The next dealer to check.
	 * @return	True if the next dealer is effective
	 * 			and if the given player is seated at this table.
	 * 			| result == (nextDealer!=null) && getTable().hasAsPlayer(nextDealer)
	 */
	public boolean canHaveAsNextDealer(Player nextDealer) {
		return (nextDealer!=null) && getTable().hasAsPlayer(nextDealer);
	}

	/**
	 * Set the next dealer of this game to the given next dealer.
	 * 
	 * @param	nextDealer
	 * 			The new next dealer for this game.
	 * @pre    	This game must be able to have the given next dealer
	 * 			as its next dealer.
	 * 			| canHaveAsNextDealer(nextDealer)
	 * @post	The next dealer of this game is set to the given
	 * 			next dealer.
	 * 			| new.getNextDealer() == nextDealer
	 */
	public void setNextDealer(Player nextDealer) {
		this.nextDealer = nextDealer;
	}
}
