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
package org.cspoker.common.api.shared.event;

import java.io.Serializable;

import net.jcip.annotations.Immutable;

@Immutable
public class EventId implements Serializable{
	
	private static final long serialVersionUID = 7553024997059636301L;
	
	private final long eventId;

	public EventId() {
		this(0);
	}
	
	public EventId(long eventId){
		this.eventId = eventId;
	}

	public long getId(){
		return eventId;
	}
	
	@Override
	public String toString(){
		return "#"+Long.toString(eventId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (eventId ^ (eventId >>> 32));
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
		final EventId other = (EventId) obj;
		if (eventId != other.eventId)
			return false;
		return true;
	}

}
