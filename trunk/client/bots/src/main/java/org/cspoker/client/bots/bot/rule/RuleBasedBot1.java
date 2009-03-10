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
package org.cspoker.client.bots.bot.rule;

import java.rmi.RemoteException;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.AbstractBot;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.TableId;

public class RuleBasedBot1
		extends AbstractBot {
	
	private final static Logger logger = Logger.getLogger(RuleBasedBot1.class);
	
	private final Random random = new Random();

	public RuleBasedBot1(PlayerId playerId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		super(playerId, tableId, lobby, buyIn, executor, botListeners);
	}
	
	@Override
	public void doNextAction() {
		executor.execute(new Runnable() {
			
			public void run() {
				try {
					if (tableContext.getGameState().getRound().equals(Round.PREFLOP)) {
						playerContext.checkOrCall();
					} else {
						int min,max;
						if(playerContext.haveA(Rank.QUEEN)){
							min = 0;
							max = 2;
						}else if(playerContext.haveA(Rank.KING)){
							min = 1;
							max = 2;
						}else if(playerContext.haveA(Rank.ACE)){
							min = 2;
							max = 3;
						}else if(playerContext.havePocketPair()){
							min = 3;
							max = 5;
						}else{
							min = 0;
							max = 0;
						}
						float r = random.nextFloat();
						if (r < 0.05) {
							min++;
						}else if (r < 0.1) {
							min--;
						}
						r = random.nextFloat();
						if (r < 0.05) {
							max++;
						}else if (r < 0.1) {
							max--;
						}
						
						min = (int)((min+(-1+2*random.nextDouble()))*playerContext.getGameState().getTableConfiguration().getBigBlind());
						max = (int)((max+(-1+2*random.nextDouble()))*playerContext.getGameState().getMinNextRaise());

						min = Math.max(0,Math.min(max, min));
						max = Math.max(0, Math.max(max, min));
						
						playerContext.raiseMaxBetWith(min,max);
					}
				} catch (IllegalActionException e) {
					logger.warn("Raise bounds: "+tableContext.getGameState().getLowerRaiseBound(RuleBasedBot1.this.playerId)+" to "+tableContext.getGameState().getUpperRaiseBound(RuleBasedBot1.this.playerId));
					logger.error(e);
					throw new IllegalStateException("Action was not allowed.",e);
				} catch (RemoteException e) {
					logger.error(e);
					throw new IllegalStateException("Action failed.",e);
				}
			}
		});
	}
	
}
