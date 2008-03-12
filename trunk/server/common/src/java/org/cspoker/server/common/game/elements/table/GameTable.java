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

package org.cspoker.server.common.game.elements.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.player.Player;
import org.cspoker.common.player.PlayerId;
import org.cspoker.server.common.game.player.GamePlayer;

/**
 * A class to represent players at the table.
 * 
 * @author Kenzo & Cedric
 * 
 * @invar A table must have a valid game property. |
 *        canHaveAsGameProperty(getGameProperty())
 * @invar Each player at the table is unique.
 * 
 */
public class GameTable {

	/***************************************************************************
	 * Variables
	 **************************************************************************/

	/**
	 * The variable containing the id of this table.
	 */
	private final TableId tableId;

	/**
	 * A map containing the mapping between a seat id and a player.
	 */
	private final ConcurrentHashMap<SeatId, GamePlayer> players;
	

	/**
	 * The variable containing the game property of this table.
	 */
	private GameProperty gameProperty;

	/**
	 * The variable containing the playing status of this table.
	 */
	private boolean playing;
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	/**
	 * Construct a new table with given game property.
	 * 
	 * @param gameProperty
	 *            The game property for this table.
	 * @effect Set the game property of this table to the given game property.
	 *         |setGameProperty(gameProperty)
	 * @effect Set the playing status to false. |setPlaying(false)
	 */
	public GameTable(TableId id, GameProperty gameProperty) {
		this(id, "", gameProperty);
	}

	public GameTable(TableId id, String name, GameProperty gameProperty) {
		this.tableId = id;
		setGameProperty(gameProperty);
		setPlaying(false);
		setName(name);
		players = new ConcurrentHashMap<SeatId, GamePlayer>(gameProperty.getMaxNbPlayers());
	}

	/**
	 * Returns the id of this table.
	 * 
	 * @return The id of this table.
	 */
	public TableId getId() {
		return tableId;
	}

	/***************************************************************************
	 * Name
	 **************************************************************************/

	/**
	 * Return the name of this table.
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Check whether tables can have the given name as their name.
	 * 
	 * @param name
	 *            The name to check.
	 * @return The given name should be effective | name!=null
	 * @return The given name should be at least one character long. |
	 *         name!=null && name.length()>0
	 */
	public static boolean canHaveAsName(String name) {
		if (name == null) {
			return false;
		}
		return name.length() > 0;
	}

	/**
	 * Set the name of this table to the given name.
	 * 
	 * @param name
	 *            The new name for this table.
	 * @post If the given name is a valid name for this table the name of this
	 *       table is set to the given name, else the default name is used. |
	 *       if(canHaveAsName(name)) | then new.getName().equals(name) | else
	 *       new.getName().equals(getDefaultName())
	 */
	private void setName(String name) {
		if (!canHaveAsName(name)) {
			this.name = getDefaultName();
		} else {
			this.name = name;
		}
	}

	/**
	 * Change the name of this table to the given name.
	 * 
	 * @param name
	 *            The new name for this table.
	 * @post If the given name is a valid name for this table the name of this
	 *       table is set to the given name, else the previous name is kept. |
	 *       if(canHaveAsName(name)) | then new.getName().equals(name) | else
	 *       new.getName().equals(getName())
	 */
	public void changeName(String name) {
		if (!canHaveAsName(name)) {
			setName(name);
		}
	}

	protected String getDefaultName() {
		return "default table";
	}

	/**
	 * This variable contains the name of this table.
	 */
	private String name;

	/***************************************************************************
	 * Game Property
	 **************************************************************************/

	/**
	 * Return the game property of this table.
	 * 
	 */
	public GameProperty getGameProperty() {
		return gameProperty;
	}

	/**
	 * Check whether tables can have the given game property as their game
	 * property.
	 * 
	 * @param gameProperty
	 *            The game property to check.
	 * @return The game property should be effective. | result ==
	 *         (gameProperty!=null)
	 */
	public static boolean canHaveAsGameProperty(GameProperty gameProperty) {
		return gameProperty != null;
	}

	/**
	 * Set the game property of this table to the given game property.
	 * 
	 * @param gameProperty
	 *            The new game property for this table.
	 * @pre This table must be able to have the given game property as its game
	 *      property. | canHaveAsGameProperty(gameProperty)
	 * @post The game property of this table is set to the given game property. |
	 *       new.getGameProperty() == gameProperty
	 */
	private void setGameProperty(GameProperty gameProperty) {
		this.gameProperty = gameProperty;
	}

	/***************************************************************************
	 * Playing Status
	 **************************************************************************/

	/**
	 * Check whether players are playing or not at this table.
	 * 
	 * @return True if players are playing at this table, False otherwise.
	 */
	public synchronized boolean isPlaying() {
		return playing;
	}

	/**
	 * Set the game property of this table to the given game property.
	 * 
	 * @param gameProperty
	 *            The new gameProperty for this table.
	 * @post The playing status of this table is set to the given playing
	 *       status. | new.isPlaying() == playing
	 */
	public synchronized void setPlaying(boolean playing) {
		this.playing = playing;
	}

