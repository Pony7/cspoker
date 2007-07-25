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

package game.gameControl.actions;

import game.elements.player.Player;
import game.gameControl.rounds.Round;

/**
 * An enumeration of all actions a player can do.
 *
 * @author Kenzo
 *
 */
public enum Action {

	FOLD{
		@Override
		public boolean canDoAction(Round round, Player player){
			return round.isBettingRound()
			&& round.onTurn(player);
		}

		@Override
		public String toString(){
			return "Fold";
		}
	},

	CHECK{
		@Override
		public boolean canDoAction(Round round, Player player){
			return round.isBettingRound()
			&& round.onTurn(player)
			&& !round.someoneHasBet();
		}

		@Override
		public String toString(){
			return "Check";
		}
	},

	BET{
		@Override
		public boolean canDoAction(Round round, Player player){
			return round.isBettingRound()
			&& round.onTurn(player)
			&& !round.someoneHasBet()
			&& !round.onlyOnePlayerLeft();
		}

		@Override
		public String toString(){
			return "Bet";
		}
	},

	CALL{
		@Override
		public boolean canDoAction(Round round, Player player){
			return round.isBettingRound()
			&& round.onTurn(player)
			&& round.someoneHasBet();
		}

		@Override
		public String toString(){
			return "Call";
		}
	},

	RAISE{
		@Override
		public boolean canDoAction(Round round, Player player){
			return round.isBettingRound()
			&& round.onTurn(player)
			&& round.someoneHasBet()
			&& !round.onlyOnePlayerLeft();
		}

		@Override
		public String toString(){
			return "Raise";
		}
	},

	DEAL{
		@Override
		public boolean canDoAction(Round round, Player player){
			return !round.isBettingRound()
			&& round.onTurn(player)
			&& (round.getGame().getTable().getNbPlayers()>1);
		}

		@Override
		public String toString(){
			return "Deal";
		}
	},

	ALL_IN{
		@Override
		public boolean canDoAction(Round round, Player player){
			return round.isBettingRound()
			&& round.onTurn(player);
		}

		@Override
		public String toString(){
			return "All-in";
		}
	};

	/**
	 * Check whether the given player can take this action in the given game.
	 *
	 * @param 	round
	 * 			The round in which the action occurs.
	 * @param 	player
	 * 			The player to check.
	 * @return	True if the action is allowed, false otherwise.
	 */
	public abstract boolean canDoAction(Round round, Player player);


}
