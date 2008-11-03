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

import net.jcip.annotations.Immutable;

/**
 * A class to represent seat ids.
 *
 */
@Immutable
public class SeatId implements Serializable{
	
	private static final long serialVersionUID = -2997117736649022785L;
	
	private final long seatId;
	
	public SeatId() {
		this(0);
	}
	
	public SeatId(long seatId){
		this.seatId = seatId;
	}

	public long getId() {
		return seatId;
	}
	
	@Override
	public String toString(){
		return "#"+Long.toString(seatId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (seatId ^ (seatId >>> 32));
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
		if (seatId != other.seatId)
			return false;
		return true;
	}
}
