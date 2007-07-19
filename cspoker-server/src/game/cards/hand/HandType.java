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
package game.cards.hand;

import game.cards.hand.compareHands.CompareDoublePairHands;
import game.cards.hand.compareHands.CompareFlushHands;
import game.cards.hand.compareHands.CompareFourOfAKindHands;
import game.cards.hand.compareHands.CompareFullHouseHands;
import game.cards.hand.compareHands.CompareHands;
import game.cards.hand.compareHands.CompareHighCardHands;
import game.cards.hand.compareHands.ComparePairHands;
import game.cards.hand.compareHands.CompareStraightFlushHands;
import game.cards.hand.compareHands.CompareStraightHands;
import game.cards.hand.compareHands.CompareThreeOfAKindHands;


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
		public int getNumberOfDeterminingCards(){
			return 0;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareHands(){
				@Override
				public int compareHands(Hand h1, Hand h2) {
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
		public int getNumberOfDeterminingCards(){
			return 1;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareHighCardHands();
		}
	},
	PAIR{
		public String toString(){
			return "pair";
		}
		public int getRanking(){
			return 1;
		}
		public int getNumberOfDeterminingCards(){
			return 2;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new ComparePairHands();
		}
	},
	DOUBLE_PAIR{
		public String toString(){
			return "double pair";
		}
		public int getRanking(){
			return 2;
		}
		public int getNumberOfDeterminingCards(){
			return 4;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareDoublePairHands();
		}
	},
	THREE_OF_A_KIND{
		public String toString(){
			return "three of a kind";
		}
		public int getRanking(){
			return 3;
		}
		public int getNumberOfDeterminingCards(){
			return 3;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareThreeOfAKindHands();
		}
	},
	STRAIGHT{
		public String toString(){
			return "straight";
		}
		public int getRanking(){
			return 4;
		}
		public int getNumberOfDeterminingCards(){
			return 5;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareStraightHands();
		}
	},
	FLUSH{
		public String toString(){
			return "flush";
		}
		public int getRanking(){
			return 5;
		}
		public int getNumberOfDeterminingCards(){
			return 5;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareFlushHands();
		}
	},
	FULL_HOUSE{
		public String toString(){
			return "full house";
		}
		public int getRanking(){
			return 6;
		}
		public int getNumberOfDeterminingCards(){
			return 5;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareFullHouseHands();
		}
	},
	FOUR_OF_A_KIND{
		public String toString(){
			return "four of a kind";
		}
		public int getRanking(){
			return 7;
		}
		public int getNumberOfDeterminingCards(){
			return 4;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareFourOfAKindHands();
		}
	},
	STRAIGHT_FLUSH{
		public String toString(){
			return "flush";
		}
		public int getRanking(){
			return 8;
		}
		public int getNumberOfDeterminingCards(){
			return 5;
		}
		public CompareHands getEqualRankHandsComparator(){
			return new CompareStraightFlushHands();
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
	 * Returns the number of cards that determine that hand type
	 * @result the result is a integer between zero and 5
	 * 			| result >=0 && result <=5
	 */
	public abstract int getNumberOfDeterminingCards();
	
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
}
