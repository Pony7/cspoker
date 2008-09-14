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
package org.cspoker.server.xml.common;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.XmlEventListener;
import org.cspoker.common.jaxbcontext.ActionJAXBContext;
import org.cspoker.common.xml.actions.Action;
import org.cspoker.server.common.game.player.GameSeatedPlayer;
import org.cspoker.server.common.game.session.PlayerKilledExcepion;
import org.cspoker.server.common.game.session.Session;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlPlayerCommunication implements XmlEventListener {

	private final PlayerCommunication playerComm;
	private final GameSeatedPlayer player;
	private final StringBuilder cache;
	private final ToXmlAllEventsListener toxmllistener;
	private XmlEventListener xmllistener;

	private final static Logger logger = Logger
			.getLogger(XmlPlayerCommunication.class);

	XmlPlayerCommunication(Session session, XmlEventListener listener)
			throws PlayerKilledExcepion {
		playerComm = session.getPlayerCommunication();
		player = session.getPlayer();
		cache = new StringBuilder();
		xmllistener = listener;
		// This will receive the XML before the listener does.
		toxmllistener = new ToXmlAllEventsListener(this);
		playerComm.subscribeAllEventsListener(toxmllistener);
	}

	public void handle(InputSource xml) throws SAXException, JAXBException,
			IOException {
		Unmarshaller um = ActionJAXBContext.context.createUnmarshaller();
		Action<?> action = (Action<?>) um
				.unmarshal(xml);
		action.perform(playerComm, toxmllistener);
	}

	public String getPlayerName() {
		return player.getName();
	}

	public XmlEventListener getXmlEventListener() {
		return xmllistener;
	}

	public synchronized void cache(String xml) {
		cache.append(xml);
	}

	public synchronized String getAndFlushCache() {
		String s = cache.toString();
		cache.setLength(0);
		return s;
	}

	public synchronized void flushToListener() {
		if (xmllistener != null) {
			xmllistener.collect(getAndFlushCache());
		}
	}

	public synchronized void updateEventListener(XmlEventListener newlist) {
		if (newlist != xmllistener) {
			xmllistener = newlist;
			flushToListener();
		}
	}

	public void collect(String xmlEvent) {
		logger.trace("Received XML event:\n" + xmlEvent);
		cache(xmlEvent);
		flushToListener();
	}
}
