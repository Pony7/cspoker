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

package game.rounds;

import game.Game;
import game.player.Player;

/**
 * An abstract class to represent rounds.
 * A player can do actions in a round,
 * such as checking, betting, ...
 * 
 * A quasi-state pattern is used.
 * 
 * @author Kenzo
 *
 */
public abstract class Round {
	
	/**
	 * The last event player is the last player
	 * that has done significant change,
	 * such as a raise.
	 * 
	 * If the next player is the last event player,
	 * the round is over.
	 * 
	 * It is initialised in each game as the first better
	 * after the big blind, in every next round,
	 * it is the player on to the left side of the player
	 * with the dealer-button.
	 */
	private Player lastEventPlayer;
	
	private Game game;
	
	public Round(Game game){
		this.game = game;
	}
	
	public void check(Player player){
		
	}
	
	public void bet(Player player, int amount){
		playerMadeEvent(player);
	}
	
	public void call(Player player){
		
	}
	
	public void raise(Player player, int amount){
		playerMadeEvent(player);
	}
	
	public void fold(Player player){
		
	}
	
	public void deal(Player player){
		
	}
	
	
	public boolean roundEnded(){
		return lastEventPlayer == game.getCurrentPlayer();
	}
	
	/**
	 * End the current round.
	 */
	public abstract void endRound();
	
	/**
	 * Returns the next round.
	 */
	public abstract Round getNextRound();
	
	/**
	 * 
	 * 
	 * @param player
	 */
	private void playerMadeEvent(Player player){
		lastEventPlayer = player;
	}

}
