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

package org.cspoker.common.player;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

/**
 * A class to represent player id's.
 * 
 * @author Kenzo
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerId implements Serializable {

	private static final long serialVersionUID = -3259296410499422525L;
	/**
	 * The variable containing the id.
	 */
	@XmlValue
	private long id;

	/**
	 * Construct a new player id with given long id.
	 * 
	 * @param id
	 *            The id to use as player id.
	 */
	public PlayerId(long id) {
		this.id = id;
	}

	protected PlayerId() {
		// no op
	}

	/**
	 * Returns a hash code value for this player id.
	 */

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 */

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlayerId other = (PlayerId) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	public String toString() {
		return Long.valueOf(id).toString();
	}

	public long getId() {
		return id;
	}

}
