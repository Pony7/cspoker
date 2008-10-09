package org.cspoker.client.gui.swt.control;

import java.rmi.RemoteException;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.cspoker.client.User;
import org.cspoker.client.gui.swt.window.GameWindow;
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
import org.cspoker.common.elements.player.SeatedPlayer;

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
	
	RemoteHoldemTableContext tableContext;
	RemoteHoldemPlayerContext playerContext;
	RemoteChatContext chatContext;
	
	/**
	 * Create a new UserSeatedPlayer representing the player for a given
	 * {@link GameWindow}
	 * 
	 * @param gameWindow The GameWindow the user is sitting at
	 * @param user The {@link User}
	 * @param gameState The {@link GameState} for this table
	 */
	public UserSeatedPlayer(GameWindow gameWindow, User user, GameState gameState) {
		super(new SeatedPlayer(user.getPlayer().getId(), Long.MAX_VALUE, user.getPlayer().getName(), 0, 0), gameState);
		assert (gameWindow != null) : "We need the GameWindow as the listener!";
		this.gameWindow = gameWindow;
		this.displayExecutor = new DisplayExecutor(gameWindow.getDisplay());
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
	 * @return The {@link RemoteChatContext} for communicating with the server
	 * @throws IllegalStateException when the chat context has not been created
	 *             yet
	 */
	public RemoteChatContext getChatContext() {
		if (chatContext == null)
			throw new IllegalStateException("No chat context initialized");
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
	 */
	public void sitIn(long seatId)
			throws RemoteException {
		if (tableContext == null)
			throw new IllegalStateException("No table context available, you can not sit in");
		assert (seatId >= 0 && seatId != Long.MAX_VALUE) : "Illegal seat id provided: " + seatId;
		playerContext = tableContext.sitIn(seatId, new AsynchronousHoldemPlayerListener(displayExecutor, gameWindow));
		
	}
	
	/**
	 * Join table to receive events for this table
	 * 
	 * @param lobbyContext {@link LobbyContext} needed to retrieve
	 *            {@link RemoteHoldemTableContext}
	 */
	public void joinTable(RemoteLobbyContext lobbyContext) {
		assert (lobbyContext != null);
		try {
			tableContext = lobbyContext.joinHoldemTable(gameState.getTableMemento().getId(),
					new AsynchronousHoldemTableListener(displayExecutor, gameWindow));
		} catch (RemoteException e) {
			throw new IllegalStateException(e);
		} catch (IllegalActionException e) {
			// TODO Auto-generated catch block
			logger.warn("You cannot join the desired table", e);
		}
	}
	
	/**
	 * Retrieves a {@link RemoteChatContext} from the server.
	 * 
	 * @param communication The {@link RemoteServerContext} to as k for the chat
	 *            context
	 */
	public void setChatContext(RemoteServerContext communication) {
		assert (communication != null);
		try {
			chatContext = communication.getChatContext(new AsynchronousChatListener(displayExecutor, gameWindow
					.getUserInputComposite()));
		} catch (RemoteException e) {
			throw new IllegalStateException(e);
		}
	}
}
