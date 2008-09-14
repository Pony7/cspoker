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
package org.cspoker.common.api.lobby.holdemtable.holdemplayer.action;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.HoldemPlayerListener;

@XmlRootElement
public class BetOrRaiseAction extends HoldemPlayerAction<Void> {

	private static final long serialVersionUID = 7370121685541484279L;

	@XmlAttribute
	private int amount;

	public BetOrRaiseAction(long id, long tableId, int amount) {
		super(id,tableId);
		this.amount = amount;
	}

	protected BetOrRaiseAction() {
		// no op
	}
	
	@Override
	public void perform(HoldemPlayerContext holdemPlayerContext,
			HoldemPlayerListener holdemPlayerListener) {
		holdemPlayerContext.betOrRaise(amount);
		dispatchResult(null, holdemPlayerListener);
	}

}
