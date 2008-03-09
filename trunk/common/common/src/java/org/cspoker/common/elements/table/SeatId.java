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

package org.cspoker.common.elements.table;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlValue;

/**
 * A class to represent seat id's.
 *
 */
public class SeatId implements Serializable{
	
	private static final long serialVersionUID = -3605432117019173699L;

	/**
	 * The variable containing the long representation of the id.
	 */
	@XmlValue
	private int id;

	/**
	 * Construct a new seat id with given id.
	 * 
	 * @param 	id
	 *      	The int to use as id.
	 */
	public SeatId(int id) {
		this.id = id;
	}

	protected SeatId() {
		// no op
	}
	
	public SeatId getNextSeatId(){
		return new SeatId(id+1);
	}
	
	public int getID() {
		return id;
	}
	
	/**
	 * Return a textual representation of the seat id.
	 */
	@Override
	public String toString() {
		return String.valueOf(id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SeatId other = (SeatId) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
