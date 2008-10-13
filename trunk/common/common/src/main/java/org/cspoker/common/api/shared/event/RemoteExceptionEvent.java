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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cspoker.common.api.shared.action.DispatchableAction;

@XmlRootElement
public class RemoteExceptionEvent<T> extends ActionEvent<T> {

        private static final long serialVersionUID = 8350435427841245148L;

    	@XmlTransient
        private RemoteException exception;

		private String message = null;

        public RemoteExceptionEvent(DispatchableAction<T> action,RemoteException exception) {
                super(action);
                this.exception = exception;
        		this.message = exception.getMessage();
        }
        
        public RemoteExceptionEvent(DispatchableAction<T> action, String message) {
            super(action);
    		this.message = message;
        }

        protected RemoteExceptionEvent() {
                // no op
        }

    	public RemoteException getException() {
    		if (exception == null) {
    			exception = new RemoteException(message);
    		}
    		return exception;
    	}

    	public String getMsg() {
    		return message;
    	}
        
        @Override
        public T getResult() throws RemoteException {
        	throw getException();
        }
        
}
