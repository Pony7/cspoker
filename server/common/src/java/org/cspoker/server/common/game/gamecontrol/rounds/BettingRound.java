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

package org.cspoker.server.common.game.gamecontrol.rounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.pots.Pots;
import org.cspoker.common.events.gameevents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameevents.WinnerEvent;
import org.cspoker.common.events.gameevents.playeractionevents.AllInEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BigBlindEvent;
import org.cspoker.common.events.gameevents.playeractionevents.SmallBlindEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.player.Winner;
import org.cspoker.server.common.game.GameMediator;
import org.cspoker.server.common.game.elements.chips.IllegalValueException;
import org.cspoker.server.common.game.elements.chips.pot.GamePots;
import org.cspoker.server.common.game.gamecontrol.Game;
import org.cspoker.server.common.game.player.GameAllInPlayer;
import org.cspoker.server.common.game.player.GamePlayer;

/**
 * A class to represent betting rounds.
 * 
 * 
 * @author Kenzo
 * 
 */
public abstract class BettingRound extends Round {
	private static Logger logger = Logger.getLogger(BettingRound.class);

	/**
	 * The current bet in this round.
	 */
	private int bet;

	/**
	 * This list contains all players who go all-in in this round.
	 */
	protected final List<GameAllInPlayer> allInPlayers;

	/**
	 * This list contains all players who folded, but who have placed chips on
	 * their bet chips pile.
	 */
	protected final List<GamePlayer> foldedPlayersWithBet;

	protected boolean someoneBigAllIn = false;

	public BettingRound(GameMediator gameMediator, Game game) {
		super(gameMediator, game);
		allInPlayers = new ArrayList<GameAllInPlayer>();
		foldedPlayersWithBet = new ArrayList<GamePlayer>();
		setBet(0);
	}

