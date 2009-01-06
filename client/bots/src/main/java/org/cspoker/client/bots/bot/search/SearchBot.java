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
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.AbstractBot;
import org.cspoker.client.bots.bot.search.node.BotActionNode;
import org.cspoker.client.bots.bot.search.node.CachingNode;
import org.cspoker.client.bots.bot.search.node.visitor.Log4JOutputVisitor;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

public class SearchBot
extends AbstractBot {

	private final static Logger logger = Logger.getLogger(SearchBot.class);

	private int searchId = 0;
	private final SearchConfiguration config;

	public SearchBot(PlayerId playerId, TableId tableId,
			SmartLobbyContext lobby, ExecutorService executor,
			SearchConfiguration config,
			BotListener... botListeners) {
		super(playerId, tableId, lobby, executor, botListeners);
		this.config = config;
	}

	@Override
	public void doNextAction() {
		executor.execute(new Runnable() {
			public void run() {
				try {
					BotActionNode actionNode;
					GameState gameState = tableContext.getGameState();
					//essential to do this with a clean game state from the context
					config.getOpponentModeler().signalNextAction(gameState);
					gameState = new CachingNode(gameState);
					switch (gameState.getRound()) {
					case PREFLOP:
						playerContext.checkOrCall();
						break;
					case FLOP:
						logger.debug("Searching flop round game tree:");
						playerContext.checkOrCall();
						break;
					case TURN:
						logger.debug("Searching turn round game tree:");
						playerContext.checkOrCall();
						break;
					case FINAL:
						logger.debug("Searching final round game tree:");
						actionNode = new BotActionNode(playerID, playerContext.getGameState(), 
								config, config.getFinalTokens(), 
								searchId++, new Log4JOutputVisitor(3));
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
	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		config.getOpponentModeler().signalNextAction(tableContext.getGameState());
		super.onNextPlayer(nextPlayerEvent);
	}

}
