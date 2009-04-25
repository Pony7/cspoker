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
package org.cspoker.client.bots.bot.gametree.search;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.gametree.opponentmodel.OpponentModel;
import org.cspoker.client.bots.bot.gametree.search.expander.SamplingExpander;
import org.cspoker.client.bots.bot.gametree.search.nodevisitor.NodeVisitor;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

public class SearchBotFactory implements BotFactory {

	private static int copies = 0;
	private final int copy;

	private final HashMap<PlayerId, OpponentModel> opponentModels = new HashMap<PlayerId, OpponentModel>();
	private final NodeVisitor.Factory[] nodeVisitorFactories;
	private final OpponentModel.Factory modelFactory;
	private final ShowdownNode.Factory showdownNodeFactory;

	public SearchBotFactory(OpponentModel.Factory modelFactory, 
			ShowdownNode.Factory showdownNodeFactory,
			NodeVisitor.Factory... nodeVisitorFactories) {
		copy = ++copies;
		this.modelFactory = modelFactory;
		this.nodeVisitorFactories = nodeVisitorFactories;
		this.showdownNodeFactory = showdownNodeFactory;
	}

	/**
	 * @see org.cspoker.client.bots.bot.BotFactory#createBot(org.cspoker.common.elements.player.PlayerId,
	 *      org.cspoker.common.elements.table.TableId,
	 *      org.cspoker.client.common.SmartLobbyContext,
	 *      java.util.concurrent.ExecutorService,
	 *      org.cspoker.client.bots.listener.BotListener[])
	 */
	public synchronized Bot createBot(final PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		copies++;

		OpponentModel opponentModel = opponentModels.get(botId);
		if(opponentModel==null){
			opponentModel = modelFactory.create();
			opponentModels.put(botId, opponentModel);
		}
		SearchConfiguration config = new SearchConfiguration(opponentModel, showdownNodeFactory,
				new SamplingExpander.Factory(), 500, 1000, 2000, 5000, 0, false, true);
		return new SearchBot(botId, tableId, lobby, executor, config, buyIn,
				nodeVisitorFactories, botListeners);
	}

	@Override
	public String toString() {
		return "SearchBot v2-" + copy;
	}
}
