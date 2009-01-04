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
package org.cspoker.common.handeval.spears;

public class SevenCardEvaluator implements HandEvaluatorSpears {
	private static SixCardEvaluator sixCardEvaluator;
	private static final int[][] choose6From7 = new int[][] {
			{0, 1, 2, 3, 4, 5},
			{0, 1, 2, 3, 4, 6},
			{0, 1, 2, 3, 5, 6},
			{0, 1, 2, 4, 5, 6},
			{0, 1, 3, 4, 5, 6},
			{0, 2, 3, 4, 5, 6},
			{1, 2, 3, 4, 5, 6}
	};
	private Card[] sixCards = new Card[6];
	
	public SevenCardEvaluator()  {
		sixCardEvaluator = new SixCardEvaluator();
	}
	
	public int evaluate(Card[] sevenCards)  {
		int minRank = Integer.MAX_VALUE;
		
		for (int[] c67 : choose6From7) {
			for (int i = 0; i < 6; i++) {
				sixCards[i] = sevenCards[c67[i]];
			}
			int rank = sixCardEvaluator.evaluate(sixCards);
			minRank = Math.min(rank, minRank);	
		}
		return minRank;
	}
	
}
