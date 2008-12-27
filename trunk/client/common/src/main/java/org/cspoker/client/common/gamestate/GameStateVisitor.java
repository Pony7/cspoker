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
package org.cspoker.client.common.gamestate;

import org.cspoker.client.common.gamestate.modifiers.AllInState;
import org.cspoker.client.common.gamestate.modifiers.BetState;
import org.cspoker.client.common.gamestate.modifiers.BigBlindState;
import org.cspoker.client.common.gamestate.modifiers.CallState;
import org.cspoker.client.common.gamestate.modifiers.CheckState;
import org.cspoker.client.common.gamestate.modifiers.FoldState;
import org.cspoker.client.common.gamestate.modifiers.JoinTableState;
import org.cspoker.client.common.gamestate.modifiers.LeaveSeatState;
import org.cspoker.client.common.gamestate.modifiers.LeaveTableState;
import org.cspoker.client.common.gamestate.modifiers.NewCommunityCardsState;
import org.cspoker.client.common.gamestate.modifiers.NewDealState;
import org.cspoker.client.common.gamestate.modifiers.NewPocketCardsState;
import org.cspoker.client.common.gamestate.modifiers.NewRoundState;
import org.cspoker.client.common.gamestate.modifiers.NextPlayerState;
import org.cspoker.client.common.gamestate.modifiers.RaiseState;
import org.cspoker.client.common.gamestate.modifiers.ShowHandState;
import org.cspoker.client.common.gamestate.modifiers.SitInState;
import org.cspoker.client.common.gamestate.modifiers.SitOutState;
import org.cspoker.client.common.gamestate.modifiers.SmallBlindState;
import org.cspoker.client.common.gamestate.modifiers.WinnerState;

public interface GameStateVisitor {

	void visitInitialGameState(InitialGameState initialGameState);

	void visitAllInState(AllInState allInState);

	void visitBetState(BetState betState);

	void visitBigBlindState(BigBlindState bigBlindState);

	void visitCallState(CallState callState);

	void visitCheckState(CheckState checkState);

	void visitFoldState(FoldState foldState);

	void visitJoinTableState(JoinTableState joinTableState);

	void visitLeaveSeatState(LeaveSeatState leaveSeatState);

	void visitLeaveTableState(LeaveTableState leaveTableState);

	void visitNewCommunityCardsState(
			NewCommunityCardsState newCommunityCardsState);

	void visitNewDealState(NewDealState newDealState);

	void visitNewPocketCardsState(NewPocketCardsState newPocketCardsState);

	void visitNewRoundState(NewRoundState newRoundState);

	void visitNextPlayerState(NextPlayerState nextPlayerState);

	void visitRaiseState(RaiseState raiseState);

	void visitShowHandState(ShowHandState showHandState);

	void visitSitInState(SitInState sitInState);

	void visitSitOutState(SitOutState sitOutState);

	void visitSmallBlindState(SmallBlindState smallBlindState);

	void visitWinnerState(WinnerState winnerState);

}
