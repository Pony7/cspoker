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
package org.cspoker.common.api.lobby.holdemtable.context;

import java.util.concurrent.Executor;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.AsynchronousHoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;

@Immutable
public class AsynchronousHoldemTableContext extends ForwardingHoldemTableContext {

	protected Executor executor;
	
	public AsynchronousHoldemTableContext(Executor executor, HoldemTableContext holdemTableContext) {
		super(holdemTableContext);
		this.executor = executor;
	}

	@Override
	public HoldemPlayerContext sitIn(long seatId, int amount, HoldemPlayerListener holdemPlayerListener) throws IllegalActionException {
		return super.sitIn(seatId, amount, new AsynchronousHoldemPlayerListener(executor,holdemPlayerListener));
	}
	
	@Override
	public HoldemPlayerContext sitIn(int amount, HoldemPlayerListener holdemPlayerListener) throws IllegalActionException {
		return super.sitIn(amount, new AsynchronousHoldemPlayerListener(executor,holdemPlayerListener));
	}
}
