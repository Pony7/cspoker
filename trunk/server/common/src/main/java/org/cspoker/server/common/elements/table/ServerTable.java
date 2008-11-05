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

package org.cspoker.server.common.elements.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.SeatId;

/**
 * A class to represent players at the table.
 * 
 */
public class ServerTable {

	/***************************************************************************
	 * Variables
	 **************************************************************************/

	/**
	 * A map containing the mapping between a seat id and a player.
	 */
	private final ConcurrentHashMap<SeatId, MutableSeatedPlayer> players;

	private int maxNbPlayers;


	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	/**
	 * Construct a new table with given maximum number of players.
	 * 
	 * @param 	maxNbPlayers
	 * 			The maximum number of players.
	 */
	public ServerTable(int maxNbPlayers) {
		if(maxNbPlayers<=1)
			throw new IllegalArgumentException("The given maximum number of players is invalid.");
		this.maxNbPlayers = maxNbPlayers;
		players = new ConcurrentHashMap<SeatId, MutableSeatedPlayer>(maxNbPlayers);
	}
	
	/***************************************************************************
	 * Max number of players
	 **************************************************************************/

	public int getMaxNbPlayers(){
		return maxNbPlayers;
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
	public synchronized void removePlayer(MutableSeatedPlayer player) {
		if (!hasAsPlayer(player)) {
			throw new IllegalArgumentException(player
					+ " is not a player of this table.");
		}
		SeatId seatId = player.getSeatId();
		if (seatId != null) {
			players.remove(seatId);
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
	public synchronized SeatId addPlayer(MutableSeatedPlayer player)
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

		while (isValidSeatId(seatId)
				&& players.putIfAbsent(seatId, player) != null) {
			seatId = new SeatId(seatId.getId()+1);
		}
		if (!isValidSeatId(seatId)) {
			throw new PlayerListFullException();
		}
		player.setSeatId(seatId);
		return seatId;
	}

	/**
	 * 
	 * @param seatId
	 * @param player
	 * @throws SeatTakenException
	 */
	public synchronized void addPlayer(SeatId seatId, MutableSeatedPlayer player)
			throws IllegalActionException, SeatTakenException {
		if (!isValidSeatId(seatId)) {
			throw new IllegalArgumentException(
					"The given seat id should be valid.");
		}
		
		if (player == null) {
			throw new IllegalArgumentException(
					"The given player should be valid.");
		}
		
		if (hasAsPlayer(player)) {
			throw new IllegalActionException(player
					+ " is already seated at this table.");
		}
		
		if (players.putIfAbsent(seatId, player) != null) {
			throw new SeatTakenException(seatId);
		}
		player.setSeatId(seatId);
	}

	public boolean isValidSeatId(SeatId seatId) {
		return seatId != null
				&& seatId.getId() < getMaxNbPlayers();
	}

	/**
	 * Checks whether this table is full.
	 * 
	 */
	public boolean fullOfPlayers() {
		return players.size() >= getMaxNbPlayers();
	}

	/**
	 * Checks whether the given player is part of this table.
	 * 
	 * @param player
	 *            The given player
	 */
	public boolean hasAsPlayer(MutableSeatedPlayer player) {
		return players.contains(player);
	}

	/**
	 * Returns the list with all the players at this table.
	 * 
	 * The returned list is unmodifiable.
	 * 
	 * @return The list with all the players at this table.
	 */
	public List<MutableSeatedPlayer> getMutableSeatedPlayers() {
		List<MutableSeatedPlayer> playerList = new ArrayList<MutableSeatedPlayer>();
		for (int i = 0; i < getMaxNbPlayers(); i++) {
			MutableSeatedPlayer player = players.get(new SeatId(i));
			if (player != null) {
				playerList.add(player);
			}
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
	public List<SeatedPlayer> getSeatedPlayers() {
		List<SeatedPlayer> toReturn = new ArrayList<SeatedPlayer>();
		for (MutableSeatedPlayer player : players.values()) {
			toReturn.add(player.getMemento());
		}
		return Collections.unmodifiableList(toReturn);
	}
	
	/**
	 * Returns a random player seated at this table.
	 * 
	 * @return A random player seated at this table.
	 */
	public synchronized MutableSeatedPlayer getRandomPlayer() {
		List<SeatId> ids = new ArrayList<SeatId>(players.keySet());
		return players.get(ids.get(new Random().nextInt(ids.size())));
	}

	/**
	 * Returns the number of players at this table.
	 * 
	 * @return The number of players at this table.
	 */
	public int getNbPlayers() {
		return players.size();
	}
}
