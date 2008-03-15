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

package org.cspoker.server.common.game.elements.chips.pot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.cspoker.server.common.game.elements.chips.Chips;
import org.cspoker.server.common.game.elements.chips.IllegalValueException;
import org.cspoker.server.common.game.player.GamePlayer;

/**
 * A class to represent a group of pots.
 * 
 * @author Kenzo
 * 
 */
public class GamePots {
	private static Logger logger = Logger.getLogger(GamePots.class);

	private final List<GamePot> pots;

	private final GamePot pot;

	private boolean isClosed;

	/**
	 * Construct a new group of pots.
	 * 
	 */
	public GamePots() {
		pot = new GamePot();
		pots = new ArrayList<GamePot>();
		isClosed = false;
	}

	/**
	 * Collect the given amount of chips from each player in the given list and
	 * transfer that amount to a new side pot, with the old pot.
	 * 
	 * If the amount is zero and the pot is empty, no new side pot is created.
	 * 
	 * @param amount
	 * @param players
	 * @throws IllegalValueException
	 */
	public void collectAmountFromPlayersToSidePot(int amount,
			List<GamePlayer> players) throws IllegalValueException {
		if (isClosed()) {
			return;
		}
		GamePots.logger.info("collect " + amount);
		if ((amount > 0) || (pot.getChips().getValue() > 0)) {
			GamePot sidePot = new GamePot();
			pot.transferAllChipsTo(sidePot);
			for (GamePlayer player : players) {
				try {
					player.getBetChips().transferAmountTo(amount,
							sidePot.getChips());
				} catch (IllegalArgumentException e) {
					GamePots.logger.error(e.getLocalizedMessage(), e);
				}
			}
			pots.add(sidePot);
		}
	}

	public GamePot getMainPot() {
		if (isClosed()) {
			return null;
		}
		return pot;
	}

	public List<GamePot> getSidePots() {
		if (isClosed) {
			return null;
		}
		return Collections.unmodifiableList(pots);
	}

	public GamePot getNewestSidePot() {
		if (isClosed || pots.isEmpty()) {
			return null;
		}
		return pots.get(pots.size() - 1);
	}

	public int getNbSidePots() {
		if (isClosed()) {
			return 0;
		}
		return pots.size();
	}

	/**
	 * Collect all chips from the bet chips pile of all players in the given
	 * list to the main pot.
	 * 
	 * @param players
	 *            The list of players from who to collect the bet chips from.
	 */
	public void collectChipsToPot(List<GamePlayer> players) {
		if (isClosed()) {
			return;
		}
		for (GamePlayer player : players) {
			player.getBetChips().transferAllChipsTo(pot.getChips());
		}
	}

	/**
	 * Collect all chips from the bet chips pile of all players in the given
	 * list to the main pot.
	 * 
	 * @param players
	 *            The list of players from who to collect the bet chips from.
	 */
	public void collectChipsToPotFromChips(List<Chips> chips) {
		if (isClosed()) {
			return;
		}
		for (Chips c : chips) {
			c.transferAllChipsTo(pot.getChips());
		}
	}

	/**
	 * Add the given player to the list of players who have to show their cards.
	 * 
	 * @param player
	 *            The player who will have to show his cards at the end.
	 */
	public void addShowdownPlayer(GamePlayer player) {
		if (isClosed()) {
			return;
		}
		for (GamePot pot : pots) {
			pot.addShowdownPlayer(player);
		}
		nbShowdownPlayers++;
	}

	private int nbShowdownPlayers;

	public int getNbShowdownPlayers() {
		return nbShowdownPlayers;
	}

	/**
	 * Close the pots. It is impossible to a
	 * 
	 * @param showdownPlayers
	 */
	public void close(List<GamePlayer> showdownPlayers) {
		if (isClosed()) {
			return;
		}
		if (pot.getValue() > 0) {
			pots.add(pot);
		}
		for (GamePlayer player : showdownPlayers) {
			addShowdownPlayer(player);
		}
		isClosed = true;
	}

	public boolean isClosed() {
		return isClosed;
	}

	/**
	 * Return a list of all pots. If the pot is closed, also the current pot is
	 * included in the list of pots. Otherwise, only the side pots are returned.
	 * 
	 * @return
	 */
	public List<GamePot> getPots() {
		if (!isClosed()) {
			return null;
		}
		return Collections.unmodifiableList(pots);
	}

	/**
	 * Returns the total value of all pots.
	 */
	public int getTotalValue() {
		int value = 0;
		if (isClosed()) {
			for (GamePot pot : getPots()) {
				value += pot.getValue();
			}
		} else {
			for (GamePot pot : getSidePots()) {
				value += pot.getValue();
			}
			value += getMainPot().getValue();
		}
		return value;
	}

	public String toString() {
		String toReturn = "Pot is ";
		if (isClosed()) {
			toReturn += "closed.\n";
			List<GamePot> pots = getPots();
			for (int i = 0; i < pots.size(); i++) {
				GamePot pot = pots.get(i);
				toReturn += "Pot " + i + ": " + pot + "\n";
			}
		} else {
			toReturn += "open.\n";
			toReturn += "Main pot: " + getMainPot() + "\n";
			List<GamePot> sidePots = getSidePots();
			for (int i = 0; i < sidePots.size(); i++) {
				GamePot pot = sidePots.get(i);
				toReturn += "Side Pot " + i + ": " + pot + "\n";
			}
		}
		return toReturn;
	}
}
