package org.cspoker.client.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.cspoker.common.api.chat.context.ForwardingRemoteChatContext;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;

public class ChatContextStub extends ForwardingRemoteChatContext{

	public ChatContextStub(RemoteChatContext chatContext)
			throws RemoteException {
		super(chatContext);
	}
	
	@Override
	public ChatListener wrapListener(ChatListener listener) throws RemoteException {
		return (ChatListener) UnicastRemoteObject.exportObject(listener, 0);
	}

}
