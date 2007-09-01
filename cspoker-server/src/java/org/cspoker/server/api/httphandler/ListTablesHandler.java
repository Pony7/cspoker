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


import java.util.Set;

import javax.xml.transform.sax.TransformerHandler;

import org.cspoker.server.api.httphandler.abstracts.NoRequestStreamHandler;
import org.cspoker.server.game.TableId;
import org.cspoker.server.game.TableManager;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.sun.net.httpserver.HttpExchange;


public class ListTablesHandler extends NoRequestStreamHandler {
    
    @Override
    protected void respond(TransformerHandler response, HttpExchange http)
	    throws SAXException {
	response.startElement("", "tables", "tables", new AttributesImpl());
	Set<TableId> tables = TableManager.getAllTableIds();
	for(TableId id:tables){
	    response.startElement("", "table", "table", new AttributesImpl());
	    String idStr = String.valueOf(id.getID());
	    response.characters(idStr.toCharArray(), 0, idStr.length());
	    response.endElement("", "table", "table");
	}
	response.endElement("", "tables", "tables");
    }

    @Override
    protected int getDefaultStatusCode() {
	return 200;
    }


}
