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

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.sun.net.httpserver.HttpExchange;

public abstract class RequestStreamHandler extends HttpHandlerImpl {

    public void handle(HttpExchange http) throws IOException {
	try {
	    XMLReader xr = XMLReaderFactory.createXMLReader();
	    xr.setContentHandler(getRequestHandler(http));
	    xr.parse(new InputSource(http.getRequestBody()));
	} catch (SAXException e) {
	    throw new IOException(e);
	}
    }
    protected abstract ContentHandler getRequestHandler(HttpExchange http);

}
