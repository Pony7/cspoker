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

import org.cspoker.ai.bots.BotRunner;

public class GameLimitingBotListener extends DealCountingListener {

	private final int maxNbGames;

	private final BotRunner botRunner;

	public GameLimitingBotListener(BotRunner botRunner) {
		this(botRunner, 10000);
	}

	public GameLimitingBotListener(BotRunner botRunner, int maxNbGames) {
		this.maxNbGames = maxNbGames;
		this.botRunner = botRunner;
	}

	@Override
	public void onNewDeal() {
		if (getDeals() == maxNbGames) {
			botRunner.moveToNextCombination();
		}
		super.onNewDeal();
	}

}
