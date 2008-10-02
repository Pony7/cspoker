package org.cspoker.server.common;

import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.login.LoginException;

import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.common.util.Wrapper;

public class CSPokerServerImpl implements CSPokerServer {

	private final ConcurrentHashMap<String, Wrapper<ServerContext,LoginException>> contexts = new ConcurrentHashMap<String, Wrapper<ServerContext,LoginException>>();

	public ServerContext login(final String username, final String password)
			throws LoginException {
		contexts.putIfAbsent(username, new Wrapper<ServerContext,LoginException>(){
			
			private ServerContext context=null;
			
			public synchronized ServerContext getContent() throws LoginException {
				if(context==null){
					context = new ServerContextImpl(username,password);
				}
				return context;
			}
		});
		return contexts.get(username).getContent();
	}

}
