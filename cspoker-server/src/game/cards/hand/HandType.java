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
	},
	PAIR{
		public String toString(){
			return "pair";
		}
		public int getRanking(){
			return 1;
		}
	},
	DOUBLE_PAIR{
		public String toString(){
			return "double pair";
		}
		public int getRanking(){
			return 0;
		}
	},
	THREE_OF_A_KIND{
		public String toString(){
			return "three of a kind";
		}
		public int getRanking(){
			return 0;
		}
	},
	FULL_HOUSE{
		public String toString(){
			return "full house";
		}
		public int getRanking(){
			return 0;
		}
	},
	FOUR_OF_A_KIND{
		public String toString(){
			return "four a kind";
		}
		public int getRanking(){
			return 0;
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
}
