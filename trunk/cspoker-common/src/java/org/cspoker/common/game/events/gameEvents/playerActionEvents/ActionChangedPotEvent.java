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

package org.cspoker.common.game.events.gameEvents.playerActionEvents;

import org.cspoker.common.game.elements.pots.Pots;
import org.cspoker.common.game.events.gameEvents.GameEvent;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class ActionChangedPotEvent extends GameEvent{

    private static final long serialVersionUID = -4260315075072948801L;

    private final Pots pots;

    public ActionChangedPotEvent(Pots pots){
	this.pots = pots;
    }
    /**
     * Returns the pots.
     * @return
     */
    public Pots getPots(){
	return pots;
    }

    @Override
    public void toXml(ContentHandler handler) throws SAXException {
	// TODO Auto-generated method stub

    }

}
