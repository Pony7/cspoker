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
package org.cspoker.server.game.playerCommunication;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.server.game.TableId;
import org.cspoker.server.game.events.Event;
import org.cspoker.server.game.events.gameEvents.NewRoundEvent;
import org.cspoker.server.game.events.gameEvents.NewRoundListener;
import org.cspoker.server.game.events.gameEvents.NextPlayerEvent;
import org.cspoker.server.game.events.gameEvents.NextPlayerListener;
import org.cspoker.server.game.gameControl.IllegalActionException;
import org.cspoker.server.game.player.GamePlayer;
import org.cspoker.server.game.player.IllegalNameException;
import org.cspoker.server.game.player.PlayerFactory;

public class PlayerCommunicationTest extends TestCase {
    private static Logger logger = Logger
	    .getLogger(PlayerCommunicationTest.class);

    private PlayerFactory playerFactory;

    @Override
    public void setUp() {
	playerFactory = new TestPlayerFactory();
    }

    public void testConstructor() throws IllegalNameException {
	GamePlayer kenzo = playerFactory.getUniquePlayer("Kenzo");
	new PlayerCommunicationImpl(kenzo);
	PlayerCommunicationManager.clear();
    }

    public void testCreateTable() throws IllegalNameException {
	GamePlayer kenzo = playerFactory.getUniquePlayer("Kenzo");
	PlayerCommunicationImpl kenzoComm = new PlayerCommunicationImpl(kenzo);
	try {
	    kenzoComm.createTable();
	} catch (IllegalActionException e) {
	    fail(e.getMessage());
	} finally {
	    PlayerCommunicationManager.clear();
	}
    }

    public void testJoinTable() throws IllegalNameException {
	GamePlayer kenzo = playerFactory.getUniquePlayer("Kenzo");
	GamePlayer guy = playerFactory.getUniquePlayer("Guy");
	PlayerCommunicationImpl kenzoComm = new PlayerCommunicationImpl(kenzo);
	PlayerCommunicationImpl guyComm = new PlayerCommunicationImpl(guy);
	try {
	    TableId tableId = kenzoComm.createTable();
	    guyComm.joinTable(tableId);
	} catch (IllegalActionException e) {
	    fail(e.getMessage());
	} finally {
	    PlayerCommunicationManager.clear();
	}
    }

    public void testStartGame() throws IllegalNameException {
	GamePlayer kenzo = playerFactory.getUniquePlayer("Kenzo");
	GamePlayer guy = playerFactory.getUniquePlayer("Guy");
	PlayerCommunication kenzoComm = new PlayerCommunicationImpl(kenzo);
	PlayerCommunication guyComm = new PlayerCommunicationImpl(guy);
	try {
	    TableId tableId = kenzoComm.createTable();
	    guyComm.joinTable(tableId);
	    kenzoComm.startGame();
	} catch (IllegalActionException e) {
	    fail(e.getMessage());
	} finally {
	    PlayerCommunicationManager.clear();
	}
    }

    private PlayerCommunication currentComm;

