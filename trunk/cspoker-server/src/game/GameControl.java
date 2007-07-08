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
import game.rounds.PreFlopRound;
import game.rounds.Round;

/**
 * This class is responsable to control the flow of the game.
 * This class can change the state of users and the table.
 * 
 * @author Kenzo
 *
 */
public class GameControl implements PlayerAction{
	
	/**********************************************************
	 * Variables
	 **********************************************************/
	
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
	
	/**
	 * This variable contains all game elements,
	 * such as players and table.
	 */
	private Game game;
	
	private Round round;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	public GameControl(){
		//TODO
		game = new Game(new GameProperty());
		round = new PreFlopRound(game);
	}
	
	/**********************************************************
	 * Player methods
	 **********************************************************/
	
	public void bet(Player player, int amount) {
		round.bet(player, amount);
		checkIfEndedAndChangeRound();
	}

	public void call(Player player) {
		round.call(player);
		checkIfEndedAndChangeRound();
	}
	
	public void check(Player player){
		round.check(player);
		checkIfEndedAndChangeRound();
	}
	
	public void raise(Player player, int amount){
		round.raise(player, amount);
		checkIfEndedAndChangeRound();
	}
	
	public void fold(Player player){
		round.fold(player);
		checkIfEndedAndChangeRound();
	}
	
	public void deal(Player player){
		round.deal(player);
		checkIfEndedAndChangeRound();
	}
	
	/**********************************************************
	 * Round change logic
	 **********************************************************/
	
	private void checkIfEndedAndChangeRound(){
		if(round.roundEnded()){
			changeToNextRound();
		}
	}
	
	private void changeToNextRound(){
		round.endRound();
		round = round.getNextRound();
	}
}
