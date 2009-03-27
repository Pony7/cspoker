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

import org.cspoker.common.util.Pair;

public class EvaluatedAction<A extends ActionWrapper> implements ActionWrapper {

	private final A action;
	private final double EV;
	private final double varEV;

	public EvaluatedAction(A action, Pair<Double, Double> EV) {
		this(action, EV.getLeft(), EV.getRight());
	}

	public EvaluatedAction(A action, double EV, double VarEV) {
		this.action = action;
		this.EV = EV;
		this.varEV = Math.max(0, VarEV);
	}

	public A getEvaluatedAction() {
		return action;
	}

	public SearchBotAction getAction() {
		return action.getAction();
	}

	public double getEV() {
		return EV;
	}

	public double getVarEV() {
		return varEV;
	}

	public double getDiscountedEV(double discount) {
		return EV - discount * Math.sqrt(varEV);
	}

	@Override
	public String toString() {
		return "EV is " + Math.round(EV) + " for " + action.toString() + " (σ="
				+ Math.round(Math.sqrt(varEV)) + ")";
	}

}
