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

package game.deck;

import game.cards.Card;

public class CardDeck {
	/**
	 * The sequence of cards in this deck
	 */
	private Card[] cards;
	
	private int lastCardDealt;
	
	/**
	 * Draw the card on the top of this deck.
	 * @post	The second card in the deck becomes the new top card
	 * @result	The top card from the deck is returned
	 * @throws	IllegalStateException
	 * 			There must be at least one card in the deck
	 * 			| getDeckSize()<=0
	 */
	public Card drawCard(){
		if(lastCardDealt>=getDeckSize())
			throw new IllegalStateException();
		lastCardDealt++;
		return cards[lastCardDealt-1];
	}
	
	/**
	 * The deck will consist again of 52 cards
	 * and will be shuffled.
	 *
	 */
	public void newDeal(){
		lastCardDealt=0;
		shuffle();
	}
	
	/**
	 * Shuffle the cards.
	 */
	private void shuffle(){
		
	}
	/**
	 * Returns the number of cards left in this deck
	 */
	public int getDeckSize(){
		return cards.length;
	}
	/**
	 * Returns the textual representation of this deck
	 */
	public String toString(){
		String result="";
		for(int j=0;j<getDeckSize();j++){
			result=result+"/n"+cards[j].toString();
		}
		return result;
	}
}
