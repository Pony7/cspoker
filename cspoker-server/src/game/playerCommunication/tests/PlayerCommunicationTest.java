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
package game.playerCommunication.tests;

import game.TableId;
import game.gameControl.actions.IllegalActionException;
import game.player.Player;
import game.player.PlayerFactory;
import game.playerCommunication.PlayerCommunication;
import game.playerCommunication.PlayerCommunicationImpl;
import game.playerCommunication.PlayerCommunicationManager;
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
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}finally{
			PlayerCommunicationManager.clear();
		}
	}


}
