package org.cspoker.server.common.game.gamecontrol.rules;

import org.cspoker.server.common.game.gamecontrol.rounds.BettingRound;

/**
 * Class for the NoLimit betting game.rounds.rules used in Texas Hold'em.
 * 
 * @author Cedric
 */
public class NoLimit extends BettingRules {
	/***************************************************************************
	 * Constructors
	 **************************************************************************/

	public NoLimit() {
	}

	/***************************************************************************
	 * Raise
	 **************************************************************************/

	public boolean isValidRaise(int amount, BettingRound round) {
		if (amount < getLastBetAmount()) {
			setLastRaiseErrorMessage("ERROR : the bet amount must be at least the amount of the previous bet/raise"
					+ " in the current round");
			return false;
		}
		return super.isValidRaise(amount, round);
	}

	public String toString() {
		return "Hold'em No Limit";
	}
}
