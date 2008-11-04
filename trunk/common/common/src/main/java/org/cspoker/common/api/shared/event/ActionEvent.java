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
package org.cspoker.common.api.shared.event;

import java.rmi.RemoteException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.exception.IllegalActionException;

public abstract class ActionEvent<T> implements Event{

        private static final long serialVersionUID = -3647664952883269411L;

        @XmlTransient
        private final DispatchableAction<T> action;

    	@XmlAttribute
        private final EventId id;

        public ActionEvent(DispatchableAction<T> action) {
                this.action = action;
                this.id = action.getID();
        }
        
        protected ActionEvent() {
			action = null;
			id = null;
		}

        public DispatchableAction<T> getAction() {
                return action;
        }
        
        public EventId getID(){
        	return id;
        }
        
        public abstract T getResult() throws IllegalActionException, RemoteException;
}

