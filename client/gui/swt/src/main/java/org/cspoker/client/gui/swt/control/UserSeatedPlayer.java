package org.cspoker.client.gui.swt.control;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;
import org.cspoker.client.User;
import org.cspoker.client.gui.swt.window.GameWindow;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.lobby.context.LobbyContext;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.SeatedPlayer;

/**
 * Represents a user who is sitting at/observing a certain table
 * 
 * @author Stephan Schmidt
 */
public class UserSeatedPlayer
		extends MutableSeatedPlayer {
	
	private final static Logger logger = Logger.getLogger(UserSeatedPlayer.class);
	private final GameWindow gameWindow;
	
	public UserSeatedPlayer(GameWindow gameWindow, User user, GameState gameState) {
		super(new SeatedPlayer(user.getPlayer().getId(), Long.MAX_VALUE, user.getPlayer().getName(), 0, 0), gameState);
		assert (gameWindow != null) : "We need the GameWindow as the listener!";
		this.gameWindow = gameWindow;
	}
	
	RemoteHoldemTableContext tableContext;
	RemoteHoldemPlayerContext playerContext;
	RemoteChatContext chatContext;
	
	public void setChatContext(RemoteChatContext chatContext) {
		this.chatContext = chatContext;
		
	}
	
	public RemoteHoldemTableContext getTableContext() {
		return tableContext;
	}
	
	public RemoteChatContext getChatContext() {
		return chatContext;
	}
	
	public RemoteHoldemPlayerContext getPlayerContext() {
		return playerContext;
	}
	
	public void sitIn(long seatId)
			throws RemoteException {
		if (tableContext == null)
			throw new IllegalStateException("No table context available, you can not sit in");
		assert (seatId >= 0 && seatId != Long.MAX_VALUE) : "Illegal seat id provided: " + seatId;
		playerContext = tableContext.sitIn(seatId, gameWindow);
		
	}
	
	/**
	 * Join table to receive events for this table
	 * 
	 * @param lobbyContext {@link LobbyContext} needed to retrieve
	 *            {@link RemoteHoldemTableContext}
	 */
	public void joinTable(RemoteLobbyContext lobbyContext) {
		if (lobbyContext == null)
			throw new IllegalArgumentException("Please provide correct lobby context");
		try {
			tableContext = lobbyContext.joinHoldemTable(gameState.getTableMemento().getId(),
					new ForwardingSWTHoldemTableListener(gameWindow));
		} catch (RemoteException e) {
			throw new IllegalStateException(e);
		} catch (IllegalActionException e) {
			// TODO Auto-generated catch block
			logger.warn("You cannot join the desired table", e);
		}
	}
	
	public void setChatContext(RemoteServerContext communication, ChatListener chatListener) {
		try {
			chatContext = communication.getChatContext(chatListener);
		} catch (RemoteException e) {
			throw new IllegalStateException(e);
		}
		
	}
}
