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

package game.cards;

/**
 * An enumeration to represent the different ranks a card can have.
 * 
 * @author Kenzo
 *
 */
public enum Rank {
	DEUCE{
		public int getValue(){
			return 2;
		}
	}, 
	
	THREE{
		public int getValue(){
			return 3;
		} 
	},
	
	FOUR{
		public int getValue(){
			return 4;
		} 
	}, 
	
	FIVE{
		public int getValue(){
			return 5;
		} 	
	}, 
	
	SIX{
		public int getValue(){
			return 6;
		} 
	},

    SEVEN{
		public int getValue(){
			return 7;
		} 
	}, 
    
    EIGHT{
		public int getValue(){
			return 8;
		} 
	},
    
    NINE{
		public int getValue(){
			return 9;
		} 
	}, 
    
    TEN{
		public int getValue(){
			return 10;
		} 
	}, 
    
    JACK{
		public int getValue(){
			return 11;
		}
		public String toString(){
			return "jack";
		}
	}, 
    
    QUEEN{
		public int getValue(){
			return 12;
		}
		public String toString(){
			return "queen";
		}
	}, 
    
    KING{
		public int getValue(){
			return 13;
		}
		public String toString(){
			return "king";
		}
	},
    
    ACE{
		public int getValue(){
			return 14;
		}
		public String toString(){
			return "ace";
		}
	};
	
	/**
	 * Returns the numeral value of this rank
	 * @return the numeral value of this rank
	 */
	public abstract int getValue();
	/**
	 * Returns a textual representation of this rank
	 */
	public String toString(){
		return Integer.toString(getValue());
	}
}
