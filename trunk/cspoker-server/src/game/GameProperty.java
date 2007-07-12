/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package game;

import game.rules.BettingRules;
import game.rules.NoLimit;

/**
 * 
 * This class should contain all parameters of a game,
 * such as maximum number of players, maximum bet/raise,
 * the amount for small/big blinds,...
 * 
 * @author Kenzo & Cedric
 *
 * @invar	This game property must have valid parameters
 * 			| hasValidParameters()
 */
public class GameProperty {
	/**********************************************************
	 * Variables
	 **********************************************************/
	/**
	 * The minimum value of chips the first player after the dealer button
	 * must place in the pot
	 */
	private final int smallBlind=getSmallBet()/2;
	/**
	 * The minimum value of chips the second player after the dealer button
	 * must place in the pot
	 */
	private final int bigBlind=getSmallBet();
	/**
	 * The value of a small bet, used in the first three rounds
	 */
	private final int smallBet;
	/**
	 * The value of a big bet, used in the fourth and final round
	 */
	private final int bigBet=getSmallBet()*2;
	/**
	 * The maximum number of players in a game
	 */
	private static final int maxNbPlayers=10;
	/**
	 * The betting rules applied to this game
	 */
	private final BettingRules bettingRules;
	/**********************************************************
	 * Constructors
	 **********************************************************/
	/**
	 * Constructs a new game property with smallbet 10 
	 * @effet	
	 * 			| this(10)
	 */
	public GameProperty(){
		this(10);
	}
	/**
	 * Constructs a new game property with the given smallbet
	 * @param	smallBet
	 * 			the given smallBet
	 * @throws	IllegalArgumentException
	 * 			if this game property can't have the given smallBet as smallBet
	 * 			| !canHaveAsSmallBet(smallBet)
	 * @post	This game has valid parameters
	 * 			| new.hasValidParameters()
	 * @post	The smallbet of this game property is the given smallbet
	 * 			| new.getSmallBet()==smallBet
	 * @post	The bigbet of this game property is twice the given smallbet
	 * 			| new.getBigBlind()==2*smallBet
	 * @post	The smallblind of this game property is half the given smallbet
	 * 			| new.getSmallBlind()==smallBet/2
	 * @post	The bigblind of this game property is the given smallbet
	 * 			| new.getBigBlind()==smallBet
	 *
	 */
	public GameProperty(int smallBet){
		this(smallBet,new NoLimit());
	}
	public GameProperty(int smallBet,BettingRules bettingRules){
		if(!canHaveAsSmallBet(smallBet) || (bettingRules==null))
			throw new IllegalArgumentException();
		this.smallBet=smallBet;
		this.bettingRules=bettingRules;
	}
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
	 * Checks whether a game property can have the given smallBet as a smallBet
	 * @param smallBet
	 * 			the given smallBet
	 * @return	True if smallBet is strictly positive and even
	 * 			| result == (smallBet>0)&&(smallBet%2==0)
	 */
	public static boolean canHaveAsSmallBet(int smallBet) {
		return (smallBet>0)&&(smallBet%2==0);
	}
	/**
	 * Returns the betting game.rounds.rules for this round
	 */
	public BettingRules getBettingRules(){
		return bettingRules;
	}
	/**********************************************************
	 * Blinds
	 **********************************************************/
	public int getSmallBlind(){
		return smallBlind;
	}
	
	public int getBigBlind(){
		return bigBlind;
	}
	/**
	 * Checks whether this game property has valid blinds
	 * @return	True if the bigblind equals the smallbet and the smallblind equals half the smallbet
	 * 			and the bigbet equals double the smallbet and a game property can have the smallbet of this game
	 * 			property as it's smallbet
	 * 			| result == (canHaveAsSmallBet(getSmallBet()))&&(getSmallBlind()==getSmallBet()/2)
	 * 			|				&&(getBigBlind()==getSmallBet())&&(getBigBet()==2*getSmallBet())
	 */
	public boolean hasValidParameters(){
		return (canHaveAsSmallBet(getSmallBet()))&&(getSmallBlind()==getSmallBet()/2)
					&&(getBigBlind()==getSmallBet())&&(getBigBet()==2*getSmallBet());
	}
	/**********************************************************
	 * Other methods
	 **********************************************************/
	
	/**
	 * Check if this game is open or closed.
	 * 
	 * In an open game new players can enter
	 * the game and take a place at the table.
	 * 
	 * In a closed game it is impossible
	 * for new players to seat at the table.
	 * 
	 */
	public boolean isClosedGame(){
		return false;
		//TODO: doel v deze methode? -> commentaar :p
	}
	
	public int getMaxNbPlayers(){
		return maxNbPlayers;
	}
}
