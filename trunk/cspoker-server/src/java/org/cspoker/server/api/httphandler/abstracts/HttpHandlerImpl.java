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
package org.cspoker.server.api.httphandler.abstracts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.server.api.httphandler.exception.HttpException;
import org.cspoker.server.api.httphandler.exception.HttpSaxException;
import org.cspoker.server.api.httphandler.util.Base64;
import org.xml.sax.helpers.AttributesImpl;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * A HttpHandler that supports Exceptions over Http.
 *
 */
public abstract class HttpHandlerImpl implements HttpHandler {

    public HttpHandlerImpl() {
	super();
    }

    /**
     * Throws an exception over the Http connection in XML format. 
     * 
     * @param http
     *        The http context.
     * @param e
     *        The throwable to throw.
     * @param status
     * 	      The http error code to send.
     * @throws IOException
     * 	       An exception occured while creating the error response.
     */
    protected void throwException(HttpExchange http, Throwable e, int status) throws IOException{
	try {
	    if (e instanceof HttpException) {
		//e has http status information
		status=((HttpSaxException)e).getStatus();
	    }
	    if(e.getCause()!=null) {
		//e is a wrapper class.
		e=e.getCause();
	    }
	    
	    //local error msg
	    e.printStackTrace();
	    //remote error msg
	    ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
	    TransformerHandler response=null;
	    StreamResult requestResult= new StreamResult(responseBody);

	    SAXTransformerFactory tf = (SAXTransformerFactory) TransformerFactory
	    .newInstance();
	    response = tf.newTransformerHandler();
	    response.setResult(requestResult);
	    response.startDocument();
	    response.startElement("", "exception", "exception", new AttributesImpl());

	    response.startElement("", "msg", "msg", new AttributesImpl());
	    String msg=(e.getMessage()==null?"unknown error":e.getMessage());
	    response.characters(msg.toCharArray(), 0, msg.length());
	    response.endElement("", "msg", "msg");

	    response.startElement("", "stacktrace", "stacktrace", new AttributesImpl());
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw, true);
	    e.printStackTrace(pw);
	    pw.flush();
	    sw.flush();
	    String trace=sw.toString();
	    response.characters(trace.toCharArray(), 0, trace.length());
	    response.endElement("", "stacktrace", "stacktrace");

	    response.endElement("", "exception", "exception");
	    response.endDocument();


	    http.sendResponseHeaders(status, responseBody.size());
	    responseBody.writeTo(http.getResponseBody());
	    http.getResponseBody().close();
	    http.close();
	} catch (Exception e1) {
	    e1.printStackTrace();
	    throw new IOException(e1);
	} 
    }

    protected void throwException(HttpExchange http, Throwable e) throws IOException{
	throwException(http, e, 500);
    }

    public static String toPlayerName(Headers requestHeaders) {
	List<String> auth=requestHeaders.get("Authorization");
	if(auth==null||auth.size()!=1)
	    throw new IllegalStateException("Incorrect Authorization");
	String base64=auth.get(0);
	try {
	    String decoded=new String(Base64.decode(base64.split(" ")[1]));
	    return decoded.split(":")[0];
	} catch (IOException e) {
	    throw new IllegalStateException(e);
	}
    }

    protected abstract int getDefaultStatusCode();

}