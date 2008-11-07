/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.server.common.gamecontrol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.GamePots;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.server.common.elements.cards.deck.Deck;
import org.cspoker.server.common.elements.table.PlayerListFullException;
import org.cspoker.server.common.elements.table.SeatTakenException;
import org.cspoker.server.common.elements.table.ServerTable;
import org.cspoker.server.common.gamecontrol.rules.BettingRules;
import org.cspoker.server.common.gamecontrol.rules.NoLimit;
import org.cspoker.server.common.util.LoopingList;

/**
 * This class contains all game elements, such as the players, open cards,
 * pots,...
 * 
 * @author Kenzo
 */
public class Game {
	
	private static Logger logger = Logger.getLogger(Game.class);
	
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	/**
	 * This variable contains the table of this game.
	 */
	private final ServerTable table;
	
	/**
	 * This variable contains the game property of this game.
	 */
	private final TableConfiguration configuration;
	
	/**
	 * This looping list contains the active players of this game.
	 */
	private LoopingList<MutableSeatedPlayer> currentHandPlayers;
	
	/**
	 * This looping list contains the initial players of this game.
	 */
	private LoopingList<MutableSeatedPlayer> initialCurrentHandPlayers;
	
	/**
	 * This list contains the players who have requested to sit out for the next
	 * deal.
	 */
	private ConcurrentHashMap<MutableSeatedPlayer, Boolean> playersSittingOutNextRound;
	/**
	 * This variable contains the deck of cards of this game.
	 */
	private Deck deck;
	
	/**
	 * This list contains all common cards.
	 */
	private List<Card> communityCards;
	
	/**
	 * This variable contains all the pots in this game.
	 */
	private GamePots pots;
	
	/**
	 * This variable contains the dealer of this game.
	 */
	private MutableSeatedPlayer dealer;
	
	/**
	 * This variable contains the firstToActPlayer of this game.
	 */
	private MutableSeatedPlayer firstToActPlayer;
	
	/**
	 * The last event player is the last player that has done significant
	 * change, such as a raise. If the next player is the last event player, the
	 * round is over. It is initialized in each game as the first better after
	 * the big blind, in every next round, it is the player on to the left side
	 * of the player with the dealer-button.
	 */
	private MutableSeatedPlayer lastActionPlayer;
	
	/**
	 * This variable contains the next dealer of this game.
	 */
	private MutableSeatedPlayer nextDealer;
	
	private BettingRules bettingRules;
	
	public MutableSeatedPlayer getLastActionPlayer() {
		return lastActionPlayer;
	}
	
	public void setLastActionPlayer(MutableSeatedPlayer player) {
		lastActionPlayer = player;
	}
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	
	/**
	 * Construct a new game with given table.
	 * 
	 * @pre The table should be effective |table!=null
	 * @pre The table should be effective and there must be at least 2 players.
	 *      |table!=null && table.getNbPlayer()>1
	 */
	public Game(ServerTable table, TableConfiguration configuration) {
		this(table, configuration, table.getRandomPlayer());
	}
	
	public Game(ServerTable table, TableConfiguration configuration, MutableSeatedPlayer dealer) {
		this(table, configuration, dealer, new NoLimit());
		
	}
	
	public Game(ServerTable table, TableConfiguration configuration, MutableSeatedPlayer dealer,
			BettingRules bettingRules) {
		this.bettingRules = bettingRules;
		this.table = table;
		this.configuration = configuration;
		List<MutableSeatedPlayer> players = table.getMutableSeatedPlayers();
		currentHandPlayers = new LoopingList<MutableSeatedPlayer>(players);
		initialCurrentHandPlayers = new LoopingList<MutableSeatedPlayer>(players);
		playersSittingOutNextRound = new ConcurrentHashMap<MutableSeatedPlayer, Boolean>();
		deck = new Deck();
		communityCards = new ArrayList<Card>();
		pots = new GamePots();
		changeDealer(dealer);
		nbShowdownPlayers = 0;
	}
	
	/***************************************************************************
	 * Deal new hand.
	 **************************************************************************/
	
