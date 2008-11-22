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
package org.cspoker.client.bots.bot.search;

import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;

public interface OpponentModel {

	double getBetProbability(GameState gameState);

	double getCallProbability(GameState gameState);

	double getCheckProbability(GameState gameState);

	double getFoldProbability(GameState gameState);

	double getRaiseProbability(GameState gameState);

	void addAllIn(GameState gameState, AllInEvent allInEvent);
	
	void addCheck();
	
	void addBet(int amount);

	void addCall();

	void addRaise(int raise);

	void addFold();
}
