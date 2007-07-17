package game.rules;

import game.rounds.Round;

/**
 * Class for the PotLimit betting game.rounds.rules used in Texas Hold'em.
 * @author Cedric
 */
public class PotLimit extends BettingRules {

	/**********************************************************
	 * Constructors
	 **********************************************************/
	public PotLimit(){
	}

	/**********************************************************
	 * Raise
	 **********************************************************/
	@Override
	public boolean isValidRaise(int amount, Round round) {
		if(amount<getLastBetAmount()){
			setLastRaiseErrorMessage("ERROR : the betted amount must be atleast the amount of the previous bet/raise " +
					"in the current round.");
			return false;
		}
		if(amount>round.getCurrentPotValue()){
			setLastRaiseErrorMessage("ERROR : the betted amount mustn't be greater than the total amount of chips in the " +
					"current pot, being "+round.getCurrentPotValue()+" !");
			return false;
		}
		return super.isValidRaise(amount, round);
	}

	/**********************************************************
	 * Betting
	 **********************************************************/
	@Override
	public boolean isValidBet(int amount, Round round) {
		int potValue=round.getCurrentPotValue();
		if(amount>potValue){
			setLastRaiseErrorMessage("ERROR : the betted amount mustn't be greater than the total amount of chips in the" +
					"current pot, being "+potValue+" !");
			return false;
		}
		return super.isValidBet(amount, round);
	}
	@Override
	public String toString() {
		return "Pot Limit Poker ";
	}
}
