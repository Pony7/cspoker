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
package org.cspoker.client.common;

import org.cspoker.common.api.chat.context.ForwardingRemoteChatContext;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.chat.listener.ForwardingChatListener;

public class SmartChatContext extends ForwardingRemoteChatContext {

	private ForwardingChatListener chatListener;

	public SmartChatContext(RemoteChatContext remoteChatContext, ForwardingChatListener chatListener) {
		super(remoteChatContext);
		this.chatListener = chatListener;
	}
	
	public void subscribe(ChatListener listener){
		chatListener.subscribe(listener);
	}
	
	public void unSubscribe(ChatListener listener){
		chatListener.unSubscribe(listener);
	}

}
