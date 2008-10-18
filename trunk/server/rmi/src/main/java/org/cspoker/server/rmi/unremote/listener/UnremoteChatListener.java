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
package org.cspoker.server.rmi.unremote.listener;

import org.apache.log4j.Logger;
import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.chat.listener.ChatListener;
import org.cspoker.common.api.chat.listener.RemoteChatListener;
import org.cspoker.common.api.shared.Trigger;
import org.cspoker.common.api.shared.listener.ForwardingListener;
import org.cspoker.server.rmi.RunRMIServer;

public class UnremoteChatListener extends ForwardingListener<RemoteChatListener> implements ChatListener{

	private final static Logger logger = Logger.getLogger(UnremoteChatListener.class);
	
	private final Trigger connection;

	public UnremoteChatListener(Trigger connection, RemoteChatListener chatListener) {
		super(chatListener);
		this.connection = connection;
	}
	
	public void onMessage(MessageEvent messageEvent){
		try {
			for (RemoteChatListener listener : listeners) {
				listener.onMessage(messageEvent);
			}
		} catch (Exception exception) {
			logger.debug("Caught exception from Remote listener", exception);
			connection.trigger();
		}
	}
}