	@Override
	public void check(GamePlayer player) throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName()
					+ " can not check in this round. It should be your turn.");
		}
		if (someoneHasBet()) {
			throw new IllegalActionException(player.getName()
					+ " can not check in this round. Someone has already bet.");
		}
		game.nextPlayer();
	}

	@Override
	public void bet(GamePlayer player, int amount)
			throws IllegalActionException {
		if (!onTurn(player) || someoneHasBet() || onlyOnePlayerLeft()) {
			throw new IllegalActionException(player.getName() + " can not bet "
					+ amount + " chips in this round.");
		}

		// Check whether the bet is valid, according to the betting rules.
		if (!getBettingRules().isValidBet(amount, this)) {
			throw new IllegalActionException(player.getSavedPlayer(),
					getBettingRules().getLastBetErrorMessage());
		}

		// Can not bet with zero, it is equal to check. Please use check.
		if (amount == 0) {
			throw new IllegalActionException(player.getSavedPlayer(),
					"Can not bet with 0 chips. Did you mean check?");
		}
		if (amount >= player.getStack().getValue()) {
			throw new IllegalActionException(player.getSavedPlayer(),
					"Can not bet an amount higher than your current amount of chips;"
							+ " did you mean all-in??");
		}

		try {
			player.transferAmountToBetPile(amountToIncreaseBetPileWith(player)
					+ amount);
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player.getSavedPlayer(), e
					.getMessage());
		}
		raiseBetWith(amount);
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(amount);
		playerMadeEvent(player);
		game.nextPlayer();
	}

	@Override
	public void call(GamePlayer player) throws IllegalActionException {
		if (!onTurn(player) || !someoneHasBet()) {
			throw new IllegalActionException(player.getName()
					+ " can not call in this round.");
		}

		if (getBet() == player.getBetChips().getValue()) {
			throw new IllegalActionException(player.getName()
					+ " can not call in this round.");
		}

		// Check whether the amount with which the bet chips pile
		// is increased exceeds the player's stack.
		if (amountToIncreaseBetPileWith(player) >= player.getStack().getValue()) {
			throw new IllegalActionException(player.getSavedPlayer(),
					"Can not call a bet higher than your current amount of chips;"
							+ " did you mean all-in??");
		}

		// Try to transfer the amount to the bet pile.
		try {
			player.transferAmountToBetPile(amountToIncreaseBetPileWith(player));
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player.getSavedPlayer(), e
					.getMessage());
		}

		/**
		 * If the last event player is an all-in player, change the last event
		 * player to the calling player.
		 */
		if (!game.hasAsActivePlayer(lastEventPlayer)) {
			playerMadeEvent(player);
		}

		// Change to next player
		game.nextPlayer();
	}

	@Override
	public void raise(GamePlayer player, int amount)
			throws IllegalActionException {
		if (!onTurn(player) || !someoneHasBet() || onlyOnePlayerLeft()) {
			throw new IllegalActionException(player.getName()
					+ " can not raise with " + amount + " chips in this round.");
		}

		// Check whether the raise is valid.
		if (!getBettingRules().isValidRaise(amount, this)) {
			throw new IllegalActionException(player.getSavedPlayer(),
					getBettingRules().getLastRaiseErrorMessage());
		}

		// Can not raise with zero, it is equal to call. Please use call.
		if (amount == 0) {
			throw new IllegalActionException(player.getSavedPlayer(),
					"Can not raise with 0 chips. Did you mean call?");
		}

		// If the total number of chips needed for this raise,
		// exceeds or equals the stack of the player, the player should
		// go all-in explicitly.
		if ((amount + amountToIncreaseBetPileWith(player)) >= player.getStack()
				.getValue()) {
			throw new IllegalActionException(player.getSavedPlayer(),
					"Can not raise with an amount higher than your current amount of chips;"
							+ " did you mean all-in??");
		}

		// Try to transfer the amount to the bet pile.
		try {
			player.transferAmountToBetPile(amountToIncreaseBetPileWith(player)
					+ amount);
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player.getSavedPlayer(), e
					.getMessage());
		}
		raiseBetWith(amount);
		getBettingRules().incrementNBRaises();
		getBettingRules().setLastBetAmount(amount);
		playerMadeEvent(player);
		game.nextPlayer();
	}

	@Override
	public void fold(GamePlayer player) throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName()
					+ " can not fold. It should be his turn to do an action.");
		}

		player.clearPocketCards();

		/**
		 * If the folding player has done a bet in this round, he should be
		 * remembered until the end of the round. It had to be implemented this
		 * way and not directly collected to the main pot, because all-in logic
		 * would be to complicated.
		 * 
		 * By doing the all-in logic at the end of a round, the code is easier
		 * to write.
		 */
		if (player.getBetChips().getValue() > 0) {
			foldedPlayersWithBet.add(player);
		}
		game.removePlayerFromCurrentDeal(player);

		// removing from game, automatically switches
		// to next player.
	}

	@Override
	public void allIn(GamePlayer player) throws IllegalActionException {
		if (!onTurn(player)) {
			throw new IllegalActionException(player.getName()
					+ " can not go all-in. It isn't his turn to do an action.");
		}
		goAllIn(player);
	}

	protected void goAllIn(GamePlayer player) {
		try {
			player.transferAllChipsToBetPile();
		} catch (IllegalValueException e) {
			assert false;
		}
		allInPlayers.add(new GameAllInPlayer(player));
		getGame().removePlayerFromCurrentDeal(player);
		if (player.getBetChips().getValue() > getBet()) {
			setBet(player.getBetChips().getValue());
			playerMadeEvent(player);
			someoneBigAllIn = true;
		}
		gameMediator.publishAllInEvent(new AllInEvent(player.getSavedPlayer(),
				new Pots(getCurrentPotValue())));
		// TODO put amount of raise
		BettingRound.logger
				.info(player.getName() + ": raises to $"
						+ player.getSavedPlayer().getBetChipsValue()
						+ " and is all-in");
	}

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
	public int getBet() {
		return bet;
	}

	/**
	 * Check whether rounds can have the given bet as their bet.
	 * 
	 * @param bet
	 *            The bet to check.
	 * @return The bet must be positive. | result == (bet>=0)
	 */
	public static boolean canHaveAsBet(int bet) {
		return bet >= 0;
	}

	/**
	 * Set the bet of this round to the given bet.
	 * 
	 * @param bet
	 *            The new bet for this round.
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
	 * @param amount
	 *            The amount to raise the bet with.
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
	 * @param player
	 *            The player to collect the small blind from.
	 * @throws IllegalValueException
	 */
	protected void collectSmallBlind(GamePlayer player)
			throws IllegalValueException {
		if (player.getStack().getValue() <= getGame().getGameProperty()
				.getSmallBlind()) {
			throw new IllegalValueException();
		}
		player.transferAmountToBetPile(getGame().getGameProperty()
				.getSmallBlind());
		setBet(getGame().getGameProperty().getSmallBlind());
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(
				getGame().getGameProperty().getSmallBlind());
		playerMadeEvent(player);
		gameMediator.publishSmallBlindEvent(new SmallBlindEvent(player
				.getSavedPlayer(), getGame().getGameProperty()
				.getSmallBlind(), new Pots(getCurrentPotValue())));
		BettingRound.logger.info(player.getName() + ": posts small blind $"
				+ getGame().getGameProperty().getSmallBlind());
		
	}

	/**
	 * Collect big blind from given player.
	 * 
	 * @param player
	 *            The player to collect the big blind from.
	 * @throws IllegalValueException
	 */
	protected void collectBigBlind(GamePlayer player)
			throws IllegalValueException {
		if (player.getStack().getValue() <= getGame().getGameProperty()
				.getBigBlind()) {
			throw new IllegalValueException();
		}
		player.transferAmountToBetPile(getGame().getGameProperty()
				.getBigBlind());
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(
				getGame().getGameProperty().getBigBlind());
		setBet(getGame().getGameProperty().getBigBlind());
		playerMadeEvent(player);
		gameMediator.publishBigBlindEvent(new BigBlindEvent(
				player.getSavedPlayer(), getGame()
						.getGameProperty().getBigBlind(), new Pots(
						getCurrentPotValue())));
		BettingRound.logger.info(getGame().getCurrentPlayer().getName()
				+ ": posts big blind $"
				+ getGame().getGameProperty().getBigBlind());
	}

	/**
	 * Returns how many chips a player must transfer to the bet pile to equal
	 * the current bet.
	 * 
	 * @param player
	 *            The player who wants to know how many chips to transfer.
	 * @return The number of chips the player must transfer to the bet pile to
	 *         equal the current bet.
	 */
	protected int amountToIncreaseBetPileWith(GamePlayer player) {
		return getBet() - player.getBetChips().getValue();
	}

	/***************************************************************************
	 * Cards
	 **************************************************************************/

	/**
	 * Draw a card from the deck and send it to the muck.
	 * 
	 */
	protected void drawMuckCard() {
		game.addMuckCard(drawCard());
	}

	/**
	 * Draw a card from the deck and add it to the community cards.
	 * 
	 */
	protected void drawOpenCard() {
		game.addOpenCard(drawCard());
	}

	protected void drawOpenCardAndPublishCommonCard() {
		Card card = drawCard();
		game.addOpenCard(card);
		Set<Card> cards = new HashSet<Card>(1);
		cards.add(card);
		gameMediator.publishNewCommonCardsEvent(new NewCommunityCardsEvent(
				cards));
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
	 * 
	 */
	protected void collectChips() {
		makeSidePots();
		collectBets();
	}

	/***************************************************************************
	 * Move all-in players to side pot.
	 * 
	 */
	private void makeSidePots() {
		Collections.sort(allInPlayers);
		List<GamePlayer> players = game.getCurrentDealPlayers();
		for (GameAllInPlayer allInPlayer : allInPlayers) {
			try {
				BettingRound.logger.info(game.getPots());
				game.getPots().collectAmountFromPlayersToSidePot(
						allInPlayer.getBetValue(), players);
				BettingRound.logger.info(game.getPots());
				int betValue = allInPlayer.getBetValue();
				for (GameAllInPlayer otherAllInPlayer : allInPlayers) {
					if (otherAllInPlayer.getBetValue() > 0) {
						otherAllInPlayer.transferAmountTo(betValue, game
								.getPots().getNewestSidePot());
					}
				}
				for (GamePlayer foldedPlayer : foldedPlayersWithBet) {
					if (foldedPlayer.getBetChips().getValue() > allInPlayer
							.getBetValue()) {
						foldedPlayer.getBetChips().transferAmountTo(
								allInPlayer.getBetValue(),
								game.getPots().getNewestSidePot().getChips());
					} else {
						foldedPlayer.getBetChips().transferAllChipsTo(
								game.getPots().getNewestSidePot().getChips());
						foldedPlayersWithBet.remove(foldedPlayer);
					}
				}

			} catch (IllegalValueException e) {
				BettingRound.logger.error(e.getLocalizedMessage(), e);
				assert false;
			}
			BettingRound.logger.info(game.getPots());
			game.getPots().addShowdownPlayer(allInPlayer.getPlayer());
		}

		allInPlayers.clear();
	}

	/**
	 * Collect the bets from all players.
	 * 
	 */
	private void collectBets() {
		game.getPots().collectChipsToPot(game.getCurrentDealPlayers());
		game.getPots().collectChipsToPot(foldedPlayersWithBet);
		foldedPlayersWithBet.clear();
	}

	/***************************************************************************
	 * Winner
	 **************************************************************************/

	protected void winner(GamePots pots) {
		try {
			BettingRound.logger.info("** Only One Player Left **");
			setPotsDividedToWinner(true);
			GamePlayer winner = pots.getPots().get(0).getPlayers().get(0);
			BettingRound.logger.info("Winner: " + winner.getName() + " wins "
					+ pots.getTotalValue() + " chips");

			int gainedChipsValue = pots.getPots().get(0).getChips().getValue();
			Set<Winner> savedWinner = new HashSet<Winner>(1);
			savedWinner.add(new Winner(winner.getSavedPlayer(),
					gainedChipsValue));
			pots.getPots().get(0).getChips().transferAllChipsTo(
					winner.getStack());

			gameMediator.publishWinner(new WinnerEvent(savedWinner));
		} catch (IllegalValueException e) {
			assert false;
		}
	}

	/**
	 * Returns true if there is only one player left, false otherwise.
	 * 
	 * This also implies there are no all-in players, otherwise there will be a
	 * showdown.
	 * 
	 * @return True if there is only one player left, False otherwise.
	 */
	public boolean onlyOnePlayerLeft() {
		return (getGame().getNbCurrentDealPlayers() + allInPlayers.size()
				+ getGame().getPots().getNbShowdownPlayers() <= 1);
	}

	public boolean onlyOnePlayerLeftBesidesAllInPlayers() {
		// the player must have called
		return ((getGame().getNbCurrentDealPlayers() == 1)
				&& (allInPlayers.size()
						+ getGame().getPots().getNbShowdownPlayers() > 1) && (getGame()
				.getCurrentDealPlayers().get(0).getBetChips().getValue() == getBet()));
	}

	public boolean onlyOneActivePlayer() {
		return ((game.getNbCurrentDealPlayers() == 1) && (getGame()
				.getCurrentDealPlayers().get(0).getBetChips().getValue() == getBet()))
				|| ((game.getNbCurrentDealPlayers() == 0) && (allInPlayers
						.size()
						+ getGame().getPots().getNbShowdownPlayers() == 1));
	}

	public boolean onlyAllInPlayers() {
		return (game.getNbCurrentDealPlayers() == 0)
				&& (allInPlayers.size()
						+ getGame().getPots().getNbShowdownPlayers() > 1);
	}

	/***************************************************************************
	 * Round Logic
	 **************************************************************************/

	/**
	 * Check whether the round is ended or not.
	 * 
	 * It is the case when there are no more active players, or when the last
	 * event player is the next player.
	 * 
	 * @return True if the round is ended, false otherwise.
	 */
	@Override
	public boolean isRoundEnded() {
		return super.isRoundEnded() || onlyAllInPlayers()
				|| onlyOnePlayerLeftBesidesAllInPlayers()
				|| onlyOneActivePlayer();
	}

	@Override
	public int getCurrentPotValue() {
		int currentPlayerBets = 0;
		for (GamePlayer player : game.getCurrentDealPlayers()) {
			currentPlayerBets += player.getBetChips().getValue();
		}
		int foldedPlayerBets = 0;
		for (GamePlayer player : foldedPlayersWithBet) {
			foldedPlayerBets += player.getBetChips().getValue();
		}

		int allInPlayerBets = 0;
		for (GameAllInPlayer player : allInPlayers) {
			allInPlayerBets += player.getBetValue();
		}
		return game.getPots().getTotalValue() + currentPlayerBets
				+ foldedPlayerBets + allInPlayerBets;
	}

	@Override
	public void endRound() {
		collectChips();
		// if there are no all-in players and only one active player left
		if (onlyOnePlayerLeft()) {
			game.getPots().close(game.getCurrentDealPlayers());
			winner(game.getPots());
		}
	}

}
