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

package game;

import game.player.Player;
import game.rounds.IllegalActionException;

/**
 * An interface defining all actions a player can do in one deal.
 * 
 * @author Kenzo
 *
 */
public interface PlayerAction {
	
	/**
	 * If there is no bet on the table and you do not wish to place a bet.
	 * You may only check when there are no prior bets.
	 * 
	 * @param	player
	 * 			The player who checks.
	 */
	public void check(Player player) throws IllegalActionException;
	
	/**
	 * The player puts money in the pot.
	 * 
	 * @param 	player
	 * 			The player who puts a bet.
	 * @param 	amount
	 * 			The amount of the bet.
	 */
	public void bet(Player player, int amount) throws IllegalActionException;
	
	/**
	 * To put into the pot an amount of money equal to
	 * the most recent bet or raise.
	 * 
	 * @param 	player
	 * 			The player who calls.
	 */
	public void call(Player player) throws IllegalActionException;
	
	/**
	 * Raise the bet with given amount.
	 * 
	 * @param	player
	 * 			The player who raises the current bet.
	 * @param 	amount
	 * 			The amount with which to raise the bet.
	 */
	public void raise(Player player, int amount) throws IllegalActionException;
	
	/**
	 * Fold the cards.
	 * The player will not be able to take any actions
	 * in the coming rounds of the current deal.
	 * 
	 * @param 	player
	 * 			The player who folds.
	 */
	public void fold(Player player) throws IllegalActionException;
	
	/**
	 * The player who the dealer-button has been dealt to
	 * can choose to start the deal.
	 * From that moment, new players can not join the on-going deal.
	 * 
	 * @param 	player
	 * 			The player who deals.
	 */
	public void deal(Player player) throws IllegalActionException;

}
