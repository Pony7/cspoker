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
package org.cspoker.common.api.lobby.holdemtable.action;

import javax.xml.bind.annotation.XmlRootElement;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.context.StaticHoldemTableContext;
import org.cspoker.common.api.shared.event.EventId;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableId;


@XmlRootElement
@Immutable
public class SitInAction extends HoldemTableAction<Void> {

	private static final long serialVersionUID = 5400969226725585815L;

	private final SeatId seatId;

	private final int buyIn;

	public SitInAction(EventId id, TableId tableId, SeatId seatId, int buyIn) {
		super(id, tableId);
		this.seatId = seatId;
		this.buyIn = buyIn;
	}

	protected SitInAction() {
		seatId = null;
		buyIn = 0;
	}

	@Override
	public Void perform(StaticHoldemTableContext holdemTableContext) throws IllegalActionException {
		holdemTableContext.sitIn(seatId,buyIn);
		return null;
	}

}
