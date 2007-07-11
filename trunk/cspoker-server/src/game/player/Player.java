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

import game.chips.Chips;
import game.chips.IllegalValueException;

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
	 * This id should be guaranteed unique upon creation
	 * of a player.
	 */
	private final long id;
	
	/**
	 * The name of the player.
	 */
	private final String name;
	
	/**
	 * The available chips of this player.
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
	private PocketCards pocketCards;
	
	private boolean isDealer;
	
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
	public Player(long id, String name, int initialNbChips) throws IllegalValueException{
		this.id = id;
		this.name = name;
		chips = new Chips(initialNbChips);
		bettedChips = new Chips();
	}
	
	public String getName(){
		return name;
	}
	
	public long getId(){
		return id;
	}
	
	public Chips getChips(){
		return chips;
	}
	
	public Chips getBettedChips(){
		return bettedChips;
	}
	
	public boolean isDealer(){
		return isDealer;
	}
	
	public void setDealer(boolean isDealer){
		this.isDealer = isDealer;
	}

	
	public void transferAmountToBettedPile(int amount) throws IllegalValueException{
		getChips().transferAmountTo(amount, getBettedChips());
	}
	
}
