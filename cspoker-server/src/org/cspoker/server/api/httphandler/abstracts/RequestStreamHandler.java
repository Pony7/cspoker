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

import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.cspoker.server.api.httphandler.exception.HttpSaxException;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.sun.net.httpserver.HttpExchange;
/**
 * A HttpHandler for requests that have an input stream from the request.
 */
public abstract class RequestStreamHandler extends HttpHandlerImpl {

    public void handle(HttpExchange http) throws IOException {
	http.getResponseHeaders().add("Cache-Control", "no-cache");
	ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
	try {
	    TransformerHandler response=null;
	    StreamResult requestResult = new StreamResult(responseBody);

	    SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
	    .newInstance();
	    response = tf.newTransformerHandler();
	    response.setResult(requestResult);
	    response.startDocument();

	    XMLReader xr = XMLReaderFactory.createXMLReader();
	    xr.setContentHandler(getRequestHandler(http, response));
	    xr.parse(new InputSource(http.getRequestBody()));

	    response.endDocument();
	} catch (IOException e){
	    //if the connection is lost then throw the exception as expected.
	    //this will probably never be able to happen here but you never know
	    throw e;
	} catch (Exception e) {
	    //otherwise send the exception over
	    throwException(http, e);
	    return;
	}
	//send the default status code (no exception occured)
	http.sendResponseHeaders(getDefaultStatusCode(), responseBody.size());
	responseBody.writeTo(http.getResponseBody());
	http.getResponseBody().close();
	http.close();

    }

    protected abstract ContentHandler getRequestHandler(HttpExchange http, TransformerHandler response) throws HttpSaxException;

}
