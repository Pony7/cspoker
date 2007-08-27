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
package org.cspoker.client.savedstate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cspoker.common.game.elements.cards.Card;


public class Cards {

    private final List<Card> privateCards;

    private final List<Card> riverCards;
    
    public Cards() {
	privateCards = new ArrayList<Card>(2);
	riverCards = new ArrayList<Card>(5);
    }

    public synchronized List<Card> getPrivateCards() {
        return Collections.unmodifiableList(privateCards);
    }

    public synchronized List<Card> getRiverCards() {
        return Collections.unmodifiableList(riverCards);
    }
    
    public synchronized void resetCards(){
	privateCards.removeAll(privateCards);
	riverCards.removeAll(riverCards);
    }
    
    public synchronized void setPrivateCards(List<Card> cards){
	if(cards.size()!=2)
	    throw new IllegalArgumentException("The number of private cards must be 2.");
	if(privateCards.size()!=0)
	    throw new IllegalStateException("There already are private cards.");
	privateCards.addAll(cards);
    }
    
    public synchronized void addRiverCards(List<Card> cards){
	if(riverCards.size()+cards.size()>5)
	    throw new IllegalStateException("Can't have more than 5 cards in the river.");
	riverCards.addAll(cards);
    }
    
}