    public void testPlayingGame() throws IllegalNameException {
	GamePlayer kenzo = playerFactory.getUniquePlayer("Kenzo");
	GamePlayer guy = playerFactory.getUniquePlayer("Guy");
	PlayerCommunicationImpl kenzoComm = new PlayerCommunicationImpl(kenzo);
	PlayerCommunicationImpl guyComm = new PlayerCommunicationImpl(guy);

	NewRoundListener newRoundListener = new NewRoundListener() {

	    public void onNewRoundEvent(NewRoundEvent event) {
		currentComm = PlayerCommunicationManager
			.getPlayerCommunication(event.getPlayer().getId());
		PlayerCommunicationTest.logger
			.info("Changed to " + currentComm);
	    }

	};

	NextPlayerListener nextPlayerListener = new NextPlayerListener() {

	    public void onNextPlayerEvent(NextPlayerEvent event) {
		currentComm = PlayerCommunicationManager
			.getPlayerCommunication(event.getPlayer().getId());
		PlayerCommunicationTest.logger
			.info("Changed to " + currentComm);
	    }

	};

	try {
	    TableId tableId = kenzoComm.createTable();
	    guyComm.joinTable(tableId);
	    guyComm.subscribeNewRoundListener(newRoundListener);
	    guyComm.subscribeNextPlayerListener(nextPlayerListener);
	    kenzoComm.startGame();
	    PlayerCommunicationTest.logger.info(kenzoComm.getLatestEvents());

	    PlayerCommunicationTest.logger.info("Kenzo's events:"
		    + kenzoComm.getLatestEvents());
	    PlayerCommunicationTest.logger.info("Guy's events:"
		    + guyComm.getLatestEvents());

	    currentComm.call();

	    PlayerCommunicationTest.logger.info("Guy's events:");
	    showEvents(guyComm.getLatestEvents().getGameEvents());

	    currentComm.check();
	    currentComm.bet(10);
	    currentComm.raise(10);
	    currentComm.call();

	    currentComm.check();
	    currentComm.check();

	    currentComm.check();
	    currentComm.check();

	    PlayerCommunicationTest.logger.info("Guy's events:");
	    showEvents(guyComm.getLatestEvents().getGameEvents());

	} catch (IllegalActionException e) {
	    fail(e.getMessage());
	} finally {
	    PlayerCommunicationManager.clear();
	}
    }

    public void testPlayingGame2() throws IllegalNameException {
	GamePlayer kenzo = playerFactory.getUniquePlayer("Kenzo");
	GamePlayer guy = playerFactory.getUniquePlayer("Guy");
	PlayerCommunicationImpl kenzoComm = new PlayerCommunicationImpl(kenzo);
	PlayerCommunicationImpl guyComm = new PlayerCommunicationImpl(guy);

	NewRoundListener newRoundListener = new NewRoundListener() {

	    public void onNewRoundEvent(NewRoundEvent event) {
		currentComm = PlayerCommunicationManager
			.getPlayerCommunication(event.getPlayer().getId());
		PlayerCommunicationTest.logger
			.info("Changed to " + currentComm);
	    }

	};

	NextPlayerListener nextPlayerListener = new NextPlayerListener() {

	    public void onNextPlayerEvent(NextPlayerEvent event) {
		currentComm = PlayerCommunicationManager
			.getPlayerCommunication(event.getPlayer().getId());
		PlayerCommunicationTest.logger
			.info("Changed to " + currentComm);
	    }

	};

	try {
	    TableId tableId = kenzoComm.createTable();
	    guyComm.subscribeNewRoundListener(newRoundListener);
	    guyComm.subscribeNextPlayerListener(nextPlayerListener);
	    guyComm.joinTable(tableId);
	    kenzoComm.startGame();
	    PlayerCommunicationTest.logger.info(kenzoComm.getLatestEvents());

	    PlayerCommunicationTest.logger.info("Kenzo's events:"
		    + kenzoComm.getLatestEvents());
	    PlayerCommunicationTest.logger.info("Guy's events:"
		    + guyComm.getLatestEvents());

	    currentComm.call();

	    PlayerCommunicationTest.logger.info("Guy's events:");
	    showEvents(guyComm.getLatestEvents().getGameEvents());

	    currentComm.allIn();
	    currentComm.allIn();

	    PlayerCommunicationTest.logger.info("Guy's events:");
	    showEvents(guyComm.getLatestEvents().getGameEvents());

	} catch (IllegalActionException e) {
	    fail(e.getMessage());
	} finally {
	    PlayerCommunicationManager.clear();
	}
    }

    public void showEvents(List<Event> events) {
	for (Event event : events) {
	    PlayerCommunicationTest.logger.info("++ " + event);
	}
    }

}
