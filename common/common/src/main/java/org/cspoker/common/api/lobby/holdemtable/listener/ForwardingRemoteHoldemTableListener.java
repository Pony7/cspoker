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

import java.rmi.RemoteException;
import java.rmi.server.Unreferenced;
import java.util.List;

import org.apache.log4j.Logger;
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
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class ForwardingRemoteHoldemTableListener extends ForwardingListener<RemoteHoldemTableListener> implements RemoteHoldemTableListener, Unreferenced {

	private final static Logger logger = Logger.getLogger(ForwardingRemoteHoldemTableListener.class);
	
	public ForwardingRemoteHoldemTableListener() {
		super();
	}

	public ForwardingRemoteHoldemTableListener(List<RemoteHoldemTableListener> listeners) {
		super(listeners);
	}
	
	public ForwardingRemoteHoldemTableListener(RemoteHoldemTableListener listener) {
		super(listener);
	}

	public void onBet(BetEvent betEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onBet(betEvent);
		}
	}

	public void onBigBlind(BigBlindEvent bigBlindEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onBigBlind(bigBlindEvent);
		}
	}

	public void onCall(CallEvent callEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onCall(callEvent);
		}
	}

	public void onCheck(CheckEvent checkEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onCheck(checkEvent);
		}
	}

	public void onFold(FoldEvent foldEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onFold(foldEvent);
		}
	}

	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onNewCommunityCards(newCommunityCardsEvent);
		}
	}

	public void onNewDeal(NewDealEvent newDealEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onNewDeal(newDealEvent);
		}
	}

	public void onNewRound(NewRoundEvent newRoundEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onNewRound(newRoundEvent);
		}
	}

	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onNextPlayer(nextPlayerEvent);
		}
	}

	public void onRaise(RaiseEvent raiseEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onRaise(raiseEvent);
		}
	}

	public void onShowHand(ShowHandEvent showHandEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onShowHand(showHandEvent);
		}
	}

	public void onSitIn(SitInEvent sitInEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onSitIn(sitInEvent);
		}
	}

	public void onSmallBlind(SmallBlindEvent smallBlindEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onSmallBlind(smallBlindEvent);
		}
	}

	public void onWinner(WinnerEvent winnerEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onWinner(winnerEvent);
		}
	}

	public void onAllIn(AllInEvent allInEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onAllIn(allInEvent);
		}
	}

	public void onJoinTable(JoinTableEvent joinTableEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onJoinTable(joinTableEvent);
		}
	}

	public void onLeaveTable(LeaveTableEvent leaveGameEvent) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onLeaveTable(leaveGameEvent);
		}
	}

	public void onSitOut(SitOutEvent sitOut) throws RemoteException {
		for(RemoteHoldemTableListener listener:listeners){
			listener.onSitOut(sitOut);
		}
	}
	
	
	@Override
	protected void finalize() throws Throwable {
		try {
			logger.debug("Garbage collecting old listener: "+this);
		} finally{
			super.finalize();
		}
	}

	public void unreferenced() {
		logger.debug("No more server referencing: "+this);
	}
	
}
