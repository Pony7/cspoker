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
package org.cspoker.client.gui.text.eventlistener;

import org.cspoker.client.gui.text.Console;
import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
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
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.listener.LobbyListener;
import org.cspoker.common.api.shared.event.Event;

public class ConsoleListener implements ChatListener,LobbyListener
	,HoldemPlayerListener,HoldemTableListener {

	private Console console;

	public ConsoleListener(Console console) {
		this.console = console;
	}

	public void onEvent(Event event){
		console.print(event.toString());
	}

	public void onTableCreated(TableCreatedEvent event) {
		onEvent(event);
	}

	public void onTableRemoved(TableRemovedEvent event) {
		onEvent(event);
	}

	public void onNewPocketCards(NewPocketCardsEvent event) {
		onEvent(event);	}

	public void onAllIn(AllInEvent event) {
		onEvent(event);
	}

	public void onBet(BetEvent event) {
		onEvent(event);
	}

	public void onBigBlind(BigBlindEvent event) {
		onEvent(event);
	}

	public void onCall(CallEvent event) {
		onEvent(event);
	}

	public void onCheck(CheckEvent event) {
		onEvent(event);
	}

	public void onFold(FoldEvent event) {
		onEvent(event);
	}

	public void onJoinTable(JoinTableEvent event) {
		onEvent(event);
	}

	public void onLeaveTable(LeaveTableEvent event) {
		onEvent(event);
	}

	public void onNewCommunityCards(
			NewCommunityCardsEvent event) {
		onEvent(event);
	}

	public void onNewDeal(NewDealEvent event) {
		onEvent(event);
	}

	public void onNewRound(NewRoundEvent event) {
		onEvent(event);
	}

	public void onNextPlayer(NextPlayerEvent event) {
		onEvent(event);
	}

	public void onRaise(RaiseEvent event) {
		onEvent(event);
	}

	public void onShowHand(ShowHandEvent event) {
		onEvent(event);
	}

	public void onSitIn(SitInEvent event) {
		onEvent(event);
	}

	public void onSitOut(SitOutEvent event) {
		onEvent(event);
	}

	public void onSmallBlind(SmallBlindEvent event) {
		onEvent(event);
	}

	public void onWinner(WinnerEvent event) {
		onEvent(event);
	}
}
