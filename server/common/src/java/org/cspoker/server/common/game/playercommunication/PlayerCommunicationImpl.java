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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.eventlisteners.AllEventsListener;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.eventlisteners.game.RemoteGameMessageListener;
import org.cspoker.common.eventlisteners.game.RemoteNewCommunityCardsListener;
import org.cspoker.common.eventlisteners.game.RemoteNewDealListener;
import org.cspoker.common.eventlisteners.game.RemoteNewRoundListener;
import org.cspoker.common.eventlisteners.game.RemoteNextPlayerListener;
import org.cspoker.common.eventlisteners.game.RemotePlayerJoinedGameListener;
import org.cspoker.common.eventlisteners.game.RemotePlayerLeftTableListener;
import org.cspoker.common.eventlisteners.game.RemoteShowHandListener;
import org.cspoker.common.eventlisteners.game.RemoteWinnerListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteAllInListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteBetListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteBigBlindListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteCallListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteCheckListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteFoldListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteRaiseListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteSmallBlindListener;
import org.cspoker.common.eventlisteners.game.privatelistener.RemoteNewPocketCardsListener;
import org.cspoker.common.eventlisteners.server.RemotePlayerJoinedListener;
import org.cspoker.common.eventlisteners.server.RemotePlayerLeftListener;
import org.cspoker.common.eventlisteners.server.RemoteServerMessageListener;
import org.cspoker.common.eventlisteners.server.RemoteTableCreatedListener;
import org.cspoker.common.events.gameEvents.GameMessageEvent;
import org.cspoker.common.events.gameEvents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameEvents.NewDealEvent;
import org.cspoker.common.events.gameEvents.NewRoundEvent;
import org.cspoker.common.events.gameEvents.NextPlayerEvent;
import org.cspoker.common.events.gameEvents.PlayerJoinedGameEvent;
import org.cspoker.common.events.gameEvents.PlayerLeftTableEvent;
import org.cspoker.common.events.gameEvents.ShowHandEvent;
import org.cspoker.common.events.gameEvents.WinnerEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.AllInEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.BetEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.BigBlindEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.CallEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.CheckEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.FoldEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.RaiseEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.SmallBlindEvent;
import org.cspoker.common.events.gameEvents.privateEvents.NewPocketCardsEvent;
import org.cspoker.common.events.serverEvents.PlayerJoinedEvent;
import org.cspoker.common.events.serverEvents.PlayerLeftEvent;
import org.cspoker.common.events.serverEvents.ServerMessageEvent;
import org.cspoker.common.events.serverEvents.TableCreatedEvent;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.player.PlayerId;
import org.cspoker.server.common.game.GameManager;
import org.cspoker.server.common.game.player.GamePlayer;
import org.cspoker.server.common.game.session.SessionManager;

/**
 * A class of player communications.
 * 
 * It's the interface to all game control actions.
 * 
 * @author Kenzo
 * 
 */
public class PlayerCommunicationImpl extends PlayerCommunication {

	private static Logger logger = Logger
			.getLogger(PlayerCommunicationImpl.class);

	/***************************************************************************
	 * Variables
	 **************************************************************************/

	/**
	 * The variable containing the player.
	 */
	private final GamePlayer player;

	/**
	 * This variable contains the player communication state.
	 */
	private PlayerCommunicationState state;

	private final EventsCollector eventsCollector = new EventsCollector();

	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	/**
	 * Construct a new player communication with given player.
	 * 
	 * @param player
	 *            The given player
	 */
	public PlayerCommunicationImpl(GamePlayer player) {
		this.player = player;
		state = new InitialState(this);
		GameManager.getServerMediator().subscribeAllServerEventsListener(
				player.getId(), getAllEventsListener());
		subscribeAllEventsListener(getEventsCollector());
	}

	/**
	 * Returns the player contained in this player communication.
	 * 
	 * @return The player contained in this player communication.
	 */
	public GamePlayer getPlayer() {
		return player;
	}

	/**
	 * Returns the player id from the player contained in this player
	 * communication.
	 * 
	 * @return The player id from the player contained in this player
	 *         communication.
	 */
	public PlayerId getId() {
		return player.getId();
	}

	/***************************************************************************
	 * Maintenance Actions
	 **************************************************************************/

	@Override
	public void kill() {
		state.kill();
	}

	/***************************************************************************
	 * Player Actions
	 **************************************************************************/

	@Override
	public void call() throws IllegalActionException {
		state.call();
	}

	@Override
	public void bet(int amount) throws IllegalActionException {
		state.bet(amount);
	}

	@Override
	public void fold() throws IllegalActionException {
		state.fold();
	}

	@Override
	public void check() throws IllegalActionException {
		state.check();
	}

	@Override
	public void raise(int amount) throws IllegalActionException {
		state.raise(amount);
	}

	@Override
	public void deal() throws IllegalActionException {
		state.deal();
	}

	@Override
	public void allIn() throws IllegalActionException {
		state.allIn();
	}

	@Override
	public void say(String message) {
		state.say(message);
	}

	/***************************************************************************
	 * Leave/Join Game
	 **************************************************************************/

	/**
	 * Join the table with given table id.
	 * 
	 * @pre The given id should be effective. |id!=null
	 * @throws IllegalActionException
	 *             [can] This actions is not a valid action in the current
	 *             state.
	 */
	@Override
	public void joinTable(TableId id) throws IllegalActionException {
		if (id == null)
			throw new IllegalArgumentException(
					"The given table id is not effective.");
		state.join(id);
	}

	@Override
	public void leaveTable() throws IllegalActionException {
		state.leaveTable();
	}

