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
package org.cspoker.client.savedstate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.cspoker.common.elements.cards.Card;

public class Cards {

	private Set<Card> privateCards;

	private Set<Card> riverCards;

	public Cards() {
		resetCards();
	}

	public synchronized Set<Card> getPrivateCards() {
		return Collections.unmodifiableSet(privateCards);
	}

	public synchronized Set<Card> getCommonCards() {
		return Collections.unmodifiableSet(riverCards);
	}

	public synchronized void resetCards() {
		privateCards = new HashSet<Card>(2);
		riverCards = new HashSet<Card>(5);
	}

	public synchronized void setPrivateCards(Set<Card> cards) {
		if (cards.size() != 2) {
			throw new IllegalArgumentException(
					"The number of private cards must be 2.");
		}
		if (privateCards.size() != 0) {
			throw new IllegalStateException("There already are private cards.");
		}
		privateCards.addAll(cards);
	}

	public synchronized void addCommonCards(Set<Card> cards) {
		if (riverCards.size() + cards.size() > 5) {
			throw new IllegalStateException(
					"Can't have more than 5 cards in the river.");
		}
		riverCards.addAll(cards);
	}

}
