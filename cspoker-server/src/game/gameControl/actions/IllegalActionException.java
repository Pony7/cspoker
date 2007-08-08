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

/**
 * Thrown to indicate that the trying action is not a valid action.
 *
 * @author Kenzo
 *
 * TODO refactor
 *
 */
public class IllegalActionException extends Exception {

	private static final long serialVersionUID = -5675804638273023229L;

	private Player player;

	private Action action;

	public IllegalActionException(){
		this("You have performed an invalid action.");
	}

	public IllegalActionException(Player player, Action action){
		this(player, action, "");
	}

	public IllegalActionException(String message){
		super(message);
	}

	public IllegalActionException(Player player, Action action, String message){
		super(player.getName()+" performed an illegal action. "+action+" is not a valid action. "+message);
		this.player = player;
		this.action = action;
	}

	public Player getPlayer(){
		return player;
	}

	public Action getAction(){
		return action;
	}
}
