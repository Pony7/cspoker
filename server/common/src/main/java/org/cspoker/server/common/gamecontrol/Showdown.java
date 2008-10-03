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

package org.cspoker.server.common.gamecontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.Winner;
import org.cspoker.server.common.elements.cards.hand.Hand;
import org.cspoker.server.common.elements.chips.IllegalValueException;
import org.cspoker.server.common.elements.chips.pot.GamePot;
import org.cspoker.server.common.player.GameSeatedPlayer;
import org.cspoker.server.common.player.GameShowdownPlayer;
import org.cspoker.server.common.player.GameWinner;

/**
 * A class to determine who has won each pot.
 * 
 * 
 */
public class Showdown {
	private static Logger logger = Logger.getLogger(Showdown.class);

	/**
	 * The game in which the showdown takes place.
	 */
	private final Game game;

	private final PokerTable gameMediator;

	private final Map<PlayerId, GameWinner> winnersMap = new HashMap<PlayerId, GameWinner>();

	/**
	 * Construct a new showdown with given game and pots.
	 * 
	 * @param game
	 *            The game in which the showdown takes place.
	 * @param pots
	 *            The pots to divide.
	 * @pre The game must be effective. |game!=null
	 * @pre The pots must be closed. |game!=null && game.getPots().isClosed()
	 */
	public Showdown(PokerTable gameMediator, Game game) {
		this.gameMediator = gameMediator;
		this.game = game;
	}

	/**
	 * Returns the game in which this showdown takes place.
	 * 
	 * @return The game in which this showdown takes place.
	 */
	public Game getGame() {
		return game;
	}

	public int getNbShowdownPlayers() {
		return game.getPots().getPots().get(0).getPlayers().size();
	}

	/**
	 * Determine the winners of each pot. Each pot is splitted between all
	 * winners of that pot.
	 * 
	 */
	public void determineWinners() {
		Showdown.logger.info("*** SHOW DOWN ***");
		Showdown.logger.info(game.getPots());

		// TODO all-in players always, others can choose to show or fold.
		List<GameShowdownPlayer> showdownPlayers = getShowdownPlayersFromPot(game
				.getPots().getPots().get(0));

		for (GameShowdownPlayer player : showdownPlayers) {
			gameMediator.publishShowHand(new ShowHandEvent(player
					.getSavedShowdownPlayer()));
		}

		for (GamePot pot : game.getPots().getPots()) {
			List<GameSeatedPlayer> winners = getWinners(pot);
			splitPot(winners, pot);
		}

		Set<Winner> savedWinners = new HashSet<Winner>();

		for (GameWinner winner : winnersMap.values()) {
			if (winner.hasGainedChips()) {
				savedWinners.add(winner.getSavedWinner());
				Showdown.logger.info(winner);
				winner.transferGainedChipsToPlayer();
			}
		}
		gameMediator.publishWinner(new WinnerEvent(savedWinners));

	}

	/**
	 * Split the pot between all winners.
	 * 
	 * If splitting a pot because of tied hands, award the odd chip to the hand
	 * that contains the highest-ranking single card, using suits to break ties
	 * if necessary (clubs ranking the lowest, followed by diamonds, hearts, and
	 * spades as in bridge).
	 * 
	 * http://en.wikipedia.org/wiki/Split_(poker)
	 * 
	 * @param winners
	 *            The list of winners of the pot.
	 * @param pot
	 *            The pot to divide between all winners.
	 */
	private void splitPot(List<GameSeatedPlayer> winners, GamePot pot) {
		for (GameSeatedPlayer winner : winners) {
			if (!winnersMap.containsKey(winner.getId())) {
				winnersMap.put(winner.getId(), new GameWinner(winner));
			}
		}

		int nbChips_per_winner = pot.getChips().getValue() / winners.size();

		for (GameSeatedPlayer player : winners) {
			try {
				pot.getChips().transferAmountTo(nbChips_per_winner,
						winnersMap.get(player.getId()).getGainedChipsPile());
			} catch (IllegalValueException e) {
				Showdown.logger.error(e);
				assert false;
			}
		}

		// the player with the single highest card gets the odd chips that can't
		// be divided over the winners
		if (pot.getChips().getValue() != 0) {
			GameSeatedPlayer playerWithHighestSingleCard = winners.get(0);
			Card highestCard = new Hand(playerWithHighestSingleCard
					.getPocketCards()).getHighestRankCard();
			for (GameSeatedPlayer player : winners) {
				Card otherHighestCard = new Hand(player.getPocketCards())
						.getHighestRankCard();
				int compareSingleBestCard = highestCard
						.compareTo(otherHighestCard);
				if ((compareSingleBestCard > 0)
						|| ((compareSingleBestCard == 0) && (otherHighestCard
								.getSuit().compareTo(highestCard.getSuit()) > 0))) {
					playerWithHighestSingleCard = player;
					highestCard = otherHighestCard;
				}
			}
			Showdown.logger
					.info("Odd chips to player with highest card in hand");
			pot.getChips().transferAllChipsTo(
					winnersMap.get(playerWithHighestSingleCard.getId())
							.getGainedChipsPile());
		}
	}

	/**
	 * Returns the list of winners of the given pot in the current game.
	 * 
	 * @param pot
	 *            The pot in which the winner(s) must be chosen.
	 * @return The list of winners of the pot in the current game.
	 */
	private List<GameSeatedPlayer> getWinners(GamePot pot) {
		List<GameShowdownPlayer> players = getShowdownPlayersFromPot(pot);
		Collections.sort(players);
		for (GameShowdownPlayer player : players) {
			Showdown.logger.info(player);
		}
		GameShowdownPlayer winner = players.get(0);
		List<GameSeatedPlayer> winners = new ArrayList<GameSeatedPlayer>();
		int i = 0;
		while ((i < players.size()) && winner.equals(players.get(i))) {
			winners.add(players.get(i).getPlayer());
			i++;
		}
		return winners;
	}

	/**
	 * Returns the list of showdown players in the current game.
	 * 
	 * @param pot
	 *            The pot from which the showdown players must be returned.
	 * @return The list of showdown players in the current game.
	 */
	private List<GameShowdownPlayer> getShowdownPlayersFromPot(GamePot pot) {
		List<GameShowdownPlayer> showDownPlayers = new ArrayList<GameShowdownPlayer>();
		for (GameSeatedPlayer player : pot.getPlayers()) {
			showDownPlayers.add(new GameShowdownPlayer(player,
					getBestFiveCardHand(player)));
		}
		return showDownPlayers;
	}

	/**
	 * Get the best hand for the given player in the current game.
	 * 
	 * @param player
	 *            The player to determine the best hand for.
	 * @return The best hand that can be made with both common and pocket cards.
	 * 
	 * @note By using this method, the recalculation of finding the best 5 card
	 *       hand is omitted.
	 */
	private Hand getBestFiveCardHand(GameSeatedPlayer player) {
		List<Card> cards = new ArrayList<Card>(7);
		cards.addAll(getGame().getCommunityCards());
		cards.addAll(player.getPocketCards());
		return new Hand(cards).getBestFive();
	}
}
