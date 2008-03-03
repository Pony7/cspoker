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
import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.Event;
import org.cspoker.common.events.gameevents.GameMessageEvent;
import org.cspoker.common.events.gameevents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameevents.NewDealEvent;
import org.cspoker.common.events.gameevents.NewRoundEvent;
import org.cspoker.common.events.gameevents.NextPlayerEvent;
import org.cspoker.common.events.gameevents.PlayerJoinedGameEvent;
import org.cspoker.common.events.gameevents.PlayerLeftTableEvent;
import org.cspoker.common.events.gameevents.ShowHandEvent;
import org.cspoker.common.events.gameevents.WinnerEvent;
import org.cspoker.common.events.gameevents.playeractionevents.AllInEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BetEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BigBlindEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CallEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CheckEvent;
import org.cspoker.common.events.gameevents.playeractionevents.FoldEvent;
import org.cspoker.common.events.gameevents.playeractionevents.RaiseEvent;
import org.cspoker.common.events.gameevents.playeractionevents.SmallBlindEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.events.serverevents.PlayerJoinedEvent;
import org.cspoker.common.events.serverevents.PlayerLeftEvent;
import org.cspoker.common.events.serverevents.ServerMessageEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.player.PlayerId;
import org.cspoker.server.common.game.gamecontrol.TestPlayerFactory;
import org.cspoker.server.common.game.player.GamePlayer;
import org.cspoker.server.common.game.player.IllegalNameException;
import org.cspoker.server.common.game.player.PlayerFactory;
import org.cspoker.server.common.game.session.PlayerKilledExcepion;
import org.cspoker.server.common.game.session.Session;
import org.cspoker.server.common.game.session.SessionManager;

public class PlayerCommunicationTest extends TestCase {

	private static Logger logger = Logger
			.getLogger(PlayerCommunicationTest.class);

	private PlayerFactory playerFactory;

	@Override
	public void setUp() {
		playerFactory = new TestPlayerFactory();
	}

	public void testConstructor() throws IllegalNameException {
		GamePlayer kenzo = playerFactory.createNewPlayer("Kenzo");
		new PlayerCommunicationImpl(kenzo);
	}

	public void testCreateTable() throws IllegalNameException {
		GamePlayer kenzo = playerFactory.createNewPlayer("Kenzo");
		PlayerCommunicationImpl kenzoComm = new PlayerCommunicationImpl(kenzo);
		try {
			kenzoComm.createTable("test");
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

	public void testJoinTable() throws IllegalNameException {
		GamePlayer kenzo = playerFactory.createNewPlayer("Kenzo");
		GamePlayer guy = playerFactory.createNewPlayer("Guy");
		PlayerCommunicationImpl kenzoComm = new PlayerCommunicationImpl(kenzo);
		PlayerCommunicationImpl guyComm = new PlayerCommunicationImpl(guy);
		try {
			TableId tableId = kenzoComm.createTable("test");
			// TODO create an event listener filter to wait for the tableID
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
			TableId tableId = kenzoComm.createTable("test");
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
			

			TableId tableId = kenzoComm.createTable("test");
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

			TableId tableId = kenzoComm.createTable("test");

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

	public void showEvents(List<Event> events) {
		for (Event event : events) {
			PlayerCommunicationTest.logger.info("++ " + event);
		}
	}

	private class TestListener implements RemoteAllEventsListener {

		private Map<PlayerId, PlayerCommunication> map;

		public TestListener(Map<PlayerId, PlayerCommunication> map) {
			this.map = map;
		}

		@Override
		public void onAllInEvent(AllInEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onBetEvent(BetEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onBigBlindEvent(BigBlindEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCallEvent(CallEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCheckEvent(CheckEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFoldEvent(FoldEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRaiseEvent(RaiseEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSmallBlindEvent(SmallBlindEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNewPocketCardsEvent(NewPocketCardsEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNewCommunityCardsEvent(NewCommunityCardsEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNewDealEvent(NewDealEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNewRoundEvent(NewRoundEvent event) throws RemoteException {
			currentComm = map.get(event.getPlayer().getId());
			PlayerCommunicationTest.logger.info("Changed to " + currentComm);
		}

		@Override
		public void onNextPlayerEvent(NextPlayerEvent event)
				throws RemoteException {
			currentComm = map.get(event.getPlayer().getId());
			PlayerCommunicationTest.logger.info("Changed to " + currentComm);

		}

		@Override
		public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPlayerLeftTableEvent(PlayerLeftTableEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onShowHandEvent(ShowHandEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onWinnerEvent(WinnerEvent event) throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGameMessageEvent(GameMessageEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPlayerJoinedEvent(PlayerJoinedEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPlayerLeftEvent(PlayerLeftEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTableCreatedEvent(TableCreatedEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServerMessageEvent(ServerMessageEvent event)
				throws RemoteException {
			// TODO Auto-generated method stub

		}

	}

	private class EventCollector implements RemoteAllEventsListener{
		
		public List<Event> getEvents(){
			return events;
		}
		
		private List<Event> events = new CopyOnWriteArrayList<Event>();

		@Override
		public void onAllInEvent(AllInEvent event) throws RemoteException{
			events.add(event);
		}

		@Override
		public void onBetEvent(BetEvent event) throws RemoteException{
			events.add(event);
		}

		@Override
		public void onBigBlindEvent(BigBlindEvent event) throws RemoteException{
			events.add(event);
			
		}

		@Override
		public void onCallEvent(CallEvent event) throws RemoteException {
			events.add(event);
		}

		@Override
		public void onCheckEvent(CheckEvent event) throws RemoteException {
			events.add(event);
		}

		@Override
		public void onFoldEvent(FoldEvent event) throws RemoteException {
			events.add(event);
		}

		@Override
		public void onRaiseEvent(RaiseEvent event) throws RemoteException {
			events.add(event);
		}

		@Override
		public void onSmallBlindEvent(SmallBlindEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onNewPocketCardsEvent(NewPocketCardsEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onNewCommunityCardsEvent(NewCommunityCardsEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onNewDealEvent(NewDealEvent event) throws RemoteException {
			events.add(event);
		}

		@Override
		public void onNewRoundEvent(NewRoundEvent event) throws RemoteException {
			events.add(event);
		}

		@Override
		public void onNextPlayerEvent(NextPlayerEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onPlayerLeftTableEvent(PlayerLeftTableEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onShowHandEvent(ShowHandEvent event) throws RemoteException {
			events.add(event);
		}

		@Override
		public void onWinnerEvent(WinnerEvent event) throws RemoteException {
			events.add(event);
		}

		@Override
		public void onGameMessageEvent(GameMessageEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onPlayerJoinedEvent(PlayerJoinedEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onPlayerLeftEvent(PlayerLeftEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onTableCreatedEvent(TableCreatedEvent event)
				throws RemoteException {
			events.add(event);
		}

		@Override
		public void onServerMessageEvent(ServerMessageEvent event)
				throws RemoteException {
			events.add(event);
		}
		
	}
}
