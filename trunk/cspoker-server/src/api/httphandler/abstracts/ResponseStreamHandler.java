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
package api.httphandler.abstracts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import com.sun.net.httpserver.HttpExchange;

public abstract class ResponseStreamHandler extends HttpHandlerImpl {

    public void handle(HttpExchange http) throws IOException{
	ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
	TransformerHandler response=null;
	StreamResult requestResult = new StreamResult(responseBody);

	SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
	.newInstance();
	try {
	    response = tf.newTransformerHandler();
	    response.setResult(requestResult);
	    response.startDocument();
	    respond(response);
	    response.endDocument();
	} catch (Exception e) {
	    //send the exception over
	    throwException(http, e);
	    return;
	} 

	http.sendResponseHeaders(getDefaultStatusCode(), responseBody.size());
	responseBody.writeTo(http.getResponseBody());
	http.getResponseBody().close();
	http.close(); 
    }

    protected abstract void respond(TransformerHandler response)
    throws SAXException;

}
