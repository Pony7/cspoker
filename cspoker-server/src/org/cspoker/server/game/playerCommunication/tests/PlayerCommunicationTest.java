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
package org.cspoker.server.game.playerCommunication.tests;

import org.cspoker.server.game.TableId;
import org.cspoker.server.game.gameControl.actions.IllegalActionException;
import org.cspoker.server.game.player.Player;
import org.cspoker.server.game.player.PlayerFactory;
import org.cspoker.server.game.playerCommunication.PlayerCommunication;
import org.cspoker.server.game.playerCommunication.PlayerCommunicationImpl;
import org.cspoker.server.game.playerCommunication.PlayerCommunicationManager;

import junit.framework.TestCase;

public class PlayerCommunicationTest extends TestCase {

	private PlayerFactory playerFactory;

	@Override
	public void setUp(){
		playerFactory = new PlayerFactory();
	}


	public void testConstructor(){
		Player kenzo = playerFactory.createNewPlayer("Kenzo");
		new PlayerCommunicationImpl(kenzo);
		PlayerCommunicationManager.clear();
	}

	public void testCreateTable(){
		Player kenzo = playerFactory.createNewPlayer("Kenzo");
		PlayerCommunication kenzoComm = new PlayerCommunicationImpl(kenzo);
		try {
			kenzoComm.createTable();
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}finally{
			PlayerCommunicationManager.clear();
		}
	}

	public void testJoinTable(){
		Player kenzo = playerFactory.createNewPlayer("Kenzo");
		Player guy = playerFactory.createNewPlayer("Guy");
		PlayerCommunication kenzoComm = new PlayerCommunicationImpl(kenzo);
		PlayerCommunication guyComm = new PlayerCommunicationImpl(guy);
		try {
			TableId tableId = kenzoComm.createTable();
			guyComm.join(tableId);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}finally{
			PlayerCommunicationManager.clear();
		}
	}

	public void testStartGame(){
		Player kenzo = playerFactory.createNewPlayer("Kenzo");
		Player guy = playerFactory.createNewPlayer("Guy");
		PlayerCommunication kenzoComm = new PlayerCommunicationImpl(kenzo);
		PlayerCommunication guyComm = new PlayerCommunicationImpl(guy);
		try {
			TableId tableId = kenzoComm.createTable();
			guyComm.join(tableId);
			kenzoComm.startGame();
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}finally{
			PlayerCommunicationManager.clear();
		}
	}

	public void testPlayingGame(){
		Player kenzo = playerFactory.createNewPlayer("Kenzo");
		Player guy = playerFactory.createNewPlayer("Guy");
		PlayerCommunication kenzoComm = new PlayerCommunicationImpl(kenzo);
		PlayerCommunication guyComm = new PlayerCommunicationImpl(guy);
		try {
			TableId tableId = kenzoComm.createTable();
			guyComm.join(tableId);
			kenzoComm.startGame();
			System.out.println(kenzoComm.getLatestGameEvents());
			try {
				kenzoComm.deal();
			} catch (IllegalActionException e) {
				try {
					guyComm.deal();
				} catch (IllegalActionException e1) {
				}
			}
			System.out.println("Kenzo's events:"+kenzoComm.getLatestGameEvents());
			System.out.println("Guy's events:"+guyComm.getLatestGameEvents());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}finally{
			PlayerCommunicationManager.clear();
		}
	}


}
