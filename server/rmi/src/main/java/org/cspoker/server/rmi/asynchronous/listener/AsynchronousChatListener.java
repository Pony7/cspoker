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
package org.cspoker.server.rmi.asynchronous.listener;

import java.rmi.RemoteException;
import java.util.concurrent.Executor;

import org.cspoker.common.api.chat.event.ChatListener;
import org.cspoker.common.api.chat.event.RemoteChatListener;
import org.cspoker.common.api.chat.event.ServerMessageEvent;
import org.cspoker.common.api.chat.event.TableMessageEvent;
import org.cspoker.common.api.shared.Killable;

public class AsynchronousChatListener extends AsynchronousListener implements ChatListener{

	private final RemoteChatListener chatListener;
	private Executor executor;

	public AsynchronousChatListener(Killable connection, Executor executor, RemoteChatListener chatListener) {
		super(connection, executor);
		this.chatListener = chatListener;
	}

	public RemoteChatListener getChatListener() {
		return chatListener;
	}

	public void onServerMessage(final ServerMessageEvent serverMessageEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					chatListener.onServerMessage(serverMessageEvent);
				} catch (RemoteException exception) {
					
				}
			}
		});
	}

	public void onTableMessage(final TableMessageEvent tableMessageEvent) {
		executor.execute(new Runnable() {
			public void run() {
				try {
					chatListener.onTableMessage(tableMessageEvent);
				} catch (RemoteException exception) {
					die();
				}
			}
		});
	}

}
