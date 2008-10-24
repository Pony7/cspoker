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
	
	@Override
	public String toString() {
		return "Embedded server";
	}

}
