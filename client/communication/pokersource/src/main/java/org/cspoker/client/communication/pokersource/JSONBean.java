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
package org.cspoker.client.communication.pokersource;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public abstract class JSONBean {

	public JSONObject toJSONObject(){
		return (JSONObject) JSONSerializer.toJSON( this );
	}
	
	public void setType(String type){
		if(!getType().equals(type)) throw new IllegalStateException(type+" should be "+getType());
	}
	
	public abstract String getType();  
	
}
