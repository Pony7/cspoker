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

package org.cspoker.server.common.playercommunication;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.cspoker.common.api.chat.event.ServerMessageEvent;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
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
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.table.DetailedTable;
import org.cspoker.common.elements.table.TableList;
import org.cspoker.server.common.GameManager;
import org.cspoker.server.common.TableManager;
import org.cspoker.server.common.exception.TableDoesNotExistException;
import org.cspoker.server.common.player.GameSeatedPlayer;
import org.cspoker.server.common.session.SessionManager;

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
	private final GameSeatedPlayer player;

	/**
	 * This variable contains the player communication state.
	 */
	private PlayerCommunicationState state;
	
	/**
	 * A boolean value to see whether the player is active or not.
	 * 
	 * This will be used to see whether a player communication should be killed.
	 */
	private AtomicBoolean isActive = new AtomicBoolean(true);

	/***************************************************************************
	 * Constructor
	 **************************************************************************/

	/**
	 * Construct a new player communication with given player.
	 * 
	 * @param player
	 *            The given player
	 */
	public PlayerCommunicationImpl(GameSeatedPlayer player) {
		this.player = player;
		state = new InitialState(this);
		GameManager.getServerMediator().subscribeAllServerEventsListener(
				player.getId(), getAllEventsListener());
	}

	/**
	 * Returns the player contained in this player communication.
	 * 
	 * @return The player contained in this player communication.
	 */
	public GameSeatedPlayer getPlayer() {
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

	public void kill() {
		eventListeners.clear();
		state.kill();
	}

	/***************************************************************************
	 * Player Actions
	 **************************************************************************/

	public void call() throws IllegalActionException {
		stillAlive();
		state.call();
	}

	public void bet(int amount) throws IllegalActionException {
		stillAlive();
		state.bet(amount);
	}

	public void fold() throws IllegalActionException {
		stillAlive();
		state.fold();
	}

	public void check() throws IllegalActionException {
		stillAlive();
		state.check();
	}

	public void raise(int amount) throws IllegalActionException {
		stillAlive();
		state.raise(amount);
	}

	public void allIn() throws IllegalActionException {
		stillAlive();
		state.allIn();
	}

	public void say(String message) {
		stillAlive();
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

	public DetailedTable joinTable(TableId tableId, SeatId seatId)
			throws IllegalActionException {
		stillAlive();
		return state.join(tableId, seatId);
	}

	/**
	 * Join the table with given table id.
	 * 
	 * @pre The given id should be effective. |id!=null
	 * @throws IllegalActionException
	 *             [can] This actions is not a valid action in the current
	 *             state.
	 */

	public DetailedTable joinTable(TableId tableId) throws IllegalActionException {
		return joinTable(tableId, null);
	}

	public void leaveTable() throws IllegalActionException {
		stillAlive();
		state.leaveTable();
	}

	public DetailedTable createTable(String name) throws IllegalActionException {
		stillAlive();
		return state.createTable(name);
	}

	public DetailedTable createTable(String name, GameProperty property)
			throws IllegalActionException {
		return state.createTable(name, property);
	}

	public DetailedTable getTable(TableId id) throws IllegalActionException {
		stillAlive();
		try {
			return TableManager.global_table_manager.getTable(id)
					.getSavedTable();
		} catch (TableDoesNotExistException e) {
			throw new IllegalActionException(
					"Can not enquire the state of the given table. "
							+ e.getMessage());
		}
	}

	public TableList getTables() {
		stillAlive();
		return new TableList(TableManager.global_table_manager.getAllTables());
	}

	public void startGame() throws IllegalActionException {
		stillAlive();
		state.startGame();
	}

	void setPlayerCommunicationState(PlayerCommunicationState state) {
		this.state = state;
	}

	public void changeToInitialState() {
		state = new InitialState(this);
	}

	public String toString() {
		return "player communication of " + player.getName();
	}

	public boolean isActive() {
		return isActive.getAndSet(false);
	}

	private void stillAlive() {
		isActive.set(true);
	}

	/***************************************************************************
	 * Publisher
	 **************************************************************************/

	public void subscribeAllEventsListener(RemoteAllEventsListener listener) {
		eventListeners.add(listener);
	}

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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
					logger.error("RemoteException from event handler", e);
					SessionManager.global_session_manager
							.killSession(getPlayer().getName());
				}
			}
		}

		public void onPlayerJoinedTableEvent(PlayerJoinedTableEvent event) {
			for (RemotePlayerJoinedTableListener listener : eventListeners) {
				try {
					listener.onPlayerJoinedTableEvent(event);
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
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
				} catch (Exception e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}
		}

		/***********************************************************************
		 * Server Events
		 **********************************************************************/

		public void onTableCreatedEvent(TableCreatedEvent event) {
			for (RemoteTableCreatedListener listener : eventListeners) {
				try {
					listener.onTableCreatedEvent(event);
				} catch (Exception e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}
		}

		public void onServerMessageEvent(ServerMessageEvent event) {
			for (RemoteServerMessageListener listener : eventListeners) {
				try {
					listener.onServerMessageEvent(event);
				} catch (Exception e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}
		}

		public void onBrokePlayerKickedOutEvent(BrokePlayerKickedOutEvent event) {
			for (RemoteBrokePlayerKickedOutListener listener : eventListeners) {
				try {
					listener.onBrokePlayerKickedOutEvent(event);
				} catch (Exception e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}

		}

		public void onTableChangedEvent(TableChangedEvent event) {
			for (RemoteTableChangedListener listener : eventListeners) {
				try {
					listener.onTableChangedEvent(event);
				} catch (Exception e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}
		}

		public void onTableRemovedEvent(TableRemovedEvent event) {
			for (RemoteTableRemovedListener listener : eventListeners) {
				try {
					listener.onTableRemovedEvent(event);
				} catch (Exception e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}
		}
	}

}
