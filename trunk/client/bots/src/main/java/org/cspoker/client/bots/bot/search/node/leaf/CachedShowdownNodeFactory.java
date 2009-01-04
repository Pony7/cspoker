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
package org.cspoker.client.bots.bot.search.node.leaf;

import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.util.Pair;

import com.google.common.base.ReferenceType;
import com.google.common.collect.ReferenceMap;

/**
 * Caches leaf nodes when they are independent of the rest of the tree for a given search instance.
 * 
 * @author guy
 *
 */
public class CachedShowdownNodeFactory implements ShowdownNode.Factory{

	private final ReferenceMap<Pair<SearchConfiguration,Integer>,Double> cache 
		= new ReferenceMap<Pair<SearchConfiguration,Integer>,Double>(ReferenceType.STRONG, ReferenceType.STRONG);
	private final AbstractShowdownNode.Factory factory;

	public CachedShowdownNodeFactory(AbstractShowdownNode.Factory factory) {
		this.factory = factory;
	}

	
	@Override
	public ShowdownNode create(final PlayerId botId, final GameState gameState,
			final int tokens, final SearchConfiguration config, final int searchId) {
		final Pair<SearchConfiguration,Integer> id = new Pair<SearchConfiguration, Integer>(config, searchId);
		return new AbstractShowdownNode(botId, gameState){
			
			@Override
			public double getExpectedPotPercentage() {
				Double EV = cache.get(id);
				if(EV==null){
					EV = factory.create(botId, gameState, tokens*100, config, searchId).getExpectedPotPercentage();
					cache.put(id, EV);
				}
				return EV;
			}
		};
	}
	
	@Override
	public String toString() {
		return "Cached "+factory.toString();
	}

}
