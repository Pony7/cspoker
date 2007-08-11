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

package org.cspoker.server.game.gameControl.rounds;

import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.gameControl.Game;

public class TurnRound extends Round{

	public TurnRound(GameMediator gameMediator, Game game) {
		super(gameMediator, game);
		System.out.println("** Turn Round **");
	}

	/**
	 * Deal the river card to the public cards.
	 *
	 */
	@Override
	public void endRound() {
		collectChips();
		System.out.println(onlyOnePlayerLeft());
		if(onlyOnePlayerLeft()){
			getGame().getPots().close(getGame().getCurrentDealPlayers());
			winner(getGame().getPots());
		}else{
		drawMuckCard();
		drawOpenCard();
		}
	}

	@Override
	public Round getNextRound() {
		return new FinalRound(gameMediator, getGame());
	}

	@Override
	public boolean isLowBettingRound() {
		return true;
	}

	@Override
	public boolean isHighBettingRound() {
		return !isLowBettingRound();
	}
}
