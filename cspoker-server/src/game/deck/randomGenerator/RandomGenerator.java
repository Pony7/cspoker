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
 **/

package game.deck.randomGenerator;

/**
 * An interface for random generators for card games.
 * 
 * @author Kenzo
 *
 */
public interface RandomGenerator {
	
	/**
	 * Returns a random sequence between 0 and 51,
	 * consisting of 52 numbers of wich each
	 * number is unique.
	 * 
	 * This number can be used to get the card index.
	 * 
	 * @return	The array is effective.
	 * 			| result!=null
	 * @return	The length is exactly 52.
	 * 			| result.length == 52
	 * @return 	Each number is unique.
	 * 			| for each number1 and number2 in result:
	 * 			| if(result[index1]==number1 && result[index2]==number2
	 * 			|		&& index1!=index2)
	 * 			|	then number1!=number2
	 * @return 	Each number is between 0 and 51.
	 * 			| for each number in result:
	 * 			|	0<=number && number<=51
	 */
	public int[] getRandomSequence();

}
