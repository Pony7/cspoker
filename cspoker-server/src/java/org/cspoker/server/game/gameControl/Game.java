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

package org.cspoker.server.game.gameControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.deck.Deck;
import org.cspoker.server.game.elements.chips.pot.Pots;
import org.cspoker.server.game.elements.table.PlayerListFullException;
import org.cspoker.server.game.elements.table.Table;
import org.cspoker.server.game.player.Player;
import org.cspoker.server.game.utilities.LoopingList;


/**
 *
 * This class contains all game elements,
 * such as the players, open cards, pots,...
 *
 * @author Kenzo
 *
 */
public class Game {
	private static Logger logger = Logger.getLogger(Game.class);

	/**********************************************************
	 * Variables
	 **********************************************************/

	/**
	 * This variable contains the table of this game.
	 */
	private final Table table;

	/**
	 * This variable contains the game property of this game.
	 */
	private final GameProperty gameProperty;

	/**
	 * This looping list contains the active players
	 * of this game.
	 */
	private LoopingList<Player> currentHandPlayers;

	private LoopingList<Player> initialCurrentHandPlayers;

	/**
	 * This variable contains the deck of cards of this game.
	 */
	private final Deck deck;

	/**
	 * This list contains all common cards.
	 */
	private List<Card> communityCards;

	/**
	 * This variable contains all the pots in this game.
	 */
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
	 * This variable contains the next dealer of this game.
	 */
	private Player nextDealer;


	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Construct a new game with given table.
	 *
	 * @pre 	The table should be effective
	 *			|table!=null
	 * @pre 	The table should be effective and there must
	 * 			be at least 2 players.
	 *			|table!=null && table.getNbPlayer()>1
	 *
	 */
	public Game(Table table){
		this.table = table;
		table.setPlaying(true);
		gameProperty = table.getGameProperty();
		List<Player> players = table.getPlayers();
		currentHandPlayers = new LoopingList<Player>(players);
		initialCurrentHandPlayers = new LoopingList<Player>(players);
		deck = new Deck();
		communityCards = new ArrayList<Card>();
		pots = new Pots();
		changeDealer(currentHandPlayers.getList().get(new Random().nextInt(currentHandPlayers.size())));
	}

	/**********************************************************
	 * Deal new hand.
	 **********************************************************/

