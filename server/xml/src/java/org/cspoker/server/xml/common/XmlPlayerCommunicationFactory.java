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

import java.util.concurrent.ConcurrentHashMap;

import org.cspoker.common.xml.XmlEventListener;
import org.cspoker.server.common.game.session.PlayerKilledExcepion;
import org.cspoker.server.common.game.session.Session;

/**
 * Creates a unique player communication instance for every player name and
 * provides it when requested.
 */
public class XmlPlayerCommunicationFactory {

	public final static XmlPlayerCommunicationFactory global_factory = new XmlPlayerCommunicationFactory();

	private final ConcurrentHashMap<Session, XmlPlayerCommunication> playerComs = new ConcurrentHashMap<Session, XmlPlayerCommunication>();

	public XmlPlayerCommunication getRegisteredXmlPlayerCommunication(
			Session session, XmlEventListener listener)
			throws PlayerKilledExcepion {
		XmlPlayerCommunication result = playerComs.get(session);
		if (result == null) {
			XmlPlayerCommunication oldresult = playerComs.putIfAbsent(session, new XmlPlayerCommunication(session, listener));
			if(oldresult==null){
				result = playerComs.get(session);
			}else{
				result=oldresult;
			}
		}
		result.updateEventListener(listener);
		return result;
	}

	public synchronized void unRegister(Session session) {
		playerComs.remove(session);
	}

}
