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

	protected static final Logger logger = Logger.getLogger(WekaRegressionModel.class);
	protected final PlayerTrackingVisitor visitor;
	
	protected final Instances preFoldCallRaiseDataSet;
	protected final Instances postFoldCallRaiseDataSet;
	protected final Instances preCheckBetDataSet;
	protected final Instances postCheckBetDataSet;
	protected final Instances showdownDataSet;
	
	private final Deque<PlayerTrackingVisitor> visitors = new ArrayDeque<PlayerTrackingVisitor>();

	public WekaModel() {
		this.visitor = new PlayerTrackingVisitor();
		try {
			this.preFoldCallRaiseDataSet = new DataSource(
					getClass().
					getClassLoader().
					getResourceAsStream("org/cspoker/client/bots/bot/search/opponentmodel/weka/PreFoldCallRaiseHeader.arff")
			).getStructure();
			preFoldCallRaiseDataSet.setClassIndex(39);
			
			this.postFoldCallRaiseDataSet = new DataSource(
					getClass().
					getClassLoader().
					getResourceAsStream("org/cspoker/client/bots/bot/search/opponentmodel/weka/PostFoldCallRaiseHeader.arff")
			).getStructure();
			postFoldCallRaiseDataSet.setClassIndex(40);

			this.preCheckBetDataSet = new DataSource(
					getClass().
					getClassLoader().
					getResourceAsStream("org/cspoker/client/bots/bot/search/opponentmodel/weka/PreCheckBetHeader.arff")
			).getStructure();
			preCheckBetDataSet.setClassIndex(24);

			this.postCheckBetDataSet = new DataSource(
					getClass().
					getClassLoader().
					getResourceAsStream("org/cspoker/client/bots/bot/search/opponentmodel/weka/PostCheckBetHeader.arff")
			).getStructure();
			postCheckBetDataSet.setClassIndex(32);

			this.showdownDataSet = new DataSource(
					getClass().
					getClassLoader().
					getResourceAsStream("org/cspoker/client/bots/bot/search/opponentmodel/weka/ShowdownHeader.arff")
			).getStructure();
			showdownDataSet.setClassIndex(80);
		} catch (Exception e) {
			//stupid Weka
			throw new IllegalStateException(e);
		}
	}

	protected Instance getPreCheckBetInstance(PlayerId actor) {
		Propositionalizer prop = getTopVisitor().getPropz();
		PlayerData p = prop.getPlayers().get(actor);
		Instance instance = new Instance(25);
		instance.setDataset(preCheckBetDataSet);
		instance.setValue(0, prop.getRoundCompletion());
		instance.setValue(1, prop.getPlayersActed());
		instance.setValue(2, prop.getPlayersToAct());
		instance.setValue(3, p.getGameCount());
		instance.setValue(4, prop.getTableGameStats().getNbRoundActions(prop));
		// Amounts
		instance.setValue(5, prop.getPotSize());
		instance.setValue(6, p.getStack());
		// Player count
		instance.setValue(7, prop.getNbSeatedPlayers());
		instance.setValue(8, prop.getNbActivePlayers());
		instance.setValue(9, prop.getActivePlayerRatio());
		// Global player frequencies
		instance.setValue(10, p.getGlobalStats().getBetFrequency(4));
		// Per-round player frequencies
		instance.setValue(11, p.getGlobalStats().getRoundBetFrequency(prop,4));
		// PT Stats
		instance.setValue(12, p.getVPIP(4));
		instance.setValue(13, p.getPFR(4));
		instance.setValue(14, p.getGlobalStats().getAF(5));
		instance.setValue(15, p.getGlobalStats().getAFq(5));
		instance.setValue(16, (float)Math.log(p.getGlobalStats().getAFAmount(5)));
		instance.setValue(17, p.getWtSD(4));
		// Table PT stat averages
		instance.setValue(18, prop.getAverageVPIP(p,4));
		instance.setValue(19, prop.getAveragePFR(p,4));
		instance.setValue(20, prop.getAverageAF(p,5));
		instance.setValue(21, prop.getAverageAFq(p,5));
		instance.setValue(22, prop.getAverageAFAmount(p,5));
		instance.setValue(23, prop.getAverageWtSD(p,4));
		return instance;
	}
	
	protected Instance getPostCheckBetInstance(PlayerId actor) {
		Propositionalizer prop = getTopVisitor().getPropz();
		PlayerData p = prop.getPlayers().get(actor);
		Instance instance = new Instance(33);
		instance.setDataset(postCheckBetDataSet);
		instance.setValue(0, prop.getRoundCompletion());
		instance.setValue(1, prop.getPlayersActed());
		instance.setValue(2, prop.getPlayersToAct());
		instance.setValue(3, prop.getRound()+"");
		instance.setValue(4, p.getGameCount());
		instance.setValue(5, prop.isSomebodyActedThisRound()+"");
		instance.setValue(6, prop.getTableGameStats().getNbRoundActions(prop));
		// Amounts
		instance.setValue(7, prop.getPotSize());
		instance.setValue(8, p.getStack());
		// Player count
		instance.setValue(9, prop.getNbSeatedPlayers());
		instance.setValue(10, prop.getNbActivePlayers());
		instance.setValue(11, prop.getActivePlayerRatio());
		// Global player frequencies
		instance.setValue(12, p.getGlobalStats().getBetFrequency(4));
		// Per-round player frequencies
		instance.setValue(13, p.getGlobalStats().getRoundBetFrequency(prop,4));
		// Game betting behaviour
		instance.setValue(14, prop.getTableGameStats().getNbBetsRaises());
		instance.setValue(15, p.getGameStats().getNbBetsRaises());
		instance.setValue(16, prop.rel(p.getGameStats().getNbBetsRaises(),prop.getTableGameStats().getNbBetsRaises()));
		instance.setValue(17, (float)Math.log1p(p.getGameStats().getTotalBetRaiseAmount()));
		instance.setValue(18, prop.rel(p.getGameStats().getTotalBetRaiseAmount(),prop.getTableGameStats().getTotalBetRaiseAmount()));
		instance.setValue(19, p.isLastActionWasRaise()+"");
		// PT Stats
		instance.setValue(20, p.getVPIP(4));
		instance.setValue(21, p.getPFR(4));
		instance.setValue(22, p.getGlobalStats().getAF(5));
		instance.setValue(23, p.getGlobalStats().getAFq(5));
		instance.setValue(24, (float)Math.log(p.getGlobalStats().getAFAmount(5)));
		instance.setValue(25, p.getWtSD(4));
		// Table PT stat averages
		instance.setValue(26, prop.getAverageVPIP(p,4));
		instance.setValue(27, prop.getAveragePFR(p,4));
		instance.setValue(28, prop.getAverageAF(p,5));
		instance.setValue(29, prop.getAverageAFq(p,5));
		instance.setValue(30, prop.getAverageAFAmount(p,5));
		instance.setValue(31, prop.getAverageWtSD(p,4));
		return instance;
	}
	
	protected Instance getPreFoldCallRaiseInstance(PlayerId actor) {
		Propositionalizer prop = getTopVisitor().getPropz();
		PlayerData p = prop.getPlayers().get(actor);
		Instance instance = new Instance(40);
		instance.setDataset(preFoldCallRaiseDataSet);
		instance.setValue(0, prop.getRoundCompletion());
		instance.setValue(1, prop.getPlayersActed());
		instance.setValue(2, prop.getPlayersToAct());
		instance.setValue(3, p.getGameCount());
		instance.setValue(4, prop.isSomebodyActedThisRound()+"");
		instance.setValue(5, prop.getTableGameStats().getNbRoundActions(prop));
		// Amounts
		instance.setValue(6, prop.getPotSize());
		instance.setValue(7, p.getStack());
		instance.setValue(8, (float)Math.log(p.getDeficit(prop)));
		instance.setValue(9, p.getPotOdds(prop));
		instance.setValue(10, (float)Math.log(prop.getMaxBet()));
		// Player count
		instance.setValue(11, prop.getNbSeatedPlayers());
		instance.setValue(12, prop.getNbActivePlayers());
		instance.setValue(13, prop.getActivePlayerRatio());
		// Global player frequencies
		instance.setValue(14, p.getGlobalStats().getFoldFrequency(4));
		instance.setValue(15, p.getGlobalStats().getCallFrequency(4));
		instance.setValue(16, p.getGlobalStats().getRaiseFrequency(4));
		// Per-round player frequencies
		instance.setValue(17, p.getGlobalStats().getRoundFoldFrequency(prop,4));
		instance.setValue(18, p.getGlobalStats().getRoundCallFrequency(prop,4));
		instance.setValue(19, p.getGlobalStats().getRoundRaiseFrequency(prop,4));
		// Game betting behaviour
		instance.setValue(20, p.isComitted()+"");
		instance.setValue(21, prop.getTableGameStats().getNbBetsRaises());
		instance.setValue(22, p.getGameStats().getNbBetsRaises());
		instance.setValue(23, prop.rel(p.getGameStats().getNbBetsRaises(),prop.getTableGameStats().getNbBetsRaises()));
		instance.setValue(24, (float)Math.log1p(p.getGameStats().getTotalBetRaiseAmount()));
		instance.setValue(25, prop.rel(p.getGameStats().getTotalBetRaiseAmount(),prop.getTableGameStats().getTotalBetRaiseAmount()));
		instance.setValue(26, p.isLastActionWasRaise()+"");
		// PT Stats
		instance.setValue(27, p.getVPIP(4));
		instance.setValue(28, p.getPFR(4));
		instance.setValue(29, p.getGlobalStats().getAF(5));
		instance.setValue(30, p.getGlobalStats().getAFq(5));
		instance.setValue(31, (float)Math.log(p.getGlobalStats().getAFAmount(5)));
		instance.setValue(32, p.getWtSD(4));
		// Table PT stat averages
		instance.setValue(33, prop.getAverageVPIP(p,4));
		instance.setValue(34, prop.getAveragePFR(p,4));
		instance.setValue(35, prop.getAverageAF(p,5));
		instance.setValue(36, prop.getAverageAFq(p,5));
		instance.setValue(37, prop.getAverageAFAmount(p,5));
		instance.setValue(38, prop.getAverageWtSD(p,4));
		return instance;
	}
	protected Instance getPostFoldCallRaiseInstance(PlayerId actor) {
		Propositionalizer prop = getTopVisitor().getPropz();
		PlayerData p = prop.getPlayers().get(actor);
		Instance instance = new Instance(41);
		instance.setDataset(postFoldCallRaiseDataSet);
		instance.setValue(0, prop.getRoundCompletion());
		instance.setValue(1, prop.getPlayersActed());
		instance.setValue(2, prop.getPlayersToAct());
		instance.setValue(3, prop.getRound());
		instance.setValue(4, p.getGameCount());
		instance.setValue(5, prop.getTableGameStats().getNbRoundActions(prop));
		// Amounts
		instance.setValue(6, prop.getPotSize());
		instance.setValue(7, p.getStack());
		instance.setValue(8, (float)Math.log(p.getDeficit(prop)));
		instance.setValue(9, p.getPotOdds(prop));
		instance.setValue(10, (float)Math.log(prop.getMaxBet()));
		// Player count
		instance.setValue(11, prop.getNbSeatedPlayers());
		instance.setValue(12, prop.getNbActivePlayers());
		instance.setValue(13, prop.getActivePlayerRatio());
		// Global player frequencies
		// Global player frequencies
		instance.setValue(14, p.getGlobalStats().getFoldFrequency(4));
		instance.setValue(15, p.getGlobalStats().getCallFrequency(4));
		instance.setValue(16, p.getGlobalStats().getRaiseFrequency(4));
		// Per-round player frequencies
		instance.setValue(17, p.getGlobalStats().getRoundFoldFrequency(prop,4));
		instance.setValue(18, p.getGlobalStats().getRoundCallFrequency(prop,4));
		instance.setValue(19, p.getGlobalStats().getRoundRaiseFrequency(prop,4));
		// Game betting behaviour
		instance.setValue(20, p.isComitted()+"");
		instance.setValue(21, prop.getTableGameStats().getNbBetsRaises());
		instance.setValue(22, p.getGameStats().getNbBetsRaises());
		instance.setValue(23, p.getGameStats().getNbRoundBetsRaises(prop));
		instance.setValue(24, prop.rel(p.getGameStats().getNbBetsRaises(),prop.getTableGameStats().getNbBetsRaises()));
		instance.setValue(25, (float)Math.log1p(p.getGameStats().getTotalBetRaiseAmount()));
		instance.setValue(26, prop.rel(p.getGameStats().getTotalBetRaiseAmount(),prop.getTableGameStats().getTotalBetRaiseAmount()));
		instance.setValue(27, p.isLastActionWasRaise()+"");
		// PT Stats
		instance.setValue(28, p.getVPIP(4));
		instance.setValue(29, p.getPFR(4));
		instance.setValue(30, p.getGlobalStats().getAF(5));
		instance.setValue(31, p.getGlobalStats().getAFq(5));
		instance.setValue(32, (float)Math.log(p.getGlobalStats().getAFAmount(5)));
		instance.setValue(33, p.getWtSD(4));
		// Table PT stat averages
		instance.setValue(34, prop.getAverageVPIP(p,4));
		instance.setValue(35, prop.getAveragePFR(p,4));
		instance.setValue(36, prop.getAverageAF(p,5));
		instance.setValue(37, prop.getAverageAFq(p,5));
		instance.setValue(38, prop.getAverageAFAmount(p,5));
		instance.setValue(39, prop.getAverageWtSD(p,4));
		return instance;
	}
	protected Instance getShowdownInstance(PlayerId actor, int minrank, int maxrank, int avgrank, int sigmarank) {
		Propositionalizer prop = getTopVisitor().getPropz();
		PlayerData p = prop.getPlayers().get(actor);
		Instance instance = new Instance(81);
		instance.setDataset(showdownDataSet);
		instance.setValue(0, p.getGameCount());
		// Amounts
		instance.setValue(1, prop.getPotSize());
		instance.setValue(2, (float)Math.log(prop.getPotSize()));
		instance.setValue(3, p.getStack());
		instance.setValue(4, (float)Math.log1p(p.getStack()));
		// Player count
		instance.setValue(5, prop.getNbSeatedPlayers());
		instance.setValue(6, prop.getNbActivePlayers());
		instance.setValue(7, prop.getActivePlayerRatio());
		// Global player frequencies
		instance.setValue(8, p.getGlobalStats().getBetFrequency(4));
		instance.setValue(9, p.getGlobalStats().getFoldFrequency(4));
		instance.setValue(10, p.getGlobalStats().getCallFrequency(4));
		instance.setValue(11, p.getGlobalStats().getRaiseFrequency(4));
		//BetRaise counts
		instance.setValue(12, prop.getTableGameStats().getNbBetsRaises());
		instance.setValue(13, p.getGameStats().getNbBetsRaises());
		instance.setValue(14, p.getGameStats().getNbBetsRaisesPreFlop());
		instance.setValue(15, p.getGameStats().getNbBetsRaisesPostFlop());
		instance.setValue(16, p.getGameStats().getNbBetsRaisesFlop());
		instance.setValue(17, p.getGameStats().getNbBetsRaisesTurn());
		instance.setValue(18, p.getGameStats().getNbBetsRaisesRiver());
		//BetRaise amount
		instance.setValue(19, (float)prop.getTableGameStats().getTotalBetRaiseAmount());
		instance.setValue(20, (float)p.getGameStats().getTotalBetRaiseAmount());
		instance.setValue(21, (float)p.getGameStats().getBetRaiseAmountPreFlop());
		instance.setValue(22, (float)p.getGameStats().getBetRaiseAmountPostFlop());
		instance.setValue(23, (float)p.getGameStats().getBetRaiseAmountFlop());
		instance.setValue(24, (float)p.getGameStats().getBetRaiseAmountTurn());
		instance.setValue(25, (float)p.getGameStats().getBetRaiseAmountRiver());
		//Bet amount
		instance.setValue(26, (float)prop.getTableGameStats().getTotalBetAmount());
		instance.setValue(27, (float)p.getGameStats().getTotalBetAmount());
		instance.setValue(28, (float)p.getGameStats().getBetAmountPreFlop());
		instance.setValue(29, (float)p.getGameStats().getBetAmountPostFlop());
		instance.setValue(30, (float)p.getGameStats().getBetAmountFlop());
		instance.setValue(31, (float)p.getGameStats().getBetAmountTurn());
		instance.setValue(32, (float)p.getGameStats().getBetAmountRiver());
		//Raise amount
		instance.setValue(33, (float)prop.getTableGameStats().getTotalRaiseAmount());
		instance.setValue(34, (float)p.getGameStats().getTotalRaiseAmount());
		instance.setValue(35, (float)p.getGameStats().getRaiseAmountPreFlop());
		instance.setValue(36, (float)p.getGameStats().getRaiseAmountPostFlop());
		instance.setValue(37, (float)p.getGameStats().getRaiseAmountFlop());
		instance.setValue(38, (float)p.getGameStats().getRaiseAmountTurn());
		instance.setValue(39, (float)p.getGameStats().getRaiseAmountRiver());
		// Relative BetRaise counts
		instance.setValue(40, prop.rel(p.getGameStats().getNbBetsRaises(),prop.getTableGameStats().getNbBetsRaises()));
		instance.setValue(41, prop.rel(p.getGameStats().getNbBetsRaisesPreFlop(),prop.getTableGameStats().getNbBetsRaisesPreFlop()));
		instance.setValue(42, prop.rel(p.getGameStats().getNbBetsRaisesPostFlop(),prop.getTableGameStats().getNbBetsRaisesPostFlop()));
		instance.setValue(43, prop.rel(p.getGameStats().getNbBetsRaisesFlop(),prop.getTableGameStats().getNbBetsRaisesFlop()));
		instance.setValue(44, prop.rel(p.getGameStats().getNbBetsRaisesTurn(),prop.getTableGameStats().getNbBetsRaisesTurn()));
		instance.setValue(45, prop.rel(p.getGameStats().getNbBetsRaisesRiver(),prop.getTableGameStats().getNbBetsRaisesRiver()));
		// Relative BetRaise amounts
		instance.setValue(46, prop.rel(p.getGameStats().getTotalBetRaiseAmount(),prop.getTableGameStats().getTotalBetRaiseAmount()));
		instance.setValue(47, prop.rel(p.getGameStats().getBetRaiseAmountPreFlop(),prop.getTableGameStats().getBetRaiseAmountPreFlop()));
		instance.setValue(48, prop.rel(p.getGameStats().getBetRaiseAmountPostFlop(),prop.getTableGameStats().getBetRaiseAmountPostFlop()));
		instance.setValue(49, prop.rel(p.getGameStats().getBetRaiseAmountFlop(),prop.getTableGameStats().getBetRaiseAmountFlop()));
		instance.setValue(50, prop.rel(p.getGameStats().getBetRaiseAmountTurn(),prop.getTableGameStats().getBetRaiseAmountTurn()));
		instance.setValue(51, prop.rel(p.getGameStats().getBetRaiseAmountRiver(),prop.getTableGameStats().getBetRaiseAmountRiver()));
		//Table BetRaise counts
		instance.setValue(52, prop.getTableGameStats().getNbBetsRaisesPreFlop());
		instance.setValue(53, prop.getTableGameStats().getNbBetsRaisesPostFlop());
		instance.setValue(54, prop.getTableGameStats().getNbBetsRaisesFlop());
		instance.setValue(55, prop.getTableGameStats().getNbBetsRaisesTurn());
		instance.setValue(56, prop.getTableGameStats().getNbBetsRaisesRiver());
		//Table BetRaise amount
		instance.setValue(57, (float)prop.getTableGameStats().getBetRaiseAmountPreFlop());
		instance.setValue(58, (float)prop.getTableGameStats().getBetRaiseAmountPostFlop());
		instance.setValue(59, (float)prop.getTableGameStats().getBetRaiseAmountFlop());
		instance.setValue(60, (float)prop.getTableGameStats().getBetRaiseAmountTurn());
		instance.setValue(61, (float)prop.getTableGameStats().getBetRaiseAmountRiver());
		// Other
		instance.setValue(62, p.isLastActionWasRaise()+"");
		// PT Stats
		instance.setValue(63, p.getVPIP(4));
		instance.setValue(64, p.getPFR(4));
		instance.setValue(65, p.getGlobalStats().getAF(5));
		instance.setValue(66, p.getGlobalStats().getAFq(5));
		instance.setValue(67, (float)Math.log(p.getGlobalStats().getAFAmount(5)));
		instance.setValue(68, p.getWtSD(4));
		// Table PT stat averages
		instance.setValue(69, prop.getAverageVPIP(p,4));
		instance.setValue(70, prop.getAveragePFR(p,4));
		instance.setValue(71, prop.getAverageAF(p,5));
		instance.setValue(72, prop.getAverageAFq(p,5));
		instance.setValue(73, prop.getAverageAFAmount(p,5));
		instance.setValue(74, prop.getAverageWtSD(p,4));
		// CommunityCards
		instance.setValue(75, minrank);
		instance.setValue(76, maxrank);
		instance.setValue(77, avgrank);
		instance.setValue(78, sigmarank);
		instance.setValue(79, (maxrank-minrank));
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