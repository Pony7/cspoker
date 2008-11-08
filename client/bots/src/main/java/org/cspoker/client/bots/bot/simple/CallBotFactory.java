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
package org.cspoker.client.bots.bot.simple;

import java.util.concurrent.ExecutorService;

import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

public class CallBotFactory implements BotFactory {

	private static int copies = 0;
	private final int copy;

	public CallBotFactory() {
		this.copy = ++copies;
	}
	
	@Override
	public Bot createBot(PlayerId playerId, TableId tableId,
			SmartLobbyContext lobby, ExecutorService executor,
			BotListener... botListeners) {
		copies++;
		return new CallBot(playerId, tableId, lobby, executor, botListeners);
	}

	@Override
	public String toString() {
		return "CallBotv1-"+copy;
	}
}
