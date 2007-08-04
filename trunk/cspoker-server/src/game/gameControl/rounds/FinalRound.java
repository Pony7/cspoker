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

package game.gameControl.rounds;

import game.GameMediator;
import game.gameControl.Game;
import game.gameControl.Showdown;

public class FinalRound extends Round{

	public FinalRound(GameMediator gameMediator, Game game) {
		super(gameMediator, game);
		System.out.println("** Final Round **");
	}

	@Override
	public void endRound() {
		
		collectChips();
		if(onlyOnePlayerLeft()){
			getGame().getPots().close(getGame().getCurrentDealPlayers());
			winner(getGame().getPots());
		}else{
			getGame().getPots().close(getGame().getCurrentDealPlayers());
			Showdown showdown = new Showdown(getGame());
			showdown.determineWinners();
		}
		
		getGame().setDealer(getGame().getNextDealer());
	}

	@Override
	public Round getNextRound() {
		return new WaitingRound(gameMediator, getGame());
	}
	@Override
	public boolean isLowBettingRound() {
		return false;
	}

	@Override
	public boolean isHighBettingRound() {
		return !isLowBettingRound();
	}
}
