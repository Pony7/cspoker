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
package org.cspoker.common.api.lobby.holdemtable.event;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;

public interface HoldemTableListener {

	HoldemPlayerListener getHoldemPlayerListener();

	void onBet(BetEvent betEvent);

	void onBigBlind(BigBlindEvent bigBlindEvent);

	void onCall(CallEvent callEvent);

	void onCheck(CheckEvent checkEvent);

	void onFold(FoldEvent foldEvent);

	void onLeaveGame(LeaveGameEvent leaveGameEvent);

	void onNewCommunityCards(NewCommunityCardsEvent newCommunityCardsEvent);

	void onNewDeal(NewDealEvent newDealEvent);

	void onNextPlayer(NextPlayerEvent nextPlayerEvent);

	void onNewRound(NewRoundEvent newRoundEvent);

	void onRaise(RaiseEvent raiseEvent);

	void onShowHand(ShowHandEvent showHandEvent);

	void onSitIn(SitInEvent sitInEvent);

	void onSmallBlind(SmallBlindEvent smallBlindEvent);

	void onWinner(WinnerEvent winnerEvent);

	void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent);
	
}
