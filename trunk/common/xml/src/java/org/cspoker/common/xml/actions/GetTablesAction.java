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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.PlayerCommunication;
import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableList;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.xml.eventlisteners.invocation.AllInvocationEventsListener;
import org.cspoker.common.xml.events.invocation.IllegalActionEvent;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GetTablesAction extends PlayerCommunicationAction<TableList> {

	private static final long serialVersionUID = 7897218843022885169L;

	public GetTablesAction(long id) {
		super(id);
	}

	protected GetTablesAction() {
		// no op
	}

	@Override
	public void perform(PlayerCommunication pc,
			AllInvocationEventsListener listener) {
		try {
			dispatchResult(pc.getTables(), listener);
		} catch (IllegalActionException e) {
			listener.onIllegalAction(new IllegalActionEvent(e, this));
		}
	}

}
