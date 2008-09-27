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

package org.cspoker.common.api.lobby.holdemtable.event;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.player.SeatedPlayer;

/**
 * A class to represent new deal events.
 * 
 * @author Kenzo
 * 
 */
@XmlRootElement
public class NewDealEvent extends HoldemTableEvent {

	private static final long serialVersionUID = 8048593844056212117L;

	private List<SeatedPlayer> players;

	private SeatedPlayer dealer;

	public NewDealEvent(List<SeatedPlayer> players, SeatedPlayer dealer) {
		this.players = Collections.unmodifiableList(players);
		this.dealer = dealer;
	}

	protected NewDealEvent() {
		// no op
	}

	public SeatedPlayer getDealer() {
		return dealer;
	}

	public String toString() {
		String toReturn = "A new deal with ";
		for (SeatedPlayer player : players) {
			toReturn += player.getName();
			toReturn += " (";
			toReturn += player.getStackValue();
			toReturn += " chips), ";
		}
		return toReturn.substring(0, toReturn.length() - 2)
				+ " as initial players of this table. " + dealer.getName()
				+ " is dealer.";
	}
	
	public void dispatch(HoldemTableListener holdemTableListener) {
		holdemTableListener.onNewDeal(this);
	}

}
