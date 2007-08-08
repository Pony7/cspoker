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

import java.util.concurrent.ConcurrentHashMap;

/**
 * A class for game managers.
 *
 * This class has static methods to access all game mediators.
 *
 * @author Kenzo
 *
 */
public class GameManager {

	/**
	 * The hash map containing all the game mediators.
	 */
	private static ConcurrentHashMap<TableId, GameMediator> hashMap = new ConcurrentHashMap<TableId, GameMediator>();

	/**
	 * Returns the game mediator for the game with given id.
	 *
	 * @param 	id
	 * 			The id of the game corresponding to the game mediator.
	 * @pre 	The id should be effective.
	 *			|id!=null
	 * @return	The game mediator for the game with given id.
	 */
	public static GameMediator getGame(TableId id){
		return hashMap.get(id);
	}

	public static void removeGame(TableId id){
		hashMap.remove(id);
	}

	public static void addGame(TableId id, GameMediator gameMediator){
		hashMap.put(id, gameMediator);
	}

}
