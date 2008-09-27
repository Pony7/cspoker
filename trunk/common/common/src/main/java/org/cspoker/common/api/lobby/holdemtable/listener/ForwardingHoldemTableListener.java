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

import java.util.List;

import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveGameEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class ForwardingHoldemTableListener extends ForwardingListener<HoldemTableListener> implements HoldemTableListener {

	public ForwardingHoldemTableListener() {
		super();
	}
	
	public ForwardingHoldemTableListener(List<HoldemTableListener> listeners) {
		super(listeners);
	}

	public void onBet(BetEvent betEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onBet(betEvent);
		}
	}

	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onBigBlind(bigBlindEvent);
		}
	}

	public void onCall(CallEvent callEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onCall(callEvent);
		}
	}

	public void onCheck(CheckEvent checkEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onCheck(checkEvent);
		}
	}

	public void onFold(FoldEvent foldEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onFold(foldEvent);
		}
	}

	public void onLeaveGame(LeaveGameEvent leaveGameEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onLeaveGame(leaveGameEvent);
		}
	}

	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onNewCommunityCards(newCommunityCardsEvent);
		}
	}

	public void onNewDeal(NewDealEvent newDealEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onNewDeal(newDealEvent);
		}
	}

	public void onNewRound(NewRoundEvent newRoundEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onNewRound(newRoundEvent);
		}
	}

	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onNextPlayer(nextPlayerEvent);
		}
	}

	public void onRaise(RaiseEvent raiseEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onRaise(raiseEvent);
		}
	}

	public void onShowHand(ShowHandEvent showHandEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onShowHand(showHandEvent);
		}
	}

	public void onSitIn(SitInEvent sitInEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onSitIn(sitInEvent);
		}
	}

	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onSmallBlind(smallBlindEvent);
		}
	}

	public void onWinner(WinnerEvent winnerEvent) {
		for(HoldemTableListener listener:listeners){
			listener.onWinner(winnerEvent);
		}
	}
	
}
