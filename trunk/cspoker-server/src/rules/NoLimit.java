package rules;

import game.rounds.Round;
/**
 * Class for the NoLimit betting rules used in Texas Hold'em.
 * @author Cedric
 */
public class NoLimit extends BettingRules {	
	/**********************************************************
	 * Constructors
	 **********************************************************/
	public NoLimit(Round round) {
		super(round);
	}

	/**********************************************************
	 * Raise
	 **********************************************************/
	@Override
	public boolean isValidRaise(int amount) {
		if(amount<getLastBetAmount()){
			setLastRaiseErrorMessage("ERROR : the betted amount must be atleast the amount of the previous bet/raise" +
					"in the current round");
			return false;
		}
		return super.isValidRaise(amount);
	}
}
