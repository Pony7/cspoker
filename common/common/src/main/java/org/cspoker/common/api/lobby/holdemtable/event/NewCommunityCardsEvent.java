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
package org.cspoker.common.api.lobby.holdemtable.event;

import java.util.EnumSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.cards.Card;

/**
 * A class to represent new community cards events.
 * 
 */
@Immutable
public class NewCommunityCardsEvent extends HoldemTableEvent {

	private static final long serialVersionUID = -5063239366087788741L;

	//JAXB doesn't like EnumSets
	@XmlElementWrapper
	@XmlElement(name = "card")
	private final Set<Card> communityCards;

	public NewCommunityCardsEvent(EnumSet<Card> commonCards) {
		communityCards = EnumSet.copyOf(commonCards);
	}

	protected NewCommunityCardsEvent() {
		communityCards = null;
	}

	public EnumSet<Card> getCommunityCards() {
		return EnumSet.copyOf(communityCards);
	}

	@Override
	public String toString() {
		String toReturn = "New Community Cards: ";
		for (Card card : communityCards) {
			toReturn += card;
			toReturn += ", ";
		}
		return toReturn.substring(0, toReturn.length() - 2) + ".";
	}
	
	@Override
	public void dispatch(HoldemTableListener holdemTableListener) {
		holdemTableListener.onNewCommunityCards(this);
	}

}
