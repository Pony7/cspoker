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
package org.cspoker.client.bots.bot.search.action;

public class SampledAction implements ActionWrapper {

	private final ProbabilityAction action;
	private final int times;
	private final int outof;

	public SampledAction(ProbabilityAction action, int times, int outof) {
		this.action = action;
		this.times = times;
		this.outof = outof;
	}

	public ProbabilityAction getProbabilityAction() {
		return action;
	}

	public SearchBotAction getAction() {
		return action.getAction();
	}

	public int getTimes() {
		return times;
	}

	public int getOutof() {
		return outof;
	}

	@Override
	public String toString() {
		return action.toString() + " sampled " + times + "/" + outof;
	}
}
