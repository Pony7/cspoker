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

package game.cards.deck.randomGenerator;

import game.cards.Card;
import game.cards.CardImpl;
import game.cards.cardElements.Rank;
import game.cards.cardElements.Suit;
import game.cards.hand.Hand;

import java.util.Random;

/**
 * A class of random generators.
 * 
 * @author Kenzo & Cedric
 *
 */
public class RandomGenerator {
	

	protected volatile Random random;
	
	/**
	 * Construct a new default random generator.
	 *
	 */
	public RandomGenerator(){
		setNewRandom();
	}
	
	/**
	 * Returns a random card.
	 * 
	 * @return A random card.
	 */
	public static Card getRandomCard() {
		Random generator = new Random();
		
		int suitIndex=generator.nextInt(4);
		int rankIndex=generator.nextInt(13);

		return new CardImpl(Suit.values()[suitIndex], Rank.values()[rankIndex]);
	}
	
	/**
	 * Returns a random hand.
	 * 
	 * @param 	nBCards
	 * 			The number of cards in the hand.
	 * @return 	A random hand.
	 */
	public static Hand getRandomHand(int nBCards) {
		Hand result=new Hand();
		while(result.getNBCards()<nBCards){
			Card randomCard=getRandomCard();
			if(!result.contains(randomCard))
				result.addCard(randomCard);
		}
		return result;
	}
	
	/**
	 * Returns a random-object.
	 * 
	 * The default implementation uses the current time as seed.
	 * 
	 * @return A random-object.
	 */
	public Random getRandom() {
		return random;
	}
	
	protected void setNewRandom(){
		random = new Random(getRandomSeed());
	}
	
	/**
	 * The seed used for the random.
	 * 
	 * The default implementation uses the current time as seed.
	 * 
	 * @return
	 */
	protected long getRandomSeed(){
		return System.currentTimeMillis();
	}
}
