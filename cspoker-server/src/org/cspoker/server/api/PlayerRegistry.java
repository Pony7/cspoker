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
package org.cspoker.server.api;


import java.util.HashMap;
import java.util.Map;

import org.cspoker.server.game.player.Player;
import org.cspoker.server.game.player.PlayerFactory;
import org.cspoker.server.game.playerCommunication.PlayerCommunicationImpl;

public class PlayerRegistry {

    private static Map<String,PlayerCommunicationImpl> playerComs=new HashMap<String,PlayerCommunicationImpl>();
    
    private static PlayerFactory factory=new PlayerFactory();
    
    public static synchronized PlayerCommunicationImpl getRegisteredPlayerCommunication(String name){
	PlayerCommunicationImpl result=playerComs.get(name);
	if(result==null){
	    result=new PlayerCommunicationImpl(factory.createNewPlayer(name));
	    playerComs.put(name, result);
	    
	}
	return result;
    }
    
    public Player getRegisteredPlayer(String name){
	return getRegisteredPlayerCommunication(name).getPlayer();
    }
    
}
