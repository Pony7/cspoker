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

import org.cspoker.server.game.events.Event;
import org.cspoker.server.game.events.EventListener;

/**
 * A class to collect and to manage
 * game events.
 *
 * @author Kenzo
 *
 */
public class EventsCollector implements EventListener{

	private int ackedToNumber=0;

	private int latestEventNumber=0;

	/**
	 * This variable contains the game events.
	 */
	private final List<Event> events = new ArrayList<Event>();

	/**
	 * This method is called when subscribed to inform a new game event occurred.
	 *
	 * @param 	event
	 * 			The event object containing all information of the occurred event.
	 */
	public synchronized void onEvent(Event event) {
		events.add(event);
		latestEventNumber++;
	}

	/**
	 * Returns the latest game events.
	 *
	 * @return The latest game events.
	 */
	public synchronized Events getLatestEvents(){
		return new Events(Collections.unmodifiableList(new ArrayList<Event>(events)), latestEventNumber);
	}

	public synchronized Events getLatestEventsAndAck(int ack){
		if(ack<=ackedToNumber)
			return getLatestEvents();
		if((ack>latestEventNumber))
			ack = latestEventNumber;
		events.subList(0, ack-ackedToNumber).clear();
		ackedToNumber = ack;
		return getLatestEvents();
	}
}
