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
package org.cspoker.client.bots.bot.search.opponentmodel;

import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.NotThreadSafe;

import org.cspoker.client.common.gamestate.GameState;
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
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.util.Pair;

@NotThreadSafe
public class AllPlayersHistogramModel implements AllPlayersModel, GameStateVisitor {

	private final ConcurrentHashMap<Pair<PlayerId,Round>, HistogramModel> opponentModels = new ConcurrentHashMap<Pair<PlayerId,Round>, HistogramModel>();
	
	private volatile GameState lastKnownState = null;
	private volatile Round round;
	private volatile boolean started = false;

	private final PlayerId botId;

	public AllPlayersHistogramModel(PlayerId botId) {
		this.botId = botId;
	}
	
	public HistogramModel getModelFor(PlayerId opponentId, GameState gameState){
		return getModelFor(opponentId, gameState.getRound());
	}
	
	public HistogramModel getModelFor(PlayerId opponentId, Round round){
		Pair<PlayerId,Round> key = new Pair<PlayerId, Round>(opponentId, round);
		if(!opponentModels.containsKey(key)){
			throw new IllegalStateException("Can't find model for "+key);
		}
		return opponentModels.get(key);
	}
	
	private void initiateModelsFor(PlayerId player) {
		//check
		//bet
		//fold
		//call
		//raise
		opponentModels.put(
				new Pair<PlayerId,Round>(player,Round.PREFLOP), 
				new HistogramModel(
						player,
						botId,
						(int)10,
						(int)0,
						(int)10,
						(int)Math.round(37556539.0/53203232.0*10),
						(int)Math.round(7410418.0/53203232.0*10),
						(int)Math.round(8236275.0/53203232.0*10),
						(int)(Math.round(37556539.0/53203232.0*10)+
								Math.round(7410418.0/53203232.0*10)+
								Math.round(8236275.0/53203232.0*10))
						));
		opponentModels.put(new Pair<PlayerId,Round>(player,Round.FLOP), 
				new HistogramModel(
						player,
						botId,
						(int)Math.round(5643188.0/9067525.0*10),
						(int)Math.round(3424337.0/9067525.0*10),
						(int)(Math.round(5643188.0/9067525.0*10)+
								Math.round(3424337.0/9067525.0*10)),
						(int)Math.round(3012394.0/5085650.0*10),
						(int)Math.round(1493533.0/5085650.0*10),
						(int)Math.round(579723.0/5085650.0*10),
						(int)(Math.round(3012394.0/5085650.0*10)+
								Math.round(1493533.0/5085650.0*10)+
								Math.round(579723.0/5085650.0*10))
						));
		opponentModels.put(new Pair<PlayerId,Round>(player,Round.TURN), 
				new HistogramModel(
						player,
						botId,
						(int)Math.round(2969602.0/4635236.0*10),
						(int)Math.round(1665634.0/4635236.0*10),
						(int)(Math.round(2969602.0/4635236.0*10)+
								Math.round(1665634.0/4635236.0*10)),
						(int)Math.round(1152214.0/2172745.0*10),
						(int)Math.round(814117.0/2172745.0*10),
						(int)Math.round(206414.0/2172745.0*10),
						(int)(Math.round(1152214.0/2172745.0*10)+
								Math.round(814117.0/2172745.0*10)+
								Math.round(206414.0/2172745.0*10))
						));
		opponentModels.put(new Pair<PlayerId,Round>(player,Round.FINAL), 
				new HistogramModel(
						player,
						botId,
						(int)Math.round(1863381.0/2843076.0*10),
						(int)Math.round(979695.0/2843076.0*10),
						(int)(Math.round(1863381.0/2843076.0*10)+
								Math.round(979695.0/2843076.0*10)),
						(int)Math.round(653301.0/1150533.0*10),
						(int)Math.round(407975.0/1150533.0*10),
						(int)Math.round(89257.0/1150533.0*10),
						(int)(Math.round(653301.0/1150533.0*10)+
								Math.round(407975.0/1150533.0*10)+
								Math.round(89257.0/1150533.0*10))
						));
	}
	
	private void forgetModelsFor(PlayerId player){
		opponentModels.remove(new Pair<PlayerId,Round>(player,Round.FINAL));
		opponentModels.remove(new Pair<PlayerId,Round>(player,Round.FLOP));
		opponentModels.remove(new Pair<PlayerId,Round>(player,Round.PREFLOP));
		opponentModels.remove(new Pair<PlayerId,Round>(player,Round.TURN));
	}

	@Override
	public void signalNextAction(GameState gameState) {
		gameState.acceptHistoryVisitor(this, lastKnownState);
		lastKnownState = gameState;
	}

	@Override
	public void visitAllInState(AllInState allInState) {
		if(started)
		getModelFor(allInState.getEvent().getPlayerId(),round).addAllIn(allInState, allInState.getEvent());
	}

	@Override
	public void visitBetState(BetState betState) {
		if(started)
		getModelFor(betState.getEvent().getPlayerId(),round).addBet(betState, betState.getEvent());
	}

	@Override
	public void visitBigBlindState(BigBlindState bigBlindState) {
		
	}

	@Override
	public void visitCallState(CallState callState) {
		if(started)
		getModelFor(callState.getEvent().getPlayerId(),round).addCall(callState, callState.getEvent());
	}

	@Override
	public void visitCheckState(CheckState checkState) {
		if(started)
		getModelFor(checkState.getEvent().getPlayerId(),round).addCheck(checkState, checkState.getEvent());
	}

	@Override
	public void visitFoldState(FoldState foldState) {
		if(started)
		getModelFor(foldState.getEvent().getPlayerId(),round).addFold(foldState, foldState.getEvent());
	}

	@Override
	public void visitInitialGameState(InitialGameState initialGameState) {

	}

	@Override
	public void visitJoinTableState(JoinTableState joinTableState) {
		initiateModelsFor(joinTableState.getLastEvent().getPlayerId());
	}

	@Override
	public void visitLeaveSeatState(LeaveSeatState leaveSeatState) {
	}

	@Override
	public void visitLeaveTableState(LeaveTableState leaveTableState) {
		forgetModelsFor(leaveTableState.getLastEvent().getPlayerId());
	}

	@Override
	public void visitNewCommunityCardsState(
			NewCommunityCardsState newCommunityCardsState) {
		
	}

	@Override
	public void visitNewDealState(NewDealState newDealState) {
		if(!started){
			started = true;
			for(PlayerId player:newDealState.getAllSeatedPlayerIds()){
				initiateModelsFor(player);
			}
		}
	}

	@Override
	public void visitNewPocketCardsState(NewPocketCardsState newPocketCardsState) {
		
	}

	@Override
	public void visitNewRoundState(NewRoundState newRoundState) {
		round = newRoundState.getRound();
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
		initiateModelsFor(sitInState.getEvent().getPlayer().getId());
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

	@Override
	public void assume(GameState gameState) {
		
	}

	@Override
	public void forgetAssumption() {
		
	}

}
