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

package org.cspoker.common.elements.chips;

import java.io.Serializable;
import java.util.List;

import net.jcip.annotations.Immutable;

@Immutable
public class Pots implements Serializable {

	private static final long serialVersionUID = 2133563839323145402L;

	private final int totalValue;
	
	private final List<Pot> pots;
	
	public Pots(List<Pot> pots, int totalValue){
		this.pots = pots;
		this.totalValue = totalValue;
	}
	

	protected Pots() {
		this.pots = null;
		totalValue = 0;
	}

	public Pot getActivePot() {
		return pots.get(0);
	}

	public List<Pot> getAllPots() {
		return pots;
	}

	public int getTotalValue() {
		return totalValue;
	}
	
	public String toString(){
		return "pots:" + pots.toString()+" total value:"+totalValue+ " chips";
	}

}
