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
package org.cspoker.client.xml.common.listener;

import net.jcip.annotations.ThreadSafe;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListenerTree;

@ThreadSafe
public class XmlHoldemTableListenerTree implements HoldemTableListenerTree {

	private volatile HoldemPlayerListener holdemPlayerListener;
	private volatile HoldemTableListener holdemTableListener;

	public HoldemPlayerListener getHoldemPlayerListener() {
		return holdemPlayerListener;
	}
	
	public void setHoldemPlayerListener(
			HoldemPlayerListener holdemPlayerListener) {
		this.holdemPlayerListener = holdemPlayerListener;
	}

	public HoldemTableListener getHoldemTableListener() {
		return holdemTableListener;
	}
	
	public void setHoldemTableListener(HoldemTableListener holdemTableListener) {
		this.holdemTableListener = holdemTableListener;
	}

}
