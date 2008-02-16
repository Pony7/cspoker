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

import org.apache.log4j.Logger;
import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.xml.XmlEventListener;
import org.cspoker.server.common.xmlcommunication.handler.CommandDelegatingHandler;
import org.cspoker.server.common.xmlcommunication.handler.DelegatingToOneHandler;
import org.cspoker.server.game.player.GamePlayer;
import org.cspoker.server.game.session.PlayerKilledExcepion;
import org.cspoker.server.game.session.Session;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XmlPlayerCommunication implements XmlEventListener{

    private final PlayerCommunication playerComm;
    private XmlEventListener listener;
    private final GamePlayer player;

    private final StringBuilder cache;

    private final static Logger logger = Logger.getLogger(XmlPlayerCommunication.class);
    
    XmlPlayerCommunication(Session session, XmlEventListener listener) throws PlayerKilledExcepion {
	this.playerComm = session.getPlayerCommunication();
	this.listener = listener;
	this.player = session.getPlayer();
	cache = new StringBuilder();
	playerComm.subscribeAllEventsListener(new XmlAllEventsListener(listener));
    }

    public void handle(InputSource xml) throws SAXException {
	XMLReader xr = XMLReaderFactory.createXMLReader();
	xr.setContentHandler(new DelegatingToOneHandler(new CommandDelegatingHandler(playerComm, this)));
	try {
	    xr.parse(xml);
	} catch (IOException e) {
	    logger.error("IOException when parsing xml request.",e);
	    throw new IllegalStateException("IOException when parsing xml request.");
	}
    }
    
    public String getPlayerName() {
	return player.getName();
    }

    public XmlEventListener getXmlEventListener() {
	return listener;
    }

    public synchronized void cache(String xml) {
	cache.append(xml);
    }

    public synchronized String getAndFlushCache(){
	String s = cache.toString();
	cache.setLength(0);
	return s;
    }
    
    public synchronized void flushToListener(){
	if(listener!=null){
	    listener.collect(getAndFlushCache());
	}
    }
    
    public synchronized void updateEventListener(XmlEventListener newlist){
	listener = newlist;
	flushToListener();
    }

    public void collect(String xmlEvent) {
	cache(xmlEvent);
	flushToListener();
    }
}
