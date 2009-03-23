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
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor.Factory;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

public class SearchBot
extends AbstractBot {

	private final static Logger logger = Logger.getLogger(SearchBot.class);

	private int searchId = 0;
	private final SearchConfiguration config;

	private final Factory[] nodeVisitorFactories;

	public SearchBot(PlayerId playerId, TableId tableId,
			SmartLobbyContext lobby, ExecutorService executor,
			SearchConfiguration config,
			int buyIn,
			NodeVisitor.Factory[] nodeVisitorFactories,
			BotListener... botListeners) {
		super(playerId, tableId, lobby, buyIn, executor, botListeners);
		this.config = config;
		this.nodeVisitorFactories = nodeVisitorFactories;
	}

	@Override
	public void doNextAction() {
		executor.execute(new Runnable() {
			public void run() {
				try {
					GameState gameState = tableContext.getGameState();
					NodeVisitor[] visitors = new NodeVisitor[nodeVisitorFactories.length];
					for (int i = 0; i < visitors.length; i++) {
						visitors[i] = nodeVisitorFactories[i].create(gameState, playerId);
					}
					BotActionNode actionNode;
					//essential to do this with a clean game state from the context, no wrappers
					config.getOpponentModeler().signalNextAction(gameState);
					switch (gameState.getRound()) {
					case PREFLOP:
						logger.debug("Searching preflop round game tree:");
						actionNode = new BotActionNode(playerId, gameState, 
								config, config.getPreflopTokens(),
								searchId++, visitors);
						actionNode.performbestAction(playerContext);
						break;
					case FLOP:
						logger.debug("Searching flop round game tree:");
						actionNode = new BotActionNode(playerId, gameState, 
								config, config.getFlopTokens(),
								searchId++, visitors);
						actionNode.performbestAction(playerContext);
						break;
					case TURN:
						logger.debug("Searching turn round game tree:");
						actionNode = new BotActionNode(playerId, gameState, 
								config, config.getTurnTokens(), 
								searchId++, visitors);
						actionNode.performbestAction(playerContext);
						break;
					case FINAL:
						logger.debug("Searching final round game tree:");
						actionNode = new BotActionNode(playerId, gameState, 
								config, config.getFinalTokens(), 
								searchId++, visitors);
						actionNode.performbestAction(playerContext);
						break;
					default:
						throw new IllegalStateException("What round are we in?");
					}		
				} catch (IllegalActionException e) {
					logger.warn("Raise bounds: "+tableContext.getGameState().getLowerRaiseBound(SearchBot.this.playerId)+" to "+tableContext.getGameState().getUpperRaiseBound(SearchBot.this.playerId));
					logger.error(e);
					throw new IllegalStateException("Action was not allowed.",e);
				} catch (RemoteException e) {
					logger.error(e);
					throw new IllegalStateException("Action failed.",e);
				}catch(StackOverflowError e){
					e.printStackTrace();
					logger.error(e);
					try {
						playerContext.checkOrCall();
					} catch (RemoteException e1) {
						logger.error(e1);
						throw new IllegalStateException("Action failed.",e1);
					} catch (IllegalActionException e1) {
						logger.error(e1);
						throw new IllegalStateException("Action was not allowed.",e1);
					}
				}
			}
		});
	}

}
