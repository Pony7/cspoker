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
import java.io.FileNotFoundException;
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
import org.cspoker.client.bots.bot.search.node.leaf.UniformShowdownNode;
import org.cspoker.client.bots.bot.search.node.visitor.Log4JOutputVisitor;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor.Factory;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

import weka.classifiers.Classifier;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Prolog;
import alice.tuprolog.Theory;

@ThreadSafe
public class WekaBotFactory implements BotFactory {

	private final static Logger logger = Logger
			.getLogger(WekaBotFactory.class);
	private static int copies = 0;

	private final int copy;

	private final Map<PlayerId, OpponentModel> opponentModels = new HashMap<PlayerId, OpponentModel>();
	private final org.cspoker.client.bots.bot.search.node.leaf.ShowdownNode.Factory showdownNodeFactory;
	private final Factory[] nodeVisitorFactories;

	public WekaBotFactory() {
		this(new UniformShowdownNode.Factory(),
				new NodeVisitor.Factory[] { new Log4JOutputVisitor.Factory(2) });
	}

	public WekaBotFactory(ShowdownNode.Factory showdownNodeFactory,
			NodeVisitor.Factory... nodeVisitorFactories) {
		copy = ++copies;
		this.showdownNodeFactory = showdownNodeFactory;
		this.nodeVisitorFactories = nodeVisitorFactories;
	}

	public synchronized Bot createBot(final PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		copies++;
		if (opponentModels.get(botId) == null) {
			Classifier callRaiseClassifier;
			Classifier checkBetClassifier;
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream("/home/guy/Bureaublad/weka-3-6-0/ANN-c-fcr.model"));
				callRaiseClassifier = (Classifier)in.readObject();
				in.close();
				in = new ObjectInputStream(new FileInputStream("/home/guy/Bureaublad/weka-3-6-0/ANN-c-cb.model"));
				checkBetClassifier = (Classifier)in.readObject();
				in.close();
				WekaClassifierModel model = new WekaClassifierModel(checkBetClassifier,callRaiseClassifier);
				opponentModels.put(botId, model);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}
		SearchConfiguration config = new SearchConfiguration(opponentModels
				.get(botId), showdownNodeFactory,
				new SamplingExpander.Factory(), 2000, 80000, 10000, 25000, 0.25);
		return new SearchBot(botId, tableId, lobby, executor, config, buyIn,
				nodeVisitorFactories, botListeners);
	}

	@Override
	public String toString() {
		return "TuPrologSearchBotv1-" + copy;
	}
}
