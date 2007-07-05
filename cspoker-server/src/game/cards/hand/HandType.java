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

import game.cards.Card;

/**
 * Enumeration containing all the possible types of hands and their ranking
 * and textual representation
 * @author Cedric
 *
 */
public enum HandType {

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
	 */
	public abstract int getNumberOfDeterminingCards();
}
