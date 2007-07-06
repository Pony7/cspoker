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
   private Card[] cards;
   
   /**
    * The type of this hand
    */
   private HandType type;
   /**
    * Creates a new empty hand
    */
   public Hand() {
      cards = new CardImpl[MAX_CARDS];
      //updateType();
   }
   /**
    * Duplicate an existing hand.
    * @param h 
    * 			the hand to clone.
    */
   public Hand(Hand h) {
      cards = new CardImpl[MAX_CARDS];
      cards=h.getCards();
      type=h.getHandType();
   }
   /**
    * Returns the cards in this hand
    */
   public Card[] getCards() {
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
	   updateType();
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
   public void addCard(Card card){
	   if(isFull() || this.contains(card))
		   throw new IllegalArgumentException();
	   cards[getNBCards()]=card;
	   //updateType();
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
	   updateType();
   }
   /**
    * Sorts the cards in this hand by their rank from highest to lowest rank
    */
   public void sort(){
	   // bubble sort variant
	   int i,j;
	   boolean swapped;
	   Card temp=null;
	    for (i=getNBCards(); --i >=0;) {
	       swapped=false;
	       for (j=0; j<i;j++) {
	          if (((CardImpl) cards[j]).compareTo(cards[j+1])<0){
	             temp=cards[j];
	             cards[j]=cards[j+1];
	             cards[j+1]=temp;
	             swapped=true;
	          }
	       }
	       if(!swapped) 
	    	   return;
	    }
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
   /**
    * Returns the type of this hand
    */
   public HandType getHandType(){
	   return type;
   }
   /**
    * Updates the type of this hand
    */
   private void updateType(){
	   type=HandTypeCalculator.calculateHandType(this);
   }

}
