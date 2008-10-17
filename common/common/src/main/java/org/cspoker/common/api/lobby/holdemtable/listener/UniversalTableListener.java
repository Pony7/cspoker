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
package org.cspoker.common.api.lobby.holdemtable.listener;

import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.chat.event.TableChatEvents;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvents;
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
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.event.ServerEvent;
import org.cspoker.common.api.shared.listener.ServerEventListener;

public class UniversalTableListener implements ServerEventListener, HoldemPlayerListener, HoldemTableListener{

	private final ServerEventListener serverEventListener;
	private final long tableId;

	public UniversalTableListener(ServerEventListener serverEventListener, long tableId) {
		this.serverEventListener = serverEventListener;
		this.tableId = tableId;
	}
	
	public void onServerEvent(ServerEvent event) {
		serverEventListener.onServerEvent(event);
	}

	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, newPocketCardsEvent));
	}

	public void onBet(BetEvent betEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, betEvent));
	}

	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, bigBlindEvent));
	}

	public void onCall(CallEvent callEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, callEvent));
	}

	public void onCheck(CheckEvent checkEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, checkEvent));
	}

	public void onFold(FoldEvent foldEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, foldEvent));
	}

	public void onLeaveGame(LeaveTableEvent leaveGameEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, leaveGameEvent));
	}

	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, newCommunityCardsEvent));
	}

	public void onNewDeal(NewDealEvent newDealEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, newDealEvent));
	}

	public void onNewRound(NewRoundEvent newRoundEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, newRoundEvent));
	}

	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, nextPlayerEvent));
	}

	public void onRaise(RaiseEvent raiseEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, raiseEvent));
	}

	public void onShowHand(ShowHandEvent showHandEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, showHandEvent));
	}

	public void onSitIn(SitInEvent sitInEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, sitInEvent));
	}

	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, smallBlindEvent));
	}

	public void onWinner(WinnerEvent winnerEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, winnerEvent));
	}

	public void onAllIn(AllInEvent allInEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, allInEvent));
	}

	public void onJoinTable(JoinTableEvent joinTableEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, joinTableEvent));
	}

	public void onLeaveTable(LeaveTableEvent leaveGameEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, leaveGameEvent));
	}

	public void onSitOut(SitOutEvent sitOutEvent) {
		onServerEvent(new HoldemTableTreeEvents(tableId, sitOutEvent));
	}
	
}
