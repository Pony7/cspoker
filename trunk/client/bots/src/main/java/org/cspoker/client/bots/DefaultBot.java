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
package org.cspoker.client.bots;

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
import org.cspoker.common.api.lobby.holdemtable.event.PotsChangedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;

public class DefaultBot implements HoldemTableListener, HoldemPlayerListener{

	public void onAllIn(AllInEvent allInEvent) {
		
	}

	public void onBet(BetEvent betEvent) {
		
	}

	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		
	}

	public void onCall(CallEvent callEvent) {
		
	}

	public void onCheck(CheckEvent checkEvent) {
		
	}

	public void onFold(FoldEvent foldEvent) {
		
	}

	public void onJoinTable(JoinTableEvent joinTableEvent) {
		
	}

	public void onLeaveTable(LeaveTableEvent leaveGameEvent) {
		
	}

	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) {
		
	}

	public void onNewDeal(NewDealEvent newDealEvent) {
		
	}

	public void onNewRound(NewRoundEvent newRoundEvent) {
		
	}

	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		
	}

	public void onRaise(RaiseEvent raiseEvent) {
		
	}

	public void onShowHand(ShowHandEvent showHandEvent) {
		
	}

	public void onSitIn(SitInEvent sitInEvent) {
		
	}

	public void onSitOut(SitOutEvent sitOutEvent) {
		
	}

	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		
	}

	public void onWinner(WinnerEvent winnerEvent) {
		
	}

	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {
		
	}

	@Override
	public void onPotsChanged(PotsChangedEvent potsChangedEvent) {
		
	}

}
