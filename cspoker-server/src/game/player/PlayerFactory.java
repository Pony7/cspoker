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

package game.player;

import game.PlayerId;
import game.elements.chips.IllegalValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * A class for player factories.
 * 
 * Each player should be created by using the given 
 * factory methods.
 * 
 * This will ensure each player id is unique.
 * 
 * Also note that the constructor of player is for this reason 
 * only-package accessible.
 * 
 * @author Kenzo
 *
 */
public class PlayerFactory {

	private final static AtomicLong counter = new AtomicLong(0);
	
	/**
	 * Create a new player with given name and standard stack value.
	 * 
	 * @param 	name
	 * 			The name for this new player.
	 * @return	A new player with given name and standard stack value.
	 */
	public Player createNewPlayer(String name){
		try {
			return createNewPlayer(name, getStdStackValue());
		} catch (IllegalValueException e) {
			throw new IllegalStateException(getStdStackValue()+" should be a valid value.");
		}
	}
	
	
	/**
	 * Create a new player with given name and initial stack value.
	 * 
	 * @param 	name
	 * 			The name for this new player.
	 * @param 	initialValue
	 * 			The initial stack value for this new player.
	 * @return	A new player with given name and initial stack value.
	 * @throws 	IllegalValueException [must]
	 * 			The given initial value is not valid.
	 */
	public Player createNewPlayer(String name, int initialValue) throws IllegalValueException{
		return new Player(getUniquePlayerId(), name, initialValue);
	}
	
	/**
	 * Returns the standard stack value.
	 * 
	 * @return The standard stack value.
	 */
	public int getStdStackValue() {
		return 100;
	}
	
	/**
	 * Returns at each call a unique player id.
	 * 
	 * This method is thread-safe.
	 * 
	 * @return A unique player id.
	 */
	protected PlayerId getUniquePlayerId(){
		return new PlayerId(counter.getAndIncrement());
	}



}
