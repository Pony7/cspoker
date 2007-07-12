package rules;

import game.rounds.Round;

public class NoLimit extends BettingRules {

	public NoLimit(Round round) {
		super(round);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValidRaise(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValidBet(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLastRaiseErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLastBetErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
