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

package game.player;

import game.cards.hand.Hand;
import game.cards.hand.HandEvaluator;
import game.cards.hand.HandTypeCalculator;

/**
 * A class to represent a showdown player.
 * 
 * This class implements the comparable interface
 * to easily get the player(s) with the best hand
 * on top of the list.
 * 
 * @author Kenzo
 *
 */
public class ShowdownPlayer implements Comparable<ShowdownPlayer>{
	
	private final Player player;
	
	private final Hand bestHand;
	
	public ShowdownPlayer(Player player, Hand bestHand){
		this.player = player;
		this.bestHand = bestHand;

	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Hand getBestHand(){
		return bestHand;
	}
	
	@Override
	public String toString(){
		return player.getName()+" has a "+HandTypeCalculator.calculateHandType(getBestHand());
	}
	
	/**
	 * After sorting, the first player is the
	 * player with the best hand.
	 * 
	 * -1 = first hand is best, 1 = second hand is best, 0 = tie
	 */
	public int compareTo(ShowdownPlayer o) {
		return HandEvaluator.compareHands(o.getBestHand(),getBestHand());
	}
	/**
	 * 2 showdown players are equal if both their hands
	 * have the same value.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ShowdownPlayer other = (ShowdownPlayer) obj;
		if (bestHand == null)
			return other.bestHand == null;
		else
			return HandEvaluator.compareHands(getBestHand(), other.getBestHand())==0;
	}

}