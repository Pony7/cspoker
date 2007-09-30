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
package org.cspoker.server.common.xmlcommunication;

import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;
import org.cspoker.server.common.xmlcommunication.handler.CommandDelegatingHandler;
import org.cspoker.server.common.xmlcommunication.handler.DelegatingToOneHandler;
import org.cspoker.server.game.player.Player;
import org.cspoker.server.game.playerCommunication.PlayerCommunication;
import org.cspoker.server.game.playerCommunication.PlayerCommunicationImpl;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlPlayerCommunication {

    private final PlayerCommunication playerComm;
    private final XmlEventCollector collector;
    private final Player player;

    private final static Logger logger = Logger.getLogger(XmlPlayerCommunication.class);
    
    XmlPlayerCommunication(Player player, XmlEventCollector collector) {
	this.playerComm = new PlayerCommunicationImpl(player);
	this.collector = collector;
	this.player = player;
	playerComm.subscribeAllEventsListener(new XmlAllEventsListener(collector));
    }

    public void handle(String xml) throws SAXException {
	XMLReader xr = XMLReaderFactory.createXMLReader();
	xr.setContentHandler(new DelegatingToOneHandler(new CommandDelegatingHandler(playerComm, collector)));
	try {
	    xr.parse(new InputSource(new StringReader(xml)));
	} catch (IOException e) {
	    logger.error("IOException when parsing xml request.",e);
	    throw new IllegalStateException("IOException when parsing xml request.");
	}

    }

    public String getPlayerName() {
	return player.getName();
    }
}
