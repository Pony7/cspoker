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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

import junit.framework.TestCase;

import org.cspoker.client.User;
import org.cspoker.client.gui.swt.control.ClientCore;
import org.cspoker.client.gui.swt.window.LobbyWindow;
import org.cspoker.client.rmi.RemoteRMIServer;
import org.cspoker.common.RemoteCSPokerServer;
import org.eclipse.swt.widgets.Display;

public class TestSWTClient
		extends TestCase {
	
	private ClientCore client1;
	private ClientCore client2;
	ExecutorService tester = Executors.newFixedThreadPool(2);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp()
			throws Exception {
		super.setUp();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown()
			throws Exception {
		super.tearDown();
	}
	
	public void testLogin() {
		final RemoteCSPokerServer server = new RemoteRMIServer(ClientCore.DEFAULT_URL, ClientCore.DEFAULT_PORT_RMI);
		client1 = new ClientCore(new User("Stephan", "test"));
		client2 = new ClientCore(new User("dummy", "test"));
		Display.getDefault().syncExec(new Runnable() {
			
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					
					public void run() {
						try {
							client1.login(server);
						} catch (LoginException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						LobbyWindow lobby = new LobbyWindow(client1);
						lobby.setLobbyContext(client1.getCommunication());
						client1.getGui().setLobby(lobby);
						lobby.show();
					}
				});
				try {
					client2.login(server);
				} catch (LoginException e) {
					e.printStackTrace();
				}
				LobbyWindow lobby = new LobbyWindow(client2);
				lobby.setLobbyContext(client2.getCommunication());
				client2.getGui().setLobby(lobby);
				lobby.show();
			}
		});
		synchronized (this) {
			try {
				wait(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
