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

package org.cspoker.common.elements;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


/**
 * 
 * This class should contain all parameters of a game, such as maximum number of
 * players, maximum bet/raise, the amount for small/big blinds,...
 * 
 *  
 * @invar This game property must have valid parameters | hasValidParameters()
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class GameProperty implements Serializable {

	private static final long serialVersionUID = -7488999804059098465L;

	/***************************************************************************
	 * Variables
	 **************************************************************************/
	/**
	 * The minimum value of chips the first player after the dealer button must
	 * place in the pot
	 */
	@XmlAttribute
	private int smallBlind;

	/**
	 * The minimum value of chips the second player after the dealer button must
	 * place in the pot
	 */
	@XmlAttribute
	private int bigBlind;

	/**
	 * The value of a small bet, used in the first three rounds
	 */
	@XmlAttribute
	private int smallBet;

	/**
	 * The value of a big bet, used in the fourth and final round
	 */
	@XmlAttribute
	private int bigBet;

	/**
	 * The maximum number of players in a game
	 */
	private static final int maxNbPlayers = 8;


	/***************************************************************************
	 * Constructors
	 **************************************************************************/
	/**
	 * Constructs a new game property with smallbet 10
	 * 
	 * @effet | this(10)
	 */
	public GameProperty() {
		this(10);
	}

	/**
	 * Constructs a new game property with the given smallbet
	 * 
	 * @param smallBet
	 *            the given smallBet
	 * @throws IllegalArgumentException
	 *             if this game property can't have the given smallBet as
	 *             smallBet | !canHaveAsSmallBet(smallBet)
	 * @post This game has valid parameters | new.hasValidParameters()
	 * @post The smallbet of this game property is the given smallbet |
	 *       new.getSmallBet()==smallBet
	 * @post The bigbet of this game property is twice the given smallbet |
	 *       new.getBigBlind()==2*smallBet
	 * @post The smallblind of this game property is half the given smallbet |
	 *       new.getSmallBlind()==smallBet/2
	 * @post The bigblind of this game property is the given smallbet |
	 *       new.getBigBlind()==smallBet
	 * 
	 */
	public GameProperty(int smallBet) {
		if (!canHaveAsSmallBet(smallBet)) {
			throw new IllegalArgumentException();
		}
		this.smallBet = smallBet;
		smallBlind = getSmallBet() / 2;
		bigBlind = getSmallBet();
		bigBet = getSmallBet() * 2;
	}

	/***************************************************************************
	 * Bets
	 **************************************************************************/
	public int getSmallBet() {
		return smallBet;
	}

	public int getBigBet() {
		return bigBet;
	}

	/**
	 * Checks whether a game property can have the given smallBet as a smallBet
	 * 
	 * @param smallBet
	 *            the given smallBet
	 * @return True if smallBet is strictly positive and even | result ==
	 *         (smallBet>0)&&(smallBet%2==0)
	 */
	public static boolean canHaveAsSmallBet(int smallBet) {
		return (smallBet > 0) && (smallBet % 2 == 0);
	}


	/***************************************************************************
	 * Blinds
	 **************************************************************************/
	public int getSmallBlind() {
		return smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	/**
	 * Checks whether this game property has valid blinds
	 * 
	 * @return True if the bigblind equals the smallbet and the smallblind
	 *         equals half the smallbet and the bigbet equals double the
	 *         smallbet and a game property can have the smallbet of this game
	 *         property as it's smallbet | result ==
	 *         (canHaveAsSmallBet(getSmallBet()))&&(getSmallBlind()==getSmallBet()/2) |
	 *         &&(getBigBlind()==getSmallBet())&&(getBigBet()==2*getSmallBet())
	 */
	public boolean hasValidParameters() {
		return (canHaveAsSmallBet(getSmallBet()))
				&& (getSmallBlind() == getSmallBet() / 2)
				&& (getBigBlind() == getSmallBet())
				&& (getBigBet() == 2 * getSmallBet());
	}
	
	/***************************************************************************
	 * Max Nb Players
	 **************************************************************************/

	public int getMaxNbPlayers() {
		return maxNbPlayers;
	}
	
	/**
	 * 
	 * There should be at least 2 players to play a poker game.
	 * 
	 * @param 	nbPlayers
	 * 			The number of players to check.
	 * @return	True if the given number of players is at least 2, false otherwise.
	 */
	public static boolean canHaveAsMaxNbPlayers(int nbPlayers){
		return nbPlayers>=2;
	}
	
	/***************************************************************************
	 * Other methods
	 **************************************************************************/

	/**
	 * Check if this game is open or closed.
	 * 
	 * In an open game new players can enter the game and take a place at the
	 * table.
	 * 
	 * In a closed game it is impossible for new players to seat at the table.
	 * 
	 */
	public boolean isClosedGame() {
		return false;
	}

}
