package org.cspoker.server.common;

import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.server.common.account.ExtendedAccountContext;

public class ChatContextImpl implements ChatContext {

	public ChatContextImpl(ExtendedAccountContext accountContext) {
	}

	public void sendServerMessage(String message) {
	}

	public void sendTableMessage(long tableId, String message) {
	}

	public void subscribe(ChatListener chatListener) {
	}

	public void unSubscribe(ChatListener chatListener) {
	}

}
