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
package org.cspoker.common;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.cspoker.common.actions.ActionJAXBContext;
import org.cspoker.common.events.EventJAXBContext;

public class EventAndActionJAXBContext {

	private final static Logger logger = Logger.getLogger(EventAndActionJAXBContext.class);

	public final static JAXBContext context = initContext();

    private static JAXBContext initContext() {
        try {
			return JAXBContext.newInstance(getActions());
		} catch (JAXBException e) {
			logger.fatal(e);
			throw new IllegalStateException(e);
		}
    }
    
    public static Class[] getActions(){
    	ArrayList<Class> l = new ArrayList<Class>();
    	l.addAll(Arrays.asList(ActionJAXBContext.getActions()));
    	l.addAll(Arrays.asList(EventJAXBContext.getActions()));
    	return l.toArray(new Class[l.size()]);
    }
	
}
