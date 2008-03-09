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
package org.cspoker.server.common.game.elements.table;

import junit.framework.TestCase;

import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.server.common.game.player.GamePlayer;
import org.cspoker.server.common.game.player.PlayerFactory;

public class TableTest extends TestCase {
	
	private GameProperty property;
	
	
	protected void setUp(){
		property = new GameProperty();
	}
	
	/**
	 * Add 2 players to the same seat.
	 */
	public void testAddPlayer(){
		GameTable table = new GameTable(new TableId(0), property);
		
		assertTrue(table.isValidSeatId(new SeatId(0)));
		assertEquals(0, table.getNbPlayers());
		assertEquals(0, table.getSavedTable().getNbPlayers());
		
		GamePlayer kenzo = PlayerFactory.global_Player_Factory.createNewPlayer("kenzo");
		
		assertFalse(table.hasAsPlayer(kenzo));
		assertEquals(table.getGameProperty().getMaxNbPlayers(), table.getSavedTable().getPlayers().size());
		
		try {
			table.addPlayer(new SeatId(0), kenzo);
		} catch (SeatTakenException e) {
			fail(e.getMessage());
		}
		
		assertTrue(table.hasAsPlayer(kenzo));
		assertEquals(1, table.getNbPlayers());
		assertEquals(1, table.getSavedTable().getNbPlayers());
		assertEquals(kenzo.getId(), table.getSavedTable().getPlayers().get(0).getId());
		assertEquals(table.getGameProperty().getMaxNbPlayers(), table.getSavedTable().getPlayers().size());
		
		/**
		 * Add another player to the same seat.
		 */
		
		GamePlayer guy = PlayerFactory.global_Player_Factory.createNewPlayer("guy");
		assertFalse(table.hasAsPlayer(guy));
		try {
			table.addPlayer(new SeatId(0), guy);
			fail("Exception expected");
		} catch (SeatTakenException e) {
			//This exception is expected.
		}
		assertFalse(table.hasAsPlayer(guy));
	}
	
	/**
	 * Add 2 players to the same seat.
	 */
	public void testAddPlayerWithoutSeat(){
		GameTable table = new GameTable(new TableId(0), property);
		
		assertTrue(table.isValidSeatId(new SeatId(0)));
		assertEquals(0, table.getNbPlayers());
		assertEquals(0, table.getSavedTable().getNbPlayers());
		
		GamePlayer kenzo = PlayerFactory.global_Player_Factory.createNewPlayer("kenzo");
		
		assertFalse(table.hasAsPlayer(kenzo));
		assertEquals(table.getGameProperty().getMaxNbPlayers(), table.getSavedTable().getPlayers().size());
		
		try {
			table.addPlayer(kenzo);
		} catch (PlayerListFullException e) {
			fail(e.getMessage());
		}
		
		assertTrue(table.hasAsPlayer(kenzo));
		assertEquals(1, table.getNbPlayers());
		assertEquals(1, table.getSavedTable().getNbPlayers());
		assertEquals(kenzo.getId(), table.getSavedTable().getPlayers().get(0).getId());
		assertEquals(table.getGameProperty().getMaxNbPlayers(), table.getSavedTable().getPlayers().size());
		
		/**
		 * Add another player to the same seat.
		 */
		
		GamePlayer guy = PlayerFactory.global_Player_Factory.createNewPlayer("guy");
		assertFalse(table.hasAsPlayer(guy));
		try {
			table.addPlayer(new SeatId(0), guy);
			fail("Exception expected");
		} catch (SeatTakenException e) {
			//This exception is expected.
		}
		assertFalse(table.hasAsPlayer(guy));
	}
	
	/**
	 * Add a player with a to high seat id.
	 */
	public void testAddPlayerWithAToHighSeatId(){
				
		GameTable table = new GameTable(new TableId(0), property);
		
		int maxNbPlayers = property.getMaxNbPlayers();
		
		assertTrue(maxNbPlayers>2);
		
		GamePlayer player;
		
		for (int i = 0; i < maxNbPlayers; i++) {
			player = PlayerFactory.global_Player_Factory.createNewPlayer("cedric");
			assertFalse(table.hasAsPlayer(player));
			try {
				table.addPlayer(new SeatId(i), player);
			} catch (SeatTakenException e) {
				fail(e.getMessage());
			}
			assertTrue(table.hasAsPlayer(player));
			assertEquals(i+1, table.getNbPlayers());
			assertEquals(i+1, table.getSavedTable().getNbPlayers());
			assertEquals(table.getGameProperty().getMaxNbPlayers(), table.getSavedTable().getPlayers().size());
		}
		
		assertEquals(maxNbPlayers, table.getNbPlayers());
		assertTrue(table.fullOfPlayers());
		assertEquals(table.getGameProperty().getMaxNbPlayers(), table.getSavedTable().getPlayers().size());
		
		try {
			table.addPlayer(new SeatId(maxNbPlayers), PlayerFactory.global_Player_Factory.createNewPlayer("craig"));
			fail("Exception expected");
		} catch (IllegalArgumentException e) {
			//This exception is expected.
		} catch (SeatTakenException e) {
			fail(e.getMessage());
		}
		assertEquals(maxNbPlayers, table.getNbPlayers());
	}
	
	/**
	 * Test the removal of a player.
	 */
	public void testRemovePlayer(){
		GameTable table = new GameTable(new TableId(0), property);
		
		assertTrue(table.isValidSeatId(new SeatId(0)));
		
		GamePlayer kenzo = PlayerFactory.global_Player_Factory.createNewPlayer("kenzo");
		
		try {
			table.addPlayer(new SeatId(0), kenzo);
		} catch (SeatTakenException e) {
			fail(e.getMessage());
		}
		
		assertTrue(table.hasAsPlayer(kenzo));
		assertEquals(kenzo.getId(), table.getSavedTable().getPlayers().get(0).getId());
		
		table.removePlayer(kenzo);
		
		assertFalse(table.hasAsPlayer(kenzo));
		assertNull(table.getSavedTable().getPlayers().get(0));
		
		try {
			table.addPlayer(new SeatId(0), kenzo);
		} catch (SeatTakenException e) {
			fail(e.getMessage());
		}
		
		assertTrue(table.hasAsPlayer(kenzo));
		assertEquals(kenzo.getId(), table.getSavedTable().getPlayers().get(0).getId());
	}

}
