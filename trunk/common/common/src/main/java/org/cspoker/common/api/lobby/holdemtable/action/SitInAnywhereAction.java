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
import org.cspoker.common.elements.table.TableId;

@XmlRootElement
@Immutable
public class SitInAnywhereAction extends HoldemTableAction<Void> {

	private static final long serialVersionUID = 7302000503865264469L;
	
	private final int buyIn;

	public SitInAnywhereAction(EventId id, TableId tableId, int buyIn) {
		super(id, tableId);
		this.buyIn = buyIn;
	}

	protected SitInAnywhereAction() {
		buyIn = 0;
	}

	@Override
	public Void perform(StaticHoldemTableContext holdemTableContext) throws IllegalActionException {
		holdemTableContext.sitIn(buyIn);
		return null;
	}

}
