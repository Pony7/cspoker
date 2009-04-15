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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.gametree.opponentmodel.simple.HistogramModel;
import org.cspoker.client.bots.bot.gametree.search.expander.SamplingExpander;
import org.cspoker.client.bots.bot.gametree.search.nodevisitor.NodeVisitor;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

public class SearchBotFactory implements BotFactory {

	private static int copies = 0;
	private final int copy;

	private final ConcurrentHashMap<PlayerId, HistogramModel> opponentModels = new ConcurrentHashMap<PlayerId, HistogramModel>();
	private final ShowdownNode.Factory showdownNodeFactory;
	private final NodeVisitor.Factory[] nodeVisitorFactories;

	public SearchBotFactory(ShowdownNode.Factory showdownNodeFactory,
			NodeVisitor.Factory... nodeVisitorFactories) {
		copy = ++copies;
		this.showdownNodeFactory = showdownNodeFactory;
		this.nodeVisitorFactories = nodeVisitorFactories;
	}

	/**
	 * @see org.cspoker.client.bots.bot.BotFactory#createBot(org.cspoker.common.elements.player.PlayerId,
	 *      org.cspoker.common.elements.table.TableId,
	 *      org.cspoker.client.common.SmartLobbyContext,
	 *      java.util.concurrent.ExecutorService,
	 *      org.cspoker.client.bots.listener.BotListener[])
	 */
	public Bot createBot(final PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		copies++;
		opponentModels.putIfAbsent(botId, new HistogramModel(botId));

		SearchConfiguration config = new SearchConfiguration(opponentModels
				.get(botId), showdownNodeFactory,
				new SamplingExpander.Factory(), 20000, 40000, 80000, 160000, 0.25, false, true);
		return new SearchBot(botId, tableId, lobby, executor, config, buyIn,
				nodeVisitorFactories, botListeners);
	}

	@Override
	public String toString() {
		return "SearchBot (" + showdownNodeFactory + ") v1-" + copy;
	}
}
