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
package org.cspoker.external.pokersource.eventlisteners.general;

import org.cspoker.external.pokersource.events.AuthOk;
import org.cspoker.external.pokersource.events.AuthRefused;
import org.cspoker.external.pokersource.events.Error;
import org.cspoker.external.pokersource.events.Serial;

public interface GeneralEventListener {

	void onAuthOk(AuthOk authOk);

	void onSerial(Serial serial);

	void onAuthRefused(AuthRefused authRefused);

	void onError(Error error);
	
}
