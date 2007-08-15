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

package org.cspoker.server.game.elements.cards.cardElements;

/**
 * An enumeration to represent the different ranks a card can have.
 * 
 * @author Kenzo
 *
 */
public enum Rank {
	DEUCE{
		@Override
		public int getValue(){
			return 2;
		}
	}, 
	
	THREE{
		@Override
		public int getValue(){
			return 3;
		} 
	},
	
	FOUR{
		@Override
		public int getValue(){
			return 4;
		} 
	}, 
	
	FIVE{
		@Override
		public int getValue(){
			return 5;
		} 	
	}, 
	
	SIX{
		@Override
		public int getValue(){
			return 6;
		} 
	},

    SEVEN{
		@Override
		public int getValue(){
			return 7;
		} 
	}, 
    
    EIGHT{
		@Override
		public int getValue(){
			return 8;
		} 
	},
    
    NINE{
		@Override
		public int getValue(){
			return 9;
		} 
	}, 
    
    TEN{
		@Override
		public int getValue(){
			return 10;
		} 
	}, 
    
    JACK{
		@Override
		public int getValue(){
			return 11;
		}
		@Override
		public String toString(){
			return "jack";
		}
	}, 
    
    QUEEN{
		@Override
		public int getValue(){
			return 12;
		}
		@Override
		public String toString(){
			return "queen";
		}
	}, 
    
    KING{
		@Override
		public int getValue(){
			return 13;
		}
		@Override
		public String toString(){
			return "king";
		}
	},
    
    ACE{
		@Override
		public int getValue(){
			return 14;
		}
		@Override
		public String toString(){
			return "ace";
		}
	};
	
	/**
	 * Returns the numeral value of this rank.
	 * 
	 * @return The numeral value of this rank.
	 */
	public abstract int getValue();
	/**
	 * Returns a textual representation of this rank
	 */
	@Override
	public String toString(){
		return Integer.toString(getValue());
	}
}
