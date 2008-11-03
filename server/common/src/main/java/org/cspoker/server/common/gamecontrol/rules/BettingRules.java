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
package org.cspoker.server.common.gamecontrol.rules;

import org.cspoker.server.common.gamecontrol.rounds.BettingRound;

/**
 * Superclass for all the possible betting game.rounds.rules used in Texas
 * Hold'em (Limit, NoLimit or PotLimit variety) Provides information about the
 * legality of bets and raises and their cause of failure if any.
 * 
 * @author Cedric
 * 
 */
public abstract class BettingRules {
	/***************************************************************************
	 * Constructors
	 **************************************************************************/
	public BettingRules() {
	}

	/***************************************************************************
	 * Variables
	 **************************************************************************/
	/**
	 * The error message for the last raise-attempt that failed
	 */
	private String lastErrorRaiseMessage;

	/**
	 * The error message for the last bet-attempt that failed
	 */
	private String lastErrorBetMessage;

	/**
	 * Whether a bet has been placed
	 */
	private boolean betPlaced = false;

	/**
	 * The number of raises made during the current round
	 */
	private int numberofRaises = 0;

	/**
	 * The last amount that was bet/raised during the current round
	 */
	private int lastBetAmount = 0;

	/***************************************************************************
	 * Raise's
	 **************************************************************************/
	/**
	 * Checks whether the given amount is a valid raise
	 * 
	 * @param amount
	 *            the given amount
	 */
	public boolean isValidRaise(int amount, BettingRound round) {
		if (!betPlaced()) {
			setLastRaiseErrorMessage("ERROR : cannot raise if a bet hasn't been placed yet in this round; "
					+ "did you mean bet??");
			return false;
		}
		if (amount <= 0) {
			setLastRaiseErrorMessage("ERROR : amount must be greater than zero!!");
			return false;
		}
		return true;
	}

	/**
	 * Returns the complete error message for the last raise-attempt that failed
	 */
	public String getLastRaiseErrorMessage() {
		return lastErrorRaiseMessage;
	}

	public void setLastRaiseErrorMessage(String error) {
		lastErrorRaiseMessage = error;
	}

	public int getNBRaises() {
		return numberofRaises;
	}

	/**
	 * Increases the number of raises in this round by one
	 */
	public void incrementNBRaises() {
		numberofRaises++;
	}

	public void clearNBRaises() {
		numberofRaises = 0;
	}

	/***************************************************************************
	 * Bets
	 **************************************************************************/
	/**
	 * Checks whether the given amount is a valid bet
	 * 
	 * @param amount
	 *            the given amount
	 */
	public boolean isValidBet(int amount, BettingRound round) {
		if (getNBRaises() != 0) {
			setLastBetErrorMessage("ERROR : you can't bet if someone already raised during this round!!"
					+ "; did you mean raise??");
			return false;
		}
		if (amount <= 0) {
			setLastBetErrorMessage("ERROR : amount must be greater than zero!!");
			return false;
		}
		if (betPlaced()) {
			setLastBetErrorMessage("ERROR : a bet has already been placed; did you mean raise??");
			return false;
		}
		return true;
	}

	/**
	 * Returns the complete error message for the last bet-attempt that failed
	 */
	public String getLastBetErrorMessage() {
		return lastErrorBetMessage;
	}

	public void setLastBetErrorMessage(String error) {
		lastErrorBetMessage = error;
	}

	/**
	 * Checks whether a bet has been placed
	 */
	public boolean betPlaced() {
		return betPlaced;
	}

	public void setBetPlaced(boolean flag) {
		betPlaced = flag;
	}

	public int getLastBetAmount() {
		return lastBetAmount;
	}

	public void setLastBetAmount(int amount) {
		lastBetAmount = amount;
	}

	@Override
	public abstract String toString();
}
