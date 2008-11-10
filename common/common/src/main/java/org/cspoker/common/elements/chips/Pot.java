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

package org.cspoker.common.elements.chips;

import java.io.Serializable;
import java.util.Set;

import net.jcip.annotations.Immutable;

import org.cspoker.common.elements.player.SeatedPlayer;

@Immutable
public class Pot implements Serializable {

	private static final long serialVersionUID = -6468969689319977981L;
	
	private final Set<SeatedPlayer> contributors;
	
	private final int value;
	
	public Pot(Set<SeatedPlayer> players, int value){
		this.contributors = players;
		this.value = value;
	}
	
	protected Pot(){
		this.contributors = null;
		this.value = 0;
	}
	
	/**
	 * Returns the value of this pot.
	 * 
	 * @return The value of this pot.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Returns the list of players who have contributed to this pot.
	 * 
	 * @return The list of players who have contributed to this pot.
	 */
	public Set<SeatedPlayer> getContributors() {
		return contributors;
	}
	
	public String toString(){
		return contributors.toString()+" > "+value+" chips";
	}

}
