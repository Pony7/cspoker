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
package org.cspoker.common.api.shared.socket;

import javax.xml.bind.annotation.XmlRootElement;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.context.StaticServerContext;
import org.cspoker.common.api.shared.event.EventId;
import org.cspoker.common.api.shared.exception.IllegalActionException;

@XmlRootElement
@Immutable
public class LoginAction extends DispatchableAction<Void> {

	private static final long serialVersionUID = -2677247916335298486L;
	
	private final String username;
	private final String passwordHash;

	public LoginAction(EventId id, String username, String passwordHash){
		super(id);
		this.username = username;
		this.passwordHash = passwordHash;
	}
	
	protected LoginAction() {
		username = null;
		passwordHash = null;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPasswordHash() {
		return this.passwordHash;
	}

	@Override
	public Void perform(StaticServerContext serverContext)
			throws IllegalActionException {
		return null;
	}

}
