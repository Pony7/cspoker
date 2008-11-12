/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.cspoker.client.User;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.control.DisplayExecutor;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;
import org.cspoker.common.elements.table.TableId;
import org.eclipse.swt.widgets.Display;

public abstract class TestSWTClient
		extends TestCase {
	
	private ClientCore client;
	protected RemoteCSPokerServer server;
	private DisplayExecutor displayexecutor;
	private List<User> users;
	
	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp()
			throws Exception {
		super.setUp();
		setServer();
		displayexecutor = DisplayExecutor.getInstance();
		users = new ArrayList<User>();
		users.addAll(Arrays.asList(new User("Stephan", "test"), new User("dummy", "test")/*
																						 * ,
																						 * new
																						 * User
																						 * (
																						 * "test"
																						 * ,
																						 * "test"
																						 * )
																						 */));
	}
	
	protected abstract void setServer();
	
	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown()
			throws Exception {
		super.tearDown();
	}
	
	public void testPlay() {
		int seatId = -1;
		final TableId tableId = new TableId(0);
		
		int smallBlind = 50;
		int buyin = smallBlind * 200;
		int delay = 2000;
		for (User u : users) {
			seatId++;
			try {
				client = new ClientCore(u);
				client.login(server);
				
				final LobbyWindow lobby = new LobbyWindow(client);
				lobby.setLobbyContext(client.getCommunication());
				client.getGui().setLobby(lobby);
				
				TableConfiguration tConfig = new TableConfiguration(smallBlind, delay);
				lobby.getContext().createHoldemTable(u.getUserName() + "'s test table", tConfig);
				// Run blocking calls in extra thread
				displayexecutor.execute(new Runnable() {
					
					@Override
					public void run() {
						lobby.show();
						
					}
				});
				final GameWindow w = client.getGui().getGameWindow(tableId, true);
				w.getUser().sitIn(new SeatId(seatId), buyin);
				// Run blocking calls in extra thread
				displayexecutor.execute(new Runnable() {
					
					@Override
					public void run() {
						w.show();
						
					}
				});
				
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
		}
		// Listen to events#
		Display display = Display.getDefault();
		while (!display.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
	}
}
