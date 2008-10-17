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

import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.api.shared.context.StaticServerContext;

@XmlRootElement
public class SendTableMessageAction extends SendMessageAction {
	
	private static final long serialVersionUID = -1855545270029265281L;
	
	private long tableID;

	public SendTableMessageAction(long id, String message, long tableID) {
		super(id,message);
		this.tableID = tableID;
	}

	protected SendTableMessageAction() {
		// no op
	}
	
	@Override
	public Void perform(StaticServerContext serverContext){
		return perform(serverContext.getTableChatContext(tableID));
	}

}