	/**
	 * This is the action the dealer does to start a new game.
	 * 
	 * @pre WaitingRound & Dealer calls deal();
	 */
	public void dealNewHand() {
		nbShowdownPlayers = 0;
		communityCards = new ArrayList<Card>();
		deck = new Deck();
		pots = new GamePots();
		List<MutableSeatedPlayer> players = new ArrayList<MutableSeatedPlayer>();
		players.addAll(table.getMutableSeatedPlayers());
		players.removeAll(playersSittingOutNextRound.keySet());
		playersSittingOutNextRound.clear();
		
		// new looping lists
		currentHandPlayers = new LoopingList<MutableSeatedPlayer>(players);
		initialCurrentHandPlayers = new LoopingList<MutableSeatedPlayer>(players);
		
		// make sure no one has pocket cards (TODO move to game player + bet
		// pile)
		for (MutableSeatedPlayer player : currentHandPlayers.getList()) {
			player.clearPocketCards();
		}
		setCurrentPlayer(dealer);
		nextPlayer();
		setFirstToActPlayer(getCurrentPlayer());
		setNextDealer(getCurrentPlayer());
	}
	
	public void initializeForNewHand() {
		seatInitalDealPlayers();
		setDealer(getNextDealer());
	}
	
	/***************************************************************************
	 * First to act player
	 **************************************************************************/
	
	/**
	 * Return the firstToActPlayer of this game.
	 */
	public MutableSeatedPlayer getFirstToActPlayer() {
		return firstToActPlayer;
	}
	
	/**
	 * Check whether this game can have the given firstToActPlayer as their
	 * firstToActPlayer.
	 * 
	 * @param firstToActPlayer The firstToActPlayer to check.
	 * @return True if the firstToActPlayer is effective and if the given player
	 *         is part of this game. | result == firstToActPlayer!=null &&
	 *         hasAsActivePlayer(firstToActPlayer)
	 */
	public boolean canHaveAsFirstToActPlayer(MutableSeatedPlayer firstToActPlayer) {
		return (firstToActPlayer != null) && hasAsActivePlayer(firstToActPlayer);
	}
	
	/**
	 * Set the firstToActPlayer of this game to the given firstToActPlayer.
	 * 
	 * @param firstToActPlayer The new firstToActPlayer for this game.
	 * @pre This game must be able to have the given firstToActPlayer as its
	 *      firstToActPlayer. | canHaveAsFirstToActPlayer(firstToActPlayer)
	 * @post The firstToActPlayer of this game is set to the given
	 *       firstToActPlayer. | new.getFirstToActPlayer() == firstToActPlayer
	 */
	public void setFirstToActPlayer(MutableSeatedPlayer firstToActPlayer) {
		this.firstToActPlayer = firstToActPlayer;
	}
	
	/***************************************************************************
	 * Dealer
	 **************************************************************************/
	
	/**
	 * Return the dealer of this game.
	 * 
	 * @return The dealer of this game.
	 */
	public MutableSeatedPlayer getDealer() {
		return dealer;
	}
	
	/**
	 * Check whether this game can have the given dealer as their dealer.
	 * 
	 * @param dealer The dealer to check.
	 * @return True if the dealer is effective and if the given player is seated
	 *         at this table. | result == (dealer!=null) &&
	 *         getTable().hasAsPlayer(dealer)
	 */
	public boolean canHaveAsDealer(MutableSeatedPlayer dealer) {
		return (dealer != null) && getTable().hasAsPlayer(dealer);
	}
	
	/**
	 * Set the dealer of this game to the given dealer.
	 * 
	 * @param dealer The new dealer for this game.
	 * @pre This game must be able to have the given dealer as its dealer. |
	 *      canHaveAsDealer(dealer)
	 * @post The dealer of this game is set to the given dealer. |
	 *       new.getDealer() == dealer
	 */
	public void setDealer(MutableSeatedPlayer dealer) {
		this.dealer = dealer;
	}
	
	/***************************************************************************
	 * Next Dealer
	 **************************************************************************/
	
	/**
	 * Return the next dealer of this game.
	 * 
	 * @return The next dealer of this game.
	 */
	public MutableSeatedPlayer getNextDealer() {
		return nextDealer;
	}
	
	/**
	 * Check whether this game can have the given next dealer as their next
	 * dealer.
	 * 
	 * @param nextDealer The next dealer to check.
	 * @return True if the next dealer is effective and if the given player is
	 *         seated at this table. | result == (nextDealer!=null) &&
	 *         getTable().hasAsPlayer(nextDealer)
	 */
	public boolean canHaveAsNextDealer(MutableSeatedPlayer nextDealer) {
		return (nextDealer != null) && getTable().hasAsPlayer(nextDealer);
	}
	
	/**
	 * Set the next dealer of this game to the given next dealer.
	 * 
	 * @param nextDealer The new next dealer for this game.
	 * @pre This game must be able to have the given next dealer as its next
	 *      dealer. | canHaveAsNextDealer(nextDealer)
	 * @post The next dealer of this game is set to the given next dealer. |
	 *       new.getNextDealer() == nextDealer
	 */
	public void setNextDealer(MutableSeatedPlayer nextDealer) {
		this.nextDealer = nextDealer;
	}
	
