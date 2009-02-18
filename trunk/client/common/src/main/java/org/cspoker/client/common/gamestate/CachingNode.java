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
package org.cspoker.client.common.gamestate;

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvent;
import org.cspoker.common.elements.table.Round;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class CachingNode extends ForwardingGameState {

	private final Supplier<Round> round = Suppliers.memoize(new Supplier<Round>(){
		public Round get() {
			return CachingNode.super.getRound();
		};
	});
	
	
	public CachingNode(GameState gameState) {
		super(gameState);
	}

	@Override
	public void acceptVisitor(GameStateVisitor visitor) {
		//no op
	}

	@Override
	public HoldemTableTreeEvent getLastEvent() {
		return null;
	}
	
	@Override
	public Round getRound() {
		return round.get();
	}

}
