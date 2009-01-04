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
package org.cspoker.common.handeval.spears;

public enum Rank {
	Deuce	("2"), 
	Three	("3"), 
	Four	("4"), 
	Five	("5"), 
	Six		("6"),
	Seven	("7"), 
	Eight	("8"), 
	Nine	("9"), 
	Ten		("T"), 
	Jack	("J"), 
	Queen	("Q"), 
	King	("K"), 
	Ace		("A");
	
	private final String toString;


	private Rank(String toString) {
		this.toString = toString;
	}
	
	public String toString() {
		return toString;
	}
	
	public static Rank parse(String s)  {
		for (Rank r : Rank.values()) {
			if(s.equalsIgnoreCase(r.toString)) return r;
		}
		throw new RuntimeException("Unrecognized rank: " + s);
	}
	

}
