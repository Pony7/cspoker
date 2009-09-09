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
package org.cspoker.ai.opponentmodels.prolog;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import net.jcip.annotations.NotThreadSafe;

import org.cspoker.client.common.gamestate.DetailedHoldemTableState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.gamestate.modifiers.AllInState;
import org.cspoker.client.common.gamestate.modifiers.BetState;
import org.cspoker.client.common.gamestate.modifiers.BlindState;
import org.cspoker.client.common.gamestate.modifiers.CallState;
import org.cspoker.client.common.gamestate.modifiers.CheckState;
import org.cspoker.client.common.gamestate.modifiers.ConfigChangeState;
import org.cspoker.client.common.gamestate.modifiers.FoldState;
import org.cspoker.client.common.gamestate.modifiers.JoinTableState;
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
import org.cspoker.client.common.gamestate.modifiers.WinnerState;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.ShowdownPlayer;
import org.cspoker.common.elements.table.TableConfiguration;

@NotThreadSafe
public abstract class LoggingVisitor implements GameStateVisitor {

	protected final int sessionId;
	protected int gameId = 0;
	protected int actionId = 0;
	protected SymbolTerm round;
	protected int subRound;
	private NewDealState lastDeal = null;

	private final Map<PlayerId, Integer> nbActionsPerPlayer;
	private int maxNbActions = 0;

	private GameState previousStartState = null;

	public LoggingVisitor() {
		sessionId = new Random().nextInt(Integer.MAX_VALUE);
		nbActionsPerPlayer = new HashMap<PlayerId, Integer>();
	}

	public LoggingVisitor(LoggingVisitor root) {
		sessionId = root.getSessionId();
		gameId = root.getGameId();
		actionId = root.getActionId();
		round = root.getRound();
		subRound = root.getSubRound();
		lastDeal = root.getLastDeal();
		nbActionsPerPlayer = root.getNbActionsPerPlayer();
		maxNbActions = root.getMaxNbActions();
		previousStartState = root.getPreviousStartState();
	}

	public void readHistory(GameState gameState) {
		gameState.acceptHistoryVisitor(this, previousStartState);
		previousStartState = gameState;
	}

	protected void signalAction(PlayerId playerId) {
		Integer previousNbActions = nbActionsPerPlayer.get(playerId);
		if (previousNbActions == null) {
			previousNbActions = 0;
		}
		int nbActions = previousNbActions + 1;
		if (nbActions > maxNbActions) {
			maxNbActions = nbActions;
			gameRoundStart();
		}
		nbActionsPerPlayer.put(playerId, nbActions);
	}

	@Override
	public void visitAllInState(AllInState allInState) {
		PlayerId playerId = allInState.getEvent().getPlayerId();
		signalAction(playerId);
		gamePlayerAction(allInState, playerId);
	}

	@Override
	public void visitBetState(BetState betState) {
		PlayerId playerId = betState.getEvent().getPlayerId();
		signalAction(playerId);
		gamePlayerAction(betState, playerId);
	}

	@Override
	public void visitBlindState(BlindState blindState) {
		PlayerId playerId = blindState.getEvent().getPlayerId();
		signalAction(playerId);
		if(playerId.equals(blindState.getBigBlind())){
			gamePlayerBigBlind(playerId);
		}else if(playerId.equals(blindState.getSmallBlind())){
			gamePlayerSmallBlind(playerId);
		}else throw new UnsupportedOperationException("Can't handle additional blinds in Prolog model.");
	}

	@Override
	public void visitCallState(CallState callState) {
		PlayerId playerId = callState.getEvent().getPlayerId();
		signalAction(playerId);
		gamePlayerAction(callState, playerId);
	}

	@Override
	public void visitCheckState(CheckState checkState) {
		PlayerId playerId = checkState.getEvent().getPlayerId();
		signalAction(playerId);
		gamePlayerAction(checkState, playerId);
	}

	@Override
	public void visitFoldState(FoldState foldState) {
		PlayerId playerId = foldState.getEvent().getPlayerId();
		signalAction(playerId);
		gamePlayerAction(foldState, playerId);
	}

	@Override
	public void visitRaiseState(RaiseState raiseState) {
		RaiseEvent event = raiseState.getEvent();
		PlayerId playerId = event.getPlayerId();
		signalAction(playerId);
		gamePlayerAction(raiseState, playerId);
	}

	@Override
	public void visitInitialGameState(DetailedHoldemTableState initialGameState) {
		// no op
	}
	
	@Override
	public void visitConfigChangeState(ConfigChangeState configChangeState) {
		// no op
	}

	@Override
	public void visitJoinTableState(JoinTableState joinTableState) {
		// no op
	}

	@Override
	public void visitLeaveTableState(LeaveTableState leaveTableState) {
		// no op
	}

