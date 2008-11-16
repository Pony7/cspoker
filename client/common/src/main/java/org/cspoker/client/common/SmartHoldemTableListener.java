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
package org.cspoker.client.common;

import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.AllInState;
import org.cspoker.client.common.gamestate.BetState;
import org.cspoker.client.common.gamestate.BigBlindState;
import org.cspoker.client.common.gamestate.CallState;
import org.cspoker.client.common.gamestate.CheckState;
import org.cspoker.client.common.gamestate.FoldState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.NewCommunityCardsState;
import org.cspoker.client.common.gamestate.NewDealState;
import org.cspoker.client.common.gamestate.NewRoundState;
import org.cspoker.client.common.gamestate.RaiseState;
import org.cspoker.client.common.gamestate.ShowHandState;
import org.cspoker.client.common.gamestate.SitInState;
import org.cspoker.client.common.gamestate.SitOutState;
import org.cspoker.client.common.gamestate.SmallBlindState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.JoinTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveSeatEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;

@ThreadSafe
public class SmartHoldemTableListener
		extends ForwardingHoldemTableListener {
	
	private final static Logger logger = Logger.getLogger(SmartHoldemTableListener.class);
	
	private TableState tableState;
	
	public SmartHoldemTableListener(HoldemTableListener holdemTableListener, TableState tableState) {
		super(holdemTableListener);
		this.tableState = tableState;
	}
	
	public GameState getGameState() {
		return tableState.getGameState();
	}
	
	@Override
	public void onBet(BetEvent betEvent) {
		tableState.setGameState(new BetState(tableState.getGameState(),betEvent));
		super.onBet(betEvent);
	}
	
	@Override
	public void onRaise(RaiseEvent raiseEvent) {
		tableState.setGameState(new RaiseState(tableState.getGameState(),raiseEvent));
		super.onRaise(raiseEvent);
	}
	
	@Override
	public void onCheck(CheckEvent checkEvent) {
		tableState.setGameState(new CheckState(tableState.getGameState(),checkEvent));
		super.onCheck(checkEvent);
	}
	
	@Override
	public void onCall(CallEvent callEvent) {
		tableState.setGameState(new CallState(tableState.getGameState(),callEvent));
		super.onCall(callEvent);
	}
	
	@Override
	public void onAllIn(AllInEvent allInEvent) {
		tableState.setGameState(new AllInState(tableState.getGameState(),allInEvent));
		super.onAllIn(allInEvent);
	}
	
	@Override
	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		tableState.setGameState(new SmallBlindState(tableState.getGameState(),smallBlindEvent));
		super.onSmallBlind(smallBlindEvent);
	}
	
	@Override
	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		tableState.setGameState(new BigBlindState(tableState.getGameState(),bigBlindEvent));
		super.onBigBlind(bigBlindEvent);
	}
	
	@Override
	public void onFold(FoldEvent foldEvent) {
		tableState.setGameState(new FoldState(tableState.getGameState(),foldEvent));
		super.onFold(foldEvent);
	}
	
	@Override
	public void onSitOut(SitOutEvent sitOutEvent) {
		tableState.setGameState(new SitOutState(tableState.getGameState(),sitOutEvent));
		super.onSitOut(sitOutEvent);
	}
	
	@Override
	public void onSitIn(SitInEvent sitInEvent) {
		tableState.setGameState(new SitInState(tableState.getGameState(),sitInEvent));
		super.onSitIn(sitInEvent);
	}
	
	@Override
	public void onShowHand(ShowHandEvent showHandEvent) {
		tableState.setGameState(new ShowHandState(tableState.getGameState(),showHandEvent));
		super.onShowHand(showHandEvent);
	}
	
	@Override
	public void onNewRound(NewRoundEvent newRoundEvent) {
		tableState.setGameState(new NewRoundState(tableState.getTableConfiguration(), tableState.getGameState(),newRoundEvent));
		super.onNewRound(newRoundEvent);
	}
	
	@Override
	public void onNewDeal(NewDealEvent newDealEvent) {
		logger.trace(newDealEvent);
		tableState.setGameState(new NewDealState(tableState.getTableConfiguration(), newDealEvent));
		super.onNewDeal(newDealEvent);
	}
	
	@Override
	public void onJoinTable(JoinTableEvent joinTableEvent) {
		// TODO Auto-generated method stub
		super.onJoinTable(joinTableEvent);
	}
	
	@Override
	public void onLeaveSeat(LeaveSeatEvent leaveSeatEvent) {
		// TODO Auto-generated method stub
		super.onLeaveSeat(leaveSeatEvent);
	}
	
	@Override
	public void onLeaveTable(LeaveTableEvent leaveGameEvent) {
		// TODO Auto-generated method stub
		super.onLeaveTable(leaveGameEvent);
	}
	
	@Override
	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) {
		tableState.setGameState(new NewCommunityCardsState(tableState.getGameState(),newCommunityCardsEvent));
		super.onNewCommunityCards(newCommunityCardsEvent);
	}
	
	@Override
	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		// TODO Auto-generated method stub
		super.onNextPlayer(nextPlayerEvent);
	}
	
	@Override
	public void onWinner(WinnerEvent winnerEvent) {
		// TODO Auto-generated method stub
		super.onWinner(winnerEvent);
	}
}
