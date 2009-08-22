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
import java.util.EnumSet;

import net.sf.json.JSONException;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.poker.Call;
import org.cspoker.external.pokersource.commands.poker.Check;
import org.cspoker.external.pokersource.commands.poker.Fold;
import org.cspoker.external.pokersource.commands.poker.Raise;
import org.cspoker.external.pokersource.commands.poker.Sit;
import org.cspoker.external.pokersource.commands.poker.SitOut;
import org.cspoker.external.pokersource.eventlisteners.all.DefaultListener;
import org.cspoker.external.pokersource.events.poker.BetLimit;
import org.cspoker.external.pokersource.events.poker.PlayerCards;

public class PSPlayerContext implements RemoteHoldemPlayerContext {

	private final PokersourceConnection conn;
	private final int serial;
	private final int game_id;
	private final org.cspoker.client.pokersource.PSPlayerContext.TranslatingListener transListener;
	private final HoldemPlayerListener holdemPlayerListener;
	private final PSTableContext tableContext;

	public PSPlayerContext(PokersourceConnection conn, int serial, PSTableContext tableContext,
			HoldemPlayerListener holdemPlayerListener) throws RemoteException, IllegalActionException {
		this.conn = conn;
		this.serial = serial;
		this.game_id = tableContext.game_id;
		this.tableContext = tableContext;
		this.holdemPlayerListener = holdemPlayerListener;
		this.transListener = new TranslatingListener();
		
		this.conn.addListeners(transListener);
		//TODO fix dirty hack here after pokersource bugfix
		conn.sendRemote(new SitOut(serial, game_id));
		conn.sendRemote(new Sit(serial, game_id));
	}

	@Override
	public void betOrRaise(int amount) throws IllegalActionException, RemoteException {
		try {
			conn.sendRemote(new Raise(serial, game_id, amount));
		} catch (JSONException e) {
			throw new IllegalActionException(e);
		}
	}

	@Override
	public void checkOrCall() throws IllegalActionException, RemoteException {
		try {
			if(betLimitCall==0) conn.sendRemote(new Check(serial, game_id));
			else conn.sendRemote(new Call(serial, game_id));
		} catch (JSONException e) {
			throw new IllegalActionException(e);
		}
	}

	@Override
	public void fold() throws IllegalActionException, RemoteException {
		try {
			conn.sendRemote(new Fold(serial, game_id));
		} catch (JSONException e) {
			throw new IllegalActionException(e);
		}
	}

	void signalLeaveTable() {
		conn.removeListeners(transListener);
	}

	@Override
	public void stopPlaying() throws RemoteException, IllegalActionException {
		this.conn.removeListeners(transListener);
	}

	@Override
	public void reSitIn() throws RemoteException, IllegalActionException {
	}

	@Override
	public void startGame() throws IllegalActionException {
		// no op
	}

	@Override
	public void sitOut() throws RemoteException, IllegalActionException {
	}
	
	private volatile int betLimitCall = 0;

	private class TranslatingListener extends DefaultListener{
		
		@Override
		public void onBetLimit(BetLimit betLimit) {
			betLimitCall = betLimit.getCall();
		}
		
		@Override
		public void onPlayerCards(PlayerCards playerCards) {
			Card card0 = Card.fromPokersourceInt(playerCards.getCards()[0]);
			Card card1 = Card.fromPokersourceInt(playerCards.getCards()[1]);
			holdemPlayerListener.onNewPocketCards(new NewPocketCardsEvent(EnumSet.of(card0, card1)));
		}
		
	}
}
