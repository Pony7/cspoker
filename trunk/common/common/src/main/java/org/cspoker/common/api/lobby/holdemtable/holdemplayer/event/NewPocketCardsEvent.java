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
package org.cspoker.common.api.lobby.holdemtable.holdemplayer.event;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.events.gameevents.GameEvent;
import org.cspoker.common.player.SeatedPlayer;

@XmlRootElement
public class NewPocketCardsEvent extends GameEvent {

	private static final long serialVersionUID = -3328895783353781276L;

	private SeatedPlayer player;

	@XmlElementWrapper
	@XmlElement(name = "card")
	private Set<Card> pocketCards;

	public NewPocketCardsEvent(SeatedPlayer player, Set<Card> pocketCards) {
		this.player = player;
		this.pocketCards = Collections.unmodifiableSet(pocketCards);
	}

	protected NewPocketCardsEvent() {
		// no op
	}

	public Set<Card> getPocketCards() {
		return pocketCards;
	}

	public String toString() {
		String toReturn = getPlayer().getName()
				+ " has received new pocket cards: ";
		for (Card card : getPocketCards()) {
			toReturn += card;
			toReturn += ", ";
		}
		return toReturn.substring(0, toReturn.length() - 2) + ".";
	}

	public SeatedPlayer getPlayer() {
		return player;
	}

	public void dispatch(RemoteAllEventsListener listener)
			throws RemoteException {
		listener.onNewPocketCardsEvent(this);
	}

}
