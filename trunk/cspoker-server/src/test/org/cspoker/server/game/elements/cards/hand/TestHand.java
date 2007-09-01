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
package org.cspoker.server.game.elements.cards.hand;

import java.util.Iterator;

import junit.framework.TestCase;

import org.cspoker.common.game.elements.cards.Card;
import org.cspoker.common.game.elements.cards.CardImpl;
import org.cspoker.common.game.elements.cards.cardElements.Rank;
import org.cspoker.common.game.elements.cards.cardElements.Suit;
import org.cspoker.server.game.elements.cards.hand.Hand;

/**
 * A test class for the general testing of hands
 * @author Cedric
 *
 */
public class TestHand extends TestCase {

	protected Hand hand1;
	public void test(){
		hand1=new Hand();
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.THREE));
		assertTrue(hand1.getNBCards()==1);
		assertTrue(hand1.getIndexOf(new CardImpl(Suit.HEARTS,Rank.THREE))==0);
		
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.FOUR));
		assertTrue(hand1.getNBCards()==2);
		assertTrue(hand1.getIndexOf(new CardImpl(Suit.HEARTS,Rank.THREE))==0);
		
		assertTrue(hand1.getCard(0).equals(new CardImpl(Suit.HEARTS,Rank.THREE)));
		assertTrue(hand1.getCard(1).equals(new CardImpl(Suit.SPADES,Rank.FOUR)));
		
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.EIGHT));
		assertTrue(hand1.getNBCards()==3);
		
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.SIX));
		assertTrue(hand1.getNBCards()==4);
		
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.ACE));
		assertTrue(hand1.getNBCards()==5);
		assertTrue(hand1.getIndexOf(new CardImpl(Suit.HEARTS,Rank.THREE))==0);
		assertTrue(hand1.getIndexOf(new CardImpl(Suit.HEARTS,Rank.ACE))==4);
		
		hand1.addCard(new CardImpl(Suit.SPADES,Rank.KING));
		assertTrue(hand1.getNBCards()==6);
		
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.QUEEN));
		assertTrue(hand1.getNBCards()==7);
		
		try{
			hand1.addCard(new CardImpl(Suit.CLUBS,Rank.QUEEN));
			assert(false);
		}catch(IllegalArgumentException e){
			assertTrue(hand1.getNBCards()==7);
		}
		
		assertTrue(hand1.contains(new CardImpl(Suit.CLUBS,Rank.QUEEN)));
		assertFalse(hand1.contains(new CardImpl(Suit.HEARTS,Rank.QUEEN)));
		assertTrue(hand1.isFull());
		
		hand1.removeCard(new CardImpl(Suit.CLUBS,Rank.QUEEN));
		assertFalse(hand1.contains(new CardImpl(Suit.CLUBS,Rank.QUEEN)));
		assertFalse(hand1.isFull());
		assertTrue(hand1.getNBCards()==6);
		
		try {
			hand1.removeCard(new CardImpl(Suit.CLUBS,Rank.QUEEN));
			assert(false);
		} catch (IllegalArgumentException e) {
			assertFalse(hand1.contains(new CardImpl(Suit.CLUBS,Rank.QUEEN)));
			assertFalse(hand1.isFull());
			assertTrue(hand1.getNBCards()==6);
		}
		
		assertTrue(new CardImpl(Suit.CLUBS,Rank.ACE).compareTo(new CardImpl(Suit.DIAMONDS,Rank.KING))==1);
		assertTrue(new CardImpl(Suit.HEARTS,Rank.DEUCE).compareTo(new CardImpl(Suit.SPADES,Rank.KING))==-1);
		assertTrue(new CardImpl(Suit.DIAMONDS,Rank.DEUCE).compareTo(new CardImpl(Suit.CLUBS,Rank.DEUCE))==0);
		assertFalse(new CardImpl(Suit.DIAMONDS,Rank.DEUCE).equals(new CardImpl(Suit.CLUBS,Rank.DEUCE)));
		assertTrue(new CardImpl(Suit.CLUBS,Rank.DEUCE).equals(new CardImpl(Suit.CLUBS,Rank.DEUCE)));
		
		
	}
	public void testIterator(){
		hand1=new Hand();
		hand1.addCard(new CardImpl(Suit.CLUBS,Rank.ACE));
		hand1.addCard(new CardImpl(Suit.DIAMONDS,Rank.DEUCE));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.FOUR));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.EIGHT));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.KING));
		hand1.addCard(new CardImpl(Suit.HEARTS,Rank.JACK));
		
		Iterator<Card> iterator=hand1.iterator();
		
		while(iterator.hasNext()){
			System.out.println(iterator.next().toString());
		}
	}
}
