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
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.external.pokersource.JSONPacket;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.poker.Sit;
import org.cspoker.external.pokersource.commands.poker.SitOut;
import org.cspoker.external.pokersource.eventlisteners.all.DefaultListener;

public class PSPlayerContext implements HoldemPlayerContext {

	private final PokersourceConnection conn;
	private final int serial;
	private final int game_id;
	private ErrorListener error;
	private org.cspoker.client.pokersource.PSPlayerContext.TranslatingListener transListener;
	private HoldemPlayerListener holdemPlayerListener;

	public PSPlayerContext(PokersourceConnection conn, int serial, int game_id,
			HoldemPlayerListener holdemPlayerListener) throws RemoteException, IllegalActionException {
		this.conn = conn;
		this.serial = serial;
		this.game_id = game_id;
		this.holdemPlayerListener = holdemPlayerListener;
		
		this.error = new ErrorListener();
		this.transListener = new TranslatingListener();
		
		this.conn.addListeners(error, transListener);
		sendRemote(new SitOut(serial, game_id));
		sendRemote(new Sit(serial, game_id));
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
	public void betOrRaise(int amount) throws IllegalActionException {
	}

	@Override
	public void checkOrCall() throws IllegalActionException {
	}

	@Override
	public void fold() throws IllegalActionException {
	}

	@Override
	public void sitOut() throws IllegalActionException {
	}

	@Override
	public void startGame() throws IllegalActionException {
	}

	private class TranslatingListener extends DefaultListener{
		
	}
}
