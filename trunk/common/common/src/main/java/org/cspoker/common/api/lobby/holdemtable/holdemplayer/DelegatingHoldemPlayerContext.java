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
package org.cspoker.common.api.lobby.holdemtable.holdemplayer;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.HoldemPlayerListener;

public class DelegatingHoldemPlayerContext implements HoldemPlayerContext{

	private final HoldemPlayerContext holdemPlayerContext;

	public DelegatingHoldemPlayerContext(HoldemPlayerContext holdemPlayerContext) {
		this.holdemPlayerContext  = holdemPlayerContext;
	}

	public void betOrRaise(int amount) {
		holdemPlayerContext.betOrRaise(amount);
	}

	public void checkOrCall() {
		holdemPlayerContext.checkOrCall();
	}

	public void fold() {
		holdemPlayerContext.fold();
	}

	public void leaveGame() {
		holdemPlayerContext.leaveGame();
	}

	public void subscribe(HoldemPlayerListener holdemPlayerListener) {
		holdemPlayerContext.subscribe(holdemPlayerListener);
	}

	public void unSubscribe(HoldemPlayerListener holdemPlayerListener) {
		holdemPlayerContext.unSubscribe(holdemPlayerListener);
	}
	
}
