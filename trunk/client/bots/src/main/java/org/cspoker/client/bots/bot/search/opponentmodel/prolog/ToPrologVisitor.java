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
package org.cspoker.client.bots.bot.search.opponentmodel.prolog;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.jcip.annotations.NotThreadSafe;

import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.gamestate.InitialGameState;
import org.cspoker.client.common.gamestate.PlayerState;
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
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.ShowdownPlayer;
import org.cspoker.common.elements.table.TableConfiguration;

@NotThreadSafe
public abstract class ToPrologVisitor implements GameStateVisitor {

	private final int sessionId;
	private int gameId = 0;
	private int actionId = 0;
	private String round;
	private int subRound;
	private NewDealState lastDeal = null;

	private final Map<PlayerId, Integer> nbActionsPerPlayer;
	private int maxNbActions = 0;

	private GameState previousStartState = null;
	
	public ToPrologVisitor() {
		sessionId = (new Random()).nextInt(Integer.MAX_VALUE);
		nbActionsPerPlayer = new HashMap<PlayerId, Integer>();
	}
	
	public ToPrologVisitor(ToPrologVisitor root) {
		sessionId =  root.getSessionId();
		gameId = root.getGameId();
		actionId = root.getActionId();
		round = root.getRound();
		subRound = root.getSubRound();
		lastDeal = root.getLastDeal();
		nbActionsPerPlayer = root.getNbActionsPerPlayer();
		maxNbActions = root.getMaxNbActions();
		previousStartState = root.getPreviousStartState();
	}
	

	protected abstract void addFact(String fact);
	
	public void readHistory(GameState gameState){
		gameState.acceptHistoryVisitor(this, previousStartState);
		previousStartState = gameState;
	}

	protected void signalAction(PlayerId playerId){
		Integer previousNbActions = nbActionsPerPlayer.get(playerId);
		if(previousNbActions==null){
			previousNbActions = 0;
		}
		int nbActions = previousNbActions+1;
		if(nbActions>maxNbActions){
			maxNbActions = nbActions;
			addFact("game_round_start("+gameId+", "+round+", "+(++subRound)+", "+(actionId+1)+")");
		}
		nbActionsPerPlayer.put(playerId, nbActions);
	}

	@Override
	public void visitAllInState(AllInState allInState) {
		PlayerId playerId = allInState.getEvent().getPlayerId();
		signalAction(playerId);
		addFact("game_player_action("+gameId+", "+(++actionId)+", player_"+playerId.getId()+", allin("+allInState.getEvent().getAmount()+"), "
				+allInState.getPreviousGameState().getDeficit(playerId)+", "+allInState.getGamePotSize()+")");
	}

	@Override
	public void visitBetState(BetState betState) {
		PlayerId playerId = betState.getEvent().getPlayerId();
		signalAction(playerId);
		addFact("game_player_action("+gameId+", "+(++actionId)+", player_"+playerId.getId()+", bets("+betState.getEvent().getAmount()+"), "
				+betState.getPreviousGameState().getDeficit(playerId)+", "+betState.getGamePotSize()+")");
	}

	@Override
	public void visitBigBlindState(BigBlindState bigBlindState) {
		PlayerId playerId = bigBlindState.getEvent().getPlayerId();
		signalAction(playerId);
		addFact("game_player_big_blind("+gameId+", player_"+playerId.getId()+")");
	}

	@Override
	public void visitCallState(CallState callState) {
		PlayerId playerId = callState.getEvent().getPlayerId();
		signalAction(playerId);
		//TODO check for what happens if allin, parameters might be inconsistent
		addFact("game_player_action("+gameId+", "+(++actionId)+", player_"+playerId.getId()+", calls("+callState.getPreviousGameState().getDeficit(playerId)+"), "
				+callState.getPreviousGameState().getDeficit(playerId)+", "+callState.getGamePotSize()+")");
	}

	@Override
	public void visitCheckState(CheckState checkState) {
		PlayerId playerId = checkState.getEvent().getPlayerId();
		signalAction(playerId);
		addFact("game_player_action("+gameId+", "+(++actionId)+", player_"+playerId.getId()+", checks, 0, "
				+checkState.getGamePotSize()+")");
	}

	@Override
	public void visitFoldState(FoldState foldState) {
		PlayerId playerId = foldState.getEvent().getPlayerId();
		signalAction(playerId);
		addFact("game_player_action("+gameId+", "+(++actionId)+", player_"+playerId.getId()+", folds, "
				+foldState.getPreviousGameState().getDeficit(playerId)+", "+foldState.getGamePotSize()+")");
	}

	@Override
	public void visitRaiseState(RaiseState raiseState) {
		RaiseEvent event = raiseState.getEvent();
		PlayerId playerId = event.getPlayerId();
		signalAction(playerId);
		addFact("game_player_action("+gameId+", "+(++actionId)+", player_"+playerId.getId()
				+", raises("+raiseState.getEvent().getAmount()+", "+raiseState.getEvent().getMovedAmount()+"), "
				+raiseState.getPreviousGameState().getDeficit(playerId)+", "+raiseState.getGamePotSize()+")");
	}

