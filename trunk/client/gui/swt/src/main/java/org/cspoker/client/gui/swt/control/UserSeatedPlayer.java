package org.cspoker.client.gui.swt.control;

import java.rmi.RemoteException;

import org.cspoker.client.User;
import org.cspoker.client.gui.swt.window.PlayerSeatComposite;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.elements.player.SeatedPlayer;

public class UserSeatedPlayer
		extends MutableSeatedPlayer {
	
	public UserSeatedPlayer(PlayerSeatComposite playerSeatComposite, User user, GameState gameState) {
		this(playerSeatComposite, new SeatedPlayer(user.getPlayer().getId(), Long.MAX_VALUE,
				user.getPlayer().getName(), 0, 0), gameState);
	}
	
	public UserSeatedPlayer(PlayerSeatComposite playerSeatComposite, SeatedPlayer player, GameState gameState) {
		super(playerSeatComposite, player, gameState);
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
	
	public void setTableContext(RemoteHoldemTableContext tableContext) {
		this.tableContext = tableContext;
	}
	
	public RemoteChatContext getChatContext() {
		return chatContext;
	}
	
	public RemoteHoldemPlayerContext getPlayerContext() {
		return playerContext;
	}
	
	public void setPlayerContext(RemoteHoldemPlayerContext playerContext) {
		this.playerContext = playerContext;
	}
	
	public void sitIn(long seatId, HoldemPlayerListener listener)
			throws RemoteException {
		if (tableContext == null)
			throw new IllegalStateException("No table context available, you can not sit in");
		assert (seatId >= 0 && seatId != Long.MAX_VALUE) : "Illegal seat id provided: " + seatId;
		playerContext = tableContext.sitIn(seatId, listener);
		
	}
}
