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

import org.cspoker.client.communication.pokersource.PokerPacket;
import org.cspoker.client.communication.pokersource.eventlisteners.poker.PokerEventListener;


public class Table extends PokerPacket{

	public String getType() {
		return getStaticType();
	}
	
	public static String getStaticType() {
		return "PacketPokerTable";
	}
	
	private int observers;
	private String name;
	private int waiting;
	private int percent_flop;
	private int average_pot;
	private String skin;
	private String variant;
	private int hands_per_hour;
	private String betting_structure;
	private int currency_serial;
	private int muck_timeout;
	private int players;
	private String reason;
	private int tourney_serial;
	private int seats;
	private int player_timeout;
	private int id; 

	@Override
	public void signal(PokerEventListener listener) {
		listener.onTable(this);
	}

	public int getObservers() {
		return observers;
	}

	public void setObservers(int observers) {
		this.observers = observers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWaiting() {
		return waiting;
	}

	public void setWaiting(int waiting) {
		this.waiting = waiting;
	}

	public int getPercent_flop() {
		return percent_flop;
	}

	public void setPercent_flop(int percent_flop) {
		this.percent_flop = percent_flop;
	}

	public int getAverage_pot() {
		return average_pot;
	}

	public void setAverage_pot(int average_pot) {
		this.average_pot = average_pot;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public int getHands_per_hour() {
		return hands_per_hour;
	}

	public void setHands_per_hour(int hands_per_hour) {
		this.hands_per_hour = hands_per_hour;
	}

	public String getBetting_structure() {
		return betting_structure;
	}

	public void setBetting_structure(String betting_structure) {
		this.betting_structure = betting_structure;
	}

	public int getCurrency_serial() {
		return currency_serial;
	}

	public void setCurrency_serial(int currency_serial) {
		this.currency_serial = currency_serial;
	}

	public int getMuck_timeout() {
		return muck_timeout;
	}

	public void setMuck_timeout(int muck_timeout) {
		this.muck_timeout = muck_timeout;
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getTourney_serial() {
		return tourney_serial;
	}

	public void setTourney_serial(int tourney_serial) {
		this.tourney_serial = tourney_serial;
	}

	public int getSeats() {
		return seats;
	}

	public void setSeats(int seats) {
		this.seats = seats;
	}

	public int getPlayer_timeout() {
		return player_timeout;
	}

	public void setPlayer_timeout(int player_timeout) {
		this.player_timeout = player_timeout;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
}