	/***************************************************************************
	 * Game Property
	 **************************************************************************/
	
	/**
	 * Returns the game property of this game.
	 * 
	 * @return The game property of this game.
	 */
	public TableConfiguration getTableConfiguration() {
		return configuration;
	}
	
	/***************************************************************************
	 * Game Property
	 **************************************************************************/
	
	public BettingRules getBettingRules() {
		return bettingRules;
	}
	
	/***************************************************************************
	 * Pots
	 **************************************************************************/
	
	/**
	 * Returns the pots of this game.
	 * 
	 * @return The pots of this game.
	 */
	public GamePots getPots() {
		return pots;
	}
	
	/***************************************************************************
	 * Table
	 **************************************************************************/
	
	/**
	 * Returns the table of this game.
	 * 
	 * @return The table of this game.
	 */
	public ServerTable getTable() {
		return table;
	}
	
	/***************************************************************************
	 * Round manipulation.
	 **************************************************************************/
	
	/**
	 * Change the current player to the next player.
	 */
	public void nextPlayer() {
		currentHandPlayers.next();
	}
	
	/**
	 * Returns the current player of this game.
	 * 
	 * @return The current player of this game.
	 */
	public MutableSeatedPlayer getCurrentPlayer() {
		if (currentHandPlayers.size() == 0) {
			return null;
		}
		return currentHandPlayers.getCurrent();
	}
	
	/**
	 * Returns the previous player of this game.
	 * 
	 * @return The previous player of this game.
	 */
	public MutableSeatedPlayer getPreviousPlayer() {
		return currentHandPlayers.getPrevious();
	}
	
	/**
	 * Returns the next player of this game.
	 * 
	 * @return The next player of this game.
	 */
	public MutableSeatedPlayer getNextPlayer() {
		return currentHandPlayers.getNext();
	}
	
	/**
	 * Set the current player to the given player.
	 * 
	 * @param player The given player
	 * @pre This game must be able to have the given player as its current
	 *      player. | canHaveAsCurrentPlayer(player)
	 * @post The current player is set to the given player
	 *       |new.getCurrentPlayer()==player
	 */
	public void setCurrentPlayer(MutableSeatedPlayer player) {
		currentHandPlayers.setCurrent(player);
	}
	
	public void changeCurrentPlayerToDealer() {
		setCurrentPlayer(getDealer());
	}
	
	public void changeCurrentPlayerToInitial() {
		setCurrentPlayer(getFirstToActPlayer());
	}
	
	/**
	 * Check whether this game can have the given player as its current player.
	 * 
	 * @param player The player to check.
	 * @return True if the player is effective and if the given player is seated
	 *         at this table, False otherwise. | result == (player!=null) &&
	 *         hasAsActivePlayer(player)
	 */
	public boolean canHaveAsCurrentPlayer(MutableSeatedPlayer player) {
		return (player != null) && hasAsActivePlayer(player);
	}
	
	/**
	 * Remove the given player from current deal.
	 * 
	 * @param player The player to remove.
	 * @pre The given player is an active player at this table.
	 *      |hasAsActivePlayer(player)
	 * @post The given player is no active player any more in this game.
	 *       |!new.hasAsActivePlayer(player)
	 * @post If the removed player is the first to act player in the game,
	 *       change the first to act player to the next player.
	 * @post If the given player is the current player, the current player is
	 *       changed to the next player.
	 */
	public void removePlayerFromCurrentDeal(MutableSeatedPlayer player) {
		if (getFirstToActPlayer().equals(player)) {
			setFirstToActPlayer(currentHandPlayers.getNextTo(player));
		}
		if (getLastActionPlayer().equals(player)) {
			setLastActionPlayer(currentHandPlayers.getPreviousTo(player));
		}
		currentHandPlayers.remove(player);
	}
	
	/**
	 * Returns the list of all active players in this game.
	 * 
	 * @return The list of all active players in this game.
	 */
	public List<MutableSeatedPlayer> getCurrentDealPlayers() {
		return currentHandPlayers.getList();
	}
	
	/**
	 * Returns the number of players that can act at in this deal.
	 * 
	 * @return The number of players that can act in the current deal.
	 */
	public int getNbCurrentDealPlayers() {
		return currentHandPlayers.size();
	}
	
