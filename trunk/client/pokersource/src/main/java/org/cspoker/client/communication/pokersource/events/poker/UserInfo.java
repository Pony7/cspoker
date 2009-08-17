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

import java.util.HashMap;

import org.cspoker.client.communication.pokersource.eventlisteners.poker.PokerEventListener;


public class UserInfo extends Serial{

	public String getType() {
		return getStaticType();
	}
	
	public static String getStaticType() {
		return "PacketPokerUserInfo";
	}

	private int rating;
	private String name;
	private HashMap<String,int[]> money;
	private int affiliate;
	private String password;
	private String email;

	@Override
	public void signal(PokerEventListener listener) {
		listener.onUserInfo(this);
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, int[]> getMoney() {
		return money;
	}

	public void setMoney(HashMap<String, int[]> money) {
		this.money = money;
	}

	public int getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(int affiliate) {
		this.affiliate = affiliate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
