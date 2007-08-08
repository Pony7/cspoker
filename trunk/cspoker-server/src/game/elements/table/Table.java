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

package game.elements.table;

import game.PlayerId;
import game.TableId;
import game.gameControl.GameProperty;
import game.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class to represent players at the table.
 *
 * @author Kenzo & Cedric
 *
 * @invar 	A table must have a valid game property.
 *		  	| canHaveAsGameProperty(getGameProperty())
 * @invar 	Each player at the table is unique.
 *
 */
public class Table {

	/**********************************************************
	 * Variables
	 **********************************************************/

	/**
	 * The variable containing the id of this table.
	 */
	private final TableId id;

	/**
	 * The list of players in the waiting room.
	 */
        //TODO: question from guy: shouldn't thread safety be guaranteed by
    	//	some higher level locking? then why thread safe list?

		//TODO: answer from Kenzo: while some thread tries to add/remove a player
		//other threads should be able to iterate over all players this table contains,
		//without having a ConcurrentModificationException.
		//No higher level of locking is necessary for the table itself,
		//otherwise it will be a potential bottleneck.
		//Off course there is the overhead of copying the internal array at each add/remove
		//but iteration is more frequent, so should be done quicker/easier,
		//without the need to synchronize to keep the list consistent.

	private final List<Player> players = new CopyOnWriteArrayList<Player>();

	/**
	 * The variable containing the game property of this table.
	 */
	private GameProperty gameProperty;

	/**
	 * The variable containing the playing status of this table.
	 */
	private boolean playing;

	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Construct a new table with
	 * given game property.
	 *
	 * @param 	gameProperty
	 * 			The game property for this table.
	 * @effect 	Set the game property of this table
	 * 			to the given game property.
	 *		   	|setGameProperty(gameProperty)
	 * @effect 	Set the playing status to false.
	 *		   	|setPlaying(false)
	 */
	public Table(TableId id, GameProperty gameProperty){
		this.id = id;
		setGameProperty(gameProperty);
		setPlaying(false);
	}

	/**
	 * Returns the id of this table.
	 *
	 * @return The id of this table.
	 */
	public TableId getId(){
		return id;
	}

	/**********************************************************
	 * Game Property
	 **********************************************************/

	/**
	 * Return the game property of this table.
	 *
	 */
	public GameProperty getGameProperty() {
		return gameProperty;
	}

	/**
	 * Check whether tables can have the given game property
	 * as their game property.
	 *
	 * @param	gameProperty
	 * 			The game property to check.
	 * @return	The game property should be effective.
	 * 			| result == (gameProperty!=null)
	 */
	public static boolean canHaveAsGameProperty(GameProperty gameProperty) {
		return gameProperty!=null;
	}


	/**
	 * Set the game property of this table to the given game property.
	 *
	 * @param	gameProperty
	 * 			The new game property for this table.
	 * @pre   	This table must be able to have the given game property
	 * 			as its game property.
	 * 			| canHaveAsGameProperty(gameProperty)
	 * @post	The game property of this table is set to the given
	 * 			game property.
	 * 			| new.getGameProperty() == gameProperty
	 */
	public void setGameProperty(GameProperty gameProperty) {
		this.gameProperty = gameProperty;
	}

	/**********************************************************
	 * Playing Status
	 **********************************************************/

	/**
	 * Check whether players are playing or not at this table.
	 *
	 * @return 	True if players are playing at this table,
	 * 			False otherwise.
	 */
	public synchronized boolean isPlaying() {
		return playing;
	}

	/**
	 * Set the game property of this table to the given game property.
	 *
	 * @param	gameProperty
	 * 			The new gameProperty for this table.
	 * @post	The playing status of this table is set to the given
	 * 			playing status.
	 * 			| new.isPlaying() == playing
	 */
	public synchronized void setPlaying(boolean playing) {
		this.playing = playing;
	}

	/**********************************************************
	 * Add/Remove Players to/from the table.
	 **********************************************************/


	/**
	 * Removes the given player from this table.
	 *
	 * @throws	IllegalArgumentException
	 * 			If the given player isn't seated at this table.
	 * 			| !hasAsPlayer(player)
	 * @post	The given player isn't seated at this table anymore.
	 * 			| !new.hasAsPlayer(player)
	 */
	public synchronized void removePlayer(Player player){
		if(!hasAsPlayer(player))
			throw new IllegalArgumentException(player+" is not a player of this table.");
		players.remove(player);
	}

	/**
	 * Adds the given player to this table.
	 *
	 * @param 	player
	 * 			The given player
	 * @pre 	The given player should be effective.
	 *			| player!=null
	 * @pre 	The given player should have chips.
	*			|player!=null && player.getStack().getValue()>0
	 * @throws 	PlayerListFullException [must]
	 * 			If this game is full of players or if
	 * 			players are playing and the game is closed.
	 * 			| fullOfPlayers() || (isPlaying()&&getProperty().isClosedGame())
	 * @throws	IllegalArgumentException
	 * 			If the given player is already seated at this table.
	 * 			| hasAsPlayer(player)
	 * @post	The given player is seated at this table.
	 * 			| new.hasAsPlayer(player)
	 */
	public synchronized void addPlayer(Player player) throws PlayerListFullException{
		if(player==null)
			throw new IllegalArgumentException("player should be effective.");
		if(fullOfPlayers())
			throw new PlayerListFullException();
		if(hasAsPlayer(player))
			throw new IllegalArgumentException(player+" is already seated at this table.");
		if(player.getStack().getValue()==0)
			throw new IllegalArgumentException(player+" has no chips to bet.");
		players.add(player);
	}

	/**
	 * Checks whether this table is full.
	 *
	 */
	public boolean fullOfPlayers(){
		return players.size()>=getGameProperty().getMaxNbPlayers();
	}

	/**
	 * Checks whether the given player is part of this table.
	 *
	 * @param 	player
	 * 			The given player
	 */
	public boolean hasAsPlayer(Player player){
		return players.contains(player);
	}

	/**
	 * Returns the list with all the players
	 * at this table.
	 *
	 * The returned list is unmodifiable.
	 *
	 * @return 	The list with all the players at this table.
	 */
	public List<Player> getPlayers(){
		return Collections.unmodifiableList(players);
	}

	/**
	 * Returns the list with all the player id's
	 * of all the players at the table.
	 *
	 * The returned list is unmodifiable.
	 *
	 * @return	The list with all the player id's
	 * 			of all the players at the table.
	 */
	public List<PlayerId> getPlayerIds(){
		List<PlayerId> toReturn = new ArrayList<PlayerId>();
		for(Player player:players){
			toReturn.add(player.getId());
		}
		return Collections.unmodifiableList(toReturn);
	}

	/**
	 * Returns the number of players at this table.
	 *
	 * @return The number of players at this table.
	 */
	public int getNbPlayers(){
		return players.size();
	}
}
