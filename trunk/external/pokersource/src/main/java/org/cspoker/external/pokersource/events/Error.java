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
package org.cspoker.external.pokersource.events;

import org.cspoker.external.pokersource.GeneralPacket;
import org.cspoker.external.pokersource.eventlisteners.general.GeneralEventListener;


public class Error extends GeneralPacket{
	
	public String getType() {
		return getStaticType();
	}
	
	public static String getStaticType() {
		return "PacketError";
	}
	
	private String message;
	private int code;
	private int other_type;

	
	@Override
	public void signal(GeneralEventListener listener) {
		listener.onError(this);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getOther_type() {
		return other_type;
	}

	public void setOther_type(int other_type) {
		this.other_type = other_type;
	}
	
}
