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
package org.cspoker.server.rmi.asynchronous.context;

import java.util.concurrent.Executor;

import org.cspoker.common.api.lobby.holdemtable.context.ForwardingHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.server.rmi.asynchronous.listener.AsynchronousHoldemTableListener;

public class AsynchronousHoldemTableContext extends ForwardingHoldemTableContext {

	protected Executor executor;
	protected final AsynchronousHoldemPlayerContext holdemPlayerContext;
	
	public AsynchronousHoldemTableContext(Executor executor, HoldemTableContext holdemTableContext) {
		super(holdemTableContext);
		this.executor = executor;
		this.holdemPlayerContext = new AsynchronousHoldemPlayerContext(executor, holdemTableContext.getHoldemPlayerContext());
	}
	
	@Override
	protected HoldemTableListener wrapListener(HoldemTableListener listener) {
		return new AsynchronousHoldemTableListener(executor, listener);
	}
	
	@Override
	public HoldemPlayerContext getHoldemPlayerContext() {
		return holdemPlayerContext;
	}
}
