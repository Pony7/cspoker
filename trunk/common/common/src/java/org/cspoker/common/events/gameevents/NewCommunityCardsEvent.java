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
package org.cspoker.common.events.gameevents;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;

/**
 * A class to represent new community cards events.
 * 
 * @author Kenzo
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class NewCommunityCardsEvent extends GameEvent {

	private static final long serialVersionUID = -5063239366087788741L;

	@XmlElementWrapper
	@XmlElement(name = "card")
	private Set<Card> communityCards;

	public NewCommunityCardsEvent(Set<Card> commonCards) {
		communityCards = Collections.unmodifiableSet(commonCards);
	}

	protected NewCommunityCardsEvent() {
		// no op
	}

	public Set<Card> getCommonCards() {
		return communityCards;
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
	public void dispatch(RemoteAllEventsListener listener)
			throws RemoteException {
		listener.onNewCommunityCardsEvent(this);
	}

}
