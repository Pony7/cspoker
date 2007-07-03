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

/**
 * All the rounds a game can be in.
 * 
 * @author Kenzo
 *
 */
public enum Round {
	
	PREFLOP_ROUND{
		public Round getNextRound(){
			return FLOP_ROUND;
		}
	}, 
	
	
	FLOP_ROUND{
		public Round getNextRound(){
			return TURN_ROUND;
		}
	}, 
	
	
	TURN_ROUND{
		public Round getNextRound(){
			return FINAL_ROUND;
		}
		
	}, 
	
	
	FINAL_ROUND{
		public Round getNextRound(){
			return null;
		}
		
	};
	
	public abstract Round getNextRound();
	}
