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
package org.cspoker.external.pokersource.commands.poker;

import org.cspoker.external.pokersource.eventlisteners.poker.PokerEventListener;
import org.cspoker.external.pokersource.events.poker.Id;

public class BuyIn extends Id{
	
	public BuyIn(int serial, int game_id, int amount) {
		super(serial,game_id);
		this.amount = amount;
	}
	
	public String getType() {
		return "PacketPokerBuyIn";
	}
	
	private int amount;
	
	@Override
	public void signal(PokerEventListener listener) {
		throw new IllegalStateException("This is not an event");
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
