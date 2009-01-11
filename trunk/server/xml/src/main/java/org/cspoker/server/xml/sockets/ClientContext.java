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
package org.cspoker.server.xml.sockets;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.apache.log4j.Logger;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.context.StaticServerContext;
import org.cspoker.common.api.shared.event.ActionEvent;
import org.cspoker.common.api.shared.event.ActionPerformedEvent;
import org.cspoker.common.api.shared.event.Event;
import org.cspoker.common.api.shared.event.IllegalActionEvent;
import org.cspoker.common.api.shared.event.ServerEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.api.shared.listener.ServerEventListener;
import org.cspoker.common.api.shared.listener.UniversalServerListener;
import org.cspoker.common.api.shared.socket.LoginAction;
import org.cspoker.common.jaxbcontext.EventJAXBContext;
import org.cspoker.server.xml.common.XmlServerContext;

public class ClientContext {

	private final static Logger logger = Logger.getLogger(ClientContext.class);

	private final StringBuilder buffer;

	private final List<ByteBuffer> writeBuffer = new ArrayList<ByteBuffer>();

	private final Object writeBufferLock = new Object();

	private final SocketChannel client;
	private final Selector selector;

	private final Charset charset;

	private final CSPokerServer cspokerServer;

	private volatile StaticServerContext serverContext = null;

	public ClientContext(SocketChannel client, Selector selector, CSPokerServer cspokerServer) {
		this.client = client;
		this.selector = selector;
		this.buffer = new StringBuilder();

		this.charset = Charset.forName("UTF-8");
		this.cspokerServer = cspokerServer;
	}

	public StringBuilder getBuffer() {
		return buffer;
	}

	/**
	 * Writes the current buffer to the client. If the buffer is emptied, the
	 * selector is removed from the client.
	 * 
	 * @throws IOException
	 */
	public void writeBufferToClient() throws IOException {
		synchronized (writeBufferLock) {
			Iterator<ByteBuffer> i = writeBuffer.iterator();
			while (i.hasNext()) {
				ByteBuffer bytes = i.next();
				logger.trace("trying to write " + bytes.remaining()+ "bytes.");
				client.write(bytes);
				if (bytes.remaining() > 0) {
					logger.trace("stopping write early because there are "
							+ bytes.remaining() + " bytes unwritten.");
					/* //registerWriteInterest(); //bug workaround? */
					return;
				}
				logger.trace("removing bytebuffer from the buffer list.");
				i.remove();
			}
			unregisterWriteInterest();
			logger.trace("finished entire write operation");
			if(killAfterResponse){
				logger.trace("killing connection after write");
				closeConnection();
			}
		}
	}

	private void unregisterWriteInterest() {
		client.keyFor(selector).interestOps(SelectionKey.OP_READ);
		selector.wakeup();
		logger.trace("removed write interest");

	}

	private void registerWriteInterest() {
		client.keyFor(selector).interestOps(SelectionKey.OP_READ);
		client.keyFor(selector).interestOps(
				SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		selector.wakeup();
	}

	public void appendToWriteBuffer(ByteBuffer bytes) {
		synchronized (writeBufferLock) {
			writeBuffer.add(bytes);
			logger.trace("adding write interest, added " + bytes.remaining()
					+ " bytes to the buffer.");
			registerWriteInterest();
		}
	}

	public void closeConnection() {
		try {
			client.close();
		} catch (IOException exception) {
		}
		if(serverContext!= null){
			serverContext.logout();
		}
		//TODO trigger something
	}

	public void send(String xml) {
		try {
			appendToWriteBuffer(charset.newEncoder().encode(CharBuffer.wrap(xml+ "\u0000")));
			logger.trace("wrote reply to write buffer list:\n" + xml);
		} catch (CharacterCodingException e) {
			logger.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}
	
	public void send(Event event){
		StringWriter output = new StringWriter();
		try {
			Marshaller m = EventJAXBContext.context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FRAGMENT, true);
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(event, output);
		} catch (PropertyException exception) {
			throw new IllegalStateException(exception);
		} catch (JAXBException exception) {
			throw new IllegalStateException(exception);
		}
		output.flush();
		send(output.toString());
	}
	
	public void perform(DispatchableAction<?> action){
		ActionEvent<?> result = action.wrappedPerform(serverContext);
		send(result);
	}

	public boolean isAuthenticated() {
		return serverContext!=null;
	}

	public void login(LoginAction action) {
		try {
			serverContext = new XmlServerContext(cspokerServer.login(action.getUsername(), action.getPasswordHash()),
					new UniversalServerListener(
							new ServerEventListener(){
								public void onServerEvent(ServerEvent event) {
									send(event);
								}
							}		
					));
			send(new ActionPerformedEvent<Void>(action, null));
		} catch (LoginException exception) {
			send(new IllegalActionEvent<Void>(action, new IllegalActionException("Bad Login.")));
		}
	}
	
	private volatile boolean killAfterResponse = false;

	public void killAfterResponse() {
		killAfterResponse = true;
	}

}
