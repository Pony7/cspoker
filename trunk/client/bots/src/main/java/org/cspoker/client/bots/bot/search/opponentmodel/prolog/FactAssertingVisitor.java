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
package org.cspoker.client.bots.bot.search.opponentmodel.prolog;

import jp.ac.kobe_u.cs.prolog.builtin.PRED_assert_1;
import jp.ac.kobe_u.cs.prolog.lang.Predicate;
import jp.ac.kobe_u.cs.prolog.lang.StructureTerm;

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.GameState;

import com.declarativa.interprolog.PrologEngine;

public class FactAssertingVisitor extends ToPrologTermVisitor {
	
	private final static Logger logger = Logger.getLogger(FactAssertingVisitor.class);

	private StringBuilder query = null;
	private final PrologEngine prologEngine;
	
	public FactAssertingVisitor(PrologEngine prologEngine) {
		this.prologEngine = prologEngine;
	}
	
	@Override
	protected void addTerm(StructureTerm term) {
		Predicate fact = new PRED_assert_1(term,null);
		if(query==null){
			query = new StringBuilder(fact.toString());
		}else{
			query.append(", "+fact);
		}
	}
	
	@Override
	public void readHistory(GameState gameState) {
		super.readHistory(gameState);
		if (query!=null) {
			logger.debug("Deterministic Prolog Goal: " + query);
			if (!prologEngine.deterministicGoal(query.toString())) {
				throw new IllegalStateException(
						"Could not assert game state in Prolog: " + query);
			}
			query = null;
		}
	}

}
