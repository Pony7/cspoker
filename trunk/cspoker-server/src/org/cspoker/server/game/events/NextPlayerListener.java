package org.cspoker.server.game.events;

/**
 * An interface for next player listeners.
 *
 * @author Kenzo
 *
 */
public interface NextPlayerListener {
	/**
	 * This method is called when subscribed to inform a next player event occurred.
	 *
	 * @param 	event
	 * 			The event object containing all information of the occurred event.
	 */
	public void onNextPlayerEvent(NextPlayerEvent event);
}
