package game.cards.hand;

import java.util.Arrays;

import game.cards.Card;
import game.cards.CardImpl;

public class Hand {

	/**
	 * The maximum number of cards in a hand
	 * being 7 in Texas Hold'em (5 on the table and 2 private cards)
	 */
	public final static int MAX_CARDS = 7;
	   
	/**
	 * Private array containing the cards of this hand
	 */
   private CardImpl[] cards;
   /**
    * Creates a new empty hand
    */
   public Hand() {
      cards = new CardImpl[MAX_CARDS];
   }
   /**
    * Duplicate an existing hand.
    * @param h 
    * 			the hand to clone.
    */
   public Hand(Hand h) {
      cards = new CardImpl[MAX_CARDS];
      cards=h.getCards();
   }
   /**
    * Returns the cards in this hand
    */
   private CardImpl[] getCards() {
	return cards;
   }
   /**
    * Returns the number of cards in this hand
    */
   public int getNBCards() {
	int index=0;
	while(index<MAX_CARDS && cards[index]!=null){
		index++;
	}
	return index;
   }
   	/**
    * Remove the all cards from this hand.
    * @post	there are zero cards in the new hand
    * 		| new.getNBCards()==0
    */
   public void makeEmpty() {
	   cards = new CardImpl[MAX_CARDS];
   }
   /**
    * Adds the given card to this hand if there is room
    * @param card
    * 			the given card
    * @throws	IllegalArgumentException
    * 			if this hand is full or if this hand already contains a card equal to the given card
    * 			| isFull() || this.contains(card)
    * @post		the new hand contains the given card
    * 			| new.contains(card)
    */
   public void addCard(CardImpl card){
	   if(isFull() || this.contains(card))
		   throw new IllegalArgumentException();
	   cards[getNBCards()]=card;
   }
   /**
    * Checks wether this hand is full of cards
    * @return	True if the number of cards in this hand equals the maximum number of cards of any hand
    * 			| result == (getNBCards()==MAX_CARDS)
    */
   public boolean isFull(){
	   return getNBCards()==MAX_CARDS;
   }
   /**
    * Get the card at the given position in this hand
    * @param index
    * 		 the position of the card in the hand
    * @throws	IllegalArgumentException
    * 			if the given index is negative or greater than the number of cards in this hand minus one
    * 			| index < 0 || index > getNBCards()-1
    * @result	This hand contains the resulting card
    * 			| this.contains(result)
    */
   public Card getCard(int index) {
      if (index < 0 || index > getNBCards()-1)
    	  throw new IllegalArgumentException();
      return cards[index];
   }
   /**
    * Returns the index a card equal to the given card in this hand
    * @param card
    * 			the given card
    * @throws	IllegalArgumentException
    * 			if this hand doesn't contain a card equal to the given card
    * 			| ! this.contains(card)
    */
   public int getIndexOf(Card card){
	   if(! contains(card))
		   throw new IllegalArgumentException();
	   for(int j=0;j<getNBCards();j++){
		   if(getCard(j).equals(card))
			   return j;
	   }
	   return -1; //cannot occur due to exception
   }
   /**
    * Checks wether this hand contains a card equal to the given card
    * @param card
    * 			the given card
    */
   public boolean contains(Card card){
	   for(int j=0;j<getNBCards();j++){
		   if(getCard(j).equals(card))
			   return true;
	   }
	   return false;
   }
   /**
    * Removes a card equal to the given card from this hand
    * @param card
    * 			the given card
    * @throws	IllegalArgumentException
    * 			if this hand doesn't contain a card equal to the given card
    * 			| ! this.contains(card)
    */
   public void removeCard(Card card){
	   if(! contains(card))
		   throw new IllegalArgumentException();
	   int index=getIndexOf(card);
	   int finalCardIndex=getNBCards()-1;
	   
	   cards[index]=cards[finalCardIndex];
	   cards[finalCardIndex]=null;
   }
   /**
    * Sorts the cards in this hand by their rank
    */
   public void sort(){
	  
	   Arrays.sort(cards,0,getNBCards()-1);
   }
   /**
    * Returns a textual representation of this hand
    */
   public String toString(){
	   String result="";
	   
	   for(int j=0;j<getNBCards();j++){
		   result+=" "+j+" "+cards[j].toString()+"\n";
	   }
	   return result;
   }

}
