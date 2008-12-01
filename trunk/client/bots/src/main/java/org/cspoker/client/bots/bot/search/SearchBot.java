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
package org.cspoker.client.bots.bot.search;

import java.rmi.RemoteException;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.AbstractBot;
import org.cspoker.client.bots.bot.search.node.BotActionNode;
import org.cspoker.client.bots.bot.search.node.ConcurrentBotActionNode;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.util.threading.GlobalThreadPool;

public class SearchBot
extends AbstractBot {

	private final static Logger logger = Logger.getLogger(SearchBot.class);

	Random random = new Random();

	private final AllPlayersModel opponentModeler;

	public SearchBot(PlayerId playerId, TableId tableId,
			SmartLobbyContext lobby, ExecutorService executor,
			AllPlayersModel opponentModeler,
			BotListener... botListeners) {
		super(playerId, tableId, lobby, executor, botListeners);
		this.opponentModeler = opponentModeler;
	}

	@Override
	public void doNextAction() {
		executor.execute(new Runnable() {
			public void run() {
				try {
					BotActionNode actionNode;
					switch (tableContext.getGameState().getRound()) {
					case PREFLOP:
						playerContext.checkOrCall();
						break;
					case FLOP:
						logger.debug("Searching flop round game tree:");
						actionNode = new ConcurrentBotActionNode(GlobalThreadPool.getInstance(), playerID, playerContext.getGameState(), opponentModeler, "|");
						actionNode.performbestAction(playerContext);
						break;
					case TURN:
						logger.debug("Searching turn round game tree:");
						actionNode = new ConcurrentBotActionNode(GlobalThreadPool.getInstance(), playerID, playerContext.getGameState(), opponentModeler, "|");
						actionNode.performbestAction(playerContext);
						break;
					case FINAL:
						logger.debug("Searching final round game tree:");
						actionNode = new ConcurrentBotActionNode(GlobalThreadPool.getInstance(), playerID, playerContext.getGameState(), opponentModeler, "|");
						actionNode.performbestAction(playerContext);
						break;
					default:
						throw new IllegalStateException("What round are we in?");
					}		
				} catch (IllegalActionException e) {
					logger.error(e);
					throw new IllegalStateException("Action was not allowed.",e);
				}catch (RemoteException e) {
					logger.error(e);
					throw new IllegalStateException("Action failed.",e);

				}
			}
		});
	}

	@Override
	public void onAllIn(final AllInEvent allInEvent) {
		final GameState gameState = playerContext.getGameState();
		executor.execute(new Runnable() {
			public void run() {
				opponentModeler.getModelFor(allInEvent.getPlayerId()).addAllIn(gameState, allInEvent);
			}
		});
		super.onAllIn(allInEvent);
	}

	@Override
	public void onBet(final BetEvent betEvent) {
		final GameState gameState = playerContext.getGameState();
		executor.execute(new Runnable() {
			public void run() {
				opponentModeler.getModelFor(betEvent.getPlayerId()).addBet(gameState, betEvent);
			}
		});
		super.onBet(betEvent);
	}

	@Override
	public void onCall(final CallEvent callEvent) {
		final GameState gameState = playerContext.getGameState();
		executor.execute(new Runnable() {
			public void run() {
				opponentModeler.getModelFor(callEvent.getPlayerId()).addCall(gameState, callEvent);
			}
		});
		super.onCall(callEvent);
	}

	@Override
	public void onFold(final FoldEvent foldEvent) {
		final GameState gameState = playerContext.getGameState();
		executor.execute(new Runnable() {
			public void run() {
				opponentModeler.getModelFor(foldEvent.getPlayerId()).addFold(gameState, foldEvent);
			}
		});
		super.onFold(foldEvent);
	}

	@Override
	public void onCheck(final CheckEvent checkEvent) {
		final GameState gameState = playerContext.getGameState();
		executor.execute(new Runnable() {
			public void run() {
				opponentModeler.getModelFor(checkEvent.getPlayerId()).addCheck(gameState, checkEvent);
			}
		});
		super.onCheck(checkEvent);
	}

	@Override
	public void onRaise(final RaiseEvent raiseEvent) {
		final GameState gameState = playerContext.getGameState();
		executor.execute(new Runnable() {
			public void run() {
				opponentModeler.getModelFor(raiseEvent.getPlayerId()).addRaise(gameState,raiseEvent);
			}
		});
		super.onRaise(raiseEvent);
	}

}
