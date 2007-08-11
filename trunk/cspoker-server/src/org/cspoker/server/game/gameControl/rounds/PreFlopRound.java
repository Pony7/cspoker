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
import org.cspoker.server.game.elements.chips.IllegalValueException;
import org.cspoker.server.game.gameControl.Game;


/**
 * The round after the initial 2 cards are dealt.
 *
 * @author Kenzo
 *
 */
public class PreFlopRound extends Round{

	public PreFlopRound(GameMediator gameMediator, Game game) {
		super(gameMediator, game);
		System.out.println("** PreFlop Round **");
		try {
			collectSmallBlind(getGame().getCurrentPlayer());
			System.out.println(getGame().getCurrentPlayer().getName()+" has placed small blind of "
					+getGame().getGameProperty().getSmallBlind());
			getGame().nextPlayer();
		} catch (IllegalValueException e) {
			goAllIn(getGame().getCurrentPlayer());
		}

		if(getGame().getNbCurrentDealPlayers()!=1){
			try {
				collectBigBlind(getGame().getCurrentPlayer());
				System.out.println(getGame().getCurrentPlayer().getName()+" has placed big blind of "
						+getGame().getGameProperty().getBigBlind());
				getGame().nextPlayer();
			} catch (IllegalValueException e) {
				goAllIn(getGame().getCurrentPlayer());
			}
		}
	}

	@Override
	public void endRound() {
		collectChips();
		if(onlyOnePlayerLeft()){
			getGame().getPots().close(getGame().getCurrentDealPlayers());
			winner(getGame().getPots());
		}else{
			drawMuckCard();
			drawOpenCard();
			drawOpenCard();
			drawOpenCard();
		}
	}

	@Override
	public Round getNextRound() {
		return new FlopRound(gameMediator, getGame());
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
