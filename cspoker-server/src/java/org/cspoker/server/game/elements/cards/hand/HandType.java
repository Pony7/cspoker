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
package org.cspoker.server.game.elements.cards.hand;

import org.cspoker.server.game.elements.cards.hand.compareHands.CompareDoublePairHands;
import org.cspoker.server.game.elements.cards.hand.compareHands.CompareFlushHands;
import org.cspoker.server.game.elements.cards.hand.compareHands.CompareFourOfAKindHands;
import org.cspoker.server.game.elements.cards.hand.compareHands.CompareFullHouseHands;
import org.cspoker.server.game.elements.cards.hand.compareHands.CompareHands;
import org.cspoker.server.game.elements.cards.hand.compareHands.CompareHighCardHands;
import org.cspoker.server.game.elements.cards.hand.compareHands.ComparePairHands;
import org.cspoker.server.game.elements.cards.hand.compareHands.CompareStraightFlushHands;
import org.cspoker.server.game.elements.cards.hand.compareHands.CompareStraightHands;
import org.cspoker.server.game.elements.cards.hand.compareHands.CompareThreeOfAKindHands;
import org.cspoker.server.game.elements.cards.hand.handQuality.DoublePairQualityCalculator;
import org.cspoker.server.game.elements.cards.hand.handQuality.FlushQualityCalculator;
import org.cspoker.server.game.elements.cards.hand.handQuality.FourOfAKindQualityCalculator;
import org.cspoker.server.game.elements.cards.hand.handQuality.FullHouseQualityCalculator;
import org.cspoker.server.game.elements.cards.hand.handQuality.HandQualityCalculator;
import org.cspoker.server.game.elements.cards.hand.handQuality.HighCardQualityCalculator;
import org.cspoker.server.game.elements.cards.hand.handQuality.PairQualityCalculator;
import org.cspoker.server.game.elements.cards.hand.handQuality.StraightFlushQualityCalculator;
import org.cspoker.server.game.elements.cards.hand.handQuality.StraightQualityCalculator;
import org.cspoker.server.game.elements.cards.hand.handQuality.ThreeOfAKindQualtiyCalculator;
import org.cspoker.server.game.odds.NBOutsCalculator;


/**
 * Enumeration containing all the possible types of hands and their ranking
 * and textual representation
 * 
 * @author Cedric & Kenzo(refactoring)
 *
 */
public enum HandType {

	UNKNOWN{
		@Override
		public String toString(){
			return "unknown";
		}
		@Override
		public int getRanking(){
			return -1;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new CompareHands(){
				@Override
				public int compareHands(Hand h1, Hand h2) {
					return 0;
				}
				
			};
		}
		@Override
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
		@Override
		public String toString(){
			return "high card";
		}
		@Override
		public int getRanking(){
			return 0;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new CompareHighCardHands();
		}
		@Override
		public HandQualityCalculator getHandQualityCalculator(){
			return new HighCardQualityCalculator();
		}
	},
	PAIR{
		@Override
		public String toString(){
			return "pair";
		}
		@Override
		public int getRanking(){
			return 1;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new ComparePairHands();
		}
		@Override
		public HandQualityCalculator getHandQualityCalculator(){
			return new PairQualityCalculator();
		}
	},
	DOUBLE_PAIR{
		@Override
		public String toString(){
			return "double pair";
		}
		@Override
		public int getRanking(){
			return 2;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new CompareDoublePairHands();
		}
		@Override
		public HandQualityCalculator getHandQualityCalculator(){
			return new DoublePairQualityCalculator();
		}
	},
	THREE_OF_A_KIND{
		@Override
		public String toString(){
			return "three of a kind";
		}
		@Override
		public int getRanking(){
			return 3;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new CompareThreeOfAKindHands();
		}
		@Override
		public HandQualityCalculator getHandQualityCalculator(){
			return new ThreeOfAKindQualtiyCalculator();
		}
	},
	STRAIGHT{
		@Override
		public String toString(){
			return "straight";
		}
		@Override
		public int getRanking(){
			return 4;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new CompareStraightHands();
		}
		@Override
		public HandQualityCalculator getHandQualityCalculator(){
			return new StraightQualityCalculator();
		}
	},
	FLUSH{
		@Override
		public String toString(){
			return "flush";
		}
		@Override
		public int getRanking(){
			return 5;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new CompareFlushHands();
		}
		@Override
		public HandQualityCalculator getHandQualityCalculator(){
			return new FlushQualityCalculator();
		}
	},
	FULL_HOUSE{
		@Override
		public String toString(){
			return "full house";
		}
		@Override
		public int getRanking(){
			return 6;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new CompareFullHouseHands();
		}
		@Override
		public HandQualityCalculator getHandQualityCalculator(){
			return new FullHouseQualityCalculator();
		}
	},
	FOUR_OF_A_KIND{
		@Override
		public String toString(){
			return "four of a kind";
		}
		@Override
		public int getRanking(){
			return 7;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new CompareFourOfAKindHands();
		}
		@Override
		public HandQualityCalculator getHandQualityCalculator(){
			return new FourOfAKindQualityCalculator();
		}
	},
	STRAIGHT_FLUSH{
		@Override
		public String toString(){
			return "flush";
		}
		@Override
		public int getRanking(){
			return 8;
		}
		@Override
		public CompareHands getEqualRankHandsComparator(){
			return new CompareStraightFlushHands();
		}
		@Override
		public HandQualityCalculator getHandQualityCalculator(){
			return new StraightFlushQualityCalculator();
		}
	};
	/**
	 * Returns the textual representation of this type
	 */
	@Override
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
