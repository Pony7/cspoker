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

public class SpeedTestBotListener extends DefaultBotListener{

	private final static Logger logger = Logger.getLogger(SpeedTestBotListener.class);
	
	private volatile int deals = 0;
	private volatile long startTime;

	private final int reportInterval;
	
	public SpeedTestBotListener() {
		this(64);
	}
	
	public SpeedTestBotListener(int reportInterval) {
		this.reportInterval = reportInterval;
	}
	
	@Override
	public void onNewDeal() {
		if(++deals == 1){
			startTime = System.currentTimeMillis();
		}else if(deals%reportInterval==0){
			long nowTime = System.currentTimeMillis();
			logger.warn("deal #"+deals+" in "+(nowTime-startTime)+"("+(deals*1000.0/(nowTime-startTime))+" average)");
		}
		
	}
	
}
