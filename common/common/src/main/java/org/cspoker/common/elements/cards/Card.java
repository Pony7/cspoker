/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.common.elements.cards;

import javax.xml.bind.annotation.XmlAttribute;

public enum Card {

	//ORDER IS IMPORTANT!
	
	TWO_CLUBS(Rank.TWO,Suit.CLUBS),
	TWO_DIAMONDS(Rank.TWO,Suit.DIAMONDS),
	TWO_HEARTS(Rank.TWO,Suit.HEARTS),
	TWO_SPADES(Rank.TWO,Suit.SPADES),
	
	THREE_CLUBS(Rank.THREE,Suit.CLUBS),
	THREE_DIAMONDS(Rank.THREE,Suit.DIAMONDS),
	THREE_HEARTS(Rank.THREE,Suit.HEARTS),
	THREE_SPADES(Rank.THREE,Suit.SPADES),
	
	FOUR_CLUBS(Rank.FOUR,Suit.CLUBS),
	FOUR_DIAMONDS(Rank.FOUR,Suit.DIAMONDS),
	FOUR_HEARTS(Rank.FOUR,Suit.HEARTS),
	FOUR_SPADES(Rank.FOUR,Suit.SPADES),

	FIVE_CLUBS(Rank.FIVE,Suit.CLUBS),
	FIVE_DIAMONDS(Rank.FIVE,Suit.DIAMONDS),
	FIVE_HEARTS(Rank.FIVE,Suit.HEARTS),
	FIVE_SPADES(Rank.FIVE,Suit.SPADES),
	
	SIX_CLUBS(Rank.SIX,Suit.CLUBS),
	SIX_DIAMONDS(Rank.SIX,Suit.DIAMONDS),
	SIX_HEARTS(Rank.SIX,Suit.HEARTS),
	SIX_SPADES(Rank.SIX,Suit.SPADES),
	
	SEVEN_CLUBS(Rank.SEVEN,Suit.CLUBS),
	SEVEN_DIAMONDS(Rank.SEVEN,Suit.DIAMONDS),
	SEVEN_HEARTS(Rank.SEVEN,Suit.HEARTS),
	SEVEN_SPADES(Rank.SEVEN,Suit.SPADES),
	
	EIGHT_CLUBS(Rank.EIGHT,Suit.CLUBS),
	EIGHT_DIAMONDS(Rank.EIGHT,Suit.DIAMONDS),
	EIGHT_HEARTS(Rank.EIGHT,Suit.HEARTS),
	EIGHT_SPADES(Rank.EIGHT,Suit.SPADES),
	
	NINE_CLUBS(Rank.NINE,Suit.CLUBS),
	NINE_DIAMONDS(Rank.NINE,Suit.DIAMONDS),
	NINE_HEARTS(Rank.NINE,Suit.HEARTS),
	NINE_SPADES(Rank.NINE,Suit.SPADES),
	
	TEN_CLUBS(Rank.TEN,Suit.CLUBS),
	TEN_DIAMONDS(Rank.TEN,Suit.DIAMONDS),
	TEN_HEARTS(Rank.TEN,Suit.HEARTS),
	TEN_SPADES(Rank.TEN,Suit.SPADES),
	
	JACK_CLUBS(Rank.JACK,Suit.CLUBS),
	JACK_DIAMONDS(Rank.JACK,Suit.DIAMONDS),
	JACK_HEARTS(Rank.JACK,Suit.HEARTS),
	JACK_SPADES(Rank.JACK,Suit.SPADES),
	
	QUEEN_CLUBS(Rank.QUEEN,Suit.CLUBS),
	QUEEN_DIAMONDS(Rank.QUEEN,Suit.DIAMONDS),
	QUEEN_HEARTS(Rank.QUEEN,Suit.HEARTS),
	QUEEN_SPADES(Rank.QUEEN,Suit.SPADES),
	
	KING_CLUBS(Rank.KING,Suit.CLUBS),
	KING_DIAMONDS(Rank.KING,Suit.DIAMONDS),
	KING_HEARTS(Rank.KING,Suit.HEARTS),
	KING_SPADES(Rank.KING,Suit.SPADES),
	
	ACE_CLUBS(Rank.ACE,Suit.CLUBS),
	ACE_DIAMONDS(Rank.ACE,Suit.DIAMONDS),
	ACE_HEARTS(Rank.ACE,Suit.HEARTS),
	ACE_SPADES(Rank.ACE,Suit.SPADES),
	;
	
	@XmlAttribute
	private final Rank rank;
	
	@XmlAttribute
	private final Suit suit;
	
	private Card(final Rank rank, final Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	
	public String getLongDescription() {
		return rank + " of " + suit;
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	@Override
	public String toString() {
		if (rank == null || suit == null)
			return "back"; // We just see the back of the card ...
		return rank.getShortDescription() + suit.getShortDescription();
	}
}
