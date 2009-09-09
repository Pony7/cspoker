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

import jp.ac.kobe_u.cs.prolog.lang.IntegerTerm;
import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;
import jp.ac.kobe_u.cs.prolog.lang.SymbolTerm;
import jp.ac.kobe_u.cs.prolog.lang.Term;

import org.cspoker.client.common.gamestate.modifiers.AllInState;
import org.cspoker.client.common.gamestate.modifiers.BetState;
import org.cspoker.client.common.gamestate.modifiers.CallState;
import org.cspoker.client.common.gamestate.modifiers.CheckState;
import org.cspoker.client.common.gamestate.modifiers.FoldState;
import org.cspoker.client.common.gamestate.modifiers.RaiseState;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.ShowdownPlayer;
import org.cspoker.common.elements.table.TableConfiguration;

public abstract class ToPrologTermVisitor extends LoggingVisitor {

	public ToPrologTermVisitor() {
		super();
	}

	public ToPrologTermVisitor(LoggingVisitor root) {
		super(root);
	}

	protected abstract void addTerm(StructureTerm term);

	// predicate terms
	public final static SymbolTerm game_round_start = SymbolTerm.makeSymbol(
			"game_round_start", 4);
	public final static SymbolTerm game_player_action = SymbolTerm.makeSymbol(
			"game_player_action", 6);
	public final static SymbolTerm game_player_big_blind = SymbolTerm
			.makeSymbol("game_player_big_blind", 2);
	public final static SymbolTerm game_player_small_blind = SymbolTerm
			.makeSymbol("game_player_small_blind", 2);
	public final static SymbolTerm game_river = SymbolTerm.makeSymbol(
			"game_river", 2);
	public final static SymbolTerm game_turn = SymbolTerm.makeSymbol(
			"game_round_start", 2);
	public final static SymbolTerm game_flop_1 = SymbolTerm.makeSymbol(
			"game_flop_1", 2);
	public final static SymbolTerm game_flop_2 = SymbolTerm.makeSymbol(
			"game_flop_2", 2);
	public final static SymbolTerm game_flop_3 = SymbolTerm.makeSymbol(
			"game_flop_3", 2);
	public final static SymbolTerm game_player_stack = SymbolTerm.makeSymbol(
			"game_player_stack", 3);
	public final static SymbolTerm game_player_seat = SymbolTerm.makeSymbol(
			"game_player_seat", 3);
	public final static SymbolTerm game_player_hole_card_1 = SymbolTerm
			.makeSymbol("game_player_hole_card_1", 3);
	public final static SymbolTerm game_player_hole_card_2 = SymbolTerm
			.makeSymbol("game_player_hole_card_2", 3);
	public final static SymbolTerm game_phase_start = SymbolTerm.makeSymbol(
			"game_phase_start", 3);
	public final static SymbolTerm game_player_profit = SymbolTerm.makeSymbol(
			"game_player_profit", 3);
	public final static SymbolTerm game_stakes = SymbolTerm.makeSymbol(
			"game_stakes", 3);
	public final static SymbolTerm game_session = SymbolTerm.makeSymbol(
			"game_session", 2);

	// terms
	public final static SymbolTerm allin = SymbolTerm.makeSymbol("allin", 1);
	public final static SymbolTerm bets = SymbolTerm.makeSymbol("bets", 1);
	public final static SymbolTerm calls = SymbolTerm.makeSymbol("calls", 1);
	public final static SymbolTerm checks = SymbolTerm.makeSymbol("checks", 0);
	public final static SymbolTerm folds = SymbolTerm.makeSymbol("folds", 0);
	public final static SymbolTerm raises = SymbolTerm.makeSymbol("raises", 2);
	public final static SymbolTerm card = SymbolTerm.makeSymbol("card", 2);

	public final static IntegerTerm integer_0 = new IntegerTerm(0);

	@Override
	protected void gameRoundStart() {
		addTerm(new StructureTerm(game_round_start, new Term[] {
				getGameIdTerm(), round, new IntegerTerm((++subRound)),
				new IntegerTerm((actionId + 1)), }));
	}

	private IntegerTerm getGameIdTerm() {
		return new IntegerTerm(gameId);
	}

	@Override
	protected void gamePlayerAction(AllInState allInState, PlayerId playerId) {
		addTerm(new StructureTerm(game_player_action, new Term[] {
				getGameIdTerm(),
				getIncrementedActionIdTerm(),
				getPlayerTerm(playerId),
				new StructureTerm(allin, new Term[] { new IntegerTerm(
						allInState.getEvent().getMovedAmount()) }),
				new IntegerTerm(allInState.getPreviousGameState().getDeficit(
						playerId)),
				new IntegerTerm(allInState.getGamePotSize()), }));
	}

	private IntegerTerm getIncrementedActionIdTerm() {
		return new IntegerTerm((++actionId));
	}

	@Override
	protected void gamePlayerAction(BetState betState, PlayerId playerId) {
		addTerm(new StructureTerm(game_player_action,
				new Term[] {
						getGameIdTerm(),
						getIncrementedActionIdTerm(),
						getPlayerTerm(playerId),
						new StructureTerm(bets, new Term[] { new IntegerTerm(
								betState.getEvent().getAmount()) }),
						new IntegerTerm(betState.getPreviousGameState()
								.getDeficit(playerId)),
						new IntegerTerm(betState.getGamePotSize()), }));
	}

	@Override
	protected void gamePlayerAction(CallState callState, PlayerId playerId) {
		// TODO check for what happens if allin, parameters might be
		// inconsistent
		addTerm(new StructureTerm(game_player_action, new Term[] {
				getGameIdTerm(),
				getIncrementedActionIdTerm(),
				getPlayerTerm(playerId),
				new StructureTerm(calls, new Term[] { new IntegerTerm(callState
						.getPreviousGameState().getDeficit(playerId)) }),
				new IntegerTerm(callState.getPreviousGameState().getDeficit(
						playerId)),
				new IntegerTerm(callState.getGamePotSize()), }));
	}

