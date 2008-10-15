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
package org.cspoker.server.common.game.playercommunication;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.shared.event.ServerEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.util.Log4JPropertiesLoader;
import org.cspoker.server.common.elements.id.PlayerId;
import org.cspoker.server.common.elements.id.TableId;
import org.cspoker.server.common.game.gamecontrol.TestPlayerFactory;
import org.cspoker.server.common.player.GameSeatedPlayer;
import org.cspoker.server.common.player.IllegalNameException;
import org.cspoker.server.common.player.PlayerFactory;
import org.cspoker.server.common.playercommunication.PlayerCommunicationImpl;
import org.cspoker.server.common.session.Session;
import org.cspoker.server.common.session.SessionManager;

public class PlayerCommunicationTest extends TestCase {

	static {
		Log4JPropertiesLoader
				.load("org/cspoker/server/common/logging/log4j.properties");
	}

	private static Logger logger = Logger
			.getLogger(PlayerCommunicationTest.class);

	private PlayerFactory playerFactory;

	public void setUp() {
		playerFactory = new TestPlayerFactory();
	}

	public void testConstructor() throws IllegalNameException {
		GameSeatedPlayer kenzo = playerFactory.createNewPlayer("Kenzo");
		new PlayerCommunicationImpl(kenzo);
	}

