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
import java.io.StringReader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cspoker.client.xml.common.XmlActionSerializer;
import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.event.ActionEvent;
import org.cspoker.common.api.shared.event.ServerEvent;
import org.cspoker.common.api.shared.listener.ActionAndServerEventListener;
import org.cspoker.common.jaxbcontext.AllSocketJAXBContexts;

public class XmlSocketsChannel implements XmlActionSerializer {

	private final static Logger logger = Logger.getLogger(XmlSocketsChannel.class);

	private final Socket socket;
	private final Writer socketWriter;

	private final ExecutorService executor;

	private final CharsetDecoder decoder;

	private volatile ActionAndServerEventListener eventHandler;

	public XmlSocketsChannel(String server, int port, String username,
			String password) throws RemoteException {
		Charset charset = Charset.forName("UTF-8");
		decoder = charset.newDecoder();
		try {
			socket = new Socket(server, port);
			socketWriter = new OutputStreamWriter(socket.getOutputStream());
		} catch (UnknownHostException exception) {
			throw new RemoteException("Exception opening socket.", exception);
		} catch (IOException exception) {
			throw new RemoteException("Exception opening socket.", exception);
		}
		executor = Executors.newSingleThreadExecutor();
		executor.execute(new WaitForEvents());
	}

	private String readUntilDelimiter() throws IOException,
	InterruptedException {
		StringBuilder sb = new StringBuilder();
		ByteBuffer singleByteBuffer = ByteBuffer.allocateDirect(1);
		while (true) {
			int b = socket.getInputStream().read();
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (b < 0) {
				throw new IOException("Connection lost");
			}
			if (b == 0) {
				if (sb.length() > 0) {
					return sb.toString();
				} else {
					logger.trace("Delimiter found but no xml: length "
							+ sb.length());
				}
			} else {
				singleByteBuffer.put((byte) b);
				singleByteBuffer.flip();
				CharBuffer decoded = decoder.decode(singleByteBuffer);
				sb.append(decoded);
				singleByteBuffer.clear();
			}
		}
	}

	public synchronized void close() {
		executor.shutdown();
		executor.shutdownNow();
		try {
			socketWriter.close();
		} catch (IOException e) {
			logger.error(e);
		}
		try {
			socket.close();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	private class WaitForEvents implements Runnable {

		public void run() {
			try {
				while (true) {
					String s = readUntilDelimiter();
					if (eventHandler!=null) {
						Unmarshaller unmarshaller = AllSocketJAXBContexts.context
						.createUnmarshaller();
						Object event = unmarshaller.unmarshal(new StringReader(
								s));
						if (event instanceof ActionEvent<?>) {
							eventHandler
									.onActionPerformed((ActionEvent<?>) event);
						} else if (event instanceof ServerEvent) {
							eventHandler.onServerEvent((ServerEvent) event);
						} else {
							throw new ClassCastException("Unknown event type.");
						}
					}
				}
			} catch (IOException e) {
				logger.error(e);
				close();
			} catch (InterruptedException e) {
				logger.error(e);
				Thread.currentThread().interrupt();
			} catch (JAXBException e) {
				logger.error(e);
				close();
			}
		}

	}

	public void setEventListener(ActionAndServerEventListener handler) {
		this.eventHandler = handler;
	}

	public synchronized void perform(DispatchableAction<?> action) throws RemoteException {
		try {
			Marshaller marshaller = AllSocketJAXBContexts.context.createMarshaller();
			marshaller.marshal(action, socketWriter);
			socketWriter.write(((char) 0x00));
			socketWriter.flush();
		} catch (JAXBException exception) {
			throw new RemoteException("Exception sending action.", exception);
		} catch (IOException exception) {
			throw new RemoteException("Exception sending action.", exception);
		}
	}

}
