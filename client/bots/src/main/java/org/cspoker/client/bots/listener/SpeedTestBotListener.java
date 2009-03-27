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
package org.cspoker.client.bots.listener;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.BotRunner;

public class SpeedTestBotListener extends DealCountingListener {

	private final static Logger logger = Logger
			.getLogger(SpeedTestBotListener.class);

	private volatile long startTime;

	private final int reportInterval;

	private final BotRunner runner;

	public SpeedTestBotListener(BotRunner runner) {
		this(64, runner);
	}

	public SpeedTestBotListener(int reportInterval, BotRunner runner) {
		this.reportInterval = reportInterval;
		this.runner = runner;
	}

	@Override
	public void onNewDeal() {
		int deals = getDeals();
		if (deals % reportInterval == 0) {
			long nowTime = System.currentTimeMillis();
			if (startTime > 0) {
				logger.info("deal #" + deals + " at " + reportInterval * 1000.0
						/ (nowTime - startTime) + " games/s");
				for (int i = 0; i < runner.nbPlayersPerGame; i++) {
					logger.info("(" + runner.getBotFactory(i) + " wins "
							+ runner.getBotProfit(i) / deals + " sb/game)");
				}
			}
			startTime = nowTime;
		}
		super.onNewDeal();
	}

}
