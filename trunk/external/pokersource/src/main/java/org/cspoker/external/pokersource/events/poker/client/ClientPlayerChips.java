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
package org.cspoker.external.pokersource.events.poker.client;

import org.cspoker.external.pokersource.PokerPacket;
import org.cspoker.external.pokersource.eventlisteners.poker.PokerEventListener;


public class ClientPlayerChips extends PokerPacket{

	public String getType() {
		return getStaticType();
	}
	
	public static String getStaticType() {
		return "PacketPokerClientPlayerChips";
	}

    private int[] money;
    private int game_id;
    private int serial;
    private int[] bet; 
	
	@Override
	public void signal(PokerEventListener listener) {
		listener.onClientPlayerChips(this);
	}

	public int[] getMoney() {
		return money;
	}

	public void setMoney(int[] money) {
		this.money = money;
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public int[] getBet() {
		return bet;
	}

	public void setBet(int[] bet) {
		this.bet = bet;
	}

	

}
