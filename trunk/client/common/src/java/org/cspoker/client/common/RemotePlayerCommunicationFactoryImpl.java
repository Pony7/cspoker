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
package org.cspoker.client.common;

import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.cspoker.common.RemotePlayerCommunication;

public class RemotePlayerCommunicationFactoryImpl implements
		RemotePlayerCommunicationFactory {

	public final static RemotePlayerCommunicationFactoryImpl global_factory = new RemotePlayerCommunicationFactoryImpl();

	private List<RemotePlayerCommunicationFactory> providers = new ArrayList<RemotePlayerCommunicationFactory>();

	public void addRemotePlayerCommunicationProvider(
			RemotePlayerCommunicationFactory provider) {
		providers.add(provider);
	}

	public RemotePlayerCommunication getRemotePlayerCommunication(
			String server, int port, String username, String password)
			throws ConnectException, NoProviderException, LoginException {

		NoProviderException lastNoProviderException = null;
		ConnectException lastConnectException = null;
		LoginException lastLoginException = null;

		for (RemotePlayerCommunicationFactory p : providers) {
			try {
				return p.getRemotePlayerCommunication(server, port, username,
						password);
			} catch (NoProviderException e) {
				lastNoProviderException = e;
			} catch (ConnectException e) {
				lastConnectException = e;
			} catch (LoginException e) {
				lastLoginException = e;

			}
		}
		if (lastConnectException != null)
			throw lastConnectException;
		if (lastNoProviderException != null)
			throw lastNoProviderException;
		if (lastLoginException != null)
			throw lastLoginException;
		throw new NoProviderException();
	}

}
