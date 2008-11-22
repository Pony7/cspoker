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
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.AbstractBot;
import org.cspoker.client.bots.bot.search.node.IBotActionNode;
import org.cspoker.client.bots.bot.search.node.finalround.ConcurrentFinalBotBetNode;
import org.cspoker.client.bots.bot.search.node.finalround.ConcurrentFinalBotNoBetNode;
import org.cspoker.client.bots.bot.search.opponentmodel.FinalRoundModel;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.Player;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.util.threading.GlobalThreadPool;

public class SearchBot
extends AbstractBot {

	private final static Logger logger = Logger.getLogger(SearchBot.class);

	Random random = new Random();

	private final Map<PlayerId,OpponentModel> opponentModelsFinal;

	public SearchBot(PlayerId playerId, TableId tableId,
			SmartLobbyContext lobby, ExecutorService executor,
			BotListener... botListeners) {
		super(playerId, tableId, lobby, executor, botListeners);
		this.opponentModelsFinal = new ConcurrentHashMap<PlayerId, OpponentModel>(10);
	}

	@Override
	public void doNextAction() {
		executor.execute(new Runnable() {
			public void run() {
				try {
					switch (tableContext.getGameState().getRound()) {
					case PREFLOP:
						playerContext.checkOrCall();
						break;
					case FLOP:
						playerContext.checkOrCall();
						break;
					case TURN:
						playerContext.checkOrCall();
						break;
					case FINAL:
						logger.debug("Searching final round game tree.");
						IBotActionNode actionNode;
						if(tableContext.getGameState().hasBet()){
							actionNode = new ConcurrentFinalBotBetNode(GlobalThreadPool.getInstance(), 
									playerID, playerContext.getGameState(), opponentModelsFinal, 0);
						}else{
							actionNode = new ConcurrentFinalBotNoBetNode(GlobalThreadPool.getInstance(), 
									playerID, playerContext.getGameState(), opponentModelsFinal, 0);
						}
						actionNode.expand();
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
	public void onNewDeal(final NewDealEvent newDealEvent) {
		for(Player player:newDealEvent.getPlayers()){
			if(!opponentModelsFinal.containsKey(player.getId())){
				opponentModelsFinal.put(player.getId(), new FinalRoundModel());
			}
		}
		super.onNewDeal(newDealEvent);
	}

	@Override
	public void onAllIn(final AllInEvent allInEvent) {
		if(playerContext.getGameState().getRound().equals(Round.FINAL)){
			executor.execute(new Runnable() {
				public void run() {
					opponentModelsFinal.get(allInEvent.getPlayerId()).addAllIn(playerContext.getGameState(), allInEvent);
				}
			});
		}
		super.onAllIn(allInEvent);
	}

	@Override
	public void onBet(final BetEvent betEvent) {
		if(playerContext.getGameState().getRound().equals(Round.FINAL)){
			executor.execute(new Runnable() {
				public void run() {
					opponentModelsFinal.get(betEvent.getPlayerId()).addBet(betEvent.getAmount());
				}
			});
		}
		super.onBet(betEvent);
	}

	@Override
	public void onCall(final CallEvent callEvent) {
		final GameState gameState = playerContext.getGameState();
		if(gameState.getRound().equals(Round.FINAL)){
			executor.execute(new Runnable() {
				public void run() {
					opponentModelsFinal.get(callEvent.getPlayerId()).addCall();
					if(logger.isTraceEnabled()){
						logger.trace("Call probability is "+opponentModelsFinal.get(callEvent.getPlayerId()).getCallProbability(gameState));
					}
				}
			});
		}
		super.onCall(callEvent);
	}

	@Override
	public void onFold(final FoldEvent foldEvent) {
		if(playerContext.getGameState().getRound().equals(Round.FINAL)){
			executor.execute(new Runnable() {
				public void run() {
					opponentModelsFinal.get(foldEvent.getPlayerId()).addFold();
				}
			});
		}
		super.onFold(foldEvent);
	}

	@Override
	public void onCheck(final CheckEvent checkEvent) {
		if(playerContext.getGameState().getRound().equals(Round.FINAL)){
			executor.execute(new Runnable() {
				public void run() {
					opponentModelsFinal.get(checkEvent.getPlayerId()).addCheck();
				}
			});
		}
		super.onCheck(checkEvent);
	}

	@Override
	public void onRaise(final RaiseEvent raiseEvent) {
		if(playerContext.getGameState().getRound().equals(Round.FINAL)){
			executor.execute(new Runnable() {
				public void run() {
					opponentModelsFinal.get(raiseEvent.getPlayerId()).addRaise(raiseEvent.getAmount());
				}
			});
		}
		super.onRaise(raiseEvent);
	}

}
