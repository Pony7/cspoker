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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cspoker.client.xml.common.XmlActionSerializer;
import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.event.ActionEvent;
import org.cspoker.common.api.shared.event.ServerEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.api.shared.http.HTTPRequest;
import org.cspoker.common.api.shared.http.HTTPResponse;
import org.cspoker.common.api.shared.listener.ActionAndServerEventListener;
import org.cspoker.common.jaxbcontext.ActionJAXBContext;
import org.cspoker.common.jaxbcontext.AllHTTPJAXBContexts;
import org.cspoker.common.util.Base64;

public class XmlHttpSerializer implements XmlActionSerializer {

	private final static Logger logger = Logger.getLogger(XmlHttpSerializer.class);
	private final ScheduledExecutorService scheduler;
	private final URL url;
	private final String authorizationString;
	private final AtomicBoolean polling = new AtomicBoolean(false);
	private final ConcurrentLinkedQueue<DispatchableAction<?>> actions = new ConcurrentLinkedQueue<DispatchableAction<?>>();
	private volatile ActionAndServerEventListener eventListener;

	public XmlHttpSerializer(URL url, final String username, final String password) {
		authorizationString = "Basic "
			+ Base64.encode((username + ":" + password).getBytes(), 0);
		this.url = url;
		scheduler = Executors.newScheduledThreadPool(1);
	}


	public synchronized void close() {
		scheduler.shutdown();
		scheduler.shutdownNow();
	}

	public synchronized void sendRequest() {
		if(polling.compareAndSet(false, true)){
			scheduler.scheduleWithFixedDelay(new SendRequest(this), 1500, 1500,
					TimeUnit.MILLISECONDS);
		}
		
		HTTPRequest request = new HTTPRequest(actions);
		
		try {
			HttpURLConnection connection = (HttpURLConnection) url
			.openConnection();
			connection.setRequestProperty("Authorization", authorizationString);
			connection.setConnectTimeout(20000);
			connection.setAllowUserInteraction(true);
			connection.setInstanceFollowRedirects(false);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");


			Marshaller m = ActionJAXBContext.context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(request, connection.getOutputStream());

			connection.getOutputStream().flush();
			connection.getOutputStream().close();

			if (connection.getResponseCode() == 401) {
				throwIllegalActionExceptions(request, "Login Failed.");
			} else if (connection.getResponseCode() / 100 == 4
					|| connection.getResponseCode() / 100 == 5) {
				String line;
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				StringBuffer buffer = new StringBuffer();
				while ((line = in.readLine()) != null) {
					buffer.append(line);
				}
				RemoteException e = new RemoteException(
						"Unknown error from the server:"
						+ connection.getResponseCode() + "\n"
						+ buffer.toString());
				logger.error(e);
				throwRemoteExceptions(request, e.getMessage());
			}
			Unmarshaller unmarshaller = AllHTTPJAXBContexts.context.createUnmarshaller();
			HTTPResponse response = (HTTPResponse)unmarshaller.unmarshal(connection.getInputStream());

			for(ServerEvent event:response.getEvents()){
				eventListener.onServerEvent(event);
			}
			for(ActionEvent<?> event:response.getActionResults()){
				eventListener.onActionPerformed(event);
			}
		} catch (ProtocolException exception) {
			throwRemoteExceptions(request,exception.getMessage());
		} catch (PropertyException exception) {
			throwRemoteExceptions(request,exception.getMessage());
		} catch (IOException exception) {
			throwRemoteExceptions(request,exception.getMessage());
		} catch (JAXBException exception) {
			throwRemoteExceptions(request,exception.getMessage());
		}
	}

	private void throwRemoteExceptions(HTTPRequest request, String message){
		for(DispatchableAction<?> action:request.getActions()){
			eventListener.onActionPerformed(action.getRemoteExceptionEvent(new RemoteException(message)));
		}
	}

	private void throwIllegalActionExceptions(HTTPRequest request, String message){
		for(DispatchableAction<?> action:request.getActions()){
			eventListener.onActionPerformed(action.getIllegalActionEvent(new IllegalActionException(message)));
		}
	}

	public void setEventListener(ActionAndServerEventListener listener) {
		this.eventListener = listener;
	}

	public void perform(DispatchableAction<?> action)
	throws RemoteException {
		actions.add(action);
	}
}
