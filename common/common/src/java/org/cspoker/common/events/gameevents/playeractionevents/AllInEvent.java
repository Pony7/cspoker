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

package org.cspoker.common.events.gameevents.playeractionevents;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.EventAndActionJAXBContext;
import org.cspoker.common.actions.SayAction;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import org.cspoker.common.elements.pots.Pots;
import org.cspoker.common.events.invokation.IllegalActionEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.player.Player;
import org.cspoker.common.player.PlayerId;

/**
 * A class to represent all-in events.
 * 
 * @author Kenzo
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AllInEvent extends ActionChangedPotEvent {

	private static final long serialVersionUID = 7331085187957524812L;

	/**
	 * The variable containing the saved player.
	 */
	private Player player;

	/**
	 * 
	 * @param player
	 */
	public AllInEvent(Player player, Pots pots) {
		super(pots);
		this.player = player;
	}

	protected AllInEvent() {
		super();
	}

	@Override
	public String toString() {
		return getPlayer().getName() + " goes all-in.";
	}

	public Player getPlayer() {
		return player;
	}
	
	public static void main(String[] args) throws JAXBException {
		Player p = new Player(new PlayerId(25L),"guy", 25,10);
		Pots o = new Pots(58);
		
		Set<Card> cards = new LinkedHashSet<Card>();
		cards.add(new Card(Rank.EIGHT, Suit.DIAMONDS));
		cards.add(new Card(Rank.KING, Suit.HEARTS));
		
		//Event e = new TableCreatedEvent(p, new TableId(5));
		//Event e = new SuccessfulInvokationEvent<TableId>(new SayAction(84654L, "kaka"),new TableId(5));
		Object e = new IllegalActionEvent(new IllegalActionException("not allowed"), new SayAction(4354L, "kaka"));
		
		Marshaller m = EventAndActionJAXBContext.context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FRAGMENT,true);
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		m.marshal( e, System.out );
	}

}
