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
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;

public class ReSitInBotListener extends DefaultBotListener{

	private final static Logger logger = Logger.getLogger(ReSitInBotListener.class);
	private final BotRunner runner;
	
	
	public ReSitInBotListener(BotRunner runner) {
		this.runner = runner;
	}
	
	@Override
	public void onSitOut(SitOutEvent event) {
		runner.respawnBots();
	}
	
}
