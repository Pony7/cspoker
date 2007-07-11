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

public class FlopRound extends Round {

	public FlopRound(Game game) {
		super(game);
	}

	@Override
	public void endRound() {
		collectChips();
		
		
		drawMuckCard();
		drawOpenCard();
	}

	@Override
	public Round getNextRound() {
		return new TurnRound(getGame());
	}

	/**
	 * The amount to raise with must be n times the small bet
	 */
	@Override
	protected boolean isValidRaise(int amount) {
		return amount%getGame().getGameProperty().getSmallBet()==0;
	}
	@Override
	protected String getIllegalRaiseMessage() {
		return "The amount must be n times the small bet";
	}
}