	@Override
	public TableId createTable() throws IllegalActionException {
		return state.createTable();
	}

	@Override
	public void startGame() throws IllegalActionException {
		state.startGame();
	}

	public Events getLatestEvents() throws IllegalActionException {
		return eventsCollector.getLatestEvents();
	}

	public Events getLatestEventsAndAck(int ack) throws IllegalActionException {
		return eventsCollector.getLatestEventsAndAck(ack);
	}

	EventsCollector getEventsCollector() {
		return eventsCollector;
	}

	void setPlayerCommunicationState(PlayerCommunicationState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "player communication of " + player.getName();
	}

	/***************************************************************************
	 * Publisher
	 **************************************************************************/

	@Override
	public void subscribeAllEventsListener(RemoteAllEventsListener listener) {
		eventListeners.add(listener);
	}

	@Override
	public void unsubscribeAllEventsListener(RemoteAllEventsListener listener) {
		eventListeners.remove(listener);
	}

	/**
	 * This list contains all message listeners that should be alerted on a
	 * message.
	 */
	private final List<RemoteAllEventsListener> eventListeners = new CopyOnWriteArrayList<RemoteAllEventsListener>();

	/***************************************************************************
	 * all events listener
	 **************************************************************************/

	AllEventsListener getAllEventsListener() {
		return allEventsListener;
	}

	private final AllEventsListener allEventsListener = new AllEventsListenerImpl();

	private class AllEventsListenerImpl implements AllEventsListener {

		public void onAllInEvent(AllInEvent event) {
			for (RemoteAllInListener listener : eventListeners) {
				try {
					listener.onAllInEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onBetEvent(BetEvent event) {
			for (RemoteBetListener listener : eventListeners) {
				try {
					listener.onBetEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}

		}

		public void onBigBlindEvent(BigBlindEvent event) {
			for (RemoteBigBlindListener listener : eventListeners) {
				try {
					listener.onBigBlindEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}

		}

		public void onCallEvent(CallEvent event) {
			for (RemoteCallListener listener : eventListeners) {
				try {
					listener.onCallEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onCheckEvent(CheckEvent event) {
			for (RemoteCheckListener listener : eventListeners) {
				try {
					listener.onCheckEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onFoldEvent(FoldEvent event) {
			for (RemoteFoldListener listener : eventListeners) {
				try {
					listener.onFoldEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}

		}

		public void onRaiseEvent(RaiseEvent event) {
			for (RemoteRaiseListener listener : eventListeners) {
				try {
					listener.onRaiseEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onSmallBlindEvent(SmallBlindEvent event) {
			for (RemoteSmallBlindListener listener : eventListeners) {
				try {
					listener.onSmallBlindEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onNewPocketCardsEvent(NewPocketCardsEvent event) {
			for (RemoteNewPocketCardsListener listener : eventListeners) {
				try {
					listener.onNewPocketCardsEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}

		}

		public void onNewCommunityCardsEvent(NewCommunityCardsEvent event) {
			for (RemoteNewCommunityCardsListener listener : eventListeners) {
				try {
					listener.onNewCommunityCardsEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onNewDealEvent(NewDealEvent event) {
			for (RemoteNewDealListener listener : eventListeners) {
				try {
					listener.onNewDealEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}

		}

		public void onNewRoundEvent(NewRoundEvent event) {
			for (RemoteNewRoundListener listener : eventListeners) {
				try {
					listener.onNewRoundEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}

		}

		public void onNextPlayerEvent(NextPlayerEvent event) {
			for (RemoteNextPlayerListener listener : eventListeners) {
				try {
					listener.onNextPlayerEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event) {
			for (RemotePlayerJoinedGameListener listener : eventListeners) {
				try {
					listener.onPlayerJoinedGameEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onShowHandEvent(ShowHandEvent event) {
			for (RemoteShowHandListener listener : eventListeners) {
				try {
					listener.onShowHandEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onWinnerEvent(WinnerEvent event) {
			for (RemoteWinnerListener listener : eventListeners) {
				try {
					listener.onWinnerEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onPlayerLeftTableEvent(PlayerLeftTableEvent event) {
			for (RemotePlayerLeftTableListener listener : eventListeners) {
				try {
					listener.onPlayerLeftTableEvent(event);
				} catch (RemoteException e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}

		}

		public void onGameMessageEvent(GameMessageEvent event) {
			for (RemoteGameMessageListener listener : eventListeners) {
				try {
					listener.onGameMessageEvent(event);
				} catch (RemoteException e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}
		}

		/***********************************************************************
		 * Server Events
		 **********************************************************************/

		public void onPlayerJoinedEvent(PlayerJoinedEvent event) {
			for (RemotePlayerJoinedListener listener : eventListeners) {
				try {
					listener.onPlayerJoinedEvent(event);
				} catch (RemoteException e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}

		}

		public void onTableCreatedEvent(TableCreatedEvent event) {
			for (RemoteTableCreatedListener listener : eventListeners) {
				try {
					listener.onTableCreatedEvent(event);
				} catch (RemoteException e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}
		}

		public void onPlayerLeftEvent(PlayerLeftEvent event) {
			for (RemotePlayerLeftListener listener : eventListeners) {
				try {
					listener.onPlayerLeftEvent(event);
				} catch (RemoteException e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}
		}

		public void onServerMessageEvent(ServerMessageEvent event) {
			for (RemoteServerMessageListener listener : eventListeners) {
				try {
					listener.onServerMessageEvent(event);
				} catch (RemoteException e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}
		}
	}

}
