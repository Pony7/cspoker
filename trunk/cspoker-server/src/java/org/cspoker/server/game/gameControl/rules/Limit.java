package org.cspoker.server.game.gameControl.rules;

import org.cspoker.server.game.gameControl.rounds.Round;

/**
 * Class for the Limit betting game.rounds.rules used in Texas Hold'em.
 * @author Cedric
 */
public class Limit extends BettingRules{

	/**********************************************************
	 * Constructors
	 **********************************************************/
    public Limit(){
    	this(10);
    }
    public Limit(int smallBet){
    	super();
		if(!canHaveAsSmallBet(smallBet))
			throw new IllegalArgumentException();
		this.smallBet=smallBet;
		bigBet=getSmallBet()*2;
    }
    /**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The value of a small bet, used in the first three rounds
	 */
	private final int smallBet;
	/**
	 * The value of a big bet, used in the fourth and final round
	 */
	private int bigBet=getSmallBet()*2;
	/**
	 * The maximum number of raises that can be made during any round
	 */
	public static final int maxNBRaises=3;

	
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
	/**********************************************************
	 * Raise
	 **********************************************************/
	@Override
	public boolean isValidRaise(int amount, Round round) {
		if(getNBRaises()>=maxNBRaises){
			setLastRaiseErrorMessage("ERROR : the maximum number of raises in this round has been reached");
			return false;
		}
		if(round.isLowBettingRound() && (amount%getSmallBet()!=0)){
			setLastRaiseErrorMessage("ERROR : the bet amount must be n times the small bet of this round being "+getSmallBet());
			return false;
		}
		if(round.isHighBettingRound() && (amount%getBigBet()!=0)){
			setLastRaiseErrorMessage("ERROR : the bet amount must be n times the big bet of this round being "+getBigBet());
			return false;
		}
		return super.isValidRaise(amount, round);
	}
	/**
	 * Increases the number of raises in this round by one
	 */
	@Override
	public void incrementNBRaises() {
		if(getNBRaises()>=maxNBRaises)
			throw new IllegalStateException();
		super.incrementNBRaises();
	}
	/**********************************************************
	 * Betting
	 **********************************************************/
	@Override
	public boolean isValidBet(int amount, Round round) {
		if(round.isLowBettingRound() && (amount%getSmallBet()!=0)){
			setLastRaiseErrorMessage("ERROR : the bet amount must be n times the small bet of this round being "+getSmallBet());
			return false;
		}
		if(round.isHighBettingRound() && (amount%getBigBet()!=0)){
			setLastRaiseErrorMessage("ERROR : the bet amount must be n times the big bet of this round being "+getBigBet());
			return false;
		}
		return super.isValidBet(amount, round);
	}
	@Override
	public String toString() {
		return "Hold'em Limit";
	}
	
}
