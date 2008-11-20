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
package org.cspoker.common.elements.player;

import java.util.EnumSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.cspoker.common.elements.cards.Card;

public class ShowdownPlayer extends Player {

	private static final long serialVersionUID = -1618593137613219527L;

	@XmlElementWrapper
	@XmlElement(name = "card")
	private EnumSet<Card> cards;

	private String description;

	@XmlElementWrapper
	@XmlElement(name = "card")
	private EnumSet<Card> handCards;

	public ShowdownPlayer(Player player, EnumSet<Card> cards, EnumSet<Card> handCards,
			String description) {
		super(player);
		this.handCards = EnumSet.copyOf(handCards);
		this.cards = EnumSet.copyOf(cards);
		this.description = description;
	}

	protected ShowdownPlayer() {
		// no op
	}

	@Override
	public String toString() {
		return getName() + " has a " + description;
	}

	public EnumSet<Card> getAllCards() {
		return EnumSet.copyOf(cards);
	}

	public EnumSet<Card> getHandCards() {
		return EnumSet.copyOf(handCards);
	}

	public String getHandDescription() {
		return description;
	}

}
