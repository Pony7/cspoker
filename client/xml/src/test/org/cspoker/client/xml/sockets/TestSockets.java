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
package org.cspoker.client.xml.sockets;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.security.auth.login.LoginException;

import org.cspoker.client.xml.common.XmlChannelRemotePlayerCommunication;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
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
import org.cspoker.common.util.Log4JPropertiesLoader;

public class TestSockets {

	static {
		Log4JPropertiesLoader
				.load("org/cspoker/client/xml/http/logging/log4j.properties");
	}

	public static void main(String[] args) throws AccessException,
			RemoteException, NotBoundException, IllegalActionException,
			LoginException {
		int port = 8081;
		String server = "localhost";
		RemotePlayerCommunicationFactoryForSockets temp = new RemotePlayerCommunicationFactoryForSockets(server,port);
		XmlChannelRemotePlayerCommunication kenzo = null;
		XmlChannelRemotePlayerCommunication guy = null;
		XmlChannelRemotePlayerCommunication cedric = null;
		try {
			guy = temp
					.getRemotePlayerCommunication("guy", "test");
			System.out.println("Guy Logged In and will print events to sout");

			guy.subscribeAllEventsListener(new RemoteAllEventsListener() {

				public void onAllInEvent(AllInEvent event) {
					System.out.println(event.toString());

				}

				public void onBetEvent(BetEvent event) {
					System.out.println(event.toString());

				}

				public void onBigBlindEvent(BigBlindEvent event) {
					System.out.println(event.toString());

				}

				public void onCallEvent(CallEvent event) {
					System.out.println(event.toString());

				}

				public void onCheckEvent(CheckEvent event) {
					System.out.println(event.toString());

				}

				public void onFoldEvent(FoldEvent event) {
					System.out.println(event.toString());

				}

				public void onRaiseEvent(RaiseEvent event) {
					System.out.println(event.toString());

				}

				public void onSmallBlindEvent(SmallBlindEvent event) {
					System.out.println(event.toString());

				}

				public void onNewPocketCardsEvent(NewPocketCardsEvent event) {
					System.out.println(event.toString());

				}

				public void onNewCommunityCardsEvent(
						NewCommunityCardsEvent event) {
					System.out.println(event.toString());

				}

				public void onNewDealEvent(NewDealEvent event) {
					System.out.println(event.toString());

				}

				public void onNewRoundEvent(NewRoundEvent event) {
					System.out.println(event.toString());

				}

				public void onNextPlayerEvent(NextPlayerEvent event) {
					System.out.println(event.toString());

				}

				public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event) {
					System.out.println(event.toString());

				}

				public void onPlayerLeftTableEvent(PlayerLeftTableEvent event) {
					System.out.println(event.toString());

				}

				public void onShowHandEvent(ShowHandEvent event) {
					System.out.println(event.toString());

				}

				public void onWinnerEvent(WinnerEvent event) {
					System.out.println(event.toString());

				}

				public void onGameMessageEvent(GameMessageEvent event) {
					System.out.println(event.toString());

				}

				public void onPlayerJoinedEvent(PlayerJoinedEvent event) {
					System.out.println(event.toString());

				}

				public void onPlayerLeftEvent(PlayerLeftEvent event) {
					System.out.println(event.toString());

				}

				public void onTableCreatedEvent(TableCreatedEvent event) {
					System.out.println(event.toString());

				}

				public void onServerMessageEvent(ServerMessageEvent event) {
					System.out.println(event.toString());

				}

			});

			kenzo = temp.getRemotePlayerCommunication("kenzo",
					"test");
			cedric = temp.getRemotePlayerCommunication("cedric",
					"test");

			TableId id = guy.createTable();

			kenzo.joinTable(id);
			cedric.joinTable(id);

			guy.startGame();

			for (int i = 0; i < 10; i++) {
				try {
					guy.check();
				} catch (IllegalActionException e) {
					try {
						guy.call();
					} catch (IllegalActionException e2) {

					}
				}
				try {
					kenzo.check();
				} catch (IllegalActionException e) {
					try {
						kenzo.call();
					} catch (IllegalActionException e2) {

					}
				}
				try {
					cedric.check();
				} catch (IllegalActionException e) {
					try {
						cedric.call();
					} catch (IllegalActionException e2) {

					}
				}
			}
		} finally {
			if (guy != null) {
				guy.getChannel().close();
			}
			if (kenzo != null) {
				kenzo.getChannel().close();
			}
			if (cedric != null) {
				cedric.getChannel().close();
			}
		}
	}

}
