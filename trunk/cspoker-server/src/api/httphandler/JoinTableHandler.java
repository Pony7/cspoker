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
package api.httphandler;

import game.TableId;
import game.gameControl.actions.IllegalActionException;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import api.PlayerRegistry;
import api.httphandler.abstracts.HttpHandlerImpl;
import api.httphandler.abstracts.PostHandler;
import api.httphandler.exception.HttpSaxException;

import com.sun.net.httpserver.HttpExchange;

public class JoinTableHandler extends PostHandler {

    @Override
    protected ContentHandler getRequestHandler(final HttpExchange http, final TransformerHandler response){
	
	return new DefaultHandler(){
	    
	    StringBuilder sb=new StringBuilder();
	    
	    @Override
	    public void characters(char[] ch, int start, int length)
	            throws SAXException {
		sb.append(ch, start, length);
	    }
	    
	    @Override
	    public void endDocument() throws SAXException {
		String username= HttpHandlerImpl.toPlayerName(http.getRequestHeaders());

		try {
		    PlayerRegistry.getRegisteredPlayerCommunication(username)
		        	.join(new TableId(Long.parseLong(sb.toString())));
		} catch (NumberFormatException e) {
		    throw new HttpSaxException(e, 400);
		} catch (IllegalActionException e) {
		    throw new HttpSaxException(e, 403);
		}

		response.startElement("", "ok", "ok", new AttributesImpl());
		response.endElement("", "ok", "ok");
	    }
	};
    }

}
