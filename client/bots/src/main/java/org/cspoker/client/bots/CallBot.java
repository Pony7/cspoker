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
package org.cspoker.client.bots;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public class CallBot extends DefaultBot {

	private final static Logger logger = Logger.getLogger(BotRunner.class);
	
	private final String name;
	private final HoldemTableContext tableContext;
	private final HoldemPlayerContext playerContext;
	private long deals = 1;

	public CallBot(LobbyContext lobbyContext, String name, long tableID) {
		this.name=name;
		try {
			tableContext = lobbyContext.joinHoldemTable(tableID, this);
			playerContext = tableContext.sitIn(1000, this);
		} catch (IllegalActionException e) {
			logger.error(e);
			throw new IllegalArgumentException("Failed to join table.",e);
		}
	}
	
	@Override
	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		logger.info("Next player is: "+nextPlayerEvent.getPlayer().getName());
		if(nextPlayerEvent.getPlayer().getName().equalsIgnoreCase(name)){
			try {
				logger.info(name+" calls");
				playerContext.checkOrCall();
			} catch (IllegalActionException e) {
				logger.error(e);
				throw new IllegalStateException("Call was not allowed.",e);
			}
		}
	}
	
	@Override
	public void onNewDeal(NewDealEvent newDealEvent) {
		System.out.println((deals++)+": "+newDealEvent);
	}
	
	
	
}
