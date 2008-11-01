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

import java.util.Set;

import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.PotsChangedEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.pots.Pots;

public class SmartHoldemTableListener extends ForwardingHoldemTableListener {

	private volatile Pots pots;
	private volatile Set<Card> communityCards;

	public SmartHoldemTableListener(HoldemTableListener holdemTableListener) {
		super(holdemTableListener);
	}

	public Pots getPots() {
		return pots;
	}
	
	public Set<Card> getCommunityCards() {
		return communityCards;
	}
	
	@Override
	public void onPotsChanged(PotsChangedEvent potsChangedEvent) {
		pots = potsChangedEvent.getPots();
		super.onPotsChanged(potsChangedEvent);
	}
	
	@Override
	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) {
		this.communityCards = newCommunityCardsEvent.getCommunityCards();
		super.onNewCommunityCards(newCommunityCardsEvent);
	}
}
