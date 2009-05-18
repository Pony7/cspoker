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
package org.cspoker.client.bots.bot.gametree.mcts;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.AbstractBot;
import org.cspoker.client.bots.bot.gametree.mcts.listeners.MCTSListener;
import org.cspoker.client.bots.bot.gametree.mcts.nodes.Config;
import org.cspoker.client.bots.bot.gametree.mcts.nodes.INode;
import org.cspoker.client.bots.bot.gametree.mcts.nodes.RootNode;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

public class MCTSBot extends AbstractBot {

	private final static Logger logger = Logger.getLogger(MCTSBot.class);
	private final Config config;
	private final MCTSListener.Factory[] MCTSlistenerFactories;

	public MCTSBot(PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, ExecutorService executor, int buyIn, 
			Config config,
			MCTSListener.Factory[] MCTSlisteners, 
			BotListener... botListeners) {
		super(botId, tableId, lobby, buyIn, executor, botListeners);
		this.config = config;
		this.MCTSlistenerFactories = MCTSlisteners;
	}

	@Override
	public void doNextAction() throws RemoteException, IllegalActionException {
		long endTime = System.currentTimeMillis()+config.getDecisionTime();
		GameState gameState = tableContext.getGameState();	
		RootNode root = new RootNode(gameState,botId,config);
		do{
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
			iterate(root);
		}while(System.currentTimeMillis()<endTime);
		if(logger.isInfoEnabled()){
			logger.info("Stopped MCTS.");
		}
		root.selectChild(config.getMoveSelectionStrategy()).getLastAction().getAction().perform(playerContext);
		MCTSListener[] listeners = createListeners(gameState, botId);
		for (MCTSListener listener : listeners) {
			listener.onMCTS(root);
		}
	}

	private void iterate(RootNode root) {
		INode selectedLeaf = root.selectRecursively();
		selectedLeaf.expand();
		double value = selectedLeaf.simulate();
		selectedLeaf.backPropagate(value);
	}

	private MCTSListener[] createListeners(GameState gameState, PlayerId actor) {
		MCTSListener[] listeners = new MCTSListener[MCTSlistenerFactories.length];
		for (int i=0;i<MCTSlistenerFactories.length;i++) {
			listeners[i] = MCTSlistenerFactories[i].create(gameState, actor);
		}
		return listeners;
	}

}
