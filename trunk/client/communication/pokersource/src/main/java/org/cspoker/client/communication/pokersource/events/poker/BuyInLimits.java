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
package org.cspoker.client.communication.pokersource.events.poker;

import org.cspoker.client.communication.pokersource.eventlisteners.poker.PokerEventListener;


public class BuyInLimits extends PokerEvent{

	public String getType() {
		return getStaticType();
	}
	
	public static String getStaticType() {
		return "PacketPokerBuyInLimits";
	}
	
	private int min;
	private int max;
	private int rebuy_min;
	private int length;
	private int game_id;
	private int best; 
	
	@Override
	public void signal(PokerEventListener listener) {
		listener.onBuyInLimits(this);
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getRebuy_min() {
		return rebuy_min;
	}

	public void setRebuy_min(int rebuy_min) {
		this.rebuy_min = rebuy_min;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public int getBest() {
		return best;
	}

	public void setBest(int best) {
		this.best = best;
	}
	
	
	
}
