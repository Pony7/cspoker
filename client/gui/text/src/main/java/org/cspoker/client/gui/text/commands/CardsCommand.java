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
package org.cspoker.client.gui.text.commands;

import java.util.Iterator;
import java.util.Set;

import org.cspoker.client.gui.text.Client;
import org.cspoker.client.gui.text.Console;
import org.cspoker.common.elements.cards.Card;

public class CardsCommand extends CommandImpl {

	public CardsCommand(Client client, Console console) {
		super(client,console);
	}

	public void execute(String... args) throws Exception {
		Set<Card>  privateCards = client.getCurrentPlayerContext().getGameState().getPlayer(client.getServerContext().getAccountContext().getPlayerID()).getCards();
		Set<Card>  communityCards = client.getCurrentTableContext().getGameState().getCommunityCards();
		String result = "You hold: ";
		if (privateCards.size() != 2) {
			result += "nothing";
		} else {
			Iterator<Card> iter = privateCards.iterator();
			result += iter.next();
			result += ", ";
			result += iter.next();
		}
		result += "." + n;

		result += "The community cards: ";
		if (communityCards.size() == 0) {
			result += "nothing";
		} else {
			Iterator<Card> iter = communityCards.iterator();
			result += iter.next();
			while (iter.hasNext()) {
				result += ", ";
				result += iter.next();
			}
		}
		result += "." + n;
		console.print(result);
	}

}
