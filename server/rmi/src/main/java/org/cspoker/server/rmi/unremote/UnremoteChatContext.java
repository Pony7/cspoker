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
package org.cspoker.server.rmi.unremote;

import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.chat.context.ForwardingChatContext;
import org.cspoker.common.api.chat.event.ServerMessageEvent;
import org.cspoker.common.api.chat.event.TableMessageEvent;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.chat.listener.RemoteChatListener;
import org.cspoker.common.api.shared.Killable;
import org.cspoker.common.api.shared.listener.ForwardingListener;

public class UnremoteChatContext extends ForwardingChatContext implements
ChatContext {

	private final Killable connection;
	private UnremoteChatListener remoteListener;

	public UnremoteChatContext(Killable connection, ChatContext chatContext) {
		super(chatContext);
		this.connection = connection;
	}

	@Override
	protected ChatListener wrapListener(ChatListener listener) {
		remoteListener = new UnremoteChatListener();
		remoteListener.subscribe(listener);
		return remoteListener;
	}

	public void subscribe(RemoteChatListener chatListener) {
		remoteListener.subscribe(chatListener);
	}

	public void unSubscribe(RemoteChatListener chatListener) {
		remoteListener.unSubscribe(chatListener);
	}

	public class UnremoteChatListener extends ForwardingListener<RemoteChatListener> implements ChatListener{

		public void onServerMessage(ServerMessageEvent serverMessageEvent){
			try {
				for (RemoteChatListener listener : listeners) {
					listener.onServerMessage(serverMessageEvent);
				}
			} catch (Exception exception) {
				connection.kill();
			}
		}

		public void onTableMessage(TableMessageEvent tableMessageEvent) {			
			try {
				for (RemoteChatListener listener : listeners) {
					listener.onTableMessage(tableMessageEvent);
				}
			} catch (Exception exception) {
				connection.kill();
			}
		}

	}

}
