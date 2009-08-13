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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.cspoker.client.communication.pokersource.beans.commands.JSONCommand;
import org.cspoker.client.communication.pokersource.beans.commands.Login;
import org.cspoker.client.communication.pokersource.beans.commands.Ping;
import org.cspoker.client.communication.pokersource.beans.events.AuthOk;
import org.cspoker.client.communication.pokersource.beans.events.DefaultListener;
import org.cspoker.client.communication.pokersource.beans.events.EventListener;
import org.cspoker.client.communication.pokersource.beans.events.JSONEvent;
import org.cspoker.client.communication.pokersource.beans.events.Serial;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class PokersourceConnection extends RESTConnection{

	public static void main(String[] args) throws IOException {
		PokersourceConnection conn = new PokersourceConnection("http://pokersource.eu/POKER_REST");
		conn.addListener(new DefaultListener(){
			
			@Override
			public void onAuthOk(AuthOk authOk) {
				System.out.println(authOk.toJSONObject().toString());
			}
			
			@Override
			public void onSerial(Serial serial) {
				System.out.println(serial.toJSONObject().toString());
			}
			
		});
		conn.send(new Ping());
		conn.send(new Login("foobar", "foobar"));
	}
	
	public PokersourceConnection(String server) throws MalformedURLException {
		super(server);
	}

	public void send(JSONCommand command) throws IOException{
		String response = put(command.toJSONObject().toString());
		JSONArray jsonResponse = (JSONArray) JSONSerializer.toJSON( response );
		for(int i=0; i<jsonResponse.size();i++){
			JSONObject jsonEvent = jsonResponse.getJSONObject(i);
			JsonConfig jsonConfig = new JsonConfig();  
			jsonConfig.setRootClass( eventTypes.get(jsonEvent.get("type")) );  
			JSONEvent event = (JSONEvent) JSONSerializer.toJava( jsonEvent, jsonConfig );  
			signal(event);
		}
	}
	
	private final List<EventListener> listeners = new CopyOnWriteArrayList<EventListener>();
	
	public void addListener(EventListener listener){
		listeners.add(listener);
	}
	
	private void signal(JSONEvent event) {
		for(EventListener listener:listeners) event.signal(listener);
	}

	private final static ImmutableMap<String, Class<? extends JSONEvent>> eventTypes = 
		(new Builder<String, Class<? extends JSONEvent>>())
		.put(AuthOk.getStaticType(), AuthOk.class)
		.put(Serial.getStaticType(), Serial.class)
		.build();
}
