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

package org.cspoker.server.game.playerCommunication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cspoker.server.game.events.gameEvents.GameEvent;
import org.cspoker.server.game.events.gameEvents.GameEventListener;

/**
 * A class to collect and to manage
 * game events.
 *
 * @author Kenzo
 *
 */
public class GameEventsCollector implements GameEventListener{

	private int ackedToNumber=0;

	private int latestEventNumber=0;

	private final int latestAckedToNumber = 0;

	/**
	 * This variable contains the game events.
	 */
	private final List<GameEvent> events = new ArrayList<GameEvent>();

	/**
	 * This method is called when subscribed to inform a new game event occurred.
	 *
	 * @param 	event
	 * 			The event object containing all information of the occurred event.
	 */
	public synchronized void onGameEvent(GameEvent event) {
		events.add(event);
		latestEventNumber++;
	}

	/**
	 * Returns the latest game events.
	 *
	 * @return The latest game events.
	 */
	public synchronized GameEvents getLatestEvents(){
		return new GameEvents(Collections.unmodifiableList(new ArrayList<GameEvent>(events)), latestEventNumber);
	}

	public synchronized GameEvents getLatestEventsAndAck(int ack){
		if(ack<=ackedToNumber)
			return getLatestEvents();
		if((ack>latestEventNumber))
			ack = latestEventNumber;
		events.subList(0, ack-ackedToNumber).clear();
		ackedToNumber = ack;
		return getLatestEvents();
	}
}
