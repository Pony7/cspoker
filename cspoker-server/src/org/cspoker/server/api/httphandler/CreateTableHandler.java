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
package org.cspoker.server.api.httphandler;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.server.api.PlayerCommunicationFactory;
import org.cspoker.server.api.httphandler.abstracts.HttpHandlerImpl;
import org.cspoker.server.api.httphandler.abstracts.RequestStreamHandler;
import org.cspoker.server.api.httphandler.exception.HttpSaxException;
import org.cspoker.server.game.gameControl.IllegalActionException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.net.httpserver.HttpExchange;

public class CreateTableHandler extends RequestStreamHandler {

    @Override
    protected ContentHandler getRequestHandler(final HttpExchange http, final TransformerHandler response){
	
	return new DefaultHandler(){
	    @Override
	    public void endDocument() throws SAXException {
		String username= HttpHandlerImpl.toPlayerName(http.getRequestHeaders());
		long id;
		try {
		    id=PlayerCommunicationFactory.getRegisteredPlayerCommunication(username).createTable().getID();
		} catch (IllegalActionException e) {
		    throw new HttpSaxException(e, 403);
		}
		response.startElement("", "id", "id", new AttributesImpl());
		String chars=String.valueOf(id);
		response.characters(chars.toCharArray(), 0, chars.length());
		response.endElement("", "id", "id");
	    }
	};
    }

    @Override
    protected int getDefaultStatusCode() {
	return 201;
    }

}
