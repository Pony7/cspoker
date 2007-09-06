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

package org.cspoker.server.game;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.server.game.events.Event;
import org.cspoker.server.game.events.EventListener;
import org.cspoker.server.game.events.MessageEvent;
import org.cspoker.server.game.events.MessageListener;
import org.cspoker.server.game.events.serverEvents.PlayerJoinedEvent;
import org.cspoker.server.game.events.serverEvents.PlayerJoinedListener;
import org.cspoker.server.game.events.serverEvents.PlayerLeftEvent;
import org.cspoker.server.game.events.serverEvents.PlayerLeftListener;
import org.cspoker.server.game.events.serverEvents.TableCreatedEvent;
import org.cspoker.server.game.events.serverEvents.TableCreatedListener;

public class ServerMediator {

	public ServerMediator(){

	}

	/**
	 * Inform all subscribed player left listeners a player left event has occurred.
	 *
	 * Each subscribed player left listener is updated
	 * by calling their onPlayerLeftEvent() method.
	 *
	 */
	public void publishPlayerLeftEvent(PlayerLeftEvent event) {
		for (PlayerLeftListener listener : playerLeftListeners) {
			listener.onPlayerLeftEvent(event);
		}
		publishServerEvent(event);
	}

	/**
	 * Subscribe the given player left listener for player left events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribePlayerLeftListener(PlayerLeftListener listener) {
		playerLeftListeners.add(listener);
	}

	/**
	 * Unsubscribe the given player left listener for player left events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribePlayerLeftListener(PlayerLeftListener listener) {
		playerLeftListeners.remove(listener);
	}

	/**
	 * This list contains all player left listeners that
	 * should be alerted on a player left.
	 */
	private final List<PlayerLeftListener> playerLeftListeners = new CopyOnWriteArrayList<PlayerLeftListener>();

	/**
	 * Inform all subscribed table created listeners a table created event has occurred.
	 *
	 * Each subscribed table created listener is updated
	 * by calling their onTableCreatedEvent() method.
	 *
	 */
	public void publishTableCreatedEvent(TableCreatedEvent event) {
		for (TableCreatedListener listener : tableCreatedListeners) {
			listener.onTableCreatedEvent(event);
		}
		publishServerEvent(event);
	}

	/**
	 * Subscribe the given table created listener for table created events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeTableCreatedListener(TableCreatedListener listener) {
		tableCreatedListeners.add(listener);
	}

	/**
	 * Unsubscribe the given table created listener for table created events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeTableCreatedListener(TableCreatedListener listener) {
		tableCreatedListeners.remove(listener);
	}

	/**
	 * This list contains all table created listeners that
	 * should be alerted on a table created.
	 */
	private final List<TableCreatedListener> tableCreatedListeners = new CopyOnWriteArrayList<TableCreatedListener>();

	/**
	 * Inform all subscribed player joined listeners a player joined event has occurred.
	 *
	 * Each subscribed player joined listener is updated
	 * by calling their onPlayerJoinedEvent() method.
	 *
	 */
	public void publishPlayerJoinedEvent(PlayerJoinedEvent event) {
		for (PlayerJoinedListener listener : playerJoinedListeners) {
			listener.onPlayerJoinedEvent(event);
		}
		publishServerEvent(event);
	}

	/**
	 * Subscribe the given player joined listener for player joined events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribePlayerJoinedListener(PlayerJoinedListener listener) {
		playerJoinedListeners.add(listener);
	}

	/**
	 * Unsubscribe the given player joined listener for player joined events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribePlayerJoinedListener(PlayerJoinedListener listener) {
		playerJoinedListeners.remove(listener);
	}

	/**
	 * This list contains all player joined listeners that
	 * should be alerted on a player joined event.
	 */
	private final List<PlayerJoinedListener> playerJoinedListeners = new CopyOnWriteArrayList<PlayerJoinedListener>();

	/**
	 * Inform all subscribed message listeners a message event has occurred.
	 *
	 * Each subscribed message listener is updated
	 * by calling their onMessageEvent() method.
	 *
	 */
	public void publishMessageEvent(MessageEvent event) {
		for (MessageListener listener : messageListeners) {
			listener.onMessageEvent(event);
		}
		publishServerEvent(event);
	}

	/**
	 * Subscribe the given message listener for message events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeMessageListener(MessageListener listener) {
		messageListeners.add(listener);
	}

	/**
	 * Unsubscribe the given message listener for message events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeMessageListener(MessageListener listener) {
		messageListeners.remove(listener);
	}

	/**
	 * This list contains all message listeners that
	 * should be alerted on a message.
	 */
	private final List<MessageListener> messageListeners = new CopyOnWriteArrayList<MessageListener>();


	/**********************************************************
	 * Server Events
	 **********************************************************/


	/**
	 * Inform all subscribed server event listeners a server event has occurred.
	 *
	 * Each subscribed server event listener is updated
	 * by calling their onEvent() method.
	 *
	 */
	private void publishServerEvent(Event event) {
		for (EventListener listener : serverEventListeners) {
			listener.onEvent(event);
		}
	}

	/**
	 * Subscribe the given server event listener for server events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeServerEventListener(EventListener listener) {
		serverEventListeners.add(listener);
	}

	/**
	 * Unsubscribe the given server event listener for server events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeServerEventListener(EventListener listener) {
		serverEventListeners.remove(listener);
	}

	/**
	 * This list contains all server event listeners that
	 * should be alerted on a server event.
	 */
	private final List<EventListener> serverEventListeners = new CopyOnWriteArrayList<EventListener>();
}
