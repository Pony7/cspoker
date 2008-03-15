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

import org.apache.log4j.Logger;
import org.cspoker.common.xml.XmlEventListener;
import org.cspoker.server.common.game.player.IllegalNameException;
import org.cspoker.server.common.game.session.PlayerKilledExcepion;
import org.cspoker.server.common.game.session.Session;
import org.cspoker.server.common.game.session.SessionManager;
import org.cspoker.server.xml.common.XmlPlayerCommunication;
import org.cspoker.server.xml.common.XmlPlayerCommunicationFactory;

public class ClientContext implements XmlEventListener {

	private final static Logger logger = Logger.getLogger(ClientContext.class);

	private final StringBuilder buffer;
	private XmlPlayerCommunication playerComm;
	private Session session;

	private final List<ByteBuffer> writeBuffer = new ArrayList<ByteBuffer>();

	private final Object writeBufferLock = new Object();
	private final Object authenticateLock = new Object();

	private final SocketChannel client;
	private final Selector selector;

	private final Charset charset;
	// CharsetEncoder is not thread safe!
	private final CharsetEncoder encoder;

	public ClientContext(SocketChannel client, Selector selector) {
		this.client = client;
		this.selector = selector;
		buffer = new StringBuilder();

		charset = Charset.forName("UTF-8");
		encoder = charset.newEncoder();
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
				// logger.trace("trying to write " + bytes.remaining()+ "
				// bytes.");
				client.write(bytes);
				if (bytes.remaining() > 0) {
					logger.trace("stopping write early because there are "
							+ bytes.remaining() + " bytes unwritten.");
					/* //registerWriteInterest(); //bug workaround? */
					return;
				}
				// logger.trace("removing bytebuffer from the buffer list.");
				i.remove();
			}
			unregisterWriteInterest();
			logger.trace("finished entire write operation");
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
			logger.trace("adding write interest, added " + bytes.remaining()+ " bytes to the buffer.");
			registerWriteInterest();
		}
	}

	public void closeConnection() {
		if (playerComm != null) {
			// TODO will this sequence of calls end?
			XmlPlayerCommunicationFactory.global_factory.unRegister(session);
			SessionManager.global_session_manager
			.killSession(session.getUserName());
		}
		try {
			client.close();
		} catch (IOException e) {
			logger.error("Can't close connection.", e);
		}
	}

	public void send(String xml) {
		try {
			appendToWriteBuffer(encoder.encode(CharBuffer.wrap(xml
					+ ((char) 0x00))));
			logger.trace("wrote reply to write buffer list:\n" + xml);
		} catch (CharacterCodingException e) {
			logger.error(e.getMessage());
			throw new IllegalStateException(e);
		}
	}

	public XmlPlayerCommunication getXmlPlayerCommunication() {
		return playerComm;
	}

	public void collect(String xmlEvent) {
		send(xmlEvent);
	}

	public void login(String username, String password, String useragent)
	throws IllegalNameException {
		synchronized (authenticateLock) {
			if (isAuthenticated()) {
				throw new IllegalStateException("Can't login twice");
			}
			session = SessionManager.global_session_manager
			.getSession(username);
			try {
				playerComm = XmlPlayerCommunicationFactory.global_factory
				.getRegisteredXmlPlayerCommunication(session, this);
			} catch (PlayerKilledExcepion e) {
				logger.error("player killed right after login", e);
				// ignore
			}
		}
	}

	public boolean isAuthenticated() {
		synchronized (authenticateLock) {
			return playerComm != null;
		}
	}

}
