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
package org.cspoker.client.pokersource.listeners;

import org.cspoker.external.pokersource.eventlisteners.all.DefaultListener;
import org.cspoker.external.pokersource.events.Error;

public class ErrorListener extends DefaultListener{
	
	public ErrorListener() {
		reset();
	}
	
	private Error error;
	
	@Override
	public void onError(Error error) {
		this.error = error;
	}
	
	public void reset() {
		error = null;
	}
	
	public boolean isError() {
		return error!=null;
	}
	
	public String getMsg() {
		return error.getMessage();
	}
	
}