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
package org.cspoker.client.bots.bot.search.node;

import java.util.Set;

import net.jcip.annotations.Immutable;

import org.cspoker.client.bots.bot.search.action.ActionWrapper;
import org.cspoker.client.bots.bot.search.action.EvaluatedAction;
import org.cspoker.client.bots.bot.search.action.ProbabilityAction;
import org.cspoker.client.bots.bot.search.action.SearchBotAction;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.util.Pair;

@Immutable
public interface InnerGameTreeNode extends GameTreeNode{

	Pair<Double, Double> getEV();

	<A extends ActionWrapper> EvaluatedAction<A> expandWith(A action, int tokens);

	GameState getGameState();

	Set<ProbabilityAction> getProbabilityActions();

	Set<SearchBotAction> getAllPossibleActions();

}
