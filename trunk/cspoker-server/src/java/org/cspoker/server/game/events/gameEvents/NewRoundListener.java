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

package org.cspoker.server.game.events.gameEvents;

/**
 * An interface for new round listeners.
 * 
 * @author Kenzo
 * 
 */
public interface NewRoundListener {

    /**
     * This method is called when subscribed to inform a new round event
     * occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onNewRoundEvent(NewRoundEvent event);

}
