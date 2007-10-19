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


import java.util.HashMap;
import java.util.Map;

import org.cspoker.server.game.player.GamePlayer;

/**
 * Creates a unique player communication instance for every player name and provides
 * it when requested. 
 */
public class XmlPlayerCommunicationFactory {

    private final static Map<GamePlayer,XmlPlayerCommunication> playerComs=new HashMap<GamePlayer,XmlPlayerCommunication>();
    
    public static synchronized XmlPlayerCommunication getRegisteredXmlPlayerCommunication(GamePlayer player, XmlEventCollector collector){
	XmlPlayerCommunication result=playerComs.get(player);
	if(result==null){
	    
	    result=new XmlPlayerCommunication(player, collector);
	    playerComs.put(player, result);
	    
	}
	return result;
    }
    
    public static synchronized void unRegister(String name) {
	playerComs.remove(name);
    }
    
}
