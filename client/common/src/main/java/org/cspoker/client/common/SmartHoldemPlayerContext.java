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
package org.cspoker.client.common;

import java.rmi.RemoteException;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.ForwardingRemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.player.PlayerId;

public class SmartHoldemPlayerContext
		extends ForwardingRemoteHoldemPlayerContext {
	
	private final static Logger logger = Logger.getLogger(SmartHoldemPlayerContext.class);
	private final TableState state;
	private final PlayerId playerId;
	
	
	public SmartHoldemPlayerContext(RemoteHoldemPlayerContext remoteHoldemPlayerContext,TableState state, PlayerId playerId) {
		super(remoteHoldemPlayerContext);
		this.state = state;
		this.playerId = playerId;
	}

	
	public GameState getGameState() {
		return state.getGameState();
	}
	
	public boolean havePocketPair() {
		Set<Card> cards = getGameState().getPlayer(playerId).getCards();
		Card previous = null;
		for (Card card : cards) {
			if (previous == null) {
				previous = card;
			} else {
				if (previous.getRank().equals(card.getRank())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean haveA(Rank rank) {
		Set<Card> cards = getGameState().getPlayer(playerId).getCards();
		for (Card card : cards) {
			if (card.getRank().equals(rank)) {
				return true;
			}
		}
		return false;
	}
	
	public void raiseMaxBetWith(int bet)
			throws RemoteException, IllegalActionException {
		int deficit = getGameState().getDeficit(playerId);
		if (deficit > bet) {
			logger.trace("Folding");
			fold();
		} else if (deficit == bet) {
			logger.trace("Calling");
			checkOrCall();
		} else {
			logger.trace("Raising with " + (bet - deficit));
			betOrRaise(bet - deficit);
		}
	}
	
}
