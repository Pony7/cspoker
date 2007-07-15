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

public class WaitingRound extends Round {
	
	public WaitingRound(Game game) {
		super(game);
		System.out.println("** Waiting Round **");
		removeBrokePlayers();
		game.setDealer(game.getNextDealer());
	}

	@Override
	public void endRound() {
		getGame().dealNewHand();
		for(Player player:getGame().getCurrentDealPlayers()){
			player.dealPocketCard(drawCard());
			player.dealPocketCard(drawCard());
		}
	}

	@Override
	public Round getNextRound() {
		return new PreFlopRound(getGame());
	}
	
	@Override
	public boolean isBettingRound(){
		return false;
	}
	@Override
	public boolean isLowBettingRound() {
		return false;
	}

	@Override
	public boolean isHighBettingRound() {
		return false;
	}
}