	public void testCreateTable() throws IllegalNameException {
		GameSeatedPlayer kenzo = playerFactory.createNewPlayer("Kenzo");
		PlayerCommunicationImpl kenzoComm = new PlayerCommunicationImpl(kenzo);
		try {
			kenzoComm.createTable("test");
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testJoinTable() throws IllegalNameException {
		GameSeatedPlayer kenzo = playerFactory.createNewPlayer("Kenzo");
		GameSeatedPlayer guy = playerFactory.createNewPlayer("Guy");
		PlayerCommunicationImpl kenzoComm = new PlayerCommunicationImpl(kenzo);
		PlayerCommunicationImpl guyComm = new PlayerCommunicationImpl(guy);
		try {
			TableId tableId = kenzoComm.createTable("test").getId();
			guyComm.joinTable(tableId);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testStartGame() throws IllegalNameException {
		Session guySession = SessionManager.global_session_manager
				.getSession("Guy");
		Session kenzoSession = SessionManager.global_session_manager
				.getSession("Kenzo");

		try {
			PlayerCommunication guyComm = guySession.getPlayerCommunication();
			PlayerCommunication kenzoComm = kenzoSession
					.getPlayerCommunication();
			TableId tableId = kenzoComm.createTable("test").getId();
			// TODO create an event listener filter to wait for the tableID
			guyComm.joinTable(tableId);
			kenzoComm.startGame();
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		} catch (PlayerKilledExcepion e) {
			fail(e.getMessage());
		} finally {
			SessionManager.global_session_manager.killSession("Guy");
			SessionManager.global_session_manager.killSession("Kenzo");
		}
	}

	private PlayerCommunication currentComm;

	public void testPlayingGame() throws IllegalNameException {
		Session guySession = SessionManager.global_session_manager
				.getSession("Guy");
		Session kenzoSession = SessionManager.global_session_manager
				.getSession("Kenzo");

		Map<PlayerId, PlayerCommunication> map = new HashMap<PlayerId, PlayerCommunication>();

		try {
			PlayerCommunicationImpl kenzoComm = kenzoSession
					.getPlayerCommunication();
			PlayerCommunicationImpl guyComm = guySession
					.getPlayerCommunication();
			map.put(kenzoComm.getId(), kenzoComm);
			map.put(guyComm.getId(), guyComm);

			EventCollector kenzoEvents = new EventCollector();
			EventCollector guyEvents = new EventCollector();

			kenzoComm.subscribeAllEventsListener(kenzoEvents);
			guyComm.subscribeAllEventsListener(guyEvents);

			TableId tableId = kenzoComm.createTable("test").getId();
			guyComm.joinTable(tableId);
			guyComm.subscribeAllEventsListener(new TestListener(map));
			kenzoComm.startGame();
			PlayerCommunicationTest.logger.info(kenzoEvents.getEvents());

			PlayerCommunicationTest.logger.info("Kenzo's events:"
					+ kenzoEvents.getEvents());
			PlayerCommunicationTest.logger.info("Guy's events:"
					+ guyEvents.getEvents());

			currentComm.call();

			PlayerCommunicationTest.logger.info("Guy's events:");
			showEvents(guyEvents.getEvents());

			currentComm.check();
			currentComm.bet(10);
			currentComm.raise(10);
			currentComm.call();

			currentComm.check();
			currentComm.check();

			currentComm.check();
			currentComm.check();

			PlayerCommunicationTest.logger.info("Guy's events:");
			showEvents(guyEvents.getEvents());

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		} catch (PlayerKilledExcepion e) {
			fail(e.getMessage());
		} finally {
			SessionManager.global_session_manager.killSession("Guy");
			SessionManager.global_session_manager.killSession("Kenzo");
		}
	}

	public void testPlayingGame2() throws IllegalNameException {
		Session guySession = SessionManager.global_session_manager
				.getSession("Guy");
		Session kenzoSession = SessionManager.global_session_manager
				.getSession("Kenzo");

		Map<PlayerId, PlayerCommunication> map = new HashMap<PlayerId, PlayerCommunication>();

		try {

			PlayerCommunicationImpl kenzoComm = kenzoSession
					.getPlayerCommunication();
			PlayerCommunicationImpl guyComm = guySession
					.getPlayerCommunication();

			map.put(kenzoComm.getId(), kenzoComm);
			map.put(guyComm.getId(), guyComm);

			EventCollector kenzoEvents = new EventCollector();
			EventCollector guyEvents = new EventCollector();

			kenzoComm.subscribeAllEventsListener(kenzoEvents);
			guyComm.subscribeAllEventsListener(guyEvents);

			TableId tableId = kenzoComm.createTable("test").getId();

			guyComm.subscribeAllEventsListener(new TestListener(map));
			guyComm.joinTable(tableId);
			kenzoComm.startGame();
			PlayerCommunicationTest.logger.info(kenzoEvents.getEvents());

			PlayerCommunicationTest.logger.info("Kenzo's events:"
					+ kenzoEvents.getEvents());
			PlayerCommunicationTest.logger.info("Guy's events:"
					+ kenzoEvents.getEvents());

			currentComm.call();

			PlayerCommunicationTest.logger.info("Guy's events:");
			showEvents(guyEvents.getEvents());

			currentComm.allIn();
			currentComm.allIn();

			PlayerCommunicationTest.logger.info("Guy's events:");
			showEvents(guyEvents.getEvents());

		} catch (IllegalActionException e) {
			fail(e.getMessage());
		} catch (PlayerKilledExcepion e) {
			fail(e.getMessage());
		} finally {
			SessionManager.global_session_manager.killSession("Guy");
			SessionManager.global_session_manager.killSession("Kenzo");
		}
	}

	public void showEvents(List<ServerEvent> events) {
		for (ServerEvent event : events) {
			PlayerCommunicationTest.logger.info("++ " + event);
		}
	}

	private class TestListener implements RemoteAllEventsListener {

		private Map<PlayerId, PlayerCommunication> map;

		public TestListener(Map<PlayerId, PlayerCommunication> map) {
			this.map = map;
		}

		public void onAllInEvent(AllInEvent event) throws RemoteException {
		}

		public void onBetEvent(BetEvent event) throws RemoteException {
		}

		public void onBigBlindEvent(BigBlindEvent event) throws RemoteException {
		}

		public void onCallEvent(CallEvent event) throws RemoteException {
		}

		public void onCheckEvent(CheckEvent event) throws RemoteException {
		}

		public void onFoldEvent(FoldEvent event) throws RemoteException {
		}

		public void onRaiseEvent(RaiseEvent event) throws RemoteException {
		}

		public void onSmallBlindEvent(SmallBlindEvent event)
				throws RemoteException {
		}

		public void onNewPocketCardsEvent(NewPocketCardsEvent event)
				throws RemoteException {
		}

		public void onNewCommunityCardsEvent(NewCommunityCardsEvent event)
				throws RemoteException {
		}

		public void onNewDealEvent(NewDealEvent event) throws RemoteException {
		}

		public void onNewRoundEvent(NewRoundEvent event) throws RemoteException {
			currentComm = map.get(event.getPlayer().getId());
			PlayerCommunicationTest.logger.info("Changed to " + currentComm);
		}

		public void onNextPlayerEvent(NextPlayerEvent event)
				throws RemoteException {
			currentComm = map.get(event.getPlayer().getId());
			PlayerCommunicationTest.logger.info("Changed to " + currentComm);

		}

		public void onPlayerJoinedTableEvent(PlayerJoinedTableEvent event)
				throws RemoteException {
		}

		public void onPlayerLeftTableEvent(PlayerLeftTableEvent event)
				throws RemoteException {
		}

		public void onShowHandEvent(ShowHandEvent event) throws RemoteException {
		}

		public void onWinnerEvent(WinnerEvent event) throws RemoteException {
		}

		public void onGameMessageEvent(GameMessageEvent event)
				throws RemoteException {
		}

		public void onTableCreatedEvent(TableCreatedEvent event)
				throws RemoteException {
		}

		public void onServerMessageEvent(ServerMessageEvent event)
				throws RemoteException {
		}

		public void onBrokePlayerKickedOutEvent(BrokePlayerKickedOutEvent event)
				throws RemoteException {
		}

		public void onTableChangedEvent(TableChangedEvent event)
				throws RemoteException {
		}

		public void onTableRemovedEvent(TableRemovedEvent event)
				throws RemoteException {
		}
	}

	private static class EventCollector implements RemoteAllEventsListener {

		public List<ServerEvent> getEvents() {
			return events;
		}

		private List<ServerEvent> events = new CopyOnWriteArrayList<ServerEvent>();

		public void onAllInEvent(AllInEvent event) throws RemoteException {
			events.add(event);
		}

		public void onBetEvent(BetEvent event) throws RemoteException {
			events.add(event);
		}

		public void onBigBlindEvent(BigBlindEvent event) throws RemoteException {
			events.add(event);

		}

		public void onCallEvent(CallEvent event) throws RemoteException {
			events.add(event);
		}

		public void onCheckEvent(CheckEvent event) throws RemoteException {
			events.add(event);
		}

		public void onFoldEvent(FoldEvent event) throws RemoteException {
			events.add(event);
		}

		public void onRaiseEvent(RaiseEvent event) throws RemoteException {
			events.add(event);
		}

		public void onSmallBlindEvent(SmallBlindEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onNewPocketCardsEvent(NewPocketCardsEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onNewCommunityCardsEvent(NewCommunityCardsEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onNewDealEvent(NewDealEvent event) throws RemoteException {
			events.add(event);
		}

		public void onNewRoundEvent(NewRoundEvent event) throws RemoteException {
			events.add(event);
		}

		public void onNextPlayerEvent(NextPlayerEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onPlayerJoinedTableEvent(PlayerJoinedTableEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onPlayerLeftTableEvent(PlayerLeftTableEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onShowHandEvent(ShowHandEvent event) throws RemoteException {
			events.add(event);
		}

		public void onWinnerEvent(WinnerEvent event) throws RemoteException {
			events.add(event);
		}

		public void onGameMessageEvent(GameMessageEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onTableCreatedEvent(TableCreatedEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onServerMessageEvent(ServerMessageEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onBrokePlayerKickedOutEvent(BrokePlayerKickedOutEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onTableChangedEvent(TableChangedEvent event)
				throws RemoteException {
			events.add(event);
		}

		public void onTableRemovedEvent(TableRemovedEvent event)
				throws RemoteException {
			events.add(event);
		}

	}
}
