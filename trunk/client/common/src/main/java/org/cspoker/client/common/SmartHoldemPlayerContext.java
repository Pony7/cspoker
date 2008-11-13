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
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.ForwardingRemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;

public class SmartHoldemPlayerContext
		extends ForwardingRemoteHoldemPlayerContext {
	
	private final static Logger logger = Logger.getLogger(SmartHoldemPlayerContext.class);
	
	private final SmartHoldemPlayerListener smartPlayerListener;
	private final SmartHoldemTableListener smartTableListener;
	private final SmartClientContext smartClientContext;
	
	public SmartHoldemPlayerContext(RemoteHoldemPlayerContext remoteHoldemPlayerContext,
			SmartHoldemTableListener smartTableListener, SmartHoldemPlayerListener smartPlayerListener,
			SmartClientContext smartClientContext) {
		super(remoteHoldemPlayerContext);
		this.smartTableListener = smartTableListener;
		this.smartPlayerListener = smartPlayerListener;
		this.smartClientContext = smartClientContext;
	}
	
	public Set<Card> getPocketCards() {
		return smartPlayerListener.getPocketCards();
	}
	
	public boolean havePocketPair() {
		Set<Card> cards = getPocketCards();
		Card previous = null;
		for (Card card : cards) {
			if (previous == null) {
				previous = card;
			} else {
				if (previous.getRank().equals(card)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean haveA(Rank rank) {
		Set<Card> cards = getPocketCards();
		for (Card card : cards) {
			if (card.getRank().equals(rank)) {
				return true;
			}
		}
		return false;
	}
	
	public void raiseMaxBetWith(int bet)
			throws RemoteException, IllegalActionException {
		int deficit = smartTableListener.getTableInformationProvider().getToCall(
				smartClientContext.getAccountContext().getPlayerID());
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
