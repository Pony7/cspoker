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


public class SendMessageAction extends ChatAction<Void> {

	private static final long serialVersionUID = -4885167132627383948L;

	private String message;

	public SendMessageAction(long id, String msg) {
		super(id);
		this.message = msg;
	}

	protected SendMessageAction() {
		// no op
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public Void perform(ChatContext chatContext) {
		chatContext.sendMessage(getMessage());
		return null;
	}

}
