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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.CharBuffer;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.apache.log4j.Logger;
import org.cspoker.client.xml.common.XmlChannel;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.xml.XmlEventListener;
import org.cspoker.common.xml.actions.ActionJAXBContext;
import org.cspoker.common.xml.actions.SayAction;

public class XmlHttpChannel implements XmlChannel {

	private final static Logger logger = Logger.getLogger(XmlHttpChannel.class);

	private ScheduledExecutorService scheduler;
	
	private List<XmlEventListener> xmlEventListeners = new ArrayList<XmlEventListener>();

	private URL url;

	private final CharBuffer buffer;

	private final StringBuilder sb;

	public XmlHttpChannel(URL url, final String username, final String password) {
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password
						.toCharArray());
			}
		});
		this.url = url;
		this.buffer = CharBuffer.allocate(2048);
		this.sb = new StringBuilder();
	}

	public synchronized void open() throws LoginException, RemoteException {
	       scheduler = Executors.newScheduledThreadPool(1);
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(20000);
			connection.setAllowUserInteraction(true);
			connection.setInstanceFollowRedirects(false);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			Writer w = new OutputStreamWriter(connection.getOutputStream());
			w.write(noOpXml);
			connection.getOutputStream().close();
			if(connection.getResponseCode()==401){
				LoginException e = new LoginException("Login Failed, received 401.");
				logger.error(e);
				throw e;
			}else if (connection.getResponseCode() / 100 == 4
					|| connection.getResponseCode() / 100 == 5) {
				throw new RemoteException("Unknown error from the server.");
			}
		} catch (IOException e) {
			throw new RemoteException("IOException in XML channel",e);
		} 
		scheduler.scheduleWithFixedDelay(new NoOpSubmitter(this), 1500, 1500, TimeUnit.MILLISECONDS );
	}

	public synchronized void close() {
		scheduler.shutdownNow();
	}

	public synchronized void send(String xml) throws RemoteException {
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
				RemoteException e = new RemoteException("Unknown error from the server:"+connection.getResponseCode());
				logger.error(e);
				throw e;
			}
			
			InputStreamReader r = new InputStreamReader(connection.getInputStream());
			int read = r.read(buffer);
			boolean intag = false, first = false, wasslash=false;
			int open = 0;
			while(read!=-1){
				buffer.flip();
				while(buffer.hasRemaining()){
					char c = buffer.get();
					sb.append(c);
					if(c=='>'){
						intag = false;
						if(wasslash)
							open--;
						if(open==0){
							dispatch(sb.toString());
							sb.setLength(0);
						}
					}
					if(wasslash)
						wasslash=false;
					if(first){
						first=false;
						if(c=='/')
							--open;
						else{
							++open;
						}
					}else if(intag && c=='/'){
						wasslash=true;
					}
					if(c=='<'){
						intag = true;
						first = true;
					}
				}
				buffer.rewind();
				read = r.read(buffer);
			}
			
		} catch (IOException e) {
			throw new RemoteException("IOException in XML channel",e);
		} 

	}

	private void dispatch(String xml) {
		for(XmlEventListener l:xmlEventListeners)
			l.collect(xml);
	}

	public synchronized void registerXmlEventListener(XmlEventListener listener) {
		xmlEventListeners.add(listener);
	}

	public synchronized void unRegisterXmlEventListener(XmlEventListener listener) {
		xmlEventListeners.remove(listener);
	}

	public static String noOpXml = initNoOp();

	private static String initNoOp() {
		try {
			StringWriter s = new StringWriter();
			Marshaller m = ActionJAXBContext.context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(new SayAction(5,"sfdsqd/qsd<"),s);
			return s.toString();
		} catch (PropertyException e) {
			logger.fatal(e);
			throw new IllegalStateException(e);
		} catch (JAXBException e) {
			logger.fatal(e);
			throw new IllegalStateException(e);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(noOpXml);
	}

}
