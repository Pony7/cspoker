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
import org.cspoker.common.elements.GameProperty;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.elements.table.TableList;
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
import org.cspoker.server.common.game.GameManager;
import org.cspoker.server.common.game.TableManager;
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
	public Table joinTable(TableId tableId, SeatId seatId) throws IllegalActionException {
		state.join(tableId, seatId);
		return TableManager.global_table_manager.getTable(tableId).getSavedTable();
	}
	
	/**
	 * Join the table with given table id.
	 * 
	 * @pre The given id should be effective. |id!=null
	 * @throws IllegalActionException
	 *             [can] This actions is not a valid action in the current
	 *             state.
	 */
	@Override
	public Table joinTable(TableId tableId) throws IllegalActionException {
		state.join(tableId, null);
		return TableManager.global_table_manager.getTable(tableId).getSavedTable();
	}

	@Override
	public void leaveTable() throws IllegalActionException {
		state.leaveTable();
	}

	@Override
	public TableId createTable(String name) throws IllegalActionException {
		return state.createTable(name);
	}
	

	@Override
	public TableId createTable(String name, GameProperty property)
			throws IllegalActionException {
		return state.createTable(name, property);
	}
	
	@Override
	public Table getTable(TableId id) throws IllegalActionException{
		try {
			return TableManager.global_table_manager.getTable(id).getSavedTable();
		} catch (IllegalArgumentException e) {
			throw new IllegalActionException(e.getMessage());
		}
	}

	@Override
	public TableList getTables(){
		return new TableList(TableManager.global_table_manager.getAllTables());
	}

	@Override
	public void startGame() throws IllegalActionException {
		state.startGame();
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

		public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event) {
			for (RemotePlayerJoinedGameListener listener : eventListeners) {
				try {
					listener.onPlayerJoinedGameEvent(event);
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

		public void onPlayerJoinedEvent(PlayerJoinedEvent event) {
			for (RemotePlayerJoinedListener listener : eventListeners) {
				try {
					listener.onPlayerJoinedEvent(event);
				} catch (Exception e) {
					logger.error(
							"RemoteException from event handler, ignoring", e);
				}
			}

		}

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

		public void onPlayerLeftEvent(PlayerLeftEvent event) {
			for (RemotePlayerLeftListener listener : eventListeners) {
				try {
					listener.onPlayerLeftEvent(event);
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
	}

}
