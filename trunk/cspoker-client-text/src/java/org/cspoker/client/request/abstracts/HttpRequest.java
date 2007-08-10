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
package org.cspoker.client.request.abstracts;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.client.CommandExecutor;
import org.cspoker.client.exceptions.FailedAuthenticationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public abstract class HttpRequest extends DefaultHandler implements CommandExecutor{

    private URL url;

    public HttpRequest(String address) throws MalformedURLException {
	this.url=new URL(address+getPath());
    }
    
    protected URL getURL(){
	return url;
    }
    
    public String send(String... args) throws Exception{
	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	connection.setAllowUserInteraction(true);
	connection.setInstanceFollowRedirects(false);
	connection.setDoOutput(isDoOutput());
	connection.setRequestMethod(getRequestMethod());
	
	if (isDoOutput()) {
	    StreamResult requestResult = new StreamResult(connection
		    .getOutputStream());
	    SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
		    .newInstance();
	    TransformerHandler request = tf.newTransformerHandler();
	    request.setResult(requestResult);
	    request.startDocument();
	    doOutput(request, args);
	    request.endDocument();
	    //TODO remove?
	    connection.getOutputStream().flush();
	    connection.getOutputStream().close();
	}
	
	XMLReader xr = XMLReaderFactory.createXMLReader();
	xr.setContentHandler(this);
	xr.setErrorHandler(this);
	try {
	    xr.parse(new InputSource(connection.getInputStream()));
	} catch (ProtocolException e) {
	    //TODO fix problem with 20 redirects when login fails.
	    throw new FailedAuthenticationException("Authentication failed.");
	} catch (IOException e){
	    e.getMessage().concat("Server returned HTTP response code: 500");
	}
	return getResult();
    }
    
    protected abstract String getResult();
    
    protected abstract String getPath();
    
    protected abstract boolean isDoOutput();
    
    protected abstract void doOutput(TransformerHandler request, String... args) throws SAXException;
    
    protected abstract String getRequestMethod();


    
}
