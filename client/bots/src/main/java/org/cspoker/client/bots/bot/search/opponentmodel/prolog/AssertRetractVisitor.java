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

public class AssertRetractVisitor extends ToPrologVisitor {
	
	private final static Logger logger = Logger.getLogger(AssertRetractVisitor.class);

	private StringBuilder assertions = null;
	private StringBuilder retractions = null;
	
	public AssertRetractVisitor() {
		super();
	}
	
	public AssertRetractVisitor(ToPrologVisitor root) {
		super(root);
	}

	@Override
	protected void addFact(String fact) {
		String assertfact = "assert("+fact+")";
		if(assertions==null){
			assertions = new StringBuilder(assertfact);
		}else{
			assertions.append(", "+assertfact);
		}
		
		String retractfact = "retract("+fact+")";
		if(retractions==null){
			retractions = new StringBuilder(retractfact);
		}else{
			retractions.append(", "+retractfact);
		}
	}
	
	public String wrapGoal(String goal) {
		return assertions.toString()+", "+goal+", "+retractions.toString();
	}

}
