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
package org.cspoker.server.common.game.session;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.cspoker.common.player.PlayerId;
import org.cspoker.server.common.util.threading.ScheduledRequestExecutor;

public class SessionManager {

	public final static SessionManager global_session_manager = new SessionManager();

	private ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	private ConcurrentHashMap<PlayerId, Session> sessionByID = new ConcurrentHashMap<PlayerId, Session>();

	public Session getSession(String username) {
		Session newSession = new Session(username);
		Session oldSession = sessions.putIfAbsent(username, newSession);
		if (oldSession == null) {
			try {
				sessionByID.put(newSession.getPlayer().getId(), newSession);
			} catch (PlayerKilledExcepion e) {
				// no op
			}
			submitTimeOutHandler(newSession);
			return newSession;
		}
		return oldSession;
	}

	public void killSession(String username) {
		Session s = getSession(username);
		if(s!=null){
			PlayerId id = null;
			try {
				id = s.getPlayer().getId();
			} catch (PlayerKilledExcepion e) {
				// no op
				// ID will be removed elsewhere?
			}
			s.kill();
			sessions.remove(s.getUserName());
			if (id != null) {
				sessionByID.remove(id);
			}
		}
	}
	
	public void killSession(Session session) {
		try {
			PlayerId id = session.getPlayer().getId();
			session.kill();
			sessions.remove(session.getUserName());
			sessionByID.remove(id);
		} catch (PlayerKilledExcepion e) {
		}
	}
	
	private void submitTimeOutHandler(final Session session){
		System.out.println("submit "+session.toString());
		ScheduledRequestExecutor.getInstance().scheduleWithFixedDelay(new Runnable(){

			@Override
			public void run(){
				try {
					System.out.println("called "+session.toString());
					if(!session.getPlayerCommunication().isActive()){
						System.out.println("kill session "+session.toString());
						killSession(session);
						throw new RejectedExecutionException();
					}
				} catch (PlayerKilledExcepion e) {
					throw new RejectedExecutionException();
				}
			}
			
		}, 20, 20, TimeUnit.MINUTES);
		}
	
	public Session getSession(PlayerId id) {
		return sessionByID.get(id);
	}

}
