package rules;

import game.rounds.Round;

public abstract class BettingRules {
	/**********************************************************
	 * Constructors
	 **********************************************************/
	/**
	 * Creates a new set of betting rules for the given round
	 * @param game
	 * 			the given game
	 * @post	The round of this set of betting rules is the given round
	 * 			| new.getRound()==round
	 */
	public BettingRules(Round round){
		if(round==null)
			throw new IllegalArgumentException();
		this.round=round;
	}
	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The game for which these betting rules apply
	 */
	private Round round;
	private String lastErrorRaiseMessage;
	private String lastErrorBetMessage;
	/**********************************************************
	 * Round
	 **********************************************************/
	/**
	 * Returns the game for which these betting rules apply
	 */
	public Round getRound(){
		return round;
	}
	public void setRound(Round round){
		this.round=round;
	}
	/**********************************************************
	 * Raise's
	 **********************************************************/
	/**
	 * Checks whether the given amount is a valid raise
	 * @param amount
	 * 			the given amount
	 */
	public abstract boolean isValidRaise(int amount);
	public abstract String getLastRaiseErrorMessage();
	protected String getErrorRaiseMessage(){
		return lastErrorRaiseMessage;
	}
	/**********************************************************
	 * Bets
	 **********************************************************/
	/**
	 * Checks whether the given amount is a valid bet
	 * @param amount
	 * 			the given amount
	 */
	public abstract boolean isValidBet(int amount);
	public abstract String getLastBetErrorMessage();
	protected String getErrorBetMessage(){
		return lastErrorBetMessage;
	}
}
