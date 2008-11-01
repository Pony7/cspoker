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
package org.cspoker.server.xml.http.handler;

import java.io.StringWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.common.api.shared.context.StaticServerContext;
import org.cspoker.common.api.shared.event.ServerEvent;
import org.cspoker.common.api.shared.http.HTTPRequest;
import org.cspoker.common.api.shared.http.HTTPResponse;
import org.cspoker.common.api.shared.listener.ServerEventListener;
import org.cspoker.common.api.shared.listener.UniversalServerListener;
import org.cspoker.common.jaxbcontext.AllHTTPJAXBContexts;
import org.cspoker.common.util.Pair;
import org.cspoker.common.util.lazy.IFactory1;
import org.cspoker.common.util.lazy.LazyMap1;
import org.cspoker.server.xml.common.XmlServerContext;
import org.cspoker.server.xml.http.handler.abstracts.AbstractHttpHandler;
import org.cspoker.server.xml.http.handler.exception.HttpExceptionImpl;
import org.xml.sax.InputSource;

import com.sun.net.httpserver.HttpExchange;

public class CSPokerHandler extends AbstractHttpHandler {

	private final static Logger logger = Logger.getLogger(CSPokerHandler.class);

	private CSPokerServer cspokerServer;

	private LazyMap1<String, Pair<StaticServerContext, Queue<ServerEvent>>, LoginException> contexts 
	= new LazyMap1<String, Pair<StaticServerContext, Queue<ServerEvent>>, LoginException>();

	public CSPokerHandler(CSPokerServer cspokerServer) {
		this.cspokerServer = cspokerServer;
	}

	protected byte[] getResponse(HttpExchange http) throws HttpExceptionImpl {
		final Pair<String,String> credentials = AbstractHttpHandler.getCredentials(http.getRequestHeaders());

		logger.debug("HTTP request from "+credentials.getLeft());
		
		Pair<StaticServerContext, Queue<ServerEvent>> state;
		try {
			state = contexts.getOrCreate(credentials.getLeft(), new IFactory1<Pair<StaticServerContext, Queue<ServerEvent>>, LoginException>(){

				public Pair<StaticServerContext, Queue<ServerEvent>> create() throws LoginException {
					ServerContext serverContext = cspokerServer.login(credentials.getLeft(), credentials.getRight());
					final ConcurrentLinkedQueue<ServerEvent> eventQueue = new ConcurrentLinkedQueue<ServerEvent>();
					StaticServerContext staticServerContext = new XmlServerContext(serverContext,
							new UniversalServerListener(
									new ServerEventListener(){

										public void onServerEvent(ServerEvent event) {
											eventQueue.offer(event);
										}

									}
							)
					);
					return new Pair<StaticServerContext, Queue<ServerEvent>>(staticServerContext,eventQueue);
				}

			});
			
			if(!state.getLeft().getAccountContext().hasPassword(credentials.getRight())){
				throw new LoginException("Bad Password");
			}
			
			Unmarshaller um = AllHTTPJAXBContexts.context.createUnmarshaller();
			HTTPRequest request = (HTTPRequest) um.unmarshal(new InputSource(http.getRequestBody()));

			HTTPResponse response = request.performRequest(state.getLeft(), state.getRight());

			StringWriter xml = new StringWriter();
			Marshaller m = AllHTTPJAXBContexts.context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FRAGMENT, true);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(response, xml);
			xml.flush();
			logger.trace("Returning response of length " + xml.toString().length()
					+ ":\n" + xml);
			return xml.toString().getBytes();
		} catch (JAXBException e) {
			logger.debug(e);
			throw new HttpExceptionImpl(e, 400);
		} catch (LoginException e) {
			logger.debug(e);
			throw new HttpExceptionImpl(e, 401);
		}
	}

}
