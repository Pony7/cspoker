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
package org.cspoker.client.bots.bot.gametree.search.expander;

import java.util.List;

import org.cspoker.client.bots.bot.gametree.action.ActionWrapper;
import org.cspoker.client.bots.bot.gametree.search.GameTreeNode;
import org.cspoker.client.bots.bot.gametree.search.InnerGameTreeNode;
import org.cspoker.common.util.Pair;

public abstract class TokenExpander extends Expander{

	public final int tokens;
	protected final InnerGameTreeNode node;
	
	public TokenExpander(InnerGameTreeNode node, int tokens) {
		super(node.getGameState(), node.getOpponentModel(), node.getPlayerId(), node.getBotId());
		this.tokens = tokens;
		this.node = node;
	}
	
	public abstract List<Pair<ActionWrapper,GameTreeNode>> getChildren(boolean uniformTokens);


	public static interface Factory extends Expander.Factory {
		TokenExpander create(InnerGameTreeNode node, int tokens);
	}
}