	/***************************************************************************
	 * Add/Remove Players to/from the table.
	 **************************************************************************/

	/**
	 * Removes the given player from this table.
	 * 
	 * @throws IllegalArgumentException
	 *             If the given player isn't seated at this table. |
	 *             !hasAsPlayer(player)
	 * @post The given player isn't seated at this table anymore. |
	 *       !new.hasAsPlayer(player)
	 */
	public synchronized void removePlayer(GamePlayer player) {
		if (!hasAsPlayer(player)) {
			throw new IllegalArgumentException(player
					+ " is not a player of this table.");
		}
		SeatId seatId = player.getSeatId();
		if(seatId!=null){
			players.remove(seatId);
			player.setSeatId(null);
		}
	}
	

	/**
	 * Adds the given player to this table.
	 * 
	 * @param player
	 *            The given player
	 * @pre The given player should be effective. | player!=null
	 * @pre The given player should have chips. |player!=null &&
	 *      player.getStack().getValue()>0
	 * @throws PlayerListFullException
	 *             [must] If this game is full of players or if players are
	 *             playing and the game is closed. | fullOfPlayers() ||
	 *             (isPlaying()&&getProperty().isClosedGame())
	 * @throws IllegalArgumentException
	 *             If the given player is already seated at this table. |
	 *             hasAsPlayer(player)
	 * @post The given player is seated at this table. | new.hasAsPlayer(player)
	 */
	public synchronized SeatId addPlayer(GamePlayer player)
			throws PlayerListFullException {
		if (player == null) {
			throw new IllegalArgumentException("player should be effective.");
		}
		
		if (hasAsPlayer(player)) {
			throw new IllegalArgumentException(player
					+ " is already seated at this table.");
		}
		if (player.getStack().getValue() == 0) {
			throw new IllegalArgumentException(player + " has no chips to bet.");
		}
		
		SeatId seatId = new SeatId(0);
		
		while(isValidSeatId(seatId) && players.putIfAbsent(seatId, player)!=null){
			seatId = seatId.getNextSeatId();
		}
		if(!isValidSeatId(seatId))
			throw new PlayerListFullException();
		player.setSeatId(seatId);
		return seatId;
	}
	
	/**
	 * 
	 * @param seatId
	 * @param player
	 * @throws SeatTakenException
	 */
	public synchronized void addPlayer(SeatId seatId, GamePlayer player) throws SeatTakenException{
		if(!isValidSeatId(seatId))
			throw new IllegalArgumentException("The given seat id should be valid.");
		if(player==null)
			throw new IllegalArgumentException("The given player should be valid.");
		if(players.putIfAbsent(seatId, player)!=null){
			throw new SeatTakenException(tableId, seatId);
		}
		player.setSeatId(seatId);
	}
	
	public boolean isValidSeatId(SeatId seatId){
		return seatId!=null && seatId.getId()<gameProperty.getMaxNbPlayers();
	}

	/**
	 * Checks whether this table is full.
	 * 
	 */
	public boolean fullOfPlayers() {
		return players.size() >= getGameProperty().getMaxNbPlayers();
	}

	/**
	 * Checks whether the given player is part of this table.
	 * 
	 * @param player
	 *            The given player
	 */
	public boolean hasAsPlayer(GamePlayer player) {
		return players.contains(player);
	}

	/**
	 * Returns the list with all the players at this table.
	 * 
	 * The returned list is unmodifiable.
	 * 
	 * @return The list with all the players at this table.
	 */
	public List<GamePlayer> getPlayers() {
		List<GamePlayer> playerList = new ArrayList<GamePlayer>();
		
		for(int i=0;i<gameProperty.getMaxNbPlayers();i++){
			GamePlayer player = players.get(new SeatId(i));
			if(player!=null)
				playerList.add(player);
		}
		return Collections.unmodifiableList(playerList);
	}

	/**
	 * Returns the list with all the player id's of all the players at the
	 * table.
	 * 
	 * The returned list is unmodifiable.
	 * 
	 * @return The list with all the player id's of all the players at the
	 *         table.
	 */
	public List<PlayerId> getPlayerIds() {
		List<PlayerId> toReturn = new ArrayList<PlayerId>();
		for (GamePlayer player : players.values()) {
			toReturn.add(player.getId());
		}
		return Collections.unmodifiableList(toReturn);
	}

	public synchronized GamePlayer getRandomPlayer() {
		List<SeatId> ids = new ArrayList<SeatId>(players.keySet());
		return players.get(ids.get(new Random().nextInt(ids.size())));
	}

	/**
	 * Returns the number of players at this table.
	 * 
	 * @return The number of players at this table.
	 */
	public synchronized int getNbPlayers() {
		return players.size();
	}
	
	public synchronized Table getSavedTable(){
		List<Player> playerList = new ArrayList<Player>(getNbPlayers());
		for(GamePlayer player:players.values()){
			playerList.add(player.getSavedPlayer());
		}
		return new Table(tableId, name, playerList, playing, gameProperty);
	}
}
