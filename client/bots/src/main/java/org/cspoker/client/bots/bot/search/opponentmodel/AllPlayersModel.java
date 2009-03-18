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
package org.cspoker.client.bots.bot.search.opponentmodel;

import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

/**
 * A collection of opponent models related to the same game.
 * @author guy
 *
 */
public interface AllPlayersModel {

	OpponentModel getModelFor(PlayerId opponentId, GameState gameState);

	/**
	 * Assume the given game state permanently.
	 * @param gameState
	 */
	void signalNextAction(GameState gameState);

	/**
	 * Assume the given game state temporarily.
	 * @param gameState
	 */
	void assume(GameState gameState);

	/**
	 * Forget the last assumption.
	 */
	void forgetAssumption();

}