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
package org.cspoker.client.xml.common.context;

import java.rmi.RemoteException;

import net.jcip.annotations.ThreadSafe;

import org.cspoker.client.xml.common.IDGenerator;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.action.BetOrRaiseAction;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.action.CheckOrCallAction;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.action.FoldAction;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.action.LeaveSeatAction;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.action.SitInAction;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.action.SitOutAction;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.action.StartGameAction;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.Trigger;
import org.cspoker.common.api.shared.action.ActionPerformer;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.TableId;

@ThreadSafe
public class XmlRemoteHoldemPlayerContext implements RemoteHoldemPlayerContext {

	private final ActionPerformer performer;
	private final IDGenerator generator;
	private final TableId tableID;
	private final Trigger stalePlayerContextTrigger;

	public XmlRemoteHoldemPlayerContext(ActionPerformer performer, IDGenerator generator, TableId tableID, Trigger stalePlayerContextTrigger) {
		this.performer = performer;
		this.generator = generator;
		this.tableID = tableID;
		this.stalePlayerContextTrigger = stalePlayerContextTrigger;
	}

	public void betOrRaise(int amount) throws RemoteException,
	IllegalActionException {
		performer.perform(new BetOrRaiseAction(generator.getNextID(),tableID,amount));
	}

	public void checkOrCall() throws RemoteException, IllegalActionException {
		performer.perform(new CheckOrCallAction(generator.getNextID(),tableID));
	}

	public void fold() throws RemoteException, IllegalActionException {
		performer.perform(new FoldAction(generator.getNextID(),tableID));
	}

	public void sitOut() throws RemoteException, IllegalActionException {
		performer.perform(new SitOutAction(generator.getNextID(),
				tableID));
	}

	@Override
	public void leaveSeat() throws RemoteException, IllegalActionException {
		performer.perform(new LeaveSeatAction(generator.getNextID(),
				tableID));
		//TODO synchronize?
		stalePlayerContextTrigger.trigger();
	}

	@Override
	public void sitIn() throws RemoteException, IllegalActionException {
		performer.perform(new SitInAction(generator.getNextID(),
				tableID));
		
	}

	@Override
	public void startGame() throws RemoteException, IllegalActionException {
		performer.perform(new StartGameAction(generator.getNextID(), tableID));
	}

}
