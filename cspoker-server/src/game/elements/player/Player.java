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

package game.elements.player;

import game.PlayerId;
import game.elements.cards.Card;
import game.elements.chips.Chips;
import game.elements.chips.IllegalValueException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class to represent players: bots or humans.
 *
 * @author Kenzo
 *
 */
public class Player {

	/**********************************************************
	 * Variables
	 **********************************************************/

	/**
	 * The variable containing the id of the player.
	 */
	private final PlayerId id;

	/**
	 * The name of the player.
	 */
	private final String name;

	/**
	 * The stack of this player.
	 */
	private final Chips chips;

	/**
	 * The chips the player has bet in this round.
	 *
	 */
	private final Chips bettedChips;

	/**
	 * The hidden cards.
	 */
	private final List<Card> pocketCards;

	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Construct a new player with given id,
	 * name and initial number of chips.
	 *
	 * @throws 	IllegalValueException
	 * 			The given initial value is not valid.
	 *
	 * @post	The chips pile is effective and
	 * 			the value of chips is the same
	 * 			as the given initial value.
	 *		 	|new.getBettedChips()!=null && new.getChips.getValue()==initialNbChips
	 * @post 	The betted chips pile is effective and
	 * 			There are no chips on this pile.
	 *		 	|new.getBettedChips()!=null && new.getBettedChips().getValue()==0
	 */
	public Player(PlayerId id, String name, int initialNbChips) throws IllegalValueException{
		this.id = id;
		this.name = name;
		chips = new Chips(initialNbChips);
		bettedChips = new Chips();
		pocketCards = new ArrayList<Card>();
	}

	/**********************************************************
	 * Name
	 **********************************************************/

	/**
	 * Returns the name of this player.
	 *
	 * @return The name of this player.
	 */
	public String getName(){
		return name;
	}

	/**********************************************************
	 * Id
	 **********************************************************/

	/**
	 * Returns the id of this player.
	 *
	 * @return The id of this player.
	 */
	public PlayerId getId(){
		return id;
	}

	/**********************************************************
	 * Chips
	 **********************************************************/


	public Chips getStack(){
		return chips;
	}

	public Chips getBettedChips(){
		return bettedChips;
	}

	public void transferAmountToBettedPile(int amount) throws IllegalValueException{
		getStack().transferAmountTo(amount, getBettedChips());
	}

	public void transferAllChipsToBettedPile() throws IllegalValueException{
		getStack().transferAllChipsTo(getBettedChips());
	}

	/**********************************************************
	 * Cards
	 **********************************************************/

	/**
	 * Deal a pocket card to this player.
	 *
	 */
	public void dealPocketCard(Card card){
		pocketCards.add(card);
	}

	public List<Card> getPocketCards(){
		return Collections.unmodifiableList(pocketCards);
	}

	public void clearPocketCards(){
		pocketCards.clear();
	}

	@Override
	public String toString(){
		return "ID: "+getId()+", Name: "+getName()+", Stack: "+getStack();
	}

	/**
	 * Returns a hash code value for this player.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Player other = (Player) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
