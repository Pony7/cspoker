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

import org.apache.log4j.Logger;
import org.cspoker.ai.bots.util.Gaussian;
import org.cspoker.ai.bots.util.RunningStats;

public class ProfitListener extends DealCountingListener {

	private final static Logger logger = Logger
	.getLogger(ProfitListener.class);

	private volatile long startTime;

	private final int reportInterval;

	private final ProfitInfo profitInfo;

	public ProfitListener(ProfitInfo profitCalculator) {
		this(64, profitCalculator);
	}

	public ProfitListener(int reportInterval, ProfitInfo profitInfo) {
		this.reportInterval = reportInterval;
		this.profitInfo = profitInfo;
	}

	@Override
	public void onNewDeal() {
		int deals = getDeals();
		if (deals % reportInterval == 0) {
			long nowTime = System.currentTimeMillis();
			if (startTime > 0) {
				logger.info("deal #" + deals + " at " + reportInterval * 1000.0
						/ (nowTime - startTime) + " games/s");
				RunningStats profit = profitInfo.getProfit();
				int smallBet = profitInfo.getTableConfiguration().getSmallBet();
				double avgProfit = (profit.getMean() / smallBet);
				double stdDev = (profit.getEVStdDev()/ smallBet);
				double prob = Gaussian.bigPhi(avgProfit/stdDev);
				logger.info("(" + profitInfo.getBotName() + " wins "
						+ (float)avgProfit + " sb/game) "
						+ " +- "+(float)stdDev+" ("+(float)prob+"% profitable)");
			}
			startTime = nowTime;
		}
		super.onNewDeal();
	}

}
