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

public class FinalRound extends Round{

	public FinalRound(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void endRound() {
		collectChips();
		
		
		if(onlyOnePlayerLeft()){

		}
		//TODO determine winner.
	}

	@Override
	public Round getNextRound() {
		return new WaitingRound(getGame());
	}

	/**
	 * The amount to raise with must be n times the big bet
	 */
	@Override
	protected boolean isValidRaise(int amount) {
		return amount%getGame().getGameProperty().getBigBet()==0;
	}
	@Override
	protected String getIllegalRaiseMessage() {
		return "The amount must be n times the big bet";
	}
}
