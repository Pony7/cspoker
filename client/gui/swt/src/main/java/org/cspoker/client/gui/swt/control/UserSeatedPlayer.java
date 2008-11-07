/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.cspoker.client.gui.swt.control;

import java.rmi.RemoteException;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.cspoker.client.User;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.common.api.cashier.context.RemoteCashierContext;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.AsynchronousChatListener;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.AsynchronousHoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.AsynchronousHoldemTableListener;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.chips.Chips;
import org.cspoker.common.elements.chips.IllegalValueException;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableId;

/**
 * Represents a user who is sitting at/observing a certain table
 * <p>
 * Central entity for creating and registering the Listeners and managing the
 * Contexts
 * 
 * @author Stephan Schmidt
 */
public class UserSeatedPlayer
		extends MutableSeatedPlayer {
	
	private final static Logger logger = Logger.getLogger(UserSeatedPlayer.class);
	private final GameWindow gameWindow;
	private final Executor displayExecutor;
	private final TableId tableId;
	
	RemoteHoldemTableContext tableContext;
	RemoteHoldemPlayerContext playerContext;
	RemoteChatContext chatContext;
	private RemoteCashierContext cashierContext;
	private GameState gameState;
	
	/**
	 * Create a new UserSeatedPlayer representing the player for a given
	 * {@link GameWindow}
	 * 
	 * @param gameWindow The GameWindow the user is sitting at
	 * @param user The {@link User}
	 * @param gameState The {@link GameState} for this table
	 * @throws IllegalValueException
	 */
	public UserSeatedPlayer(GameWindow gameWindow, ClientCore core, GameState gameState)
			throws IllegalValueException {
		super(new SeatedPlayer(core.getUser().getPlayerId(), new SeatId(Long.MAX_VALUE), core.getUser().getUserName(),
				0, 0, false));
		this.gameState = gameState;
		assert (gameWindow != null) : "We need the GameWindow as the listener!";
		this.gameWindow = gameWindow;
		this.displayExecutor = DisplayExecutor.getInstance();
		tableId = gameState.getTableMemento().getId();
	}
	
	/**
	 * @return The {@link RemoteHoldemTableContext} for the GameWindow this
	 *         player is seated at
	 * @throws IllegalStateException when the table context has not been created
	 *             yet
	 */
	public RemoteHoldemTableContext getTableContext() {
		if (tableContext == null)
			throw new IllegalStateException("No table context initialized");
		return tableContext;
	}
	
	/**
	 * @return The {@link RemoteCashierContext} for the GameWindow this player
	 *         is seated at
	 * @throws IllegalStateException when the table context has not been created
	 *             yet
	 */
	public RemoteCashierContext getCashierContext() {
		if (cashierContext == null)
			initializeCashierContext(gameWindow.getClientCore().getCommunication());
		return cashierContext;
	}
	
	/**
	 * @return The {@link RemoteChatContext} for communicating with the server
	 * @throws IllegalStateException when the chat context has not been created
	 *             yet
	 */
	public RemoteChatContext getChatContext() {
		if (chatContext == null)
			initializeChatContext(gameWindow.getClientCore().getCommunication());
		return chatContext;
	}
	
	/**
	 * @return The {@link RemoteHoldemPlayerContext} for communicating with the
	 *         server
	 * @throws IllegalStateException when the player context has not been
	 *             created yet
	 */
	public RemoteHoldemPlayerContext getPlayerContext() {
		if (playerContext == null)
			throw new IllegalStateException("No player context initialized");
		
		return playerContext;
	}
	
	/**
	 * The User sits in at the table
	 * <p>
	 * Creates a corresponding {@link RemoteHoldemPlayerContext} for this user.
	 * 
	 * @param seatId The seat id where to sit in
	 * @throws RemoteException When the sit in request was unsuccessful
	 * @throws IllegalStateException
	 */
	public void sitIn(SeatId seatId, int amount)
			throws RemoteException, IllegalActionException {
		if (tableContext == null)
			throw new IllegalStateException("No table context available, you can not sit in");
		assert (seatId.getId() >= 0 && seatId.getId() != Long.MAX_VALUE) : "Illegal seat id provided: " + seatId;
		playerContext = tableContext.sitIn(seatId, amount, new AsynchronousHoldemPlayerListener(displayExecutor,
				gameWindow));
	}
	
	/**
	 * Join table to receive events for this table
	 * 
	 * @param lobbyContext {@link LobbyContext} needed to retrieve
	 *            {@link RemoteHoldemTableContext}
	 * @throws IllegalStateException When the table can not be joined for some
	 *             reason
	 */
	public void joinTable(RemoteLobbyContext lobbyContext) {
		assert (lobbyContext != null);
		try {
			tableContext = lobbyContext.joinHoldemTable(gameState.getTableMemento().getId(),
					new AsynchronousHoldemTableListener(displayExecutor, gameWindow));
		} catch (RemoteException e) {
			throw new IllegalStateException(e);
		} catch (IllegalActionException e) {
			logger.warn("You cannot join the desired table", e);
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * Retrieves a {@link RemoteChatContext} from the server.
	 * 
	 * @param communication The {@link RemoteServerContext} to as k for the chat
	 *            context
	 */
	private void initializeChatContext(RemoteServerContext communication) {
		assert (communication != null);
		try {
			chatContext = communication.getTableChatContext(new AsynchronousChatListener(displayExecutor, gameWindow
					.getUserInputComposite()), tableId);
		} catch (RemoteException e) {
			throw new IllegalStateException(e);
		} catch (IllegalActionException e) {
			logger.error("This should not happen", e);
		}
	}
	
	private void initializeCashierContext(RemoteServerContext communication) {
		assert (communication != null);
		try {
			cashierContext = communication.getCashierContext();
		} catch (RemoteException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public void update(SeatedPlayer player) {
		assert (player.getId().equals(this.getId()));
		try {
			new Chips(player.getStackValue()).transferAllChipsTo(getStack());
			new Chips(player.getBetChipsValue()).transferAllChipsTo(getBetChips());
		} catch (IllegalArgumentException e) {
			logger.error(e);
		}
		
	}
}
