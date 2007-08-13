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

import org.cspoker.server.game.events.GameEvent;
import org.cspoker.server.game.events.GameEventListener;

/**
 * A class to collect and to manage
 * game events.
 *
 * @author Kenzo
 *
 */
public class GameEventsCollector implements GameEventListener{

	private int ackedToNumber=0;

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
	@Override
	public synchronized void onGameEvent(GameEvent event) {
		events.add(event);
	}

	/**
	 * Returns the latest game events.
	 *
	 * @return The latest game events.
	 */
	public synchronized List<GameEvent> getLatestEvents(){
		return Collections.unmodifiableList(new ArrayList<GameEvent>(events.subList(ackedToNumber, events.size())));
	}

	public synchronized List<GameEvent> getLatestEventsAndAck(int ack){
		if(ack<=ackedToNumber)
			return getLatestEvents();
		if((ack>events.size()))
			ack = events.size();
		ackedToNumber = ack;

		return Collections.unmodifiableList(new ArrayList<GameEvent>(events.subList(ack, events.size())));
	}

}
