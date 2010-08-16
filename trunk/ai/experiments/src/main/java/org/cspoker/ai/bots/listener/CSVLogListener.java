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
package org.cspoker.ai.bots.listener;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.cspoker.ai.bots.BotRunner;
import org.cspoker.ai.bots.bot.gametree.mcts.MCTSBot;
import org.cspoker.ai.bots.bot.gametree.mcts.nodes.Config;
import org.cspoker.ai.bots.util.RunningStats;
import org.cspoker.ai.opponentmodels.weka.ARFFPlayer;
import org.cspoker.ai.opponentmodels.weka.WekaLearningModel;
import org.cspoker.ai.opponentmodels.weka.WekaRegressionModel;
import org.cspoker.common.elements.player.PlayerId;

public class CSVLogListener extends DealCountingListener {

	private final static Logger logger = Logger
			.getLogger(CSVLogListener.class);

	private volatile long startTime;

	private final int reportInterval;

	private final BotRunner runner;

	private final FileWriter file;

	private long overallStartTime;

	public CSVLogListener(BotRunner runner) {
		this(64, runner, "bots");
	}

	public CSVLogListener(int reportInterval, BotRunner runner, String logName) {
		this.reportInterval = reportInterval;
		this.runner = runner;
		try {
			this.file = new FileWriter("logs/" + logName + ".csv");
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void onNewDeal() {
		int deals = getDeals();
		if (deals % reportInterval == 0) {
			long nowTime = System.currentTimeMillis();
			if (deals == 1)
				overallStartTime = startTime;
			if (startTime > 0) {
//				logger.info("deal #" + deals + " at " + reportInterval * 1000.0
//						/ (nowTime - startTime) + " games/s");
				try {
					file.write(deals+"");
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				for (int i = 0; i < runner.nbPlayersPerGame; i++) {
					try {
						if (modelCreated(runner.getBot(i).getId()))
							file.write("\tMODELCREATED");
						else
							file.write("\t");
						RunningStats profit = runner.getBot(i).getProfit();
						int smallBet = runner.getConfig().getSmallBet();
						double mean = profit.getMean() / smallBet;
						double stdDevMean = profit.getEVStdDev()/ smallBet;
						file.write("\t"+mean+"\t"+stdDevMean);
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
				}

				try {
					file.write("\t" + deals * 1000.0/ (nowTime - overallStartTime));
					file.write("\n");
					file.flush();
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
			}
			startTime = nowTime;
		}
		super.onNewDeal();
	}
	
	private static MCTSBot bot = null;
	
	private MCTSBot getLearningMCTSBot() {
		if (bot != null) return bot;
		
		for (int i = 0; i < runner.nbPlayersPerGame; i++) {
			try {
				bot = (MCTSBot) runner.getBot(i);
				Config config = bot.getConfig();
				WekaLearningModel model = (WekaLearningModel) config.getModel();
				if (model.getConfig().useOnlineLearning())
					return bot;
			} catch (Exception e) {
				bot = null;
			}
		}
		return null;
	}
	
	private boolean modelCreated(PlayerId actor) {
		try {
			MCTSBot bot = getLearningMCTSBot();
			if (bot.getId().equals(actor)) return false;
			Config config = bot.getConfig();
			WekaLearningModel model = (WekaLearningModel) config.getModel();
			ARFFPlayer player = model.getPlayer(actor);
			return player.learningAllowed();
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

}
