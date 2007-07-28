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
package game.elements.cards.hand;

import game.elements.cards.hand.compareHands.CompareDoublePairHands;
import game.elements.cards.hand.compareHands.CompareFlushHands;
import game.elements.cards.hand.compareHands.CompareFourOfAKindHands;
import game.elements.cards.hand.compareHands.CompareFullHouseHands;
import game.elements.cards.hand.compareHands.CompareHands;
import game.elements.cards.hand.compareHands.CompareHighCardHands;
import game.elements.cards.hand.compareHands.ComparePairHands;
import game.elements.cards.hand.compareHands.CompareStraightFlushHands;
import game.elements.cards.hand.compareHands.CompareStraightHands;
import game.elements.cards.hand.compareHands.CompareThreeOfAKindHands;
import game.elements.cards.hand.handQuality.DoublePairQualityCalculator;
import game.elements.cards.hand.handQuality.FlushQualityCalculator;
import game.elements.cards.hand.handQuality.FourOfAKindQualityCalculator;
import game.elements.cards.hand.handQuality.FullHouseQualityCalculator;
import game.elements.cards.hand.handQuality.HandQualityCalculator;
import game.elements.cards.hand.handQuality.HighCardQualityCalculator;
import game.elements.cards.hand.handQuality.PairQualityCalculator;
import game.elements.cards.hand.handQuality.StraightFlushQualityCalculator;
import game.elements.cards.hand.handQuality.StraightQualityCalculator;
import game.elements.cards.hand.handQuality.ThreeOfAKindQualtiyCalculator;
import game.odds.NBOutsCalculator;


/**
 * Enumeration containing all the possible types of hands and their ranking
 * and textual representation
 * 
 * @author Cedric & Kenzo(refactoring)
 *
 */
public enum HandType {

	UNKNOWN{
		public String toString(){
			return "unknown";
		}
		public int getRanking(){
			return -1;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareHands(){
				@Override
				public int compareHands(Hand h1, Hand h2) {
					return 0;
				}
				
			};
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new HandQualityCalculator(){
				@Override
				public double calculateQualityWithinType(Hand hand) {
					return 0;
				}
			};
		}
	},
	HIGH_CARD{
		public String toString(){
			return "high card";
		}
		public int getRanking(){
			return 0;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareHighCardHands();
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new HighCardQualityCalculator();
		}
	},
	PAIR{
		public String toString(){
			return "pair";
		}
		public int getRanking(){
			return 1;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new ComparePairHands();
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new PairQualityCalculator();
		}
	},
	DOUBLE_PAIR{
		public String toString(){
			return "double pair";
		}
		public int getRanking(){
			return 2;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareDoublePairHands();
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new DoublePairQualityCalculator();
		}
	},
	THREE_OF_A_KIND{
		public String toString(){
			return "three of a kind";
		}
		public int getRanking(){
			return 3;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareThreeOfAKindHands();
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new ThreeOfAKindQualtiyCalculator();
		}
	},
	STRAIGHT{
		public String toString(){
			return "straight";
		}
		public int getRanking(){
			return 4;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareStraightHands();
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new StraightQualityCalculator();
		}
	},
	FLUSH{
		public String toString(){
			return "flush";
		}
		public int getRanking(){
			return 5;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareFlushHands();
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new FlushQualityCalculator();
		}
	},
	FULL_HOUSE{
		public String toString(){
			return "full house";
		}
		public int getRanking(){
			return 6;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareFullHouseHands();
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new FullHouseQualityCalculator();
		}
	},
	FOUR_OF_A_KIND{
		public String toString(){
			return "four of a kind";
		}
		public int getRanking(){
			return 7;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareFourOfAKindHands();
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new FourOfAKindQualityCalculator();
		}
	},
	STRAIGHT_FLUSH{
		public String toString(){
			return "flush";
		}
		public int getRanking(){
			return 8;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareStraightFlushHands();
		}
		public HandQualityCalculator getHandQualityCalculator(){
			return new StraightFlushQualityCalculator();
		}
	};
	/**
	 * Returns the textual representation of this type
	 */
	public abstract String toString();
	/**
	 * Returns the raking of this type
	 * @result the result is a positive integer
	 * 			| result >=0
	 */
	public abstract int getRanking();
	/**
	 * Compares 2 hands from the same hand type.
	 * 
	 * @param 	h1
	 *          The first hand
	 * @param 	h2
	 *          The second hand
	 *          
	 * @throws  IllegalArgumentException [can]
	 * 			The given hands are not from the same hand type.
	 *          
	 * @return 1 = first hand is best, -1 = second hand is best, 0 = tie
	 */
	public abstract CompareHands getEqualRankHandsComparator();
	
	public abstract HandQualityCalculator getHandQualityCalculator();
	public NBOutsCalculator getNBOutsCalculator() {
		// TODO Auto-generated method stub
		return null;
	}
}
