package org.cspoker.server.common;

import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.server.common.account.ExtendedAccountContext;
import org.cspoker.server.common.chat.room.ChatRoom;

public class ChatContextImpl implements ChatContext {

	private ExtendedAccountContext accountContext;
	private ChatRoom room;
	private ChatListener listener;
	
	public ChatContextImpl(ExtendedAccountContext accountContext,ChatRoom room,ChatListener listener) {
		this.accountContext=accountContext;
		changeChatRoom(room);
		setListener(listener);
	}
	public void sendMessage(String message){
		room.sendMessage(accountContext.getPlayer().getMemento(), message);
	}
	public void changeChatRoom(ChatRoom newRoom){
		this.room=newRoom;
	}
	public void setListener(ChatListener newListener){
		if(!room.canSubscribeListener(accountContext.getPlayer()))
			throw new IllegalArgumentException("invalid listener");
		this.listener=newListener;
		room.subscribe(listener);
	}
}