	@Override
	public void visitInitialGameState(InitialGameState initialGameState) {
		//no op
	}

	@Override
	public void visitJoinTableState(JoinTableState joinTableState) {
		//no op
	}

	@Override
	public void visitLeaveSeatState(LeaveSeatState leaveSeatState) {
		//no op
	}

	@Override
	public void visitLeaveTableState(LeaveTableState leaveTableState) {
		//no op
	}

	@Override
	public void visitNewCommunityCardsState(
			NewCommunityCardsState newCommunityCardsState) {
		if("flop".equals(round)){
			int cardNumber = 0;
			for(Card card:newCommunityCardsState.getEvent().getCommunityCards()){
				addFact("game_flop_"+(++cardNumber)+"("+gameId+", card("+card.getRank().ordinal()+", "+card.getSuit().ordinal()+"))");
			}
		}else if("turn".equals(round)){
			for(Card card:newCommunityCardsState.getEvent().getCommunityCards()){
				addFact("game_turn("+gameId+", card("+card.getRank().ordinal()+", "+card.getSuit().ordinal()+"))");
			}
		}else if("river".equals(round)){
			for(Card card:newCommunityCardsState.getEvent().getCommunityCards()){
				addFact("game_river("+gameId+", card("+card.getRank().ordinal()+", "+card.getSuit().ordinal()+"))");
			}
		}
	}

	@Override
	public void visitNewDealState(NewDealState newDealState) {
		lastDeal=newDealState;
		actionId = 0;
		addFact("game_session("+(++gameId)+", "+sessionId+")"); 
		TableConfiguration tableConfiguration = newDealState.getTableConfiguration();
		addFact("game_stakes("+gameId+", "+tableConfiguration.getSmallBlind()+", "+tableConfiguration.getBigBlind()+")");
		Set<PlayerState> players = newDealState.getAllSeatedPlayers();
		for(PlayerState player:players){
			addFact("game_player_seat("+gameId+", player_"+player.getPlayerId().getId()+", "+player.getSeatId().getId()+")");
			addFact("game_player_stack("+gameId+", player_"+player.getPlayerId().getId()+", "+player.getStack()+")");
		}
	}

	@Override
	public void visitNewPocketCardsState(NewPocketCardsState newPocketCardsState) {
		// no op
	}

	@Override
	public void visitNewRoundState(NewRoundState newRoundState) {
		subRound=0;
		maxNbActions=0;
		nbActionsPerPlayer.clear();

		switch(newRoundState.getRound()){
		case PREFLOP:
			round="preflop";
			addFact("game_phase_start("+gameId+", preflop, "+(actionId+1)+")");
			break;
		case FLOP:
			round="flop";
			addFact("game_phase_start("+gameId+", flop, "+(actionId+1)+")");
			break;
		case TURN:
			round="turn";
			addFact("game_phase_start("+gameId+", turn, "+(actionId+1)+")");
			break;
		case FINAL:
			round="river";
			addFact("game_phase_start("+gameId+", river, "+(actionId+1)+")");
		default:
			//do nothing in waiting round...
		}
	}

	@Override
	public void visitNextPlayerState(NextPlayerState nextPlayerState) {
		// no op
	}

	@Override
	public void visitShowHandState(ShowHandState showHandState) {
		int cardNumber = 0;
		ShowdownPlayer showdownPlayer = showHandState.getEvent().getShowdownPlayer();
		for(Card card:showdownPlayer.getHandCards()){
			addFact("game_player_hole_card_"+(++cardNumber)+"("+gameId+", player_"+showdownPlayer.getId().getId()+", card("+card.getRank().ordinal()+", "+card.getSuit().ordinal()+"))");
		}
	}

	@Override
	public void visitSitInState(SitInState sitInState) {
		// no op
	}

	@Override
	public void visitSitOutState(SitOutState sitOutState) {
		// no op
	}

	@Override
	public void visitSmallBlindState(SmallBlindState smallBlindState) {
		signalAction(smallBlindState.getEvent().getPlayerId());
		addFact("game_player_small_blind("+gameId+", player_"+smallBlindState.getEvent().getPlayerId().getId()+")");
	}

	@Override
	public void visitWinnerState(WinnerState winnerState) {
		Set<PlayerState> players = lastDeal.getAllSeatedPlayers();
		for(PlayerState player:players){
			int profit = winnerState.getPlayer(player.getPlayerId()).getStack()-player.getStack();
			addFact("game_player_profit("+gameId+", player_"+player.getPlayerId().getId()+", "+profit+")");
		}
	}

	int getSessionId() {
		return sessionId;
	}

	int getGameId() {
		return gameId;
	}

	int getActionId() {
		return actionId;
	}

	String getRound() {
		return round;
	}

	int getSubRound() {
		return subRound;
	}

	NewDealState getLastDeal() {
		return lastDeal;
	}

	Map<PlayerId, Integer> getNbActionsPerPlayer() {
		return new HashMap<PlayerId, Integer>(nbActionsPerPlayer);
	}

	int getMaxNbActions() {
		return maxNbActions;
	}

	GameState getPreviousStartState() {
		return previousStartState;
	}

}
