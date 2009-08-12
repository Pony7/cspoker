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

package org.cspoker.server.gamecontrol.rounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.Chips;
import org.cspoker.common.elements.chips.IllegalValueException;
import org.cspoker.common.elements.chips.MutablePots;
import org.cspoker.common.elements.player.MutableAllInPlayer;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.Winner;
import org.cspoker.common.util.Util;
import org.cspoker.server.gamecontrol.Game;
import org.cspoker.server.gamecontrol.PokerTable;

/**
 * A class to represent betting rounds.
 */
public abstract class BettingRound
		extends AbstractRound {
	
	private static Logger logger = Logger.getLogger(BettingRound.class);
	
	/**
	 * The current bet in this round.
	 */
	private int bet;
	
	/**
	 * This list contains all players who go all-in in this round.
	 */
	protected final List<MutableAllInPlayer> allInPlayers;
	
	protected final List<Chips> betsFromFoldedPlayers;
	
	protected boolean someoneBigAllIn = false;
	
	public BettingRound(PokerTable gameMediator, Game game) {
		super(gameMediator, game);
		allInPlayers = new ArrayList<MutableAllInPlayer>();
		betsFromFoldedPlayers = new ArrayList<Chips>();
		setBet(0);
	}
	
	@Override
	public void check(MutableSeatedPlayer player)
			throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName() + " can not check in this round. It should be your turn.");
		}
		if (someoneHasBet()) {
			throw new IllegalActionException(player.getName()
					+ " can not check in this round. Someone has already bet.");
		}
		game.nextPlayer();
	}
	
	@Override
	public void bet(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		if (!onTurn(player) || someoneHasBet() || onlyOneShowdownPlayerLeft() || onlyOnePlayerLeftBesidesAllInPlayers()) {
			throw new IllegalActionException(player.getName() + " can not bet " + amount + " chips in this round.");
		}
		
		// Check whether the bet is valid, according to the betting rules.
		if (!getBettingRules().isValidBet(amount, this)) {
			throw new IllegalActionException(player.toString() + "can not bet. "
					+ getBettingRules().getLastBetErrorMessage());
		}
		
		// Can not bet with zero, it is equal to check. Please use check.
		if (amount == 0) {
			throw new IllegalActionException(player.toString() + " can not bet. "
					+ "Can not bet with 0 chips. Did you mean check?");
		}
		
		if (amount >= player.getStack().getValue()) {
			allIn(player);
			return;
		}
		
		player.transferAmountToBetPile(amountToIncreaseBetPileWith(player) + amount);
		raiseBetWith(amount);
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(amount);
		playerMadeEvent(player);
		game.nextPlayer();
		gameMediator.publishBetEvent(new BetEvent(player.getId(), amount));
		BettingRound.logger.info(player.getName() + " bets " + Util.parseDollars(amount) + ".");
	}
	
	@Override
	public void call(MutableSeatedPlayer player)
			throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName() + " can not call in this round. It's not his turn.");
		}
		
		if (!someoneHasBet()) {
			throw new IllegalActionException(player.getName() + " can not call in this round. No one has bet.");
		}
		
		if (getBet() == player.getBetChips().getValue()) {
			throw new IllegalActionException(player.getName()
					+ " can not call in this round. He has already bet the amount.");
		}
		
		// Check whether the amount with which the bet chips pile
		// is increased exceeds the player's stack.
		int movedAmount = amountToIncreaseBetPileWith(player);
		if (movedAmount >= player.getStack().getValue()) {
			allIn(player);
			return;
		}
		
		// Try to transfer the amount to the bet pile.
		player.transferAmountToBetPile(movedAmount);
		
		/**
		 * If the last event player is an all-in player, change the last event
		 * player to the calling player.
		 */
		if (!game.hasAsActivePlayer(game.getLastActionPlayer())) {
			playerMadeEvent(player);
		}
		
		// Change to next player
		game.nextPlayer();
		
		gameMediator.publishCallEvent(new CallEvent(player.getId(),movedAmount, isRoundEnded()));
		BettingRound.logger.info(player.getName() + " calls.");
	}
	
	@Override
	public void raise(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName() + " can not raise with $" + amount
					+ " chips in this round because it's not his turn.");
		}
		if (!someoneHasBet()) {
			throw new IllegalActionException(player.getName() + " can not raise with $" + amount
					+ " chips in this round because nobody has placed a bet yet.");
		}
		if (onlyOneShowdownPlayerLeft()) {
			throw new IllegalActionException(player.getName() + " can not raise with $" + amount
					+ " chips in this round because there's only one player left.");
		}
		if (onlyOnePlayerLeftBesidesAllInPlayers()) {
			throw new IllegalActionException(player.getName() + " can not raise with $" + amount
					+ " chips in this round because there's only one player left who's not all-in.");
		}
		
		// Check whether the raise is valid.
		if (!getBettingRules().isValidRaise(amount, this) && !((amount + amountToIncreaseBetPileWith(player)) >= player.getStack().getValue())) {
			throw new IllegalActionException(player.toString() + " can not raise with $"+amount+". "
					+ getBettingRules().getLastRaiseErrorMessage());
		}
		
		// Can not raise with zero, it is equal to call. Please use call.
		if (amount == 0) {
			throw new IllegalActionException(player.toString() + " can not raise. "
					+ "Can not raise with 0 chips. Did you mean call?");
		}
		
		// If the total number of chips needed for this raise,
		// exceeds or equals the stack of the player, the player should
		// go all-in.
		if ((amount + amountToIncreaseBetPileWith(player)) >= player.getStack().getValue()) {
			allIn(player);
			return;
		}
		
		// Try to transfer the amount to the bet pile.
		
		int movedAmount = amountToIncreaseBetPileWith(player) + amount;
		
		player.transferAmountToBetPile(movedAmount);
		raiseBetWith(amount);
		getBettingRules().incrementNBRaises();
		getBettingRules().setLastBetAmount(amount);
		playerMadeEvent(player);
		game.nextPlayer();
		
		gameMediator.publishRaiseEvent(new RaiseEvent(player.getId(), amount, movedAmount));
		BettingRound.logger.info(player.getName() + ": raises with " + Util.parseDollars(amount) + " to "
				+ Util.parseDollars(player.getMemento().getBetChipsValue()));
	}
	
	@Override
	public void fold(MutableSeatedPlayer player)
			throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName() + " can not fold. It should be his turn to do an action.");
		}
		
		foldAction(player);
	}
	
	@Override
	public void foldAction(MutableSeatedPlayer player) {
		player.clearPocketCards();
		
		/**
		 * If the folding player has done a bet in this round, he should be
		 * remembered until the end of the round. It had to be implemented this
		 * way and not directly collected to the main pot, because all-in logic
		 * would be to complicated. By doing the all-in logic at the end of a
		 * round, the code is easier to write.
		 */
		if (player.getBetChips().getValue() > 0) {
			Chips chips = new Chips();
			player.getBetChips().transferAllChipsTo(chips);
			betsFromFoldedPlayers.add(chips);
		}
		game.removePlayerFromCurrentDeal(player);
		game.getPots().removeContributor(player);
		
		// removing from game, automatically switches
		// to next player.
	}
	
	@Override
	public void allIn(MutableSeatedPlayer player)
			throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName()
					+ " can not go all-in. It isn't his turn to do an action.");
		}
		goAllIn(player);
	}
	
	protected void goAllIn(MutableSeatedPlayer player) {
		int amount = player.getStack().getValue();
		player.transferAllChipsToBetPile();
		
		allInPlayers.add(new MutableAllInPlayer(player));
		getGame().removePlayerFromCurrentDeal(player);
		if (player.getBetChips().getValue() > getBet()) {
			if(getBet()>0){
				getBettingRules().incrementNBRaises();
			}
			setBet(player.getBetChips().getValue());
			getBettingRules().setBetPlaced(true);
			getBettingRules().setLastBetAmount(Math.max(player.getBetChips().getValue()-getBet(), getBettingRules().getLastBetAmount()));
			playerMadeEvent(player);
			someoneBigAllIn = true;
		}
		gameMediator.publishAllInEvent(new AllInEvent(player.getId(), amount, isRoundEnded()));
		
		BettingRound.logger.info(player.getName() + ": "+" goes all-in with " + Util.parseDollars(player.getMemento().getBetChipsValue()));
	}
	
	/**
	 * Someone has gone all-in, while raising.
	 * 
	 * @return
	 */
	protected boolean someoneBigAllIn() {
		return someoneBigAllIn;
	}
	
	/***************************************************************************
	 * Bet
	 **************************************************************************/
	
	/**
	 * Returns the current bet of this round.
	 * 
	 * @return The current bet of this round.
	 */
	@Override
	public int getBet() {
		return bet;
	}
	
	/**
	 * Check whether rounds can have the given bet as their bet.
	 * 
	 * @param bet The bet to check.
	 * @return The bet must be positive. | result == (bet>=0)
	 */
	public static boolean canHaveAsBet(int bet) {
		return bet >= 0;
	}
	
	/**
	 * Set the bet of this round to the given bet.
	 * 
	 * @param bet The new bet for this round.
	 * @pre This round must be able to have the given bet as its bet. |
	 *      canHaveAsBet(bet)
	 * @post The bet of this round is set to the given bet. | new.getBet() ==
	 *       bet
	 */
	protected void setBet(int bet) {
		this.bet = bet;
	}
	
	/**
	 * Returns true if someone has bet.
	 * 
	 * @return True if someone has bet, False otherwise.
	 */
	public boolean someoneHasBet() {
		return bet > 0;
	}
	
	public boolean someoneHasRaised() {
		return getBettingRules().getNBRaises() > 0;
	}
	
	/**
	 * Raise the bet with given amount.
	 * 
	 * @param amount The amount to raise the bet with.
	 * @pre The amount should be positive. |amount>0
	 * @effect Set the bet of this round to the current bet raised with given
	 *         amount. |setBet(getBet()+amount)
	 */
	protected void raiseBetWith(int amount) {
		setBet(getBet() + amount);
	}
	
	/***************************************************************************
	 * Collect blinds
	 **************************************************************************/
	
	/**
	 * Collect small blind from given player.
	 * 
	 * @param player The player to collect the small blind from.
	 * @throws IllegalValueException
	 */
	protected void collectSmallBlind(MutableSeatedPlayer player)
			throws IllegalValueException {
		if (player.getStack().getValue() <= getGame().getTableConfiguration().getSmallBlind()) {
			throw new IllegalValueException();
		}
		player.transferAmountToBetPile(getGame().getTableConfiguration().getSmallBlind());
		setBet(getGame().getTableConfiguration().getSmallBlind());
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(getGame().getTableConfiguration().getSmallBlind());
		playerMadeEvent(player);
		gameMediator.publishSmallBlindEvent(new SmallBlindEvent(player.getId(), getGame().getTableConfiguration()
				.getSmallBlind()));
		BettingRound.logger.info(player.getName() + ": posts small blind "
				+ Util.parseDollars(getGame().getTableConfiguration().getSmallBlind()));
		
	}
	
	/**
	 * Collect big blind from given player.
	 * 
	 * @param player The player to collect the big blind from.
	 * @throws IllegalValueException
	 */
	protected void collectBigBlind(MutableSeatedPlayer player)
			throws IllegalValueException {
		if (player.getStack().getValue() <= getGame().getTableConfiguration().getBigBlind()) {
			throw new IllegalValueException();
		}
		player.transferAmountToBetPile(getGame().getTableConfiguration().getBigBlind());
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(getGame().getTableConfiguration().getBigBlind());
		setBet(getGame().getTableConfiguration().getBigBlind());
		playerMadeEvent(player);
		gameMediator.publishBigBlindEvent(new BigBlindEvent(player.getId(), getGame().getTableConfiguration()
				.getBigBlind()));
		BettingRound.logger.info(getGame().getCurrentPlayer().getName() + ": posts big blind "
				+ Util.parseDollars(getGame().getTableConfiguration().getBigBlind()));
	}
	
	/**
	 * Returns how many chips a player must transfer to the bet pile to equal
	 * the current bet.
	 * 
	 * @param player The player who wants to know how many chips to transfer.
	 * @return The number of chips the player must transfer to the bet pile to
	 *         equal the current bet.
	 */
	protected int amountToIncreaseBetPileWith(MutableSeatedPlayer player) {
		return getBet() - player.getBetChips().getValue();
	}
	
	/***************************************************************************
	 * Cards
	 **************************************************************************/
	
	/**
	 * Draw a card from the deck and send it to the muck.
	 */
	protected void drawMuckCard() {
		game.addMuckCard(drawCard());
	}
	
	/**
	 * Draw a card from the deck and add it to the community cards.
	 */
	protected void drawOpenCard() {
		game.addOpenCard(drawCard());
	}
	
	protected void drawOpenCardAndPublishCommonCard() {
		Card card = drawCard();
		game.addOpenCard(card);
		gameMediator.publishNewCommonCardsEvent(new NewCommunityCardsEvent(EnumSet.of(card)));
	}
	
	/**
	 * Draw a card from the deck.
	 * 
	 * @return The top card from the deck is returned.
	 */
	protected Card drawCard() {
		return game.drawCard();
	}
	
	protected List<Card> drawCard(final int nbCards) {
		return game.drawCards(nbCards);
	}
	
	/***************************************************************************
	 * Collect Chips
	 **************************************************************************/
	
	/**
	 * Collect the bet chips pile from all players. Also creates new side pots
	 * if necessary in the case of all-in players.
	 */
	protected void collectChips() {
		game.getPots().createSidePots(allInPlayers, betsFromFoldedPlayers);
		collectBets();
	}
	
	/**
	 * Collect the bets from all players.
	 */
	private void collectBets() {
		game.getPots().getMainPot().collectAllChips();
		game.getPots().getMainPot().collectAllChips(betsFromFoldedPlayers);
		betsFromFoldedPlayers.clear();
	}
	
	/***************************************************************************
	 * Winner
	 **************************************************************************/
	
	protected void winner(MutablePots pots) {
		BettingRound.logger.info("** Only One Player Left **");
		setPotsDividedToWinner(true);
		
		if (pots.getMainPot().getContributors().size() != 1)
			throw new IllegalStateException("There can be only one winner. It's an illegal winner call.");
		
		MutableSeatedPlayer winner = pots.getMainPot().getContributors().iterator().next();
		
		BettingRound.logger.info("Winner: " + winner.getName() + " wins " + Util.parseDollars(pots.getTotalValue()));
		
		int gainedChipsValue = pots.getMainPot().getChips().getValue();
		Set<Winner> savedWinner = Collections.singleton(new Winner(winner.getMemento(), gainedChipsValue));
		pots.getMainPot().getChips().transferAllChipsTo(winner.getStack());
		
		gameMediator.publishWinnerEvent(new WinnerEvent(savedWinner));
	}
	
	/**
	 * Returns true if there is only one player left, false otherwise. This also
	 * implies there are no all-in players, otherwise there will be a showdown.
	 * 
	 */
	public boolean onlyOneShowdownPlayerLeft() {
		return (getGame().getPots().getNbShowdownPlayers() <= 1);
	}
	
	public boolean currentDealPlayerCalled() {
		return getGame().getCurrentDealPlayers().get(0).getBetChips().getValue() == getBet();
	}
	
	public boolean onlyOnePlayerLeftBesidesAllInPlayers() {
		return getGame().getNbCurrentDealPlayers() == 1
				&& (allInPlayers.size() + getGame().getPots().getNbShowdownPlayers() > 0);
	}
	
	public boolean onlyOnePlayerLeftBesidesAllInPlayersAndCalled() {
		// the player must have called
		return onlyOnePlayerLeftBesidesAllInPlayers() && currentDealPlayerCalled();
	}
	
	public boolean onlyOneActivePlayer() {
		return ((game.getNbCurrentDealPlayers() == 1) && (getGame().getCurrentDealPlayers().get(0).getBetChips()
				.getValue() == getBet()))
				|| ((game.getNbCurrentDealPlayers() == 0) && (allInPlayers.size()
						+ getGame().getPots().getNbShowdownPlayers() == 1));
	}
	
	/**
	 * If there are only all-in players
	 */
	public boolean onlyAllInPlayers() {
		return (game.getNbCurrentDealPlayers() == 0)
				&& (allInPlayers.size() + getGame().getPots().getNbShowdownPlayers() > 1);
	}
	
	/***************************************************************************
	 * Round Logic
	 **************************************************************************/
	
	/**
	 * Check whether the round is ended or not. It is the case when there are no
	 * more active players, or when the last event player is the next player.
	 * 
	 * @return True if the round is ended, false otherwise.
	 */
	
	@Override
	public boolean isRoundEnded() {
		return super.isRoundEnded() || onlyAllInPlayers() || onlyOnePlayerLeftBesidesAllInPlayersAndCalled()
				|| onlyOneActivePlayer();
	}
	
	@Override
	public int getCurrentPotValue() {
		int currentPlayerBets = 0;
		for (MutableSeatedPlayer player : game.getCurrentDealPlayers()) {
			currentPlayerBets += player.getBetChips().getValue();
		}
		int foldedPlayerBets = 0;
		for (Chips c : betsFromFoldedPlayers) {
			foldedPlayerBets += c.getValue();
		}
		
		int allInPlayerBets = 0;
		for (MutableAllInPlayer player : allInPlayers) {
			allInPlayerBets += player.getBetValue();
		}
		return game.getPots().getTotalValue() + currentPlayerBets + foldedPlayerBets + allInPlayerBets;
	}
	
	@Override
	public void endRound() {
		collectChips();
		// if there are no all-in players and only one active player left
		if (onlyOneShowdownPlayerLeft()) {
			winner(game.getPots());
			game.initializeForNewHand();
		}
	}
	
}
