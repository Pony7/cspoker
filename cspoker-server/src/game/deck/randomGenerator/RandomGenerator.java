package game.deck.randomGenerator;

import game.cards.Card;
import game.cards.CardImpl;
import game.cards.Rank;
import game.cards.Suit;
import game.cards.hand.Hand;

import java.util.Random;

/**
 * A class of random generators.
 * 
 * @author Kenzo & Cedric
 *
 */
public class RandomGenerator {
	

	protected volatile Random random;
	
	/**
	 * Construct a new default random generator.
	 *
	 */
	public RandomGenerator(){
		setNewRandom();
	}
	
	/**
	 * Returns a random card.
	 * 
	 * @return A random card.
	 */
	public Card getRandomCard() {
		Random generator = new Random();
		
		int suitIndex=generator.nextInt(4);
		int rankIndex=generator.nextInt(13);

		return new CardImpl(Suit.values()[suitIndex], Rank.values()[rankIndex]);
	}
	
	/**
	 * Returns a random hand.
	 * 
	 * @param 	nBCards
	 * 			The number of cards in the hand.
	 * @return 	A random hand.
	 */
	public Hand getRandomHand(int nBCards) {
		Hand result=new Hand();
		while(result.getNBCards()<nBCards){
			Card randomCard=getRandomCard();
			if(!result.contains(randomCard))
				result.addCard(randomCard);
		}
		return result;
	}
	
	/**
	 * Returns a random-object.
	 * 
	 * The default implementation uses the current time as seed.
	 * 
	 * @return A random-object.
	 */
	public Random getRandom() {
		return random;
	}
	
	protected void setNewRandom(){
		System.out.println(getRandomSeed());
		random = new Random(getRandomSeed());
	}
	
	/**
	 * The seed used for the random.
	 * 
	 * The default implementation uses the current time as seed.
	 * 
	 * @return
	 */
	protected long getRandomSeed(){
		return System.currentTimeMillis();
	}
}
