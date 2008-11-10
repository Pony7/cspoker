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


public interface HoldemTableListener extends RemoteHoldemTableListener {
	
	void onAllIn(AllInEvent allInEvent);

	void onBet(BetEvent betEvent);

	void onBigBlind(BigBlindEvent bigBlindEvent);

	void onCall(CallEvent callEvent);

	void onCheck(CheckEvent checkEvent);

	void onFold(FoldEvent foldEvent);
	
	void onJoinTable(JoinTableEvent joinTableEvent);

	void onLeaveTable(LeaveTableEvent leaveGameEvent);
	
	void onLeaveSeat(LeaveSeatEvent leaveSeatEvent);

	void onNewCommunityCards(NewCommunityCardsEvent newCommunityCardsEvent);

	void onNewDeal(NewDealEvent newDealEvent);

	void onNextPlayer(NextPlayerEvent nextPlayerEvent);

	void onNewRound(NewRoundEvent newRoundEvent);

	void onRaise(RaiseEvent raiseEvent);

	void onShowHand(ShowHandEvent showHandEvent);

	void onSitIn(SitInEvent sitInEvent);
	
	void onSitOut(SitOutEvent sitOutEvent);

	void onSmallBlind(SmallBlindEvent smallBlindEvent);

	void onWinner(WinnerEvent winnerEvent);
	
}
