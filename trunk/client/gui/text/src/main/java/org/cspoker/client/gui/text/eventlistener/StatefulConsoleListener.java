/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.client.gui.text.eventlistener;

import java.rmi.RemoteException;

import org.cspoker.client.gui.text.Console;
import org.cspoker.client.gui.text.savedstate.Cards;
import org.cspoker.client.gui.text.savedstate.Pot;
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

public class StatefulConsoleListener extends ConsoleListener {

	private Cards cards;
	private Pot pot;

	public StatefulConsoleListener(Console console, Cards cards, Pot pot) {
		super(console);
		this.cards = cards;
		this.pot = pot;
	}

	public void onTableCreated(TableCreatedEvent event)
	throws RemoteException {
		onEvent(event);
	}

	public void onTableRemoved(TableRemovedEvent event)
	throws RemoteException {
		onEvent(event);
	}

	public void onNewPocketCards(NewPocketCardsEvent event)
	throws RemoteException {
		onEvent(event);
	}

	public void onAllIn(AllInEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onBet(BetEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onBigBlind(BigBlindEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onCall(CallEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onCheck(CheckEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onFold(FoldEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onJoinTable(JoinTableEvent event)
	throws RemoteException {
		onEvent(event);
	}

	public void onLeaveTable(LeaveTableEvent event)
	throws RemoteException {
		onEvent(event);
	}

	public void onNewCommunityCards(
			NewCommunityCardsEvent event)
	throws RemoteException {
		onEvent(event);
	}

	public void onNewDeal(NewDealEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onNewRound(NewRoundEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onNextPlayer(NextPlayerEvent event)
	throws RemoteException {
		onEvent(event);
	}

	public void onRaise(RaiseEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onShowHand(ShowHandEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onSitIn(SitInEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onSitOut(SitOutEvent event) throws RemoteException {
		onEvent(event);
	}

	public void onSmallBlind(SmallBlindEvent event)
	throws RemoteException {
		onEvent(event);
	}

	public void onWinner(WinnerEvent event) throws RemoteException {
		onEvent(event);
	}

}
