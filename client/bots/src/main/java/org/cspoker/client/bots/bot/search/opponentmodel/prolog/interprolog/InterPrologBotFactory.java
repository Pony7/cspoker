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
package org.cspoker.client.bots.bot.search.opponentmodel.prolog.interprolog;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.search.SearchBot;
import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.bots.bot.search.node.leaf.UniformShowdownNode;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

import com.declarativa.interprolog.SWISubprocessEngine;

@ThreadSafe
public class InterPrologBotFactory implements BotFactory {
	
	private final static Logger logger = Logger.getLogger(InterPrologBotFactory.class);
	private static int copies = 0;
	
	private final int copy;

	private final Map<PlayerId, AllPlayersModel> opponentModels = new ConcurrentHashMap<PlayerId, AllPlayersModel>();

	public InterPrologBotFactory() {
		this.copy = ++copies;
	}

	/**
	 * @see org.cspoker.client.bots.bot.BotFactory#createBot(org.cspoker.common.elements.player.PlayerId, org.cspoker.common.elements.table.TableId, org.cspoker.client.common.SmartLobbyContext, java.util.concurrent.ExecutorService, org.cspoker.client.bots.listener.BotListener[])
	 */
	public synchronized Bot createBot(final PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, ExecutorService executor,
			BotListener... botListeners) {
		copies++;
		if(opponentModels.get(botId)==null){
			SWISubprocessEngine prologEngine = new SWISubprocessEngine("/usr/lib/swi-prolog/bin/i386/swipl",
					logger.isTraceEnabled());
			File backgroundDir = new File("/home/guy/Werk/thesis/opponentmodel/swified");
			prologEngine.consultAbsolute(new File(backgroundDir, "background.pl"));
			prologEngine.consultAbsolute(new File(backgroundDir, "model.pl"));
			InterPrologModel model = new InterPrologModel(prologEngine, botId);
			opponentModels.put(botId, model);
		}
		SearchConfiguration config = new SearchConfiguration(opponentModels.get(botId), 
				new UniformShowdownNode.Factory(),
				1,10,100);
		return new SearchBot(botId, tableId, lobby, executor, config ,botListeners);
	}

	@Override
	public String toString() {
		return "PrologSearchBotv1-"+copy;
	}
}
