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

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ConfigChangeEvent;
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
import org.cspoker.common.api.shared.listener.EventListener;

public interface RemoteHoldemTableListener extends EventListener, Remote{
	
	void onAllIn(AllInEvent allInEvent) throws RemoteException;

	void onBet(BetEvent betEvent) throws RemoteException;

	void onBigBlind(BigBlindEvent bigBlindEvent) throws RemoteException;

	void onCall(CallEvent callEvent) throws RemoteException;

	void onCheck(CheckEvent checkEvent) throws RemoteException;

	void onFold(FoldEvent foldEvent) throws RemoteException;
	
	void onJoinTable(JoinTableEvent joinTableEvent) throws RemoteException;

	void onLeaveTable(LeaveTableEvent leaveTableEvent) throws RemoteException;

	void onNewCommunityCards(NewCommunityCardsEvent newCommunityCardsEvent) throws RemoteException;

	void onNewDeal(NewDealEvent newDealEvent) throws RemoteException;

	void onNextPlayer(NextPlayerEvent nextPlayerEvent) throws RemoteException;

	void onNewRound(NewRoundEvent newRoundEvent) throws RemoteException;

	void onRaise(RaiseEvent raiseEvent) throws RemoteException;

	void onShowHand(ShowHandEvent showHandEvent) throws RemoteException;

	void onSitIn(SitInEvent sitInEvent) throws RemoteException;
	
	void onSitOut(SitOutEvent sitOutEvent) throws RemoteException;

	void onSmallBlind(SmallBlindEvent smallBlindEvent) throws RemoteException;

	void onWinner(WinnerEvent winnerEvent) throws RemoteException;
	
	void onConfigChange(ConfigChangeEvent configChangeEvent) throws RemoteException;
	
}
