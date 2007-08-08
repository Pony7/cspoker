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

package game.player;

import game.elements.chips.IllegalValueException;
import game.elements.chips.pot.Pot;

/**
 * A class to represent all-in players.
 * 
 * @author Kenzo
 *
 */
public class AllInPlayer implements Comparable<AllInPlayer>{
	
	private final Player player;

	
	public AllInPlayer(Player player){
		this.player = player;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public int getBetValue(){
		return player.getBettedChips().getValue();
	}
	
	public void transferAllChipsTo(Pot pot){
		try {
			transferAmountTo(player.getBettedChips().getValue(), pot);
		} catch (IllegalValueException e) {
			assert false;
		}
	}
	
	public void transferAmountTo(int amount,Pot pot) throws IllegalValueException{
		player.getBettedChips().transferAmountTo(amount,pot.getChips());
	}
	
	public int compareTo(AllInPlayer o) {
		return getBetValue()-o.getBetValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AllInPlayer other = (AllInPlayer) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (getBetValue()!=other.getBetValue())
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return String.valueOf(getBetValue());
	}

}
