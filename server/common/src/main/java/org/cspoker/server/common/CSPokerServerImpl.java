package org.cspoker.server.common;

import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.cspoker.common.CSPokerServer;
import org.cspoker.common.api.shared.context.ServerContext;
import org.cspoker.common.util.lazy.IWrapper1;
import org.cspoker.server.common.authentication.XmlFileAuthenticator;

public class CSPokerServerImpl implements CSPokerServer {

	private static Logger logger = Logger.getLogger(XmlFileAuthenticator.class);
	
	private final ConcurrentHashMap<String, IWrapper1<ServerContext,LoginException>> contexts = new ConcurrentHashMap<String, IWrapper1<ServerContext,LoginException>>();

	public ServerContext login(final String username, final String password)
			throws LoginException {
		contexts.putIfAbsent(username, new IWrapper1<ServerContext,LoginException>(){
			
			private ServerContext context=null;
			
			public synchronized ServerContext getContent() throws LoginException {
				if(context==null){
					context = new ServerContextImpl(username,password);
					logger.debug("Succesful login by "+username);
				}
				return context;
			}
		});
		return contexts.get(username).getContent();
	}

}
