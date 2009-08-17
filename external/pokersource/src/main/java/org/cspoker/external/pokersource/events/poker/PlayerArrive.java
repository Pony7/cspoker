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
package org.cspoker.external.pokersource.events.poker;

import org.cspoker.external.pokersource.eventlisteners.poker.PokerEventListener;


public class PlayerArrive extends Id{

	public String getType() {
		return getStaticType();
	}
	
	public static String getStaticType() {
		return "PacketPokerPlayerArrive";
	}
	
	// blind = "late" # True, None, "late", "big", "small", "big_and_dead" ##
	private String blind;
	private boolean buy_in_payed;
	private boolean wait_for;
	private String name;
	private String url;
	private boolean auto;
	private String outfit;
	private int seat;
	private boolean remove_next_turn;
	private boolean sit_out;
	private boolean auto_blind_ante;
	private boolean sit_out_next_turn; 
	
	@Override
	public void signal(PokerEventListener listener) {
		listener.onPlayerArrive(this);
	}

	public String getBlind() {
		return blind;
	}

	public void setBlind(String blind) {
		this.blind = blind;
	}

	public boolean isBuy_in_payed() {
		return buy_in_payed;
	}

	public void setBuy_in_payed(boolean buy_in_payed) {
		this.buy_in_payed = buy_in_payed;
	}

	public boolean isWait_for() {
		return wait_for;
	}

	public void setWait_for(boolean wait_for) {
		this.wait_for = wait_for;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isAuto() {
		return auto;
	}

	public void setAuto(boolean auto) {
		this.auto = auto;
	}

	public String getOutfit() {
		return outfit;
	}

	public void setOutfit(String outfit) {
		this.outfit = outfit;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public boolean isRemove_next_turn() {
		return remove_next_turn;
	}

	public void setRemove_next_turn(boolean remove_next_turn) {
		this.remove_next_turn = remove_next_turn;
	}

	public boolean isSit_out() {
		return sit_out;
	}

	public void setSit_out(boolean sit_out) {
		this.sit_out = sit_out;
	}

	public boolean isAuto_blind_ante() {
		return auto_blind_ante;
	}

	public void setAuto_blind_ante(boolean auto_blind_ante) {
		this.auto_blind_ante = auto_blind_ante;
	}

	public boolean isSit_out_next_turn() {
		return sit_out_next_turn;
	}

	public void setSit_out_next_turn(boolean sit_out_next_turn) {
		this.sit_out_next_turn = sit_out_next_turn;
	}
	
}
