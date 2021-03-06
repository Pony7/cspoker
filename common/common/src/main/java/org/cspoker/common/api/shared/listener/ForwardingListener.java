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
package org.cspoker.common.api.shared.listener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ForwardingListener<T extends EventListener> {

	protected final List<T> listeners;

	public ForwardingListener() {
		this(new CopyOnWriteArrayList<T>());
	}
	
	public ForwardingListener(T listener) {
		this();
		subscribe(listener);
	}
	
	public ForwardingListener(List<T> listeners) {
		this.listeners = listeners;
	}
	
	
	public void subscribe(T listener){
		listeners.add(listener);
	}
	
	public void unSubscribe(T listener){
		listeners.remove(listener);
	}
	
}
