package org.cspoker.server.game.odds;

import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandType;
import org.cspoker.server.game.gameControl.GameControl;
import org.cspoker.server.game.gameControl.rounds.BettingRound;
import org.cspoker.server.game.gameControl.rounds.FlopRound;
import org.cspoker.server.game.gameControl.rounds.PreFlopRound;
import org.cspoker.server.game.gameControl.rounds.TurnRound;
import org.cspoker.server.game.player.Player;


/**
 * Class for calculating and estimating the odds involved for a certain
 * hand in a game
 * @author Cedric
 *
 */
public class OddsCalculator {

	/**********************************************************
	 * Constructor
	 **********************************************************/
	public OddsCalculator(GameControl gameControl){
		if(gameControl==null)
			throw new IllegalArgumentException();
		this.gameControl=gameControl;
	}
	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The game for which the odds are calculated
	 */
	private final GameControl gameControl;
	/**********************************************************
	 * Pot Odds
	 **********************************************************/
	/**
	 * Returns the current pot odds for the given player; being the amount of chips to continue competing
	 * in the current game and the amount of chips you get if you win the game
	 */
	public double getPotOdds(Player player){
		int amountToBet=((BettingRound)gameControl.getRound()).getBet()-player.getBettedChips().getValue();
		int amountInPot=gameControl.getRound().getCurrentPotValue();
		return amountToBet/amountInPot;
	}
	/**********************************************************
	 * Hand Odds
	 **********************************************************/
	/**
	 * Returns the odds of making the goal hand or a hand with an equal quality out of the given current hand
	 * @param hand
	 * 			the given hand
	 * @param goal
	 * 			the given goal hand
	 * @throws	IllegalArgumentException
	 * 			if the given hand or goal hand aren't effective
	 * 			| hand==null || goal==null
	 */
	public double getHandOdds(Hand hand,Hand goal){
		if((hand==null) || (goal==null))
			throw new IllegalArgumentException();
		//double oddsForHandOfLow
		return 0;
	}
	/**
	 * Returns the odds of turning the given hand into the given hand type
	 * @param hand
	 * 			the given hand
	 * @param type
	 * 			the given type
	 */
	public double getHandOdds(Hand hand,HandType type){
		if(gameControl.getRound() instanceof PreFlopRound)
			return getPreFlopHandOdds(hand,type);
		if(gameControl.getRound() instanceof FlopRound)
			return getFlopHandOdds(hand,type);
		if(gameControl.getRound() instanceof TurnRound)
			return getTurnHandOdds(hand,type);
		else
			//calculating odds is useless in the waiting or final round
			return 0;
	}
	/**
	 * Returns the odds of turning the given hand into the given type from the flop to the river
	 * @param hand
	 * 			the given hand
	 * @param type
	 * 			the given hand
	 * @throws	IllegalArgumentException
	 * 			if the given hand or handtype aren't effective
	 * 			| hand==null || type==null
	 * @return	The result is the number of true outs that turn the given hand into the given type
	 * 			divided by the total amount of possible cards that remain
	 * 			| result == getNBOuts(hand,type)/getNBPossibleCardsRemaining()
	 */
	public double getPreFlopHandOdds(Hand hand,HandType type){
		if((hand==null) || (type==null))
			throw new IllegalArgumentException();
		double nBOuts=getNBOuts(hand,type);
		//in the pre-flop there are 52 - 2 pocketcards = 50 unknown cards
		return 1-(50-nBOuts)/50;
	}
	/**
	 * Returns the odds of turning the given hand into the given type from the flop to the river
	 * @param hand
	 * 			the given hand
	 * @param type
	 * 			the given hand
	 * @throws	IllegalArgumentException
	 * 			if the given hand or handtype aren't effective
	 * 			| hand==null || type==null
	 * @return	The result is the number of true outs that turn the given hand into the given type
	 * 			divided by the total amount of possible cards that remain
	 * 			| result == getNBOuts(hand,type)/getNBPossibleCardsRemaining()
	 */
	public double getFlopHandOdds(Hand hand,HandType type){
		if((hand==null) || (type==null))
			throw new IllegalArgumentException();
		double nBOuts=getNBOuts(hand,type);
		//in the flop there are 52 - 2 pocketcards - 3 common cards= 47 unknown cards
		//in the turn there are 52 - 2 pocketcards - 4 common cards= 46 unknown cards
		return 1-((47-nBOuts)/47)*((46-nBOuts)/46);
	}
	/**
	 * Returns the odds of turning the given hand into the given type from the turn to the river
	 * @param hand
	 * 			the given hand
	 * @param type
	 * 			the given hand
	 * @throws	IllegalArgumentException
	 * 			if the given hand or handtype aren't effective
	 * 			| hand==null || type==null
	 * @return	The result is the number of true outs that turn the given hand into the given type
	 * 			divided by the total amount of possible cards that remain
	 * 			| result == getNBOuts(hand,type)/getNBPossibleCardsRemaining()
	 */
	public double getTurnHandOdds(Hand hand,HandType type){
		if((hand==null) || (type==null))
			throw new IllegalArgumentException();
		double nBOuts=getNBOuts(hand,type);
		//in the turn there are 52 - 2 pocketcards - 4 common cards= 46 unknown cards
		return 1-((46-nBOuts)/46);
	}
	/**
	 * Returns the number of true outs that can turn the given hand into a hand of the given handtype
	 * @param hand
	 * 			the given hand
	 * @param type
	 * 			the given type
	 */
	public double getNBOuts(Hand hand,HandType type){
		return type.getNBOutsCalculator().getNBOuts(hand,gameControl.getGame().getCommunityCards());
	}
}
