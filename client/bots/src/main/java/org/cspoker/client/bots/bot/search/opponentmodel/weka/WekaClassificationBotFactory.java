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
package org.cspoker.client.bots.bot.search.opponentmodel.weka;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.search.SearchBot;
import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.bots.bot.search.node.expander.SamplingExpander;
import org.cspoker.client.bots.bot.search.node.leaf.ShowdownNode;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor.Factory;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

import weka.classifiers.Classifier;

@ThreadSafe
public class WekaClassificationBotFactory implements BotFactory {
	
	private final String cbModel;
	private final String fcr_model ;
	
	private final static Logger logger = Logger
			.getLogger(WekaClassificationBotFactory.class);
	private static int copies = 0;

	private final int copy;

	private final Map<PlayerId, OpponentModel> opponentModels = new HashMap<PlayerId, OpponentModel>();
	private final org.cspoker.client.bots.bot.search.node.leaf.ShowdownNode.Factory showdownNodeFactory;
	private final Factory[] nodeVisitorFactories;

	public WekaClassificationBotFactory(ShowdownNode.Factory showdownNodeFactory, String cbModel, String fcrModel,
			NodeVisitor.Factory... nodeVisitorFactories) {
		copy = ++copies;
		this.showdownNodeFactory = showdownNodeFactory;
		this.nodeVisitorFactories = nodeVisitorFactories;
		this.cbModel = cbModel;
		this.fcr_model = fcrModel;
	}

	public synchronized Bot createBot(final PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		copies++;
		if (opponentModels.get(botId) == null) {
			Classifier callRaiseClassifier;
			Classifier checkBetClassifier;
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(fcr_model));
				callRaiseClassifier = (Classifier)in.readObject();
				in.close();
				in = new ObjectInputStream(new FileInputStream(cbModel));
				checkBetClassifier = (Classifier)in.readObject();
				in.close();
				WekaClassificationModel model = new WekaClassificationModel(checkBetClassifier,callRaiseClassifier);
				opponentModels.put(botId, model);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}
		SearchConfiguration config = new SearchConfiguration(opponentModels
				.get(botId), showdownNodeFactory,
				new SamplingExpander.Factory(), 2000, 4000, 8000, 16000, 0.25, false, true);
		return new SearchBot(botId, tableId, lobby, executor, config, buyIn,
				nodeVisitorFactories, botListeners);
	}

	@Override
	public String toString() {
		return "WekaClasificationSearchBotv1-" + copy;
	}
}
