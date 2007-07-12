package rules;

import game.rounds.Round;

public class Limit extends BettingRules{

	public Limit(Round round,int smallBet){
		super(round);
		if(!canHaveAsSmallBet(smallBet))
			throw new IllegalArgumentException();
		this.smallBet=smallBet;
	}
    public Limit(Round round) {
		super(round);
		this.smallBet=10;
	}
	/**
	 * The value of a small bet, used in the first three rounds
	 */
	private final int smallBet;
	/**
	 * The value of a big bet, used in the fourth and final round
	 */
	private final int bigBet=getSmallBet()*2;
	
	/**********************************************************
	 * Bets
	 **********************************************************/
	public int getSmallBet(){
		return smallBet;
	}
	public int getBigBet(){
		return bigBet;
	}
	/**
	 * Checks wether a game property can have the given smallBet as a smallBet
	 * @param smallBet
	 * 			the given smallBet
	 * @return	True if smallBet is strictly positive
	 * 			| result == (smallBet>0)
	 */
	public static boolean canHaveAsSmallBet(int smallBet) {
		return (smallBet>0);
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
