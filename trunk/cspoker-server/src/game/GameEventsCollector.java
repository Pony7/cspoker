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

import game.events.GameEvent;
import game.events.GameEventListener;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class to collect and to manage
 * game events.
 *
 * @author Kenzo
 *
 */
public class GameEventsCollector implements GameEventListener{

	/**
	 * This variable contains the game events.
	 */
	private final List<GameEvent> events = new CopyOnWriteArrayList<GameEvent>();

	/**
	 * This method is called when subscribed to inform a new game event occurred.
	 *
	 * @param 	event
	 * 			The event object containing all information of the occurred event.
	 */
	public void onGameEvent(GameEvent event) {
		events.add(event);
	}

	/**
	 * Returns the latest game events.
	 *
	 * @return The latest game events.
	 */
	public List<GameEvent> getLatestEvents(){
		return Collections.unmodifiableList(events);
	}

	public List<GameEvent> getLatestEventsAndAck(long ack){
		//TODO
		return null;
	}

	public void clear() {
		events.clear();
	}

}
