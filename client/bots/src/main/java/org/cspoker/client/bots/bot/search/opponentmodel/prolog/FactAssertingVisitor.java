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

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.GameState;

import com.declarativa.interprolog.PrologEngine;

public class FactAssertingVisitor extends ToPrologVisitor {
	
	private final static Logger logger = Logger.getLogger(FactAssertingVisitor.class);

	private StringBuilder query = null;
	private final PrologEngine prologEngine;
	
	public FactAssertingVisitor(PrologEngine prologEngine) {
		this.prologEngine = prologEngine;
	}
	
	@Override
	protected void addFact(String fact) {
		fact = "assert("+fact+")";
		if(query==null){
			query = new StringBuilder(fact);
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
