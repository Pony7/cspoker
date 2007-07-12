package rules;

import game.rounds.Round;

public abstract class PotLimit extends BettingRules {

	public PotLimit(Round round) {
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

	

}
