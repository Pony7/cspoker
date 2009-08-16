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
package org.cspoker.ai.lore;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.cspoker.client.communication.pokersource.PokersourceConnection;
import org.cspoker.client.communication.pokersource.commands.Login;
import org.cspoker.client.communication.pokersource.commands.Ping;
import org.cspoker.client.communication.pokersource.commands.poker.GetPlayerInfo;
import org.cspoker.client.communication.pokersource.commands.poker.TablePicker;
import org.cspoker.client.communication.pokersource.eventlisteners.all.AllEventListener;
import org.cspoker.client.communication.pokersource.eventlisteners.all.LoggingListener;
import org.cspoker.client.communication.pokersource.events.JSONEvent;
import org.cspoker.client.communication.pokersource.events.general.Serial;
import org.cspoker.common.util.Log4JPropertiesLoader;

public class TestScenario {
	
	static {
		Log4JPropertiesLoader
		.load("org/cspoker/ai/lore/logging/log4j.properties");
	}
	
	private final static Logger logger = Logger.getLogger(TestScenario.class);
	
	public static void main(String[] args) throws IOException {
		final PokersourceConnection conn = new PokersourceConnection("http://pokersource.eu/POKER_REST");
		try {
			AllEventListener listener = new LoggingListener(){
				
				@Override
				protected void log(JSONEvent event) {
					logger.info(event.getClass().getSimpleName()+": "+event.toJSONObject().toString());
				}
				
				@Override
				public void onSerial(Serial serial) {
					super.onSerial(serial);
					try {
						conn.send(new TablePicker(serial.getSerial(), true));
						conn.send(new GetPlayerInfo());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			};
			conn.addListener(listener);
			conn.send(new Ping());
			conn.send(new Login("foobar", "foobar"));
			for (int i = 0; i < 20; i++) {
				try {
					conn.send(new Ping());
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} finally{
			conn.close();
		}
	}
	
}
