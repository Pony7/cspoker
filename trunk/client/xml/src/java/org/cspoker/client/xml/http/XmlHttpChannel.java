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
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.cspoker.client.xml.common.XmlChannel;
import org.cspoker.client.xml.sockets.exceptions.LoginFailedException;
import org.cspoker.common.xml.XmlEventListener;

public class XmlHttpChannel implements XmlChannel {

    private ExecutorService executor;

    private List<XmlEventListener> xmlEventListeners = new ArrayList<XmlEventListener>();

    private final String server;
    private final int port;
    private final String username;
    private final String password;

    private URL url;
    
    
    public XmlHttpChannel(String server, int port, String path, final String username, final String password) throws MalformedURLException{
	this.server = server;
	this.port=port;
	this.username = username;
	this.password = password;
	Authenticator.setDefault(new Authenticator() {
	    @Override
	    protected PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication (username, password.toCharArray());
	    }
	});
	String address = "http://"+server + ":" + port;
	this.url=new URL(address+path);
    }

    public void open() throws IOException{
	executor = Executors.newSingleThreadExecutor();
    }
    
    public void close() {
	executor.shutdownNow();
    }


    public void send(String xml) throws IOException, LoginFailedException {
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	connection.setConnectTimeout(20000);
	connection.setAllowUserInteraction(true);
	connection.setInstanceFollowRedirects(false);
	connection.setDoOutput(true);
	connection.setRequestMethod("POST");
	
	Writer w = new OutputStreamWriter(connection.getOutputStream());
	w.write(xml);
	connection.getOutputStream().close();
	
	if(connection.getResponseCode()==401){
	    throw new LoginFailedException();
	}else if(connection.getResponseCode()/100==4||connection.getResponseCode()/100==5){
	    throw new IOException("Unknown error from the server.");
	}
	
	
	
    }
    
    public void registerXmlEventListener(XmlEventListener listener){
	xmlEventListeners.add(listener);
    }
    
    public void unRegisterXmlEventListener(XmlEventListener listener){
	xmlEventListeners.remove(listener);
    }
    
    private void fireXmlEvent(String xmlEvent){
	for(XmlEventListener listener:xmlEventListeners){
	    listener.collect(xmlEvent);
	}
    }


}
