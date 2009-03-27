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
package org.cspoker.client.bots.bot.search.opponentmodel.weka;

import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.gamestate.InitialGameState;
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

public class PlayerTrackingVisitor implements GameStateVisitor {

	@Override
	public void visitAllInState(AllInState allInState) {

	}

	@Override
	public void visitBetState(BetState betState) {

	}

	@Override
	public void visitBigBlindState(BigBlindState bigBlindState) {

	}

	@Override
	public void visitCallState(CallState callState) {

	}

	@Override
	public void visitCheckState(CheckState checkState) {

	}

	@Override
	public void visitFoldState(FoldState foldState) {

	}

	@Override
	public void visitInitialGameState(InitialGameState initialGameState) {

	}

	@Override
	public void visitJoinTableState(JoinTableState joinTableState) {

	}

	@Override
	public void visitLeaveSeatState(LeaveSeatState leaveSeatState) {

	}

	@Override
	public void visitLeaveTableState(LeaveTableState leaveTableState) {

	}

	@Override
	public void visitNewCommunityCardsState(
			NewCommunityCardsState newCommunityCardsState) {

	}

	@Override
	public void visitNewDealState(NewDealState newDealState) {

	}

	@Override
	public void visitNewPocketCardsState(NewPocketCardsState newPocketCardsState) {

	}

	@Override
	public void visitNewRoundState(NewRoundState newRoundState) {

	}

	@Override
	public void visitNextPlayerState(NextPlayerState nextPlayerState) {

	}

	@Override
	public void visitRaiseState(RaiseState raiseState) {

	}

	@Override
	public void visitShowHandState(ShowHandState showHandState) {

	}

	@Override
	public void visitSitInState(SitInState sitInState) {

	}

	@Override
	public void visitSitOutState(SitOutState sitOutState) {

	}

	@Override
	public void visitSmallBlindState(SmallBlindState smallBlindState) {

	}

	@Override
	public void visitWinnerState(WinnerState winnerState) {

	}

}
