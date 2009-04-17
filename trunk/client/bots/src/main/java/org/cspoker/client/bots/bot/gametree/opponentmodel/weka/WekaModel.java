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
package org.cspoker.client.bots.bot.gametree.opponentmodel.weka;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.gametree.opponentmodel.OpponentModel;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.common.elements.player.PlayerId;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public abstract class WekaModel implements OpponentModel{

	protected static final Logger logger = Logger.getLogger(WekaClassificationModel.class);
	protected final PlayerTrackingVisitor visitor;
	protected final Instances callRaiseDataSet;
	protected final Instances checkBetDataSet;
	private final Deque<PlayerTrackingVisitor> visitors = new ArrayDeque<PlayerTrackingVisitor>();

	public WekaModel() {
		this.visitor = new PlayerTrackingVisitor();
		try {
			this.callRaiseDataSet = new DataSource(
					getClass().
					getClassLoader().
					getResourceAsStream("org/cspoker/client/bots/bot/search/opponentmodel/weka/CallRaiseActionHeader.arff")
			).getStructure();
			callRaiseDataSet.setClassIndex(28);
			this.checkBetDataSet = new DataSource(
					getClass().
					getClassLoader().
					getResourceAsStream("org/cspoker/client/bots/bot/search/opponentmodel/weka/CheckBetActionHeader.arff")
			).getStructure();
			checkBetDataSet.setClassIndex(22);
		} catch (Exception e) {
			//stupid Weka
			throw new IllegalStateException(e);
		}
	}
	
	protected Instance getCheckBetInstance(PlayerId actor) {
		Propositionalizer prop = getTopVisitor().getPropz();
		PlayerData p = prop.getPlayers().get(actor);
		Instance instance = new Instance(23);
		instance.setDataset(checkBetDataSet);
		// Timing
		instance.setValue(0,prop.getRoundCompletion());
		instance.setValue(1,prop.getPlayersActed());
		instance.setValue(2,prop.getPlayersToAct());
		instance.setValue(3,prop.getRound());
		instance.setValue(4,p.getGameCount());
		instance.setValue(5,prop.isSomebodyActedThisRound()+"");
		// Amounts
		instance.setValue(6,prop.getPotSize());
		instance.setValue(7,p.getStackSize(prop));
		// Player count
		instance.setValue(8,prop.getNbSeatedPlayers());
		instance.setValue(9,prop.getNbActivePlayers() );
		instance.setValue(10,prop.getActivePlayerRatio());
		// Global player frequencies
		instance.setValue(11,p.getBetFrequency());
		// Per-round player frequencies
		instance.setValue(12,p.getRoundBetFrequency(prop));
		// Game betting behaviour
		instance.setValue(13,p.isComitted()+"");
		instance.setValue(14,prop.getNbGameRaises());
		instance.setValue(15,p.getNbPlayerRaises());
		instance.setValue(16,p.getGameRaisePercentage(prop));
		instance.setValue(17,p.isLastActionWasRaise()+"");
		// PT Stats
		instance.setValue(18,p.getVPIP());
		instance.setValue(19,p.getPFR());
		instance.setValue(20,p.getAF());
		instance.setValue(21,p.getWtSD());
		return instance;
	}

	protected Instance getFoldCallRaiseInstance(PlayerId actor) {
		Propositionalizer prop = getTopVisitor().getPropz();
		PlayerData p = prop.getPlayers().get(actor);
		Instance instance = new Instance(29);
		instance.setDataset(callRaiseDataSet);
		// Timing
		instance.setValue(0,prop.getRoundCompletion());
		instance.setValue(1,prop.getPlayersActed());
		instance.setValue(2,prop.getPlayersToAct());
		instance.setValue(3,prop.getRound());
		instance.setValue(4,p.getGameCount());
		instance.setValue(5,prop.isSomebodyActedThisRound()+"");
		// Amounts
		instance.setValue(6,prop.getPotSize());
		instance.setValue(7,p.getDeficit(prop));
		instance.setValue(8,p.getPotOdds(prop));
		instance.setValue(9,p.getStackSize(prop));
		// Player count
		instance.setValue(10,prop.getNbSeatedPlayers());
		instance.setValue(11,prop.getNbActivePlayers() );
		instance.setValue(12,prop.getActivePlayerRatio());
		// Global player frequencies
		instance.setValue(13,p.getFoldFrequency());
		instance.setValue(14,p.getCallFrequency());
		instance.setValue(15,p.getRaiseFrequency());
		// Per-round player frequencies
		instance.setValue(16,p.getRoundFoldFrequency(prop));
		instance.setValue(17,p.getRoundCallFrequency(prop));
		instance.setValue(18,p.getRoundRaiseFrequency(prop));
		// Game betting behaviour
		instance.setValue(19,p.isComitted()+"");
		instance.setValue(20,prop.getNbGameRaises());
		instance.setValue(21,p.getNbPlayerRaises());
		instance.setValue(22,p.getGameRaisePercentage(prop));
		instance.setValue(23,p.isLastActionWasRaise()+"");
		// PT Stats
		instance.setValue(24,p.getVPIP());
		instance.setValue(25,p.getPFR());
		instance.setValue(26,p.getAF());
		instance.setValue(27,p.getWtSD());
		return instance;
	}

	@Override
	public void assumePermanently(GameState gameState) {
		visitor.readHistory(gameState);
	}

	@Override
	public void assumeTemporarily(GameState gameState) {
		PlayerTrackingVisitor root = visitors.isEmpty() ? visitor : getTopVisitor();
		PlayerTrackingVisitor clonedTopVisitor = root.clone();
		clonedTopVisitor.readHistory(gameState);
		visitors.push(clonedTopVisitor);
	}

	@Override
	public void forgetLastAssumption() {
		visitors.pop();
	}

	protected PlayerTrackingVisitor getTopVisitor() {
		if(visitors.isEmpty()){
			return visitor;
		}
		return visitors.peek();
	}

}