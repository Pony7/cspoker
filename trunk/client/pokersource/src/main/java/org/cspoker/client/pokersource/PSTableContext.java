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

import java.nio.channels.IllegalSelectorException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.json.JSONException;

import org.apache.log4j.Logger;
import org.cspoker.client.common.GameStateContainer;
import org.cspoker.client.common.SmartHoldemTableListener;
import org.cspoker.client.common.gamestate.DetailedHoldemTableState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ConfigChangeEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.JoinTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.player.ShowdownPlayer;
import org.cspoker.common.elements.player.Winner;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.poker.Call;
import org.cspoker.external.pokersource.commands.poker.Check;
import org.cspoker.external.pokersource.commands.poker.Fold;
import org.cspoker.external.pokersource.commands.poker.Raise;
import org.cspoker.external.pokersource.commands.poker.Sit;
import org.cspoker.external.pokersource.commands.poker.SitOut;
import org.cspoker.external.pokersource.commands.poker.TablePicker;
import org.cspoker.external.pokersource.commands.poker.TableQuit;
import org.cspoker.external.pokersource.eventlisteners.all.DefaultListener;
import org.cspoker.external.pokersource.events.poker.BeginRound;
import org.cspoker.external.pokersource.events.poker.Blind;
import org.cspoker.external.pokersource.events.poker.BoardCards;
import org.cspoker.external.pokersource.events.poker.ChipsPotReset;
import org.cspoker.external.pokersource.events.poker.Dealer;
import org.cspoker.external.pokersource.events.poker.EndRound;
import org.cspoker.external.pokersource.events.poker.EndRoundLast;
import org.cspoker.external.pokersource.events.poker.Id;
import org.cspoker.external.pokersource.events.poker.InGame;
import org.cspoker.external.pokersource.events.poker.PlayerArrive;
import org.cspoker.external.pokersource.events.poker.PlayerCards;
import org.cspoker.external.pokersource.events.poker.PlayerChips;
import org.cspoker.external.pokersource.events.poker.PlayerLeave;
import org.cspoker.external.pokersource.events.poker.Position;
import org.cspoker.external.pokersource.events.poker.Table;
import org.cspoker.external.pokersource.events.poker.Win;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class PSTableContext implements RemoteHoldemTableContext {

	private final static Logger logger = Logger.getLogger(PSTableContext.class);

	private final PokersourceConnection conn;
	private final int serial;
	private final HoldemTableListener holdemTableListener;
	private final TranslatingListener transListener;

	final TableId tableId;

	private PSPlayerContext psPlayerContext = null;

	private final PSLobbyContext psLobbyContext;

	public PSTableContext(PokersourceConnection conn, int serial, TableId tableId, HoldemTableListener holdemTableListener, PSLobbyContext psLobbyContext) throws IllegalActionException, RemoteException {
		this.conn = conn;
		this.serial = serial;
		this.tableId=tableId;
		this.transListener = new TranslatingListener();
		this.psLobbyContext = psLobbyContext;
		this.holdemTableListener = new ForwardingHoldemTableListener(ImmutableList.of(holdemTableListener, new HoldemTableListener() {

			@Override
			public void onWinner(WinnerEvent winnerEvent) {
				logger.info(winnerEvent);
			}

			@Override
			public void onSitOut(SitOutEvent sitOutEvent) {
				logger.info(sitOutEvent);
			}

			@Override
			public void onSitIn(SitInEvent sitInEvent) {
				logger.info(sitInEvent);
			}

			@Override
			public void onShowHand(ShowHandEvent showHandEvent) {
				logger.info(showHandEvent);
			}

			@Override
			public void onRaise(RaiseEvent raiseEvent) {
				logger.info(raiseEvent);
			}

			@Override
			public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
				logger.info(nextPlayerEvent);
			}

			@Override
			public void onNewRound(NewRoundEvent newRoundEvent) {
				logger.info(newRoundEvent);
			}

			@Override
			public void onNewDeal(NewDealEvent newDealEvent) {
				logger.info(newDealEvent);
			}

			@Override
			public void onNewCommunityCards(
					NewCommunityCardsEvent newCommunityCardsEvent) {
				logger.info(newCommunityCardsEvent);
			}

			@Override
			public void onLeaveTable(LeaveTableEvent leaveTableEvent) {
				logger.info(leaveTableEvent);
			}

			@Override
			public void onJoinTable(JoinTableEvent joinTableEvent) {
				logger.info(joinTableEvent);
			}

			@Override
			public void onFold(FoldEvent foldEvent) {
				logger.info(foldEvent);
			}

			@Override
			public void onConfigChange(ConfigChangeEvent configChangeEvent) {
				logger.info(configChangeEvent);
			}

			@Override
			public void onCheck(CheckEvent checkEvent) {
				logger.info(checkEvent);
			}

			@Override
			public void onCall(CallEvent callEvent) {
				logger.info(callEvent);
			}

			@Override
			public void onBlind(BlindEvent blindEvent) {
				logger.info(blindEvent);
			}

			@Override
			public void onBet(BetEvent betEvent) {
				logger.info(betEvent);
			}

			@Override
			public void onAllIn(AllInEvent allInEvent) {
				logger.info(allInEvent);
			}
		}));
		this.conn.addListeners(transListener);
		try {
			conn.sendRemote(new TablePicker(serial, true));
			tableInfoObtained.await();
			conn.sendRemote(new SitOut(serial, game_id));
		} catch (JSONException e) {
			throw new IllegalActionException(e.getMessage());
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	private volatile boolean isPastFirstFakeSitOut = false;

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
		if(psPlayerContext!=null) throw new IllegalActionException("Can't sit in twice");
		psPlayerContext = new PSPlayerContext(conn, serial, this, holdemPlayerListener, gameState, buyIn);
		return psPlayerContext;
	}

	volatile int game_id;
	volatile int potsize;
	final CountDownLatch tableInfoObtained = new CountDownLatch(1);

	volatile TableConfiguration config;
	volatile GameStateContainer gameState;
	private SmartHoldemTableListener smartListener;

	volatile boolean pastInitialBuyIn = false;

	private class TranslatingListener extends DefaultListener{

		private static final double default_rake = 0.05;

		@Override
		public void onTable(Table table) {
			game_id = table.getId();
			conn.startPolling(game_id);
			String bettingStruct = table.getBetting_structure();
			changeConfig(new TableConfiguration(100*Integer.parseInt(bettingStruct.split("-")[1]),0,default_rake));
			gameState = new GameStateContainer(new DetailedHoldemTableState(new DetailedHoldemTable(tableId, config)));
			smartListener = new SmartHoldemTableListener(holdemTableListener, gameState);
			tableInfoObtained.countDown();
			dispatch(new ConfigChangeEvent(config));
		}

		private void changeConfig(TableConfiguration tableConfiguration) {
			config = tableConfiguration;
			psLobbyContext.config = config;
		}

		@Override
		public void onCall(Call call) {
			PlayerId id = getId(call);
			int movedAmount = getGameState().getCallValue(id);
			if(movedAmount == getGameState().getPlayer(id).getStack())
				dispatch(new AllInEvent(id, movedAmount));
			else dispatch(new CallEvent(id, movedAmount));
		}

		@Override
		public void onCheck(Check check) {
			dispatch(new CheckEvent(getId(check)));
		}

		@Override
		public void onFold(Fold fold) {
			dispatch(new FoldEvent(getId(fold)));
		}

		@Override
		public void onRaise(Raise raise) {
			//can also be bet!
			PlayerId id = getId(raise);
			int movedAmount = raise.getAmount();
			int callValue = getGameState().getCallValue(id);
			if(movedAmount == getGameState().getPlayer(id).getStack())
				dispatch(new AllInEvent(id, movedAmount));
			else if(callValue == 0) 
				dispatch(new BetEvent(id, movedAmount));
			else
				dispatch(new RaiseEvent(id, movedAmount-callValue, movedAmount));
		}

		@Override
		public void onBlind(Blind blind) {
			PlayerId id = getId(blind);
			int amount = blind.getAmount();
//			if(getGameState().get)
			if(amount == getGameState().getPlayer(id).getStack())
				dispatch(new AllInEvent(id, amount));
			else if(amount==config.getSmallBlind() || amount==config.getBigBlind())
				dispatch(new BlindEvent(id, amount));
			else throw new IllegalStateException("Unknown blind amount: "+amount+" (sb="+config.getSmallBet()+")");
		}

		private List<SeatedPlayer> arrivedPlayers = Collections.synchronizedList(new ArrayList<SeatedPlayer>());

		@Override
		public void onPlayerArrive(PlayerArrive a) {
			SeatedPlayer player = new SeatedPlayer(
					getId(a),
					new SeatId(a.getSeat()),
					a.getName(),
					0,0,false,false);
			arrivedPlayers.add(player);
		}

		@Override
		public void onPlayerLeave(PlayerLeave playerLeave) {
			Iterator<SeatedPlayer> iter = arrivedPlayers.iterator();
			while(iter.hasNext()){
				if(iter.next().getId().getId() == playerLeave.getSerial()) {
					iter.remove();
					return;
				}
			}
			throw new IllegalStateException();
		}

		@Override
		public void onPlayerChips(PlayerChips playerChips) {
			//update sitting players
			PlayerId id = getId(playerChips);
			PlayerState player = getGameState().getPlayer(id);
			if(player!= null && inWinnerZone.get()){
				int prevStack = player.getStack();
				if(prevStack != playerChips.getMoney()){
					dispatch(new WinnerEvent(ImmutableSet.of(new Winner(id,playerChips.getMoney()-prevStack))));
				}
			}
		
			//refresh state
			player = getGameState().getPlayer(id);
			//sanity checks
			if(player!=null){
				if(player.getStack()!=playerChips.getMoney()){
					String msg = "Stack of "+id+" is "+player.getStack()+" but should be "+playerChips.getMoney();
					throw new IllegalStateException(msg);
				}
				if(!endLastRound.get() && player.getBet()!=playerChips.getBet()){
					String msg = "Bet of "+id+" is "+player.getBet()+" but should be "+playerChips.getBet();
					throw new IllegalStateException(msg);
				}
			}
			
			///update arrivedPlayers
			ListIterator<SeatedPlayer> iter = arrivedPlayers.listIterator();
			while(iter.hasNext()){
				SeatedPlayer prevPlayer = iter.next();
				if(prevPlayer.getId().equals(id)){
					iter.remove();
					iter.add(new SeatedPlayer(prevPlayer, playerChips.getMoney(), playerChips.getBet()));
					return;
				}
			}
			throw new IllegalStateException();
		}

		@Override
		public void onSit(Sit sit) {
			PlayerId id = getId(sit);
			if(getGameState().getSeatMap().containsValue(id)){
				logger.warn("Ignoring superfluous "+sit+" because player is already sitting in.");
				return;
			}
			Iterator<SeatedPlayer> iter = arrivedPlayers.iterator();
			while(iter.hasNext()){
				SeatedPlayer sitter = iter.next();
				if(sitter.getId().equals(id)) {
					// set the sitting in flag to true
					dispatch(new SitInEvent(new SeatedPlayer(sitter, true)));
					return;
				}
			}
			throw new IllegalStateException();
		}

		@Override
		public void onSitOut(SitOut sitOut) {
			if(isPastFirstFakeSitOut || sitOut.getSerial()!=serial){
				if(sitOut.getSerial() == serial && psPlayerContext!=null){
					psPlayerContext.signalSitOut();
					psPlayerContext = null;
				}
				dispatch(new SitOutEvent(new PlayerId(sitOut.getSerial())));
			}else{
				if(sitOut.getSerial()==serial){
					logger.info("Ignoring first fake sitout.");
					isPastFirstFakeSitOut = true;
				}
			}
		}

		@Override
		public void onBoardCards(BoardCards boardCards) {
			EnumSet<Card> cards = EnumSet.noneOf(Card.class);
			for(int c:boardCards.getCards()){
				cards.add(Card.fromPokersourceInt(c));
			}
			//BoardCards has up to all 5 cards.
			cards.removeAll(getGameState().getCommunityCards());
			if(cards.size()>0)
				dispatch(new NewCommunityCardsEvent(cards));
		}

		private volatile int[] dealtTo = new int[]{};

		@Override
		public void onInGame(InGame inGame) {
			dealtTo = inGame.getPlayers();
		}

		@Override
		public void onDealer(Dealer dealer) {
			List<SeatedPlayer> players = new ArrayList<SeatedPlayer>();
			PlayerId dealerId = null;
			//only send players that actually sit in AND are ingame in the new deal event.
			for(int i:dealtTo){
				PlayerState p = getGameState().getPlayer(new PlayerId(i));
				players.add(new SeatedPlayer(p.getPlayerId(),p.getSeatId(),p.getName(),p.getStack(), p.getBet(), true, false));
				if(p.getSeatId().getId() == dealer.getDealer()){
					dealerId = p.getPlayerId();
				}
			}
			if(dealerId == null) throw new IllegalSelectorException();
			blindsDone.set(false);
			endLastRound.set(false);
			dispatch(new NewDealEvent(players, dealerId));
			dispatch(new NewRoundEvent(Round.PREFLOP, new Pots(0)));
		}

		final AtomicBoolean blindsDone = new AtomicBoolean(false);

		@Override
		public void onPosition(Position position) {
			if(blindsDone.get() && position.getSerial()!=0){
				//onPosition is also fired for the blinds!
				dispatch(new NextPlayerEvent(new PlayerId(position.getSerial())));
			}
		}

		@Override
		public void onBeginRound(BeginRound beginRound) {
			if(!blindsDone.get()){
				//this is the start of the preflop round that has already started according to cspoker conventions
				blindsDone.set(true);
			}
		}

		@Override
		public void onEndRound(EndRound endRound) {
			dispatch(new NewRoundEvent(getGameState().getRound().getNextRound(),new Pots(getGameState().getGamePotSize())));
		}

		final AtomicBoolean endLastRound = new AtomicBoolean(false);

		@Override
		public void onEndRoundLast(EndRoundLast endRoundLast) {
			endLastRound.set(true);
		}

		@Override
		public void onPlayerCards(PlayerCards playerCards) {
			EnumSet<Card> cards = EnumSet.noneOf(Card.class);
			for(int c:playerCards.getCards()){
				if(c==255 || playerCards.getSerial()==serial) {
					//we don't care for hidden cards.
					return;
				}
				cards.add(Card.fromPokersourceInt(c));
			}
			dispatch(new ShowHandEvent(new ShowdownPlayer(new PlayerId(playerCards.getSerial()), cards, "(Unknown)")));
		}

		private AtomicBoolean inWinnerZone = new AtomicBoolean(false);

		@Override
		public void onWin(Win win) {
			inWinnerZone.set(true);
		}

		@Override
		public void onChipsPotReset(ChipsPotReset chipsPotReset) {
			inWinnerZone.set(false);
		}

		private PlayerId getId(Id id) {
			return new PlayerId(id.getSerial());
		}

	}

	private void dispatch(HoldemTableEvent event) {
		event.dispatch(smartListener);
	}

	private GameState getGameState(){
		return gameState.getGameState();
	}

}