	/**
	 * Check whether this game has the given player as an active player.
	 * 
	 * @param player The player to check.
	 * @return True if the given player is contained in the list of current deal
	 *         players, False otherwise.
	 */
	public boolean hasAsActivePlayer(MutableSeatedPlayer player) {
		return currentHandPlayers.contains(player);
	}
	
	/***************************************************************************
	 * Card Logic
	 **************************************************************************/
	
	public List<Card> drawCards(int nbCards) {
		return deck.deal(nbCards);
	}
	
	/**
	 * Draw a card from the deck.
	 */
	public Card drawCard() {
		return deck.drawCard();
	}
	
	/**
	 * Add the given card to the community cards.
	 * 
	 * @param card The card to add to the community cards.
	 */
	public void addOpenCard(Card card) {
		communityCards.add(card);
	}
	
	/**
	 * Add the given card to the muck. The given card is not added to the
	 * community cards.
	 * 
	 * @param card The card to add to the muck.
	 */
	public void addMuckCard(Card card) {
	// only for formalism :)
	// it does what is says it does...
	}
	
	/**
	 * Returns the list of all community cards.
	 * 
	 * @return The list of all community cards.
	 */
	public List<Card> getCommunityCards() {
		return new ArrayList<Card>(communityCards);
	}
	
	public void addTablePlayersToGame() {
		currentHandPlayers = new LoopingList<MutableSeatedPlayer>(table.getMutableSeatedPlayers());
	}
	
	public void seatInitalDealPlayers() {
		currentHandPlayers = initialCurrentHandPlayers;
	}
	
	/**
	 * @return The number of players sitting at the table which are ready to
	 *         play or playing (not "sitting out")
	 */
	public int getNbSeatedPlayers() {
		int result = 0;
		for (MutableSeatedPlayer player : table.getMutableSeatedPlayers()) {
			if (player.isSittingIn()) {
				result++;
			}
		}
		return result;
	}
	
	public boolean hasNoSeatedPlayers() {
		return getNbSeatedPlayers() == 0;
	}
	
	/***************************************************************************
	 * Leave/Join Game
	 **************************************************************************/
	
	public SeatId sitIn(SeatId seatId, MutableSeatedPlayer player)
			throws SeatTakenException, IllegalActionException {
		if (seatId == null) {
			try {
				seatId = table.addPlayer(player);
			} catch (PlayerListFullException e) {
				throw new IllegalActionException(e.getMessage(), e);
			}
		}else{
			table.addPlayer(seatId, player);
		}
		
		if (getDealer() == null) {
			setDealer(player);
		} else if (getNextDealer() == null) {
			setNextDealer(player);
		}
		playersSittingOutNextRound.remove(player);
		Game.logger.info(player.getName() + " sits in. [" + "]");
		return seatId;
	}
	
	public void sitOut(MutableSeatedPlayer player, boolean forced)
			throws IllegalActionException {
		if (!table.hasAsPlayer(player)) {
			throw new IllegalActionException(player.getName() + " is not seated at this table.");
		}
		if ((getNextDealer() == null) || getNextDealer().equals(player)) {
			setNextDealer(initialCurrentHandPlayers.getNextTo(player));
		}
		if ((getDealer() == null) || getDealer().equals(player)) {
			setDealer(getNextDealer());
		}
		
		playersSittingOutNextRound.put(player, forced);
		Game.logger.info(player.getName() + " sits out next hand.");
	}
	
	/**
	 * Change the dealer of this game to the given dealer.
	 * 
	 * @param dealer The dealer to change to.
	 * @pre The given dealer should be effective. |dealer!=null
	 * @pre The given dealer should be an active player at the table.
	 *      |dealer!=null && hasAsActivePlayer(dealer)
	 */
	public void changeDealer(MutableSeatedPlayer dealer) {
		if (dealer == null) {
			throw new IllegalArgumentException("The given dealer should be effective");
		}
		if (!hasAsActivePlayer(dealer)) {
			throw new IllegalArgumentException("The dealer should be an active player of the current deal");
		}
		setDealer(dealer);
		setNextDealer(initialCurrentHandPlayers.getNextTo(dealer));
		setFirstToActPlayer(initialCurrentHandPlayers.getNextTo(dealer));
		setCurrentPlayer(dealer);
	}
	
	private int nbShowdownPlayers;
	
	public int getNbLastShowdown() {
		return nbShowdownPlayers;
	}
	
	public void showdownOccured(int nbShowdownPlayers) {
		this.nbShowdownPlayers = nbShowdownPlayers;
	}
	
	public Map<MutableSeatedPlayer, Boolean> getPlayersSittingOutNextRound() {
		return playersSittingOutNextRound;
	}
}
