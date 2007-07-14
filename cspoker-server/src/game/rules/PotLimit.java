package game.rules;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
import game.rounds.Round;

/**
 * Class for the PotLimit betting game.rounds.rules used in Texas Hold'em.
 * @author Cedric
 */
public class PotLimit extends BettingRules {

	/**********************************************************
	 * Constructors
	 **********************************************************/
	public PotLimit(Round round) {
		super(round);
	}
	public PotLimit(){
		super();
	}

	/**********************************************************
	 * Raise
	 **********************************************************/
	@Override
	public boolean isValidRaise(int amount) {
		if(amount<getLastBetAmount()){
			setLastRaiseErrorMessage("ERROR : the betted amount must be atleast the amount of the previous bet/raise" +
					"in the current round.");
			return false;
		}
		int potValue=getRound().getGame().getPots().getTotalValue();
		if(amount>potValue){
			setLastRaiseErrorMessage("ERROR : the betted amount mustn't be greater than the total amount of chips in the" +
					"current pot, being "+potValue+" !");
			return false;
		}
		return super.isValidRaise(amount);
	}

	/**********************************************************
	 * Betting
	 **********************************************************/
	@Override
	public boolean isValidBet(int amount) {
		int potValue=getRound().getGame().getPots().getTotalValue();
		if(amount>potValue){
			setLastRaiseErrorMessage("ERROR : the betted amount mustn't be greater than the total amount of chips in the" +
					"current pot, being "+potValue+" !");
			return false;
		}
		return super.isValidBet(amount);
	}
}