	@Override
	public void visitNewCommunityCardsState(
			NewCommunityCardsState newCommunityCardsState) {
		if (flop.equals(round)) {
			int cardNumber = 0;
			for (Card card : newCommunityCardsState.getEvent()
					.getCommunityCards()) {
				++cardNumber;
				gameFlop(cardNumber, card);
			}
		} else if (turn.equals(round)) {
			for (Card card : newCommunityCardsState.getEvent()
					.getCommunityCards()) {
				gameTurn(card);
			}
		} else if (river.equals(round)) {
			for (Card card : newCommunityCardsState.getEvent()
					.getCommunityCards()) {
				gameRiver(card);
			}
		}
	}

	@Override
	public void visitNewDealState(NewDealState newDealState) {
		lastDeal = newDealState;
		actionId = 0;
		++gameId;
		gameSession();
		TableConfiguration tableConfiguration = newDealState
				.getTableConfiguration();
		gameStakes(tableConfiguration);
		Set<PlayerState> players = newDealState.getAllSeatedPlayers();
		for (PlayerState player : players) {
			gamePlayerSeat(player);
			gamePlayerStack(player);
		}
	}

	protected abstract void gameStakes(TableConfiguration tableConfiguration);

	protected abstract void gameSession();

	@Override
	public void visitNewPocketCardsState(NewPocketCardsState newPocketCardsState) {
		// no op
	}

	public final static SymbolTerm preflop = SymbolTerm.makeSymbol("preflop");
	public final static SymbolTerm flop = SymbolTerm.makeSymbol("flop");
	public final static SymbolTerm turn = SymbolTerm.makeSymbol("turn");
	public final static SymbolTerm river = SymbolTerm.makeSymbol("river");

	@Override
	public void visitNewRoundState(NewRoundState newRoundState) {
		subRound = 0;
		maxNbActions = 0;
		nbActionsPerPlayer.clear();

		switch (newRoundState.getRound()) {
		case PREFLOP:
			round = preflop;
			gamePhaseStart();
			break;
		case FLOP:
			round = flop;
			gamePhaseStart();
			break;
		case TURN:
			round = turn;
			gamePhaseStart();
			break;
		case FINAL:
			round = river;
			gamePhaseStart();
		default:
			// do nothing in waiting round...
		}
	}

	@Override
	public void visitNextPlayerState(NextPlayerState nextPlayerState) {
		// no op
	}

	@Override
	public void visitShowHandState(ShowHandState showHandState) {
		int cardNumber = 0;
		ShowdownPlayer showdownPlayer = showHandState.getEvent()
				.getShowdownPlayer();
		for (Card card : showdownPlayer.getHandCards()) {
			++cardNumber;
			gamePlayerHoleCards(cardNumber, showdownPlayer, card);
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
	public void visitWinnerState(WinnerState winnerState) {
		Set<PlayerState> players = lastDeal.getAllSeatedPlayers();
		for (PlayerState player : players) {
			int profit = winnerState.getPlayer(player.getPlayerId()).getStack()
					- player.getStack();
			gamePlayerProfit(player, profit);
		}
	}

	protected abstract void gameRoundStart();

	protected abstract void gamePlayerAction(AllInState allInState,
			PlayerId playerId);

	protected abstract void gamePlayerAction(BetState betState,
			PlayerId playerId);

	protected abstract void gamePlayerBigBlind(PlayerId playerId);

	protected abstract void gamePlayerAction(CallState callState,
			PlayerId playerId);

	protected abstract void gamePlayerAction(CheckState checkState,
			PlayerId playerId);

	protected abstract void gamePlayerAction(FoldState foldState,
			PlayerId playerId);

	protected abstract void gamePlayerAction(RaiseState raiseState,
			PlayerId playerId);

	protected abstract void gameRiver(Card card);

	protected abstract void gameTurn(Card card);

	protected abstract void gameFlop(int cardNumber, Card card);

	protected abstract void gamePlayerStack(PlayerState player);

	protected abstract void gamePlayerSeat(PlayerState player);

	protected abstract void gamePhaseStart();

	protected abstract void gamePlayerHoleCards(int cardNumber,
			ShowdownPlayer showdownPlayer, Card card);

	protected abstract void gamePlayerSmallBlind(PlayerId playerId);

	protected abstract void gamePlayerProfit(PlayerState player, int profit);

	public int getSessionId() {
		return sessionId;
	}

	public int getGameId() {
		return gameId;
	}

	public int getActionId() {
		return actionId;
	}

	public SymbolTerm getRound() {
		return round;
	}

	public int getSubRound() {
		return subRound;
	}

	public NewDealState getLastDeal() {
		return lastDeal;
	}

	public Map<PlayerId, Integer> getNbActionsPerPlayer() {
		return new HashMap<PlayerId, Integer>(nbActionsPerPlayer);
	}

	public int getMaxNbActions() {
		return maxNbActions;
	}

	public GameState getPreviousStartState() {
		return previousStartState;
	}

}
