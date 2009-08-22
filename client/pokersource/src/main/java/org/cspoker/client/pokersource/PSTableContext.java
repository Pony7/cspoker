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

import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;

import net.sf.json.JSONException;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.poker.Call;
import org.cspoker.external.pokersource.commands.poker.TablePicker;
import org.cspoker.external.pokersource.commands.poker.TableQuit;
import org.cspoker.external.pokersource.eventlisteners.all.DefaultListener;
import org.cspoker.external.pokersource.events.poker.Table;

public class PSTableContext implements RemoteHoldemTableContext {

	private final static Logger logger = Logger.getLogger(PSTableContext.class);
	
	private final PokersourceConnection conn;
	private final int serial;
	private final HoldemTableListener tableListener;
	private final TranslatingListener transListener;

	public PSTableContext(PokersourceConnection conn, int serial, HoldemTableListener holdemTableListener) throws IllegalActionException, RemoteException {
		this.conn = conn;
		this.serial = serial;
		this.tableListener = holdemTableListener;
		this.transListener = new TranslatingListener();
		
		this.conn.addListeners(transListener);
		try {
			conn.sendRemote(new TablePicker(serial, true));
			game_idObtained.await();
		} catch (JSONException e) {
			throw new IllegalActionException(e.getMessage());
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void leaveTable() throws RemoteException {
		conn.stopPolling(game_id);
		conn.removeListeners(transListener);
		conn.sendRemote(new TableQuit(serial,game_id));
	}

	@Override
	public HoldemPlayerContext sitIn(SeatId seatId, int buyIn,
			HoldemPlayerListener holdemPlayerListener)
			throws IllegalActionException {
		throw new UnsupportedOperationException();
	}

	@Override
	public RemoteHoldemPlayerContext sitIn(int buyIn,
			HoldemPlayerListener holdemPlayerListener)
			throws IllegalActionException, RemoteException {
		return new PSPlayerContext(conn, serial, this, holdemPlayerListener);
	}
	
	volatile int game_id;
	
	volatile int potsize;
	
	private final CountDownLatch game_idObtained = new CountDownLatch(1);
	
	private class TranslatingListener extends DefaultListener{
		
		@Override
		public void onTable(Table table) {
			game_id = table.getId();
			game_idObtained.countDown();
			conn.startPolling(game_id);
		}
		
		@Override
		public void onCall(Call call) {
			PlayerId id = new PlayerId(call.getSerial());
//			tableListener.onCall(new CallEvent(id, ));
		}
		
	}

}
