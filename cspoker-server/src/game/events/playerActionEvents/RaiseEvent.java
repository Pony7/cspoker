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

package game.events.playerActionEvents;

import game.elements.player.Player;
import game.events.GameEvent;
import game.gameControl.actions.Action;

/**
 * A class to represent raise events.
 * 
 * @author Kenzo
 *
 */
public class RaiseEvent extends GameEvent {
	
	private final Player player;
	
	private final int amount;
	
	public RaiseEvent(Player player, int amount){
		this.player = player;
		this.amount = amount;
	}

	@Override
	public String[] getAction() {
		return new String[] {String.valueOf(player.getId()),Action.RAISE.toString(), String.valueOf(amount)};
	}
	
	@Override
	public String toString(){
		return player.getName()+" raises with "+amount+".";
	}

}
