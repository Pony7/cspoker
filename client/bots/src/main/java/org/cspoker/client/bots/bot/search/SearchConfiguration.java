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
package org.cspoker.client.bots.bot.search;

import org.cspoker.client.bots.bot.search.node.leaf.ShowdownNode;
import org.cspoker.client.bots.bot.search.opponentmodel.AllPlayersModel;

public class SearchConfiguration {
	
	private final AllPlayersModel opponentModeler;
	private final ShowdownNode.Factory showdownNodeFactory;
	private final int flopTokens;
	private final int turnTokens;
	private final int finalTokens;
	
	public SearchConfiguration(AllPlayersModel opponentModel, ShowdownNode.Factory showdownNodeFactory,
			int flopTokens,
			int turnTokens,
			int finalTokens) {
		this.opponentModeler = opponentModel;
		this.showdownNodeFactory = showdownNodeFactory;
		this.flopTokens=flopTokens;
		this.turnTokens=turnTokens;
		this.finalTokens=finalTokens;
	}
	
	public AllPlayersModel getOpponentModeler() {
		return opponentModeler;
	}
	
	public ShowdownNode.Factory getShowdownNodeFactory() {
		return showdownNodeFactory;
	}
	
	public int getFinalTokens() {
		return finalTokens;
	}
	
	public int getFlopTokens() {
		return flopTokens;
	}
	
	public int getTurnTokens() {
		return turnTokens;
	}
	
}
