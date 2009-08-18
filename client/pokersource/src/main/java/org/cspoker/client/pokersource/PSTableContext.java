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
package org.cspoker.client.pokersource;

import java.io.IOException;
import java.rmi.RemoteException;

import org.cspoker.client.pokersource.listeners.ErrorListener;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.external.pokersource.JSONPacket;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.poker.TablePicker;
import org.cspoker.external.pokersource.commands.poker.TableQuit;
import org.cspoker.external.pokersource.eventlisteners.all.DefaultListener;
import org.cspoker.external.pokersource.events.poker.Table;

public class PSTableContext implements RemoteHoldemTableContext {

	private final PokersourceConnection conn;
	private final int serial;
	private final HoldemTableListener holdemTableListener;
	private final ErrorListener error;
	private final TranslatingListener transListener;

	public PSTableContext(PokersourceConnection conn, int serial, HoldemTableListener holdemTableListener) throws IllegalActionException, RemoteException {
		this.conn = conn;
		this.serial = serial;
		this.holdemTableListener = holdemTableListener;
		this.error = new ErrorListener();
		this.transListener = new TranslatingListener();
		
		this.conn.addListeners(error, transListener);
		sendRemote(new TablePicker(serial, true));
		if(error.isError()) throw new IllegalActionException(error.getMsg());
		error.reset();
	}

	private void sendRemote(JSONPacket packet) throws RemoteException {
		try {
			this.conn.send(packet);
		} catch (IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public void leaveTable() throws RemoteException {
		conn.removeListeners(error,transListener);
		sendRemote(new TableQuit(serial,game_id));
	}

	@Override
	public HoldemPlayerContext sitIn(SeatId seatId, int buyIn,
			HoldemPlayerListener holdemPlayerListener)
			throws IllegalActionException {
		throw new UnsupportedOperationException();
	}

	@Override
	public HoldemPlayerContext sitIn(int buyIn,
			HoldemPlayerListener holdemPlayerListener)
			throws IllegalActionException, RemoteException {
		return new PSPlayerContext(conn, serial, game_id, holdemPlayerListener);
	}
	
	private int game_id;
	
	private class TranslatingListener extends DefaultListener{
		@Override
		public void onTable(Table table) {
			game_id = table.getId();
		}
	}
	

}
