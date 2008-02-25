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
package org.cspoker.client.xml.sockets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.client.xml.common.XmlChannel;
import org.cspoker.common.xml.XmlEventListener;

public class XmlSocketsChannel implements XmlChannel {

	private final static Logger logger = Logger
			.getLogger(XmlSocketsChannel.class);

	public final static byte DELIMITER = 0x00;
	public final static char[] DELIMITER_ARRAY = new char[] { (char) DELIMITER };

	private Socket s;
	private Writer w;

	private ExecutorService executor;

	private CharsetDecoder decoder;

	private List<XmlEventListener> xmlEventListeners = new ArrayList<XmlEventListener>();

	private final String server;
	private final int port;
	private final String username;
	private final String password;

	public XmlSocketsChannel(String server, int port, String username,
			String password) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.password = password;
		Charset charset = Charset.forName("UTF-8");
		decoder = charset.newDecoder();
	}

	public synchronized void open() throws LoginException, RemoteException {
		try {
			s = new Socket(server, port);
			w = new OutputStreamWriter(s.getOutputStream());
			if (!login(username, password))
				throw new LoginException();
			executor = Executors.newSingleThreadExecutor();
			executor.execute(new WaitForEvents());
		} catch (IOException e) {
			logger.error(e);
			throw new RemoteException("IOException from socket.",e);
		}
	}

	public synchronized void registerXmlEventListener(XmlEventListener listener) {
		xmlEventListeners.add(listener);
	}

	public synchronized void unRegisterXmlEventListener(XmlEventListener listener) {
		xmlEventListeners.remove(listener);
	}

	private void fireXmlEvent(String xmlEvent) {
		for (XmlEventListener listener : xmlEventListeners) {
			listener.collect(xmlEvent);
		}
	}

	private boolean login(String username, String password) throws IOException {
		w.write("<login username='" + username + "' password='" + password
				+ "' useragent='sockets client'/>");
		try {
			return readUntilDelimiter().contains("<login");
		} catch (IOException e) {
			logger.error("Connection lost during login.", e);
		} catch (InterruptedException e) {
			// no op
		}
		return false;
	}

	public synchronized void send(final String xml) throws RemoteException {
		try {
			w.write(xml);
			w.write(DELIMITER_ARRAY);
		} catch (IOException e) {
			logger.error(e);
			throw new RemoteException("IOException from socket",e);
		}
	}

	private String readUntilDelimiter() throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		ByteBuffer singleByteBuffer = ByteBuffer.allocateDirect(1);
		while (true) {
			int b = s.getInputStream().read();
			if (Thread.interrupted())
				throw new InterruptedException();
			if (b < 0)
				throw new IOException("Connection lost");
			if (b == DELIMITER)
				return sb.toString();
			else {
				singleByteBuffer.put((byte) b);
				CharBuffer decoded = decoder.decode(singleByteBuffer);
				sb.append(decoded);
				singleByteBuffer.clear();
			}
		}
	}

	private AtomicBoolean closed = new AtomicBoolean(false);

	public void close() {
		if (!closed.getAndSet(true)) {
			executor.shutdownNow();
			try {
				w.close();
			} catch (IOException e) {
				logger.error(e);
			}
			try {
				s.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	private class WaitForEvents implements Runnable {

		public void run() {
			try {
				String s = readUntilDelimiter();
				fireXmlEvent(s);
				executor.execute(this);
			} catch (IOException e) {
				close();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

	}

}
