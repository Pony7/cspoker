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
package org.cspoker.client.bots.bot.gametree.opponentmodel.weka;

import java.io.File;
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
import org.cspoker.client.bots.bot.gametree.opponentmodel.OpponentModel;
import org.cspoker.client.bots.bot.gametree.search.SearchBot;
import org.cspoker.client.bots.bot.gametree.search.SearchConfiguration;
import org.cspoker.client.bots.bot.gametree.search.ShowdownNode;
import org.cspoker.client.bots.bot.gametree.search.expander.SamplingExpander;
import org.cspoker.client.bots.bot.gametree.search.nodevisitor.NodeVisitor;
import org.cspoker.client.bots.bot.gametree.search.nodevisitor.NodeVisitor.Factory;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

import weka.classifiers.Classifier;

@ThreadSafe
public class WekaRegressionBotFactory implements BotFactory {

	private final File modelDir;
	
	private final static Logger logger = Logger
			.getLogger(WekaRegressionBotFactory.class);
	private static int copies = 0;

	private final int copy;

	private final Map<PlayerId, OpponentModel> opponentModels = new HashMap<PlayerId, OpponentModel>();
	private final org.cspoker.client.bots.bot.gametree.search.ShowdownNode.Factory showdownNodeFactory;
	private final Factory[] nodeVisitorFactories;

	public WekaRegressionBotFactory(ShowdownNode.Factory showdownNodeFactory, 
			String modelDir,
			NodeVisitor.Factory... nodeVisitorFactories) {
		copy = ++copies;
		this.showdownNodeFactory = showdownNodeFactory;
		this.nodeVisitorFactories = nodeVisitorFactories;
		this.modelDir = new File(modelDir);
	}

	public synchronized Bot createBot(final PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		copies++;
		if (opponentModels.get(botId) == null) {
			Classifier preBetModel, preFoldModel, preCallModel, preRaiseModel, postBetModel, postFoldModel, postCallModel, postRaiseModel;
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(modelDir,"preBet.model")));
				preBetModel = (Classifier)in.readObject();
				in.close();
				in = new ObjectInputStream(new FileInputStream(new File(modelDir,"preFold.model")));
				preFoldModel = (Classifier)in.readObject();
				in.close();
				in = new ObjectInputStream(new FileInputStream(new File(modelDir,"preCall.model")));
				preCallModel = (Classifier)in.readObject();
				in.close();
				in = new ObjectInputStream(new FileInputStream(new File(modelDir,"preRaise.model")));
				preRaiseModel = (Classifier)in.readObject();
				in.close();
				in = new ObjectInputStream(new FileInputStream(new File(modelDir,"postBet.model")));
				postBetModel = (Classifier)in.readObject();
				in.close();
				in = new ObjectInputStream(new FileInputStream(new File(modelDir,"postFold.model")));
				postFoldModel = (Classifier)in.readObject();
				in.close();
				in = new ObjectInputStream(new FileInputStream(new File(modelDir,"postCall.model")));
				postCallModel = (Classifier)in.readObject();
				in.close();
				in = new ObjectInputStream(new FileInputStream(new File(modelDir,"postRaise.model")));
				postRaiseModel = (Classifier)in.readObject();
				in.close();
				WekaRegressionModel model = new WekaRegressionModel(preBetModel, preFoldModel, preCallModel, preRaiseModel, postBetModel, postFoldModel, postCallModel, postRaiseModel);
				opponentModels.put(botId, model);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}
		SearchConfiguration config = new SearchConfiguration(opponentModels
				.get(botId), showdownNodeFactory,
				new SamplingExpander.Factory(), 500, 1000, 5000, 10000, 0, false, true);
		return new SearchBot(botId, tableId, lobby, executor, config, buyIn,
				nodeVisitorFactories, botListeners);
	}

	@Override
	public String toString() {
		return "WekaRegressionSearchBotv1-" + copy;
	}
}
