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
package org.cspoker.server.xml.common;

import net.jcip.annotations.NotThreadSafe;

import org.cspoker.common.api.lobby.holdemtable.context.ForwardingHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.StaticHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.listener.UniversalTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.SeatId;

@NotThreadSafe
public class XmlHoldemTableContext extends ForwardingHoldemTableContext
implements StaticHoldemTableContext {

	private UniversalTableListener tableListener;
	private HoldemPlayerContext context = null;

	public XmlHoldemTableContext(HoldemTableContext context, UniversalTableListener tableListener) {
		super(context);
		this.tableListener = tableListener;
	}

	//convention is to return null when the right action as not yet been performed.
	public HoldemPlayerContext getHoldemPlayerContext() {
		return context;
	}

	public void sitIn(SeatId seatId, int buyIn) throws IllegalActionException {
		context = super.sitIn(seatId, buyIn, tableListener);
	}

	public void sitIn(int buyIn) throws IllegalActionException {
		context = super.sitIn(buyIn, tableListener);
	}

}
