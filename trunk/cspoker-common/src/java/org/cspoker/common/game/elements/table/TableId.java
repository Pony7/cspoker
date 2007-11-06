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

package org.cspoker.common.game.elements.table;

import java.io.Serializable;


/**
 * A class to represent table id's.
 * 
 * @author Kenzo
 * 
 */
public class TableId implements Serializable {

    private static final long serialVersionUID = -3605432117019173699L;
    
    /**
     * The variable containing the long representation of the id.
     */
    private final long id;

    /**
     * Construct a new table id with given id.
     * 
     * @param id
     *                The long to use as id.
     */
    public TableId(long id) {
	this.id = id;
    }

    /**
     * Returns a hash code value for this table id.
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final TableId other = (TableId) obj;
	if (id != other.id)
	    return false;
	return true;
    }

    @Override
    public String toString() {
    	return "table: " + String.valueOf(id);
    }

    public long getID() {
	return id;
    }

}
