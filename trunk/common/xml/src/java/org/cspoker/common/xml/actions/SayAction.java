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
package org.cspoker.common.xml.actions;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.xml.eventlisteners.invocation.AllInvocationEventsListener;
import org.cspoker.common.xml.events.invocation.SuccessfulInvocationEvent;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SayAction extends PlayerCommunicationAction {

	private static final long serialVersionUID = -8892178534623422101L;

	private String msg;

	public SayAction(long id, String msg) {
		super(id);
		this.msg = msg;
	}

	protected SayAction() {
		// no op
	}

	@Override
	public void perform(PlayerCommunication pc,
			AllInvocationEventsListener listener) {
		pc.say(msg);
		listener.onSuccessfullInvokation(new SuccessfulInvocationEvent<Void>(
				this, null));
	}

}
