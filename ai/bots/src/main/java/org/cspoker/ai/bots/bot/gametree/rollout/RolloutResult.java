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
package org.cspoker.ai.bots.bot.gametree.rollout;

import java.util.Collections;
import java.util.SortedMap;
import java.util.Map.Entry;

import org.cspoker.common.util.MutableDouble;

public class RolloutResult {
	
	public final SortedMap<Integer, MutableDouble> values;
	public final double totalProb;

	public RolloutResult(SortedMap<Integer, MutableDouble> values, double totalProb) {
		this.values = Collections.unmodifiableSortedMap(values);
		this.totalProb = totalProb;
	}
	
	public double getMean() {
		double mean = 0;
		for (Entry<Integer, MutableDouble> entry : values.entrySet()) {
			mean += entry.getKey() * entry.getValue().getValue();
		}
		mean /= totalProb;
		return mean;
	}

	public double getVariance(double mean, int nbSamples) {
		double var = 0;
		for (Entry<Integer, MutableDouble> entry : values.entrySet()) {
			double diff = mean - entry.getKey();
			var += diff * diff * entry.getValue().getValue();
		}
		var /= totalProb;
		// use sample variance because variance of samples is smaller than real
		// variance
		var *=  nbSamples / (double)(nbSamples - 1);
		return var;
	}
	
}
