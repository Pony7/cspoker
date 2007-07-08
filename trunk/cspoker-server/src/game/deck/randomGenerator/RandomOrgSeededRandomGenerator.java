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

package game.deck.randomGenerator;

import java.security.SecureRandom;
import java.util.Random;

import game.cards.Card;
import game.cards.CardImpl;
import game.cards.Rank;
import game.cards.Suit;


public class RandomOrgSeededRandomGenerator implements RandomGenerator {

	public int[] getRandomSequence() {
		// TODO Auto-generated method stub
		return null;
	}

	public Card getRandomCard() {
		Random generator = new Random();
		
		int suitNumber=generator.nextInt(5);
		int rankNumber=1+generator.nextInt(14);
		Suit randomSuit=null;
		Rank randomRank=null;
		switch (suitNumber) {
		case 0:
			randomSuit=Suit.CLUBS;
			break;
		case 1:
			randomSuit=Suit.DIAMONDS;
			break;
		case 2:
			randomSuit=Suit.HEARTS;
			break;
		case 3:
			randomSuit=Suit.SPADES;
			break;
		}
		switch (rankNumber) {
		case 2:
			randomRank=Rank.DEUCE;
			break;
		case 3:
			randomRank=Rank.THREE;
			break;
		case 4:
			randomRank=Rank.FOUR;
			break;
		case 5:
			randomRank=Rank.FIVE;
			break;
		case 6:
			randomRank=Rank.SIX;
			break;
		case 7:
			randomRank=Rank.SEVEN;
			break;
		case 8:
			randomRank=Rank.EIGHT;
			break;
		case 9:
			randomRank=Rank.NINE;
			break;
		case 10:
			randomRank=Rank.TEN;
			break;
		case 11:
			randomRank=Rank.JACK;
			break;
		case 12:
			randomRank=Rank.QUEEN;
			break;
		case 13:
			randomRank=Rank.KING;
			break;
		case 14:
			randomRank=Rank.ACE;
			break;
		}
		
		return new CardImpl(randomSuit, randomRank);
	}

}
