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
package org.cspoker.common.api.account.action;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.account.context.AccountContext;
import org.cspoker.common.api.shared.event.EventId;

@XmlRootElement
@Immutable
public class HasPasswordAction extends AccountAction<Boolean> {

	private static final long serialVersionUID = -1707794253164954354L;
	
	@XmlAttribute
	private final String passwordHash;
	
	public HasPasswordAction(EventId id, String passwordHash) {
		super(id);
		this.passwordHash = passwordHash;
	}

	protected HasPasswordAction() {
		passwordHash = null;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	@Override
	public Boolean perform(AccountContext accountContext) {
		return accountContext.hasPassword(passwordHash);
	}

}
