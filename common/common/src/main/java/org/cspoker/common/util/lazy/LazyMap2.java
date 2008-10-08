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
package org.cspoker.common.util.lazy;

import java.util.concurrent.ConcurrentHashMap;


public class LazyMap2<K,V,E extends Throwable, F extends Throwable> {

	private static final long serialVersionUID = -7366678021380297939L;

	private final ConcurrentHashMap<K, IWrapper2<V,E,F>> wrappedMap = new ConcurrentHashMap<K, IWrapper2<V,E,F>>();
	
	public V getOrCreate(K key, final IFactory2<? extends V,? extends E, ? extends F> factory) throws E,F {
		IWrapper2<V, E, F> wrapper = wrappedMap.get(key);
		if(wrapper!=null){
			return wrapper.getContent();
		}
		wrappedMap.putIfAbsent(key, new IWrapper2<V,E,F>(){

			private V content = null;
			
			public synchronized V getContent() throws E,F {
				if(content == null){
					content = factory.create();
				}
				return content;
			}
			
		});
		return wrappedMap.get(key).getContent();
	}

	public void remove(Object key) {
		wrappedMap.remove(key);
	}

}
