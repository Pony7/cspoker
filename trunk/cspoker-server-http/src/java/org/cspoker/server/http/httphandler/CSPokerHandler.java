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
package org.cspoker.server.http.httphandler;

import org.cspoker.server.common.xmlcommunication.XmlPlayerCommunication;
import org.cspoker.server.common.xmlcommunication.XmlPlayerCommunicationFactory;
import org.cspoker.server.game.player.IllegalNameException;
import org.cspoker.server.game.player.Player;
import org.cspoker.server.game.player.PlayerFactory;
import org.cspoker.server.http.StringCollector;
import org.cspoker.server.http.StringCollectorFactory;
import org.cspoker.server.http.httphandler.abstracts.AbstractHttpHandlerImpl;
import org.cspoker.server.http.httphandler.exception.HttpExceptionImpl;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.net.httpserver.HttpExchange;


public class CSPokerHandler extends AbstractHttpHandlerImpl{

    @Override
    protected String getResponse(HttpExchange http) throws HttpExceptionImpl{
	
	    String username= AbstractHttpHandlerImpl.toPlayerName(http.getRequestHeaders());
	    Player player;
	    try {
		player = PlayerFactory.global_Player_Factory.getUniquePlayer(username);
	    } catch (IllegalNameException e) {
		throw new HttpExceptionImpl(e,400);
	    }
	    StringCollector collector = StringCollectorFactory.getUniqueStringCollector(player);
	    XmlPlayerCommunication playerComm = XmlPlayerCommunicationFactory.getRegisteredXmlPlayerCommunication(player, collector);
	    try {
		playerComm.handle(new InputSource(http.getRequestBody()));
	    } catch (SAXException e) {
		throw new HttpExceptionImpl(e, 400);
	    }
	    return collector.getAndFlush();

    }
    
    
   

}
