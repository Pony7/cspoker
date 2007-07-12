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
import game.chips.IllegalValueException;
import game.rounds.rules.BettingRules;


/**
 * The round after the initial 2 cards are dealt.
 * 
 * @author Kenzo
 *
 */
public class PreFlopRound extends Round implements LowBettingRound{

	public PreFlopRound(Game game, BettingRules bettingRules) {
		super(game,bettingRules);
		try {
			collectSmallBlind(getGame().getCurrentPlayer());
		} catch (IllegalValueException e) {
			goAllIn(getGame().getCurrentPlayer());
		}
		
		getGame().nextPlayer();
		//TODO problem if there are only 2 players left.
		//2nd player can only call the small blind.
		
		try {
			collectBigBlind(getGame().getCurrentPlayer());
		} catch (IllegalValueException e) {
			goAllIn(getGame().getCurrentPlayer());
		}
		
		setBet(getGame().getCurrentPlayer().getBettedChips().getValue());
		
		getGame().nextPlayer();
	}

	@Override
	public void endRound() {
		collectChips();
		drawMuckCard();
		drawOpenCard();
		drawOpenCard();
		drawOpenCard();
	}

	@Override
	public Round getNextRound() {
		return new FlopRound(getGame(),getBettingRules());
	}
}
