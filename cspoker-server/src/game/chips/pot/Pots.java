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

package game.chips.pot;

import game.chips.IllegalValueException;
import game.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class to represent a group of pots.
 * 
 * @author Kenzo
 *
 */
public class Pots {
	
	private final List<Pot> pots;
	
	private final Pot pot;
	
	private boolean isClosed;
	
	/**
	 * Construct a new group of pots. 
	 * 
	 */
	public Pots(){
		pot = new Pot();
		pots = new ArrayList<Pot>();
		isClosed = false;
	}
	
	/**
	 * Collect the given amount of chips
	 * from each player in the given list
	 * and transfer that amount to
	 * a new side pot, with the old pot.
	 * 
	 * If the amount is zero and the pot
	 * is empty, no new side pot is created.
	 * 
	 * @param amount
	 * @param players
	 * @throws IllegalValueException
	 */
	public void collectAmountFromPlayersToSidePot(int amount, List<Player> players) throws IllegalValueException{
		if(isClosed())
			return;
		if((amount>0) || (pot.getChips().getValue()>0)){
			Pot sidePot = new Pot();
			pot.transferAllChipsTo(sidePot);
			for(Player player:players){
				player.getBettedChips().transferAmountTo(amount, sidePot.getChips());
			}
			pots.add(sidePot);
		}
	}
	
	public Pot getMainPot(){
		if(isClosed())
			return null;
		return pot;
	}
	
	public List<Pot> getSidePots(){
		if(isClosed)
			return null;
		return Collections.unmodifiableList(pots);
	}
	
	public Pot getNewestSidePot(){
		if(isClosed || pots.isEmpty())
			return null;
		return pots.get(pots.size()-1);
	}

	/**
	 * Collect all chips from the betted chips pile
	 * of all players in the given list to the main pot.
	 * 
	 * @param 	players
	 * 			The list of players from who
	 * 			to collect the betted chips from.
	 */
	public void collectChipsToPot(List<Player> players){
		if(isClosed())
			return;
		for(Player player:players){
			try {
				player.getBettedChips().transferAllChipsTo(pot.getChips());
			} catch (IllegalValueException e) {
				assert false;
			}
		}
	}
	
	/**
	 * Add the given player to the list of
	 * players who have to show their cards.
	 * 
	 * @param 	player
	 * 			The player who will have
	 * 			to show his cards at the end.
	 */
	public void addShowdownPlayer(Player player) {
		if(isClosed())
			return;
		for(Pot pot:pots){
			pot.addShowdownPlayer(player);
		}
	}
	
	/**
	 * Close the pots. It is impossible
	 * to a
	 * 
	 * @param showdownPlayers
	 */
	public void close(List<Player> showdownPlayers){
		if(isClosed())
			return;
		pots.add(pot);
		for(Player player: showdownPlayers){
			addShowdownPlayer(player);
		}
		isClosed = true;
	}
	
	public boolean isClosed(){
		return isClosed;
	}

	
	/**
	 * Return a list of all pots.
	 * If the pot is closed,
	 * also the current pot is included in the list of pots.
	 * Otherwise, only the side pots are returned.
	 * 
	 * @return
	 */
	public List<Pot> getPots(){
		if(!isClosed())
			return null;
		return Collections.unmodifiableList(pots);
	}
}
