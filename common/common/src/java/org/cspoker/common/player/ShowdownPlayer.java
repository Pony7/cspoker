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
package org.cspoker.common.player;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.cspoker.common.elements.cards.Card;

@XmlAccessorType(XmlAccessType.FIELD)
public class ShowdownPlayer implements Serializable {

	private static final long serialVersionUID = -1618593137613219527L;

	private SeatedPlayer player;

	@XmlElementWrapper
	@XmlElement(name = "card")
	private Set<Card> cards;

	private String description;

	@XmlElementWrapper
	@XmlElement(name = "card")
	private Set<Card> handCards;

	public ShowdownPlayer(SeatedPlayer player, Set<Card> cards, Set<Card> handCards,
			String description) {
		this.player = player;
		this.handCards = Collections.unmodifiableSet(handCards);
		this.cards = Collections.unmodifiableSet(cards);
		this.description = description;
	}

	protected ShowdownPlayer() {
		// no op
	}

	/**
	 * Returns a textual representation of this showdown player.
	 */

	public String toString() {
		return player.getName() + " has a " + description;
	}

	public SeatedPlayer getPlayer() {
		return player;
	}

	public Set<Card> getAllCards() {
		return cards;
	}

	public Set<Card> getHandCards() {
		return handCards;
	}

	public String getHandDescription() {
		return description;
	}

}
