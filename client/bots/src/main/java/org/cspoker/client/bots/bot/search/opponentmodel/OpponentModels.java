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
public interface OpponentModels {

	/**
	 * Get the opponent model for the given player in the given game state.
	 */
	OpponentModel getModelFor(GameState gameState, PlayerId opponentId);

	/**
	 * Assume the given game state permanently.
	 */
	void signalNextAction(GameState gameState);

	/**
	 * Assume the given game state temporarily.
	 */
	void assume(GameState gameState);

	/**
	 * Forget the last assumption.
	 */
	void forgetAssumption();

}