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

package org.cspoker.server.game.gameControl.actions;



/**
 * An enumeration of all actions a player can do.
 *
 * @author Kenzo
 *
 */
public enum Action {

	FOLD{

		@Override
		public String toString(){
			return "Fold";
		}
	},

	CHECK{

		@Override
		public String toString(){
			return "Check";
		}
	},

	BET{

		@Override
		public String toString(){
			return "Bet";
		}
	},

	CALL{

		@Override
		public String toString(){
			return "Call";
		}
	},

	RAISE{

		@Override
		public String toString(){
			return "Raise";
		}
	},

	DEAL{
		@Override
		public String toString(){
			return "Deal";
		}
	},

	ALL_IN{

		@Override
		public String toString(){
			return "All-in";
		}
	};
}
