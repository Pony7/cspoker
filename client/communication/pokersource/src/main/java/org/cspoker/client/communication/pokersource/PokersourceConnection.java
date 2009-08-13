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

import java.io.IOException;
import java.net.MalformedURLException;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.cspoker.client.communication.pokersource.beans.commands.Login;
import org.cspoker.client.communication.pokersource.beans.commands.Ping;

public class PokersourceConnection extends RESTConnection{

	public PokersourceConnection(String server) throws MalformedURLException {
		super(server);
	}

	private void ping() throws IOException {
		String response = put((new Ping()).toJSONObject().toString());
		JSONArray jsonArray = (JSONArray) JSONSerializer.toJSON( response );  
		assert(jsonArray.isEmpty()):"Got: "+jsonArray.toString();
	}
	
	private String login(Login login) throws IOException {
		return put(login.toJSONObject().toString());
	}
	
	public static void main(String[] args) throws IOException {
		PokersourceConnection conn = new PokersourceConnection("http://pokersource.eu/POKER_REST");
		conn.ping();
		System.out.println(conn.login(new Login("foobar", "foobar")));
	}
	
}
