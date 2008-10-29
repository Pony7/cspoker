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

import org.cspoker.common.api.chat.context.ChatContext;
import org.cspoker.common.api.shared.context.StaticServerContext;

public abstract class SendMessageAction extends ChatAction<Void> {

	private static final long serialVersionUID = -7513936752700505929L;
	
	private String message;

	public SendMessageAction(long id, String message) {
		super(id);
		this.message = message;
	}

	protected SendMessageAction() {
		// no op
	}
	
	@Override
	public abstract Void perform(StaticServerContext serverContext);

	@Override
	public Void perform(ChatContext chatContext) {
		chatContext.sendMessage(message);
		return null;
	}
}
