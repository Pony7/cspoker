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
package org.cspoker.client.bots.bot.search;

import java.util.Map;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;

@Immutable
public class GameState {

	private final Set<Card> communityCards;
	private final Map<PlayerId, SeatedPlayer> players;
	private final int minRaise;
	private final int stack;

	public GameState(Set<Card> communityCards,
			Map<PlayerId, SeatedPlayer> players, int minRaise, int stack) {
		this.communityCards = communityCards;
		this.players = players;
		this.minRaise = minRaise;
		this.stack = stack;
	}
	
	public Set<Card> getCommunityCards() {
		return communityCards;
	}
	
	public Map<PlayerId, SeatedPlayer> getPlayers() {
		return players;
	}
	
	public int getMinRaise() {
		return minRaise;
	}
	
	public int getStack() {
		return stack;
	}

}
