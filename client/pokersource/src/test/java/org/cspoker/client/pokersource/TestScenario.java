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
package org.cspoker.client.pokersource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.external.pokersource.JSONPacket;
import org.cspoker.external.pokersource.PokersourceConnection;
import org.cspoker.external.pokersource.commands.Login;
import org.cspoker.external.pokersource.commands.poker.Call;
import org.cspoker.external.pokersource.commands.poker.Poll;
import org.cspoker.external.pokersource.commands.poker.Sit;
import org.cspoker.external.pokersource.commands.poker.SitOut;
import org.cspoker.external.pokersource.commands.poker.TablePicker;
import org.cspoker.external.pokersource.eventlisteners.all.AllEventListener;
import org.cspoker.external.pokersource.eventlisteners.all.LoggingListener;
import org.cspoker.external.pokersource.events.Error;
import org.cspoker.external.pokersource.events.Serial;
import org.cspoker.external.pokersource.events.poker.SelfInPosition;
import org.cspoker.external.pokersource.events.poker.Table;

public class TestScenario {

	static {
		Log4JPropertiesLoader
		.load("org/cspoker/client/pokersource/logging/log4j.properties");
	}

	private final static Logger logger = Logger.getLogger(TestScenario.class);


	public static void main(String[] args) throws IOException {
		(new TestScenario()).run();
	}

	private int serial;
	private int game_id;
	private boolean running = true;

	private Queue<Runnable> todos = new LinkedList<Runnable>();

	private void run() throws IOException {
		final PokersourceConnection conn = new PokersourceConnection("http://pokersource.eu/POKER_REST");
		try {
			AllEventListener listener = new LoggingListener(){

				@Override
				protected void log(JSONPacket event) {
					logger.info(event.getClass().getSimpleName()+": "+event.toJSONObject().toString());
				}

				@Override
				public void onSerial(Serial serial2) {
					super.onSerial(serial2);
					serial = serial2.getSerial();
				}

				@Override
				public void onTable(Table table) {
					super.onTable(table);
					game_id = table.getId();
				}
				
				@Override
				public void onError(Error error) {
					super.onError(error);
					running = false;
				}

				@Override
				public void onSelfInPosition(SelfInPosition selfInPosition) {
					super.onSelfInPosition(selfInPosition);
					todos.add(new Runnable(){
						@Override
						public void run() {
							try {
								conn.send(new Call(serial, game_id));
							} catch (IOException e) {
								throw new IllegalStateException(e);
							}
						}
					});
				}

				@Override
				public void onSitOut(SitOut sitOut) {
					super.onSitOut(sitOut);
					if(sitOut.getGame_id()==game_id && sitOut.getSerial() == serial){
						todos.add(new Runnable(){
							@Override
							public void run() {
								try {
									conn.send(new Sit(serial, game_id));
								} catch (IOException e) {
									throw new IllegalStateException(e);
								}
							}
						});
					}
				}

			};
			conn.addListener(listener);
			
			todos.add(new Runnable(){
				@Override
				public void run() {
					try {
						conn.send(new Login("foobar", "foobar"));
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
				}
			});
			todos.add(new Runnable(){
				@Override
				public void run() {
					try {
						conn.send(new TablePicker(serial, true));
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
				}
			});
			todos.add(new Runnable(){
				@Override
				public void run() {
					try {
						conn.send(new SitOut(serial, game_id));
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
				}
			});
			while (running) {
				if(todos.isEmpty()){
					conn.send(new Poll(game_id,0));
				}else{
					while(running && !todos.isEmpty()) {
						todos.poll().run();
					}
				}
				try {
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
