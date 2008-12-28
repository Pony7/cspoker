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

import java.util.concurrent.ConcurrentHashMap;

import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModel.OpponentModelFactory;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

public class AllPlayersModelFactory implements AllPlayersModel {

	private final ConcurrentHashMap<PlayerId, OpponentModel> opponentModels = new ConcurrentHashMap<PlayerId, OpponentModel>();
	private final OpponentModelFactory factory;
	
	public AllPlayersModelFactory(OpponentModelFactory factory) {
		this.factory = factory;
	}
	
	/* (non-Javadoc)
	 * @see org.cspoker.client.bots.bot.search.opponentmodel.IAllPlayersModel#getModelFor(org.cspoker.common.elements.player.PlayerId)
	 */
	public OpponentModel getModelFor(PlayerId opponentId){
		OpponentModel model = opponentModels.get(opponentId);
		if(model==null){
			opponentModels.putIfAbsent(opponentId, factory.create(opponentId));
			return opponentModels.get(opponentId);
		}
		return model;
	}

	@Override
	public void signalNextAction(GameState gameState) {
		// no op
	}

}