	/**
	 * Deal a new hand.
	 *
	 */
	public void dealNewHand(){
		communityCards = new ArrayList<Card>();
		deck.newDeal();
		pots = new Pots();
		List<Player> players = table.getPlayers();
		currentHandPlayers = new LoopingList<Player>(players);
		initialCurrentHandPlayers = new LoopingList<Player>(players);
		for(Player player:currentHandPlayers.getList()){
			player.clearPocketCards();
		}
		setCurrentPlayer(dealer);
		nextPlayer();
		setFirstToActPlayer(getCurrentPlayer());
		setNextDealer(getCurrentPlayer());
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

	/**********************************************************
	 * Game Property
	 **********************************************************/

	/**
	 * Returns the game property of this game.
	 *
	 * @return	The game property of this game.
	 */
	public GameProperty getGameProperty(){
		return gameProperty;
	}

	/**********************************************************
	 * Pots
	 **********************************************************/

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

	/**
	 * Change the current player to the next player.
	 */
	public void nextPlayer(){
		currentHandPlayers.next();
	}

	/**
	 * Returns the current player of this game.
	 *
	 * @return The current player of this game.
	 */
	public Player getCurrentPlayer(){
		if(currentHandPlayers.size()==0)
			return null;
		return currentHandPlayers.getCurrent();
	}

	/**
	 * Returns the previous player of this game.
	 *
	 * @return The previous player of this game.
	 */
	public Player getPreviousPlayer(){
		return currentHandPlayers.getPrevious();
	}

	/**
	 * Returns the next player of this game.
	 *
	 * @return The next player of this game.
	 */
	public Player getNextPlayer(){
		return currentHandPlayers.getNext();
	}

	/**
	 * Set the current player to the given player.
	 *
	 * @param 	player
	 * 			The given player
	 * @pre    	This game must be able to have the given player
	 * 			as its current player.
	 * 			| canHaveAsCurrentPlayer(player)
	 * @post 	The current player is set to the given player
	 *		 	|new.getCurrentPlayer()==player
	 */
	public void setCurrentPlayer(Player player){
		currentHandPlayers.setCurrent(player);
	}

	/**
	 * Check whether this game can have the given player
	 * as its current player.
	 *
	 * @param	player
	 * 			The player to check.
	 * @return	True if the player is effective
	 * 			and if the given player is seated at this table,
	 * 			False otherwise.
	 * 			| result == (player!=null) && hasAsActivePlayer(player)
	 */
	public boolean canHaveAsCurrentPlayer(Player player){
		return (player!=null) && hasAsActivePlayer(player);
	}

	/**
	 * Remove the given player from current deal.
	 *
	 * @param 	player
	 * 			The player to remove.
	 * @pre 	The given player is an active player at this table.
	 *			|hasAsActivePlayer(player)
	 * @post	The given player is no active player
	 * 			any more in this game.
	 * 			|!new.hasAsActivePlayer(player)
	 * @post	If the removed player is the first to act
	 * 			player in the game, change the first
	 * 			to act player to the next player.
	 * @post 	If the given player is the current player,
	 * 			the current player is changed to the next player.
	 */
	public void removePlayerFromCurrentDeal(Player player){
		if(getFirstToActPlayer().equals(player)){
			setFirstToActPlayer(currentHandPlayers.getNextTo(player));
		}
		currentHandPlayers.remove(player);
	}

	/**
	 * Returns the list of all active players in this game.
	 *
	 * @return The list of all active players in this game.
	 */
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

	/**
	 * Check whether this game has the given player
	 * as an active player.
	 *
	 * @param 	player
	 * 			The player to check.
	 * @return	True if the given player is contained
	 * 			in the list of current deal players,
	 * 			False otherwise.
	 */
	public boolean hasAsActivePlayer(Player player){
		return currentHandPlayers.contains(player);
	}


	/**********************************************************
	 * Card Logic
	 **********************************************************/

	/**
	 * Draw a card from the deck.
	 */
	public Card drawCard(){
		return deck.drawCard();
	}

	/**
	 * Add the given card to the community cards.
	 *
	 * @param 	card
	 * 			The card to add to the community cards.
	 */
	public void addOpenCard(Card card){
		communityCards.add(card);
	}

	/**
	 * Add the given card to the muck.
	 * The given card is not added to the community cards.
	 *
	 * @param 	card
	 * 			The card to add to the muck.
	 */
	public void addMuckCard(Card card){
		//only for formalism :)
		//it does what is says it does...
	}

	/**
	 * Returns the list of all community cards.
	 *
	 * @return The list of all community cards.
	 */
	public List<Card> getCommunityCards(){
		return new ArrayList<Card>(communityCards);
	}


	public void setToInitialHandPlayers(){
		currentHandPlayers = initialCurrentHandPlayers;
	}

	/**********************************************************
	 * Leave/Join Game
	 * @throws PlayerListFullException
	 **********************************************************/

	public void joinGame(Player player) throws PlayerListFullException{
		table.addPlayer(player);
		if(getDealer()==null){
			setDealer(player);
		}else if(getNextDealer()==null){
			setNextDealer(player);
		}
	}

	public void leaveGame(Player player) throws IllegalActionException{
		Game.logger.info("Kick out: " + player.getName());
		if(!table.hasAsPlayer(player))
			return;
		if(hasAsActivePlayer(player))
			throw new IllegalActionException("An active player can not be removed. He should fold first.");
		if((getNextDealer()==null) || getNextDealer().equals(player)){
			setNextDealer(initialCurrentHandPlayers.getNextTo(player));
		}
		if((getDealer()==null) || getDealer().equals(player)){
			setDealer(getNextDealer());
		}
		initialCurrentHandPlayers.remove(player);
		table.removePlayer(player);
	}

	/**
	 * Change the dealer of this game to the given dealer.
	 *
	 * @param 	dealer
	 * 			The dealer to change to.
	 * @pre 	The given dealer should be effective.
	 *			|dealer!=null
	 * @pre 	The given dealer should be an active player at the table.
	 *			|dealer!=null && hasAsActivePlayer(dealer)
	 */
	public void changeDealer(Player dealer){
		if(dealer==null)
			throw new IllegalArgumentException("The given dealer should be effective");
		if(!hasAsActivePlayer(dealer))
			throw new IllegalArgumentException("The dealer should be an active player of the current deal");
		setDealer(dealer);
		setNextDealer(initialCurrentHandPlayers.getNextTo(dealer));
		setFirstToActPlayer(initialCurrentHandPlayers.getNextTo(dealer));
		setCurrentPlayer(dealer);
	}
}
