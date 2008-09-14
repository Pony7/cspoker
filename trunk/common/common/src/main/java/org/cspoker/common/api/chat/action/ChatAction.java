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
package org.cspoker.common.api.chat.action;

import org.cspoker.common.api.chat.ChatContext;
import org.cspoker.common.api.chat.event.ChatListener;
import org.cspoker.common.api.shared.ServerContext;
import org.cspoker.common.api.shared.action.Action;
import org.cspoker.common.api.shared.event.ServerListener;

public abstract class ChatAction<T> extends Action<T> {

	private static final long serialVersionUID = 6542312781797096164L;

	public ChatAction(long id) {
		super(id);
	}

	protected ChatAction() {
		// no op
	}

	@Override
	public void perform(ServerContext serverContext, ServerListener listener) {
		perform(serverContext.getChatContext(), listener.getChatListener());
	}

	public abstract void perform(ChatContext chatContext,
			ChatListener chatListener);
	
}
