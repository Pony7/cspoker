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
package org.cspoker.client.xml.http;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

import org.cspoker.client.xml.common.XmlChannel;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.xml.XmlEventListener;

public class XmlHttpChannel implements XmlChannel {

	private ExecutorService executor;

	private List<XmlEventListener> xmlEventListeners = new ArrayList<XmlEventListener>();

	private URL url;

	public XmlHttpChannel(URL url, final String username, final String password) {
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password
						.toCharArray());
			}
		});
		this.url = url;
	}

	public void open() throws LoginException {
		//TODO check login success
		executor = Executors.newSingleThreadExecutor();
	}

	public void close() {
		executor.shutdownNow();
	}

	public void send(String xml) throws IllegalActionException, RemoteException {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(20000);
			connection.setAllowUserInteraction(true);
			connection.setInstanceFollowRedirects(false);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			Writer w = new OutputStreamWriter(connection.getOutputStream());
			w.write(xml);
			connection.getOutputStream().close();

			if (connection.getResponseCode() / 100 == 4
					|| connection.getResponseCode() / 100 == 5) {
				throw new RemoteException("Unknown error from the server.");
			}
		} catch (IOException e) {
			throw new RemoteException("IOException in XML channel",e);
		} 

	}

	public void registerXmlEventListener(XmlEventListener listener) {
		xmlEventListeners.add(listener);
	}

	public void unRegisterXmlEventListener(XmlEventListener listener) {
		xmlEventListeners.remove(listener);
	}

}
