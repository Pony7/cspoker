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
package org.cspoker.server.game.session;

import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    public final static SessionManager global_session_manager = new SessionManager();

    private ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String,Session>();

    public Session getSession(String username) {
	Session newSession = new Session(username);
	Session oldSession = sessions.putIfAbsent(username, newSession);
	if(oldSession==null)
	    return newSession;
	return oldSession;
    }
    
    public void killSession(String username){
	Session s = getSession(username);
	s.kill();
	sessions.remove(s.getUserName());
    }

}
