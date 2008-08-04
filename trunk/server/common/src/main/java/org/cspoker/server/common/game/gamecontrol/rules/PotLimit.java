package org.cspoker.server.common.game.gamecontrol.rules;

import org.cspoker.server.common.game.gamecontrol.rounds.BettingRound;

/**
 * Class for the PotLimit betting game.rounds.rules used in Texas Hold'em.
 * 
 * @author Cedric
 */
public class PotLimit extends BettingRules {

	/***************************************************************************
	 * Constructors
	 **************************************************************************/
	public PotLimit() {
	}

	/***************************************************************************
	 * Raise
	 **************************************************************************/
	
	//TODO The pot limit is the value after a call!

	public boolean isValidRaise(int amount, BettingRound round) {
		if (amount < getLastBetAmount()) {
			setLastRaiseErrorMessage("ERROR : the bet amount must be at least the amount of the previous bet/raise "
					+ "in the current round.");
			return false;
		}
		
		if (amount > round.getCurrentPotValue()) {
			setLastRaiseErrorMessage("ERROR : the bet amount mustn't be greater than the total amount of chips in the "
					+ "current pot, being " + round.getCurrentPotValue() + " !");
			return false;
		}
		return super.isValidRaise(amount, round);
	}

	/***************************************************************************
	 * Betting
	 **************************************************************************/

	public boolean isValidBet(int amount, BettingRound round) {
		int potValue = round.getCurrentPotValue();
		if (amount > potValue) {
			setLastRaiseErrorMessage("ERROR : the bet amount mustn't be greater than the total amount of chips in the"
					+ "current pot, being " + potValue + " !");
			return false;
		}
		return super.isValidBet(amount, round);
	}

	public String toString() {
		return "Hold'em Pot Limit";
	}
}
