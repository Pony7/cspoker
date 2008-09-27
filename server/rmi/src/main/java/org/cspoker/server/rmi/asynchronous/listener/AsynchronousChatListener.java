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

import java.util.concurrent.Executor;

import org.cspoker.common.api.chat.event.ServerMessageEvent;
import org.cspoker.common.api.chat.event.TableMessageEvent;
import org.cspoker.common.api.chat.listener.ChatListener;

public class AsynchronousChatListener implements ChatListener{

	private final ChatListener chatListener;
	private Executor executor;

	public AsynchronousChatListener(Executor executor, ChatListener chatListener) {
		this.executor = executor;
		this.chatListener = chatListener;
	}

	public void onServerMessage(final ServerMessageEvent serverMessageEvent) {
		executor.execute(new Runnable() {
			public void run() {
				chatListener.onServerMessage(serverMessageEvent);
			}
		});
	}

	public void onTableMessage(final TableMessageEvent tableMessageEvent) {
		executor.execute(new Runnable() {
			public void run() {
				chatListener.onTableMessage(tableMessageEvent);
			}
		});
	}

}
