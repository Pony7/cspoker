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
package org.cspoker.common.util;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.cspoker.common.api.lobby.holdemtable.action.SitInAnywhereAction;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEventWrapper;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.action.BetOrRaiseAction;
import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.event.ActionPerformedEvent;
import org.cspoker.common.api.shared.event.EventId;
import org.cspoker.common.api.shared.event.ServerEvent;
import org.cspoker.common.api.shared.http.HTTPRequest;
import org.cspoker.common.api.shared.http.HTTPResponse;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.jaxbcontext.AllJAXBContexts;

/**
 * Generates example instances of the XML schema.
 * 
 * @author guy
 */
public class SchemaInstance {

	public static void main(String[] args) throws JAXBException {
		File baseDir = new File(new File("schema"),"instances");
		baseDir.mkdirs();
		
		File output = new File(baseDir,"http.xml");
		Marshaller marschaller = AllJAXBContexts.context.createMarshaller();
		marschaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marschaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

		EventId eid1 = new EventId(42);
		EventId eid2 = new EventId(43);
		
		TableId tid = new TableId(1337);
		
		Queue<DispatchableAction<?>> queue = new LinkedList<DispatchableAction<?>>();
		SitInAnywhereAction action1 = new SitInAnywhereAction(eid1,tid,6500);
		queue.add(action1);
		BetOrRaiseAction action2 = new BetOrRaiseAction(eid2,tid,25);
		queue.add(action2);
		
		HTTPRequest request =  new HTTPRequest(queue);
		
		marschaller.marshal(request, output);
		
		HTTPResponse response = new HTTPResponse();
		response.addActionResult(new ActionPerformedEvent<Void>(action1,null));
		response.addActionResult(new ActionPerformedEvent<Void>(action2,null));
		
		Queue<ServerEvent> equeue = new LinkedList<ServerEvent>();
		equeue.add(new HoldemTableTreeEventWrapper(tid, new BetEvent(new PlayerId(5),25)));
		response.addEvents(equeue);
		
		marschaller.marshal(response, output);

		output = new File(baseDir,"http.xml");
		
		
		System.out.println("done");
	}

}
