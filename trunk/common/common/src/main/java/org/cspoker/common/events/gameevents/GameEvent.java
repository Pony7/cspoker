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

package org.cspoker.common.events.gameevents;

import java.rmi.RemoteException;

import org.cspoker.common.api.shared.event.Event;

/**
 * A class of game events.
 * 
 * All game events should inherit from this abstract class.
 * 
 * @author Kenzo
 * 
 */
public abstract class GameEvent extends Event {

	private static final long serialVersionUID = 3585256155687704729L;

	public abstract void dispatch(RemoteAllEventsListener listener)
			throws RemoteException;

}
