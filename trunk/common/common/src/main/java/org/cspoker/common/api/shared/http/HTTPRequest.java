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
package org.cspoker.common.api.shared.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.jcip.annotations.Immutable;

import org.cspoker.common.api.shared.action.DispatchableAction;
import org.cspoker.common.api.shared.context.StaticServerContext;
import org.cspoker.common.api.shared.event.ServerEvent;

@XmlRootElement
@Immutable
public class HTTPRequest {

	@XmlElement(required=true,nillable=false) 
	private final List<DispatchableAction<?>> actions = Collections.synchronizedList(new ArrayList<DispatchableAction<?>>());

	public HTTPRequest(Queue<DispatchableAction<?>> queue) {
		DispatchableAction<?> action;
		while((action = queue.poll())!=null){
			actions.add(action);
		}
	}
	protected HTTPRequest() {
		// no op
	}
	
	public void addAction(DispatchableAction<?> action){
		actions.add(action);
	}
	
	public List<DispatchableAction<?>> getActions() {
		return Collections.unmodifiableList(this.actions);
	}
	
	public HTTPResponse performRequest(StaticServerContext serverContext, Queue<ServerEvent> events){
		HTTPResponse response = new HTTPResponse();
		for (DispatchableAction<?> action : actions) {
			response.addActionResult(action.wrappedPerform(serverContext));
		}
		response.addEvents(events);
		return response;
	}
	
}