	@Override
	protected void gamePlayerAction(CheckState checkState, PlayerId playerId) {
		addTerm(new StructureTerm(game_player_action, new Term[] {
				getGameIdTerm(), getIncrementedActionIdTerm(),
				getPlayerTerm(playerId), checks, integer_0,
				new IntegerTerm(checkState.getGamePotSize()), }));
	}

	@Override
	protected void gamePlayerAction(FoldState foldState, PlayerId playerId) {
		addTerm(new StructureTerm(game_player_action, new Term[] {
				getGameIdTerm(),
				getIncrementedActionIdTerm(),
				getPlayerTerm(playerId),
				folds,
				new IntegerTerm(foldState.getPreviousGameState().getDeficit(
						playerId)),
				new IntegerTerm(foldState.getGamePotSize()), }));
	}

	@Override
	protected void gamePlayerAction(RaiseState raiseState, PlayerId playerId) {
		addTerm(new StructureTerm(game_player_action, new Term[] {
				getGameIdTerm(),
				getIncrementedActionIdTerm(),
				getPlayerTerm(playerId),
				new StructureTerm(raises,
						new Term[] {
								new IntegerTerm(raiseState.getEvent()
										.getAmount()),
								new IntegerTerm(raiseState.getEvent()
										.getMovedAmount()) }),
				new IntegerTerm(raiseState.getPreviousGameState().getDeficit(
						playerId)),
				new IntegerTerm(raiseState.getGamePotSize()), }));
	}

	private SymbolTerm getPlayerTerm(PlayerId playerId) {
		return SymbolTerm.makeSymbol("player_" + playerId.getId());
	}

	@Override
	protected void gamePlayerSmallBlind(PlayerId playerId) {
		addTerm(new StructureTerm(game_player_small_blind, new Term[] {
				getGameIdTerm(),
				getPlayerTerm(playerId), }));
	}

	@Override
	protected void gamePlayerBigBlind(PlayerId playerId) {
		addTerm(new StructureTerm(game_player_big_blind, new Term[] {
				getGameIdTerm(), getPlayerTerm(playerId), }));
	}

	@Override
	protected void gameRiver(Card card) {
		addTerm(new StructureTerm(game_river, new Term[] { getGameIdTerm(),
				getCardTerm(card), }));
	}

	@Override
	protected void gameTurn(Card card) {
		addTerm(new StructureTerm(game_turn, new Term[] { getGameIdTerm(),
				getCardTerm(card), }));
	}

	@Override
	protected void gameFlop(int cardNumber, Card card) {
		SymbolTerm term;
		if (cardNumber == 1) {
			term = game_flop_1;
		} else if (cardNumber == 2) {
			term = game_flop_1;
		} else if (cardNumber == 3) {
			term = game_flop_1;
		} else {
			throw new IllegalArgumentException("Unknown card number: "
					+ cardNumber);
		}
		addTerm(new StructureTerm(term, new Term[] { getGameIdTerm(),
				getCardTerm(card), }));
	}

	private StructureTerm getCardTerm(Card card) {
		return new StructureTerm(ToPrologTermVisitor.card, new Term[] {
				new IntegerTerm(card.getRank().ordinal()),
				new IntegerTerm(card.getSuit().ordinal()),

		});
	}

	@Override
	protected void gamePlayerStack(PlayerState player) {
		addTerm(new StructureTerm(game_player_stack, new Term[] {
				getGameIdTerm(), getPlayerTerm(player.getPlayerId()),
				new IntegerTerm(player.getStack()), }));
	}

	@Override
	protected void gamePlayerSeat(PlayerState player) {
		addTerm(new StructureTerm(game_player_seat, new Term[] {
				getGameIdTerm(), getPlayerTerm(player.getPlayerId()),
				new IntegerTerm(player.getSeatId().getId()), }));
	}

	@Override
	protected void gamePhaseStart() {
		addTerm(new StructureTerm(game_phase_start, new Term[] {
				getGameIdTerm(), round, new IntegerTerm(actionId + 1), }));
	}

	@Override
	protected void gamePlayerHoleCards(int cardNumber,
			ShowdownPlayer showdownPlayer, Card card) {
		SymbolTerm term;
		if (cardNumber == 1) {
			term = game_player_hole_card_1;
		} else if (cardNumber == 2) {
			term = game_player_hole_card_2;
		} else {
			throw new IllegalArgumentException("Unknown card number: "
					+ cardNumber);
		}
		addTerm(new StructureTerm(term, new Term[] { getGameIdTerm(),
				getPlayerTerm(showdownPlayer.getPlayerId()), getCardTerm(card), }));
	}

	@Override
	protected void gamePlayerProfit(PlayerState player, int profit) {
		addTerm(new StructureTerm(game_player_profit, new Term[] {
				getGameIdTerm(), getPlayerTerm(player.getPlayerId()),
				new IntegerTerm(profit), }));
	}

	@Override
	protected void gameStakes(TableConfiguration tableConfiguration) {
		addTerm(new StructureTerm(game_stakes, new Term[] { getGameIdTerm(),
				new IntegerTerm(tableConfiguration.getSmallBlind()),
				new IntegerTerm(tableConfiguration.getBigBlind()), }));
	}

	@Override
	protected void gameSession() {
		addTerm(new StructureTerm(game_session, new Term[] { getGameIdTerm(),
				new IntegerTerm(sessionId), }));
	}

}
