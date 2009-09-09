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

import org.apache.log4j.Logger;
import org.cspoker.client.common.GameStateContainer;
import org.cspoker.client.common.SmartHoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.ForwardingHoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.poker.BuyIn;
import org.cspoker.external.pokersource.commands.poker.Call;
import org.cspoker.external.pokersource.commands.poker.Check;
import org.cspoker.external.pokersource.commands.poker.Fold;
import org.cspoker.external.pokersource.commands.poker.Raise;
import org.cspoker.external.pokersource.commands.poker.Sit;
import org.cspoker.external.pokersource.eventlisteners.all.DefaultListener;
import org.cspoker.external.pokersource.events.poker.BetLimit;
import org.cspoker.external.pokersource.events.poker.PlayerCards;

import com.google.common.collect.ImmutableList;

public class PSPlayerContext implements RemoteHoldemPlayerContext {
	
	private final static Logger logger = Logger.getLogger(PSPlayerContext.class);

	private final PokersourceConnection conn;
	private final int serial;
	private final int game_id;
	private final org.cspoker.client.pokersource.PSPlayerContext.TranslatingListener transListener;
	private final SmartHoldemPlayerListener smartListener;
	private final GameStateContainer gameStateContainer;

	
	public PSPlayerContext(PokersourceConnection conn, int serial, PSTableContext tableContext,
			HoldemPlayerListener holdemPlayerListener, GameStateContainer gameStateContainer, int buyIn) throws RemoteException {
		this.conn = conn;
		this.serial = serial;
		this.game_id = tableContext.game_id;
		this.smartListener = new SmartHoldemPlayerListener(new ForwardingHoldemPlayerListener(ImmutableList.of(new HoldemPlayerListener() {
			
			@Override
			public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {
				logger.info(newPocketCardsEvent);
			}
			
		},holdemPlayerListener)), gameStateContainer, new PlayerId(serial));
		this.transListener = new TranslatingListener();
		this.gameStateContainer = gameStateContainer;
		this.conn.addListeners(transListener);
		if(tableContext.pastInitialBuyIn){
			conn.sendRemote(new BuyIn(serial, game_id, buyIn));
		}else{
			logger.warn("Ignoring buyin because tablepicker already bought in for us.");
			tableContext.pastInitialBuyIn = true;
		}
		
		conn.sendRemote(new Sit(serial, game_id));
	}

	@Override
	public void betOrRaise(int amount) throws IllegalActionException, RemoteException {
		try {
			int callValue = gameStateContainer.getGameState().getCallValue(new PlayerId(serial));
			conn.sendRemote(new Raise(serial, game_id, callValue+amount));
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

	void signalSitOut() {
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
			//don't consider card 255 which is a hidden card
			if(playerCards.getSerial() == serial 
					&& gameStateContainer.getGameState().getPlayer(new PlayerId(serial)).getCards().size()==0){
				Card card0 = Card.fromPokersourceInt(playerCards.getCards()[0]);
				Card card1 = Card.fromPokersourceInt(playerCards.getCards()[1]);
				smartListener.onNewPocketCards(new NewPocketCardsEvent(EnumSet.of(card0, card1)));
			}
		}

	}
}
