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
package org.cspoker.client.bots.simple;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.BotRunner;
import org.cspoker.client.bots.DefaultBot;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

public class CallBot extends DefaultBot {

	private final static Logger logger = Logger.getLogger(BotRunner.class);

	public CallBot(SmartLobbyContext lobbyguy, PlayerId playerID, TableId tableID, ExecutorService executor, boolean doOutput) {
		super(lobbyguy, playerID, tableID, executor, doOutput);
	}

	@Override
	public void doNextAction() {
		executor.execute(new Runnable(){
			@Override
			public void run() {
				try {
					playerContext.checkOrCall();
				} catch (IllegalActionException e) {
					logger.error(e);
					throw new IllegalStateException("Call was not allowed.",e);
				}catch (RemoteException e) {
					logger.error(e);
					throw new IllegalStateException("Call failed.",e);
				}
			}
		});
	}



}
