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
package org.cspoker.server.rmi.context;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.cspoker.common.api.chat.ChatContext;
import org.cspoker.common.api.chat.DelegatingChatContext;
import org.cspoker.common.api.chat.event.RemoteChatListener;
import org.cspoker.server.rmi.listener.AsynchronousChatListener;

public class AsynchronousChatContext extends DelegatingChatContext {
	
	protected ConcurrentHashMap<RemoteChatListener, AsynchronousChatListener> wrappers = 
		new ConcurrentHashMap<RemoteChatListener, AsynchronousChatListener>();
	protected Executor executor;
	private AsynchronousServerContext asynchronousServerContext;
	
	public AsynchronousChatContext(AsynchronousServerContext asynchronousServerContext, Executor executor, ChatContext chatContext) {
		super(chatContext);
		this.asynchronousServerContext = asynchronousServerContext;
		this.executor = executor;
	}
	
	@Override
	public void subscribe(RemoteChatListener chatListener) {
		AsynchronousChatListener wrapper = new AsynchronousChatListener(asynchronousServerContext, executor, chatListener);
		if(wrappers.putIfAbsent(chatListener, wrapper)==null){
			super.subscribe(wrapper);
		}
		
	}
	
	@Override
	public void unSubscribe(RemoteChatListener chatListener) {
		AsynchronousChatListener wrapper = wrappers.remove(chatListener);
		if(wrapper!=null){
			super.unSubscribe(wrapper);
		}
	}
	
}
