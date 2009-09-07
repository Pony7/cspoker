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

package org.cspoker.ai.bots.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;

import org.cspoker.ai.opponentmodels.weka.PlayerData;
import org.cspoker.ai.opponentmodels.weka.Propositionalizer;
import org.cspoker.common.elements.cards.Card;

//TODO fix log methods to handle absolute amounts in stead of relative ones.
public class PropositionalDataSetGenerator extends Propositionalizer {

	protected static final String hole = "*** HOLE";
	protected static final String flop = "*** FLOP";
	protected static final String turn = "*** TURN";
	protected static final String river = "*** RIVER";

	protected final FileWriter preCallRaiseAction;
	protected final FileWriter preCheckBetAction;	

	protected final FileWriter postCallRaiseAction;
	protected final FileWriter postCheckBetAction;

	protected final FileWriter showdownPartitions;
	protected final FileWriter betSizeClass;
	protected final FileWriter betSize;


	private boolean forgetCurrentGame = false;

	private final HashMap<String, Card> cards = new HashMap<String, Card>();

	public PropositionalDataSetGenerator() throws IOException {
		preCallRaiseAction = new FileWriter("output/PreCallRaiseAction2.arff");
		preCallRaiseHeader();

		postCallRaiseAction = new FileWriter("output/PostCallRaiseAction2.arff");
		postCallRaiseHeader();

		preCheckBetAction = new FileWriter("output/PreCheckBetAction2.arff");
		preCheckBetHeader();

		postCheckBetAction = new FileWriter("output/PostCheckBetAction2.arff");
		postCheckBetHeader();


		showdownPartitions = new FileWriter("output/ShowdownPartitions.arff");
		showdownHeader();

		betSizeClass = new FileWriter("output/BetSizeClass.arff");
		betSize = new FileWriter("output/BetSize.arff");
		betSizeHeaders();

		for(Card c:Card.values()){
			cards.put(c.getShortDescription(), c);
		}

	}

	private void preCallRaiseHeader() throws IOException {
		preCallRaiseAction.write(
				"@relation PreCallRaiseAction\n"
				+ "@attribute roundCompletion real\n"
				+ "@attribute playersActed integer\n"
				+ "@attribute playersToAct integer\n"
				+ "@attribute gameCount integer\n"
				+ "@attribute somebodyActedThisRound {false,true}\n"
				+ "@attribute nbActionsThisRound integer\n"
				// Amounts
				+ "@attribute potSize real\n"
				+ "@attribute stackSize real\n"
				+ "@attribute deficit real\n"
				+ "@attribute potOdds real\n"
				+ "@attribute maxbet real\n"
				//				// CommunityCards
				//				+ "@attribute minRank integer\n"
				//				+ "@attribute maxRank integer\n"
				//				+ "@attribute avgRank integer\n"
				//				+ "@attribute sigmaRank real\n"
				// Player count
				+ "@attribute nbSeatedPlayers integer\n"
				+ "@attribute nbActivePlayers integer\n"
				+ "@attribute activePlayerRatio real\n"
				+
				// Global player frequencies
				"@attribute foldFrequency real\n"
				+ "@attribute callFrequency real\n"
				+ "@attribute raiseFrequency real\n"
				+
				// Per-round player frequencies
				"@attribute foldFrequencyRound real\n"
				+ "@attribute callFrequencyRound real\n"
				+ "@attribute raiseFrequencyRound real\n"
				+
				// Game betting behaviour
				"@attribute isComitted {false,true}\n"
				+ "@attribute nbAllPlayerRaises integer\n"
				+ "@attribute nbPlayerRaises integer\n"
				+ "@attribute gameRaisePercentage real\n"
				+ "@attribute gameRaiseAmount real\n"
				+ "@attribute gameRaiseAmountRatio real\n"
				+ "@attribute lastActionWasRaise {false,true}\n"
				// PT Stats
				+ "@attribute VPIP real\n" 
				+ "@attribute PFR real\n"
				+ "@attribute AF real\n" 
				+ "@attribute AFq real\n" 
				+ "@attribute AFAmount real\n" 
				+ "@attribute WtSD real\n"
				// Table PT stat averages
				+ "@attribute opponentVPIP real\n"
				+ "@attribute opponentPFR real\n"
				+ "@attribute opponentAF real\n"
				+ "@attribute opponentAFq real\n"
				+ "@attribute opponentLogAFAmount real\n"
				+ "@attribute opponentWtSD real\n"
				// Targets
				+ "@attribute foldProb real\n"
				+ "@attribute callProb real\n"
				+ "@attribute raiseProb real\n"
				+ "@attribute action {fold,call,raise}\n"
				+ "@data\n"
		);
	}

	private void preCallRaiseInstance(PlayerData p, String target)
	throws IOException {
		FileWriter file = preCallRaiseAction;
		// Timing
		file.write(getRoundCompletion() + ",");
		file.write(getPlayersActed() + ",");
		file.write(getPlayersToAct() + ",");
		file.write(p.getGameCount() + ",");
		file.write(isSomebodyActedThisRound() + ",");
		file.write(getTableGameStats().getNbRoundActions(this) + ",");
		// Amounts
		file.write(getPotSize() + ",");
		file.write(p.getStack() + ",");
		file.write((float)Math.log(p.getDeficit(this)) + ",");
		file.write(p.getPotOdds(this) + ",");
		file.write((float)Math.log(getMaxBet())+ ",");
		//		// CommunityCards
		//		file.write(getMinRank() + ",");
		//		file.write(getMaxRank() + ",");
		//		file.write(getAverageRank() + ",");
		//		file.write(getSigmaRank() + ",");
		// Player count
		file.write(getNbSeatedPlayers() + ",");
		file.write(getNbActivePlayers() + ",");
		file.write(getActivePlayerRatio() + ",");
		// Global player frequencies
		file.write(p.getGlobalStats().getFoldFrequency(4) + ",");
		file.write(p.getGlobalStats().getCallFrequency(4) + ",");
		file.write(p.getGlobalStats().getRaiseFrequency(4) + ",");
		// Per-round player frequencies
		file.write(p.getGlobalStats().getRoundFoldFrequency(this,4) + ",");
		file.write(p.getGlobalStats().getRoundCallFrequency(this,4) + ",");
		file.write(p.getGlobalStats().getRoundRaiseFrequency(this,4) + ",");
		// Game betting behaviour
		file.write(p.isComitted() + ",");
		file.write(getTableGameStats().getNbBetsRaises() + ",");
		file.write(p.getGameStats().getNbBetsRaises() + ",");
		file.write(rel(p.getGameStats().getNbBetsRaises(),getTableGameStats().getNbBetsRaises()) + ",");
		file.write((float)Math.log1p(p.getGameStats().getTotalBetRaiseAmount()) + ",");
		file.write(rel(p.getGameStats().getTotalBetRaiseAmount(),getTableGameStats().getTotalBetRaiseAmount()) + ",");
		file.write(p.isLastActionWasRaise() + ",");
		// PT Stats
		file.write(p.getVPIP(4) + ",");
		file.write(p.getPFR(4) + ",");
		file.write(p.getGlobalStats().getAF(5) + ",");
		file.write(p.getGlobalStats().getAFq(5) + ",");
		file.write((float)Math.log(p.getGlobalStats().getAFAmount(5)) + ",");
		file.write(p.getWtSD(4) + ",");
		// Table PT stat averages
		file.write(getAverageVPIP(p,4) + ",");
		file.write(getAveragePFR(p,4) + ",");
		file.write(getAverageAF(p,5) + ",");
		file.write(getAverageAFq(p,5) + ",");
		file.write(getAverageAFAmount(p,5) + ",");
		file.write(getAverageWtSD(p,4) + ",");
		// "@attribute W$SD real\n"+
		// Target
		file.write(target + "\n");
		file.flush();
	}

	private void postCallRaiseHeader() throws IOException {
		postCallRaiseAction.write(
				"@relation PostCallRaiseAction\n"
				+ "@attribute roundCompletion real\n"
				+ "@attribute playersActed integer\n"
				+ "@attribute playersToAct integer\n"
				+ "@attribute round {flop,turn,river}\n"
				+ "@attribute gameCount integer\n"
				+ "@attribute nbActionsThisRound integer\n"
				// Amounts
				+ "@attribute potSize real\n"
				+ "@attribute stackSize real\n"
				+ "@attribute deficit real\n"
				+ "@attribute potOdds real\n"
				+ "@attribute maxbet real\n"
				//				// CommunityCards
				//				+ "@attribute minRank integer\n"
				//				+ "@attribute maxRank integer\n"
				//				+ "@attribute avgRank integer\n"
				//				+ "@attribute sigmaRank real\n"
				// Player count
				+ "@attribute nbSeatedPlayers integer\n"
				+ "@attribute nbActivePlayers integer\n"
				+ "@attribute activePlayerRatio real\n"
				+
				// Global player frequencies
				"@attribute foldFrequency real\n"
				+ "@attribute callFrequency real\n"
				+ "@attribute raiseFrequency real\n"
				+
				// Per-round player frequencies
				"@attribute foldFrequencyRound real\n"
				+ "@attribute callFrequencyRound real\n"
				+ "@attribute raiseFrequencyRound real\n"
				+
				// Game betting behaviour
				"@attribute isComitted {false,true}\n"
				+ "@attribute nbAllPlayerRaises integer\n"
				+ "@attribute nbPlayerRaises integer\n"
				+ "@attribute nbPlayerRoundRaises integer\n"
				+ "@attribute gameRaisePercentage real\n"
				+ "@attribute gameRaiseAmount real\n"
				+ "@attribute gameRaiseAmountRatio real\n"
				+ "@attribute lastActionWasRaise {false,true}\n"
				// PT Stats
				+ "@attribute VPIP real\n" 
				+ "@attribute PFR real\n"
				+ "@attribute AF real\n" 
				+ "@attribute AFq real\n" 
				+ "@attribute AFAmount real\n" 
				+ "@attribute WtSD real\n"
				// Table PT stat averages
				+ "@attribute opponentVPIP real\n"
				+ "@attribute opponentPFR real\n"
				+ "@attribute opponentAF real\n"
				+ "@attribute opponentAFq real\n"
				+ "@attribute opponentLogAFAmount real\n"
				+ "@attribute opponentWtSD real\n"
				// Targets
				+ "@attribute foldProb real\n"
				+ "@attribute callProb real\n"
				+ "@attribute raiseProb real\n"
				+ "@attribute action {fold,call,raise}\n"
				+ "@data\n"
		);
	}
	
	private void postCallRaiseInstance(PlayerData p, String target)
	throws IOException {
		FileWriter file = postCallRaiseAction;
		// Timing
		file.write(getRoundCompletion() + ",");
		file.write(getPlayersActed() + ",");
		file.write(getPlayersToAct() + ",");
		file.write(getRound() + ",");
		file.write(p.getGameCount() + ",");
		file.write(getTableGameStats().getNbRoundActions(this) + ",");
		// Amounts
		file.write(getPotSize() + ",");
		file.write(p.getStack() + ",");
		file.write((float)Math.log(p.getDeficit(this)) + ",");
		file.write(p.getPotOdds(this) + ",");
		file.write((float)Math.log(getMaxBet())+ ",");
		//		// CommunityCards
		//		file.write(getMinRank() + ",");
		//		file.write(getMaxRank() + ",");
		//		file.write(getAverageRank() + ",");
		//		file.write(getSigmaRank() + ",");
		// Player count
		file.write(getNbSeatedPlayers() + ",");
		file.write(getNbActivePlayers() + ",");
		file.write(getActivePlayerRatio() + ",");
		// Global player frequencies
		// Global player frequencies
		file.write(p.getGlobalStats().getFoldFrequency(4) + ",");
		file.write(p.getGlobalStats().getCallFrequency(4) + ",");
		file.write(p.getGlobalStats().getRaiseFrequency(4) + ",");
		// Per-round player frequencies
		file.write(p.getGlobalStats().getRoundFoldFrequency(this,4) + ",");
		file.write(p.getGlobalStats().getRoundCallFrequency(this,4) + ",");
		file.write(p.getGlobalStats().getRoundRaiseFrequency(this,4) + ",");
		// Game betting behaviour
		file.write(p.isComitted() + ",");
		file.write(getTableGameStats().getNbBetsRaises() + ",");
		file.write(p.getGameStats().getNbBetsRaises() + ",");
		file.write(p.getGameStats().getNbRoundBetsRaises(this) + ",");
		file.write(rel(p.getGameStats().getNbBetsRaises(),getTableGameStats().getNbBetsRaises()) + ",");
		file.write((float)Math.log1p(p.getGameStats().getTotalBetRaiseAmount()) + ",");
		file.write(rel(p.getGameStats().getTotalBetRaiseAmount(),getTableGameStats().getTotalBetRaiseAmount()) + ",");
		file.write(p.isLastActionWasRaise() + ",");
		// PT Stats
		file.write(p.getVPIP(4) + ",");
		file.write(p.getPFR(4) + ",");
		file.write(p.getGlobalStats().getAF(5) + ",");
		file.write(p.getGlobalStats().getAFq(5) + ",");
		file.write((float)Math.log(p.getGlobalStats().getAFAmount(5)) + ",");
		file.write(p.getWtSD(4) + ",");
		// Table PT stat averages
		file.write(getAverageVPIP(p,4) + ",");
		file.write(getAveragePFR(p,4) + ",");
		file.write(getAverageAF(p,5) + ",");
		file.write(getAverageAFq(p,5) + ",");
		file.write(getAverageAFAmount(p,5) + ",");
		file.write(getAverageWtSD(p,4) + ",");
		// "@attribute W$SD real\n"+
		// Target
		file.write(target + "\n");
		file.flush();
	}

	private void preCheckBetHeader() throws IOException {
		preCheckBetAction.write(
				"@relation PreCheckBetAction\n" 
				+ "@attribute roundCompletion real\n"
				+ "@attribute playersActed integer\n"
				+ "@attribute playersToAct integer\n"
				+ "@attribute gameCount integer\n"
				+ "@attribute nbActionsThisRound integer\n"
				// Amounts
				+ "@attribute potSize real\n"
				+ "@attribute stackSize real\n"
				//				// CommunityCards
				//				+ "@attribute minRank integer\n"
				//				+ "@attribute maxRank integer\n"
				//				+ "@attribute avgRank integer\n"
				//				+ "@attribute sigmaRank real\n"
				// Player count
				+ "@attribute nbSeatedPlayers integer\n"
				+ "@attribute nbActivePlayers integer\n"
				+ "@attribute activePlayerRatio real\n"
				+
				// Global player frequencies
				"@attribute betFrequency real\n"
				+
				// Per-round player frequencies
				"@attribute betFrequencyRound real\n"
				// PT Stats
				+ "@attribute VPIP real\n" 
				+ "@attribute PFR real\n"
				+ "@attribute AF real\n" 
				+ "@attribute AFq real\n" 
				+ "@attribute AFAmount real\n" 
				+ "@attribute WtSD real\n"
				// Table PT stat averages
				+ "@attribute opponentVPIP real\n"
				+ "@attribute opponentPFR real\n"
				+ "@attribute opponentAF real\n"
				+ "@attribute opponentAFq real\n"
				+ "@attribute opponentLogAFAmount real\n"
				+ "@attribute opponentWtSD real\n"
				// Targets
				+ "@attribute betProb real\n"
				+ "@attribute action {check, bet}\n"
				+ "@data\n"
		);
	}

	private void preCheckBetInstance(PlayerData p, String target)
	throws IOException {
		FileWriter file = preCheckBetAction;
		// Timing
		file.write(getRoundCompletion() + ",");
		file.write(getPlayersActed() + ",");
		file.write(getPlayersToAct() + ",");
		file.write(p.getGameCount() + ",");
		file.write(getTableGameStats().getNbRoundActions(this) + ",");
		// Amounts
		file.write(getPotSize() + ",");
		file.write(p.getStack() + ",");
		//		// CommunityCards
		//		file.write(getMinRank() + ",");
		//		file.write(getMaxRank() + ",");
		//		file.write(getAverageRank() + ",");
		//		file.write(getSigmaRank() + ",");
		// Player count
		file.write(getNbSeatedPlayers() + ",");
		file.write(getNbActivePlayers() + ",");
		file.write(getActivePlayerRatio() + ",");
		// Global player frequencies
		file.write(p.getGlobalStats().getBetFrequency(4) + ",");
		// Per-round player frequencies
		file.write(p.getGlobalStats().getRoundBetFrequency(this,4) + ",");
		// PT Stats
		file.write(p.getVPIP(4) + ",");
		file.write(p.getPFR(4) + ",");
		file.write(p.getGlobalStats().getAF(5) + ",");
		file.write(p.getGlobalStats().getAFq(5) + ",");
		file.write((float)Math.log(p.getGlobalStats().getAFAmount(5)) + ",");
		file.write(p.getWtSD(4) + ",");
		// Table PT stat averages
		file.write(getAverageVPIP(p,4) + ",");
		file.write(getAveragePFR(p,4) + ",");
		file.write(getAverageAF(p,5) + ",");
		file.write(getAverageAFq(p,5) + ",");
		file.write(getAverageAFAmount(p,5) + ",");
		file.write(getAverageWtSD(p,4) + ",");
		// "@attribute W$SD real\n"+
		// Target
		file.write(target + "\n");
		file.flush();
	}

	private void postCheckBetHeader() throws IOException {
		postCheckBetAction.write(
				"@relation PostCheckBetAction\n" 
				+ "@attribute roundCompletion real\n"
				+ "@attribute playersActed integer\n"
				+ "@attribute playersToAct integer\n"
				+ "@attribute round {flop,turn,river}\n"
				+ "@attribute gameCount integer\n"
				+ "@attribute somebodyActedThisRound {false,true}\n"
				+ "@attribute nbActionsThisRound integer\n"
				// Amounts
				+ "@attribute potSize real\n"
				+ "@attribute stackSize real\n"
				//				// CommunityCards
				//				+ "@attribute minRank integer\n"
				//				+ "@attribute maxRank integer\n"
				//				+ "@attribute avgRank integer\n"
				//				+ "@attribute sigmaRank real\n"
				// Player count
				+ "@attribute nbSeatedPlayers integer\n"
				+ "@attribute nbActivePlayers integer\n"
				+ "@attribute activePlayerRatio real\n"
				+
				// Global player frequencies
				"@attribute betFrequency real\n"
				+
				// Per-round player frequencies
				"@attribute betFrequencyRound real\n"
				// Game betting behaviour
				+ "@attribute nbAllPlayerRaises integer\n"
				+ "@attribute nbPlayerRaises integer\n"
				+ "@attribute gameRaisePercentage real\n"
				+ "@attribute gameRaiseAmount real\n"
				+ "@attribute gameRaiseAmountRatio real\n"
				+ "@attribute lastActionWasRaise {false,true}\n"
				// PT Stats
				+ "@attribute VPIP real\n" 
				+ "@attribute PFR real\n"
				+ "@attribute AF real\n" 
				+ "@attribute AFq real\n" 
				+ "@attribute AFAmount real\n" 
				+ "@attribute WtSD real\n"
				// Table PT stat averages
				+ "@attribute opponentVPIP real\n"
				+ "@attribute opponentPFR real\n"
				+ "@attribute opponentAF real\n"
				+ "@attribute opponentAFq real\n"
				+ "@attribute opponentLogAFAmount real\n"
				+ "@attribute opponentWtSD real\n"
				// Targets
				+ "@attribute betProb real\n"
				+ "@attribute action {check, bet}\n"
				+ "@data\n"
		);
	}

	private void postCheckBetInstance(PlayerData p, String target)
	throws IOException {
		FileWriter file = postCheckBetAction;
		// Timing
		file.write(getRoundCompletion() + ",");
		file.write(getPlayersActed() + ",");
		file.write(getPlayersToAct() + ",");
		file.write(getRound() + ",");
		file.write(p.getGameCount() + ",");
		file.write(isSomebodyActedThisRound() + ",");
		file.write(getTableGameStats().getNbRoundActions(this) + ",");
		// Amounts
		file.write(getPotSize() + ",");
		file.write(p.getStack() + ",");
		//		// CommunityCards
		//		file.write(getMinRank() + ",");
		//		file.write(getMaxRank() + ",");
		//		file.write(getAverageRank() + ",");
		//		file.write(getSigmaRank() + ",");
		// Player count
		file.write(getNbSeatedPlayers() + ",");
		file.write(getNbActivePlayers() + ",");
		file.write(getActivePlayerRatio() + ",");
		// Global player frequencies
		file.write(p.getGlobalStats().getBetFrequency(4) + ",");
		// Per-round player frequencies
		file.write(p.getGlobalStats().getRoundBetFrequency(this,4) + ",");
		// Game betting behaviour
		file.write(getTableGameStats().getNbBetsRaises() + ",");
		file.write(p.getGameStats().getNbBetsRaises() + ",");
		file.write(rel(p.getGameStats().getNbBetsRaises(),getTableGameStats().getNbBetsRaises()) + ",");
		file.write((float)Math.log1p(p.getGameStats().getTotalBetRaiseAmount()) + ",");
		file.write(rel(p.getGameStats().getTotalBetRaiseAmount(),getTableGameStats().getTotalBetRaiseAmount()) + ",");
		file.write(p.isLastActionWasRaise() + ",");
		// PT Stats
		file.write(p.getVPIP(4) + ",");
		file.write(p.getPFR(4) + ",");
		file.write(p.getGlobalStats().getAF(5) + ",");
		file.write(p.getGlobalStats().getAFq(5) + ",");
		file.write((float)Math.log(p.getGlobalStats().getAFAmount(5)) + ",");
		file.write(p.getWtSD(4) + ",");
		// Table PT stat averages
		file.write(getAverageVPIP(p,4) + ",");
		file.write(getAveragePFR(p,4) + ",");
		file.write(getAverageAF(p,5) + ",");
		file.write(getAverageAFq(p,5) + ",");
		file.write(getAverageAFAmount(p,5) + ",");
		file.write(getAverageWtSD(p,4) + ",");
		// "@attribute W$SD real\n"+
		// Target
		file.write(target + "\n");
		file.flush();
	}


	private void showdownHeader() throws IOException {
		showdownPartitions.write(
				"@relation ShowdownPartitions\n"
				+ "@attribute gameCount integer\n"
				// Amounts
				+ "@attribute potSize real\n"
				+ "@attribute logPotSize real\n"
				+ "@attribute stackSize real\n"
				+ "@attribute log1pStackSize real\n"
				// Player count
				+ "@attribute nbSeatedPlayers integer\n"
				+ "@attribute nbActivePlayers integer\n"
				+ "@attribute activePlayerRatio real\n"
				// Global player frequencies
				+ "@attribute betFrequency real\n"
				+ "@attribute foldFrequency real\n"
				+ "@attribute callFrequency real\n"
				+ "@attribute raiseFrequency real\n"
				// BetRaise counts
				+ "@attribute nbAllPlayerBetRaise integer\n"
				+ "@attribute nbPlayerBetRaise integer\n"
				+ "@attribute nbPlayerPreFlopBetRaise integer\n"
				+ "@attribute nbPlayerPostFlopBetRaise integer\n"
				+ "@attribute nbPlayerFlopBetRaise integer\n"
				+ "@attribute nbPlayerTurnBetRaise integer\n"
				+ "@attribute nbPlayerRiverBetRaise integer\n"
				// BetRaise amounts
				+ "@attribute allPlayerBetRaiseAmount integer\n"
				+ "@attribute playerBetRaiseAmount integer\n"
				+ "@attribute playerPreFlopBetRaiseAmount integer\n"
				+ "@attribute playerPostFlopBetRaiseAmount integer\n"
				+ "@attribute playerFlopBetRaiseAmount integer\n"
				+ "@attribute playerTurnBetRaiseAmount integer\n"
				+ "@attribute playerRiverBetRaiseAmount integer\n"
				//Bet amounts
				+ "@attribute allPlayerBetsAmount integer\n"
				+ "@attribute playerBetsAmount integer\n"
				+ "@attribute playerPreFlopBetsAmount integer\n"
				+ "@attribute playerPostFlopBetsAmount integer\n"
				+ "@attribute playerFlopBetsAmount integer\n"
				+ "@attribute playerTuretsAmount integer\n"
				+ "@attribute playerRiverBetsAmount integer\n"
				//Raise amounts
				+ "@attribute allPlayerRaisesAmount integer\n"
				+ "@attribute playerRaisesAmount integer\n"
				+ "@attribute playerPreFlopRaisesAmount integer\n"
				+ "@attribute playerPostFlopRaisesAmount integer\n"
				+ "@attribute playerFlopRaisesAmount integer\n"
				+ "@attribute playerTurnRaisesAmount integer\n"
				+ "@attribute playerRiverRaisesAmount integer\n"
				//Relative BetRaise counts
				+ "@attribute relNbPlayerBetRaise integer\n"
				+ "@attribute relNbPlayerPreFlopBetRaise integer\n"
				+ "@attribute relNbPlayerPostFlopBetRaise integer\n"
				+ "@attribute relNbPlayerFlopBetRaise integer\n"
				+ "@attribute relNbPlayerTurnBetRaise integer\n"
				+ "@attribute relNbPlayerRiverBetRaise integer\n"
				//Relative BetRaise amounts
				+ "@attribute relPlayerBetRaiseAmount integer\n"
				+ "@attribute relPlayerPreFlopBetRaiseAmount integer\n"
				+ "@attribute relPlayerPostFlopBetRaiseAmount integer\n"
				+ "@attribute relPlayerFlopBetRaiseAmount integer\n"
				+ "@attribute relPlayerTurnBetRaiseAmount integer\n"
				+ "@attribute relPlayerRiverBetRaiseAmount integer\n"
				// Table BetRaise counts
				+ "@attribute nbPlayerPreFlopBetRaiseTable integer\n"
				+ "@attribute nbPlayerPostFlopBetRaiseTable integer\n"
				+ "@attribute nbPlayerFlopBetRaiseTable integer\n"
				+ "@attribute nbPlayerTurnBetRaiseTable integer\n"
				+ "@attribute nbPlayerRiverBetRaiseTable integer\n"
				// Table BetRaise amounts
				+ "@attribute playerPreFlopBetRaiseAmountTable integer\n"
				+ "@attribute playerPostFlopBetRaiseAmountTable integer\n"
				+ "@attribute playerFlopBetRaiseAmountTable integer\n"
				+ "@attribute playerTurnBetRaiseAmountTable integer\n"
				+ "@attribute playerRiverBetRaiseAmountTable integer\n"
				//Other
				+ "@attribute lastActionWasBetRaise {false,true}\n"
				// PT Stats
				+ "@attribute VPIP real\n" 
				+ "@attribute PFR real\n"
				+ "@attribute AF real\n" 
				+ "@attribute AFq real\n" 
				+ "@attribute logAFAmount real\n" 
				+ "@attribute WtSD real\n"
				// Table PT stat averages
				+ "@attribute opponentVPIP real\n"
				+ "@attribute opponentPFR real\n"
				+ "@attribute opponentAF real\n"
				+ "@attribute opponentAFq real\n"
				+ "@attribute opponentLogAFAmount real\n"
				+ "@attribute opponentWtSD real\n"
				// CommunityCards
				+ "@attribute minRank integer\n"
				+ "@attribute maxRank integer\n"
				+ "@attribute avgRank integer\n"
				+ "@attribute sigmaRank real\n"
				+ "@attribute rankSpan real\n"
				// Targets
				+ "@attribute part0Prob real\n"
				+ "@attribute part1Prob real\n"
				+ "@attribute part2Prob real\n"
				+ "@attribute part3Prob real\n"
				+ "@attribute part4Prob real\n"
				+ "@attribute part5Prob real\n"
				+ "@attribute part6Prob real\n"
				+ "@attribute part7Prob real\n"
				+ "@attribute part8Prob real\n"
				+ "@attribute part9Prob real\n"
				+ "@attribute avgPartition {0,1,2,3,4,5,6,7,8,9}\n"
				+ "@data\n"
		);
	}

	private void showdownInstance(PlayerData p, float[] partitionDistr,
			int average, int minrank, int maxrank, int avgrank, int sigmarank) throws IOException {
		FileWriter file = showdownPartitions;
		// Timing
		file.write(p.getGameCount() + ",");
		// Amounts
		file.write(getPotSize() + ",");
		file.write((float)Math.log(getPotSize()) + ",");
		file.write(p.getStack() + ",");
		file.write((float)Math.log1p(p.getStack()) + ",");
		// Player count
		file.write(getNbSeatedPlayers() + ",");
		file.write(getNbActivePlayers() + ",");
		file.write(getActivePlayerRatio() + ",");
		// Global player frequencies
		file.write(p.getGlobalStats().getBetFrequency(4) + ",");
		file.write(p.getGlobalStats().getFoldFrequency(4) + ",");
		file.write(p.getGlobalStats().getCallFrequency(4) + ",");
		file.write(p.getGlobalStats().getRaiseFrequency(4) + ",");
		//BetRaise counts
		file.write(getTableGameStats().getNbBetsRaises() + ",");
		file.write(p.getGameStats().getNbBetsRaises() + ",");
		file.write(p.getGameStats().getNbBetsRaisesPreFlop() + ",");
		file.write(p.getGameStats().getNbBetsRaisesPostFlop() + ",");
		file.write(p.getGameStats().getNbBetsRaisesFlop() + ",");
		file.write(p.getGameStats().getNbBetsRaisesTurn() + ",");
		file.write(p.getGameStats().getNbBetsRaisesRiver() + ",");
		//BetRaise amount
		file.write((float)getTableGameStats().getTotalBetRaiseAmount() + ",");
		file.write((float)p.getGameStats().getTotalBetRaiseAmount() + ",");
		file.write((float)p.getGameStats().getBetRaiseAmountPreFlop() + ",");
		file.write((float)p.getGameStats().getBetRaiseAmountPostFlop() + ",");
		file.write((float)p.getGameStats().getBetRaiseAmountFlop() + ",");
		file.write((float)p.getGameStats().getBetRaiseAmountTurn() + ",");
		file.write((float)p.getGameStats().getBetRaiseAmountRiver() + ",");
		//Bet amount
		file.write((float)getTableGameStats().getTotalBetAmount() + ",");
		file.write((float)p.getGameStats().getTotalBetAmount() + ",");
		file.write((float)p.getGameStats().getBetAmountPreFlop() + ",");
		file.write((float)p.getGameStats().getBetAmountPostFlop() + ",");
		file.write((float)p.getGameStats().getBetAmountFlop() + ",");
		file.write((float)p.getGameStats().getBetAmountTurn() + ",");
		file.write((float)p.getGameStats().getBetAmountRiver() + ",");
		//Raise amount
		file.write((float)getTableGameStats().getTotalRaiseAmount() + ",");
		file.write((float)p.getGameStats().getTotalRaiseAmount() + ",");
		file.write((float)p.getGameStats().getRaiseAmountPreFlop() + ",");
		file.write((float)p.getGameStats().getRaiseAmountPostFlop() + ",");
		file.write((float)p.getGameStats().getRaiseAmountFlop() + ",");
		file.write((float)p.getGameStats().getRaiseAmountTurn() + ",");
		file.write((float)p.getGameStats().getRaiseAmountRiver() + ",");
		// Relative BetRaise counts
		file.write(rel(p.getGameStats().getNbBetsRaises(),getTableGameStats().getNbBetsRaises()) + ",");
		file.write(rel(p.getGameStats().getNbBetsRaisesPreFlop(),getTableGameStats().getNbBetsRaisesPreFlop()) + ",");
		file.write(rel(p.getGameStats().getNbBetsRaisesPostFlop(),getTableGameStats().getNbBetsRaisesPostFlop()) + ",");
		file.write(rel(p.getGameStats().getNbBetsRaisesFlop(),getTableGameStats().getNbBetsRaisesFlop()) + ",");
		file.write(rel(p.getGameStats().getNbBetsRaisesTurn(),getTableGameStats().getNbBetsRaisesTurn()) + ",");
		file.write(rel(p.getGameStats().getNbBetsRaisesRiver(),getTableGameStats().getNbBetsRaisesRiver()) + ",");
		// Relative BetRaise amounts
		file.write(rel(p.getGameStats().getTotalBetRaiseAmount(),getTableGameStats().getTotalBetRaiseAmount()) + ",");
		file.write(rel(p.getGameStats().getBetRaiseAmountPreFlop(),getTableGameStats().getBetRaiseAmountPreFlop()) + ",");
		file.write(rel(p.getGameStats().getBetRaiseAmountPostFlop(),getTableGameStats().getBetRaiseAmountPostFlop()) + ",");
		file.write(rel(p.getGameStats().getBetRaiseAmountFlop(),getTableGameStats().getBetRaiseAmountFlop()) + ",");
		file.write(rel(p.getGameStats().getBetRaiseAmountTurn(),getTableGameStats().getBetRaiseAmountTurn()) + ",");
		file.write(rel(p.getGameStats().getBetRaiseAmountRiver(),getTableGameStats().getBetRaiseAmountRiver()) + ",");
		//Table BetRaise counts
		file.write(getTableGameStats().getNbBetsRaisesPreFlop() + ",");
		file.write(getTableGameStats().getNbBetsRaisesPostFlop() + ",");
		file.write(getTableGameStats().getNbBetsRaisesFlop() + ",");
		file.write(getTableGameStats().getNbBetsRaisesTurn() + ",");
		file.write(getTableGameStats().getNbBetsRaisesRiver() + ",");
		//Table BetRaise amount
		file.write((float)getTableGameStats().getBetRaiseAmountPreFlop() + ",");
		file.write((float)getTableGameStats().getBetRaiseAmountPostFlop() + ",");
		file.write((float)getTableGameStats().getBetRaiseAmountFlop() + ",");
		file.write((float)getTableGameStats().getBetRaiseAmountTurn() + ",");
		file.write((float)getTableGameStats().getBetRaiseAmountRiver() + ",");
		// Other
		file.write(p.isLastActionWasRaise() + ",");
		// PT Stats
		file.write(p.getVPIP(4) + ",");
		file.write(p.getPFR(4) + ",");
		file.write(p.getGlobalStats().getAF(5) + ",");
		file.write(p.getGlobalStats().getAFq(5) + ",");
		file.write((float)Math.log(p.getGlobalStats().getAFAmount(5)) + ",");
		file.write(p.getWtSD(4) + ",");
		// Table PT stat averages
		file.write(getAverageVPIP(p,4) + ",");
		file.write(getAveragePFR(p,4) + ",");
		file.write(getAverageAF(p,5) + ",");
		file.write(getAverageAFq(p,5) + ",");
		file.write(getAverageAFAmount(p,5) + ",");
		file.write(getAverageWtSD(p,4) + ",");
		// CommunityCards
		file.write(minrank + ",");
		file.write(maxrank + ",");
		file.write(avgrank + ",");
		file.write(sigmarank + ",");
		file.write((maxrank-minrank) + ",");
		// Target
		for (float f : partitionDistr) {
			file.write(f + ",");
		}
		file.write(average + "\n");
		file.flush();
	}

	private void betSizeHeaders() throws IOException {
//		String str = "@relation BetSize\n"
//		+ "@attribute roundCompletion real\n"
//		+ "@attribute playersActed integer\n"
//		+ "@attribute playersToAct integer\n"
//		+ "@attribute round {preflop, flop, turn, river}\n"
//		+ "@attribute gameCount integer\n"
//		+ "@attribute nbActionsThisRound integer\n"
//		// Amounts
//		+ "@attribute potSize real\n"
//		+ "@attribute stackSize real\n"
//		+ "@attribute deficit real\n"
//		+ "@attribute potOdds real\n"
//		+ "@attribute maxbet real\n"
//		//				// CommunityCards
//		//				+ "@attribute minRank integer\n"
//		//				+ "@attribute maxRank integer\n"
//		//				+ "@attribute avgRank integer\n"
//		//				+ "@attribute sigmaRank real\n"
//		// Player count
//		+ "@attribute nbSeatedPlayers integer\n"
//		+ "@attribute nbActivePlayers integer\n"
//		+ "@attribute activePlayerRatio real\n"
//		// Global player frequencies
//		+ "@attribute betFrequency real\n"
//		+ "@attribute raiseFrequency real\n"
//		// Per-round player frequencies
//		+ "@attribute betFrequencyRound real\n"
//		+ "@attribute raiseFrequencyRound real\n"
//		// Game betting behaviour
//		+ "@attribute isComitted {false,true}\n"
//		+ "@attribute nbAllPlayerRaises integer\n"
//		+ "@attribute nbPlayerRaises integer\n"
//		+ "@attribute nbPlayerRoundRaises integer\n"
//		+ "@attribute gameRaisePercentage real\n"
//		+ "@attribute gameRaiseAmount real\n"
//		+ "@attribute gameRaiseAmountRatio real\n"
//		+ "@attribute lastActionWasRaise {false,true}\n"
//		// PT Stats
//		+ "@attribute VPIP real\n" 
//		+ "@attribute PFR real\n"
//		+ "@attribute AF real\n" 
//		+ "@attribute AFq real\n" 
//		+ "@attribute AFAmount real\n" 
//		+ "@attribute WtSD real\n"
//		// Table PT stat averages
//		+ "@attribute opponentVPIP real\n"
//		+ "@attribute opponentPFR real\n"
//		+ "@attribute opponentAF real\n"
//		+ "@attribute opponentAFq real\n"
//		+ "@attribute opponentLogAFAmount real\n"
//		+ "@attribute opponentWtSD real\n"
//		// Raise Boundaries
//		+ "@attribute minRaise real\n"
//		+ "@attribute maxRaise real\n"
//		+ "@attribute raiseSpan real\n"
//		;
//		betSizeClass.write(str// Targets
//				+ "@attribute betSize real\n"
//				+ "@attribute logBetSize real\n"
//				+ "@attribute relBetSize real\n"
//				+ "@attribute pMinBet real\n"
//				+ "@attribute pAvgBet real\n"
//				+ "@attribute pAllin real\n"
//				+ "@attribute class {minBet,avg,allin}\n"
//				+ "@data\n");
//		betSize.write(str// Targets
//				+ "@attribute betSize real\n"
//				+ "@attribute logBetSize real\n"
//				+ "@attribute relBetSize real\n"
//				+ "@attribute logRelBetSize real\n"
//				+ "@data\n");
	}
	
	private void betSizeInstance(PlayerData p, float minRaise, float maxRaise, String target, FileWriter file)
	throws IOException {
		// Timing
//		file.write(getRoundCompletion() + ",");
//		file.write(getPlayersActed() + ",");
//		file.write(getPlayersToAct() + ",");
//		file.write(getRound() + ",");
//		file.write(p.getGameCount() + ",");
//		file.write(getTableGameStats().getNbRoundActions(this) + ",");
//		// Amounts
//		file.write(getPotSize() + ",");
//		file.write(p.getStack() + ",");
//		file.write(p.getDeficit(this) + ",");
//		file.write(p.getPotOdds(this) + ",");
//		file.write(getMaxBet()+ ",");
//		//		// CommunityCards
//		//		file.write(getMinRank() + ",");
//		//		file.write(getMaxRank() + ",");
//		//		file.write(getAverageRank() + ",");
//		//		file.write(getSigmaRank() + ",");
//		// Player count
//		file.write(getNbSeatedPlayers() + ",");
//		file.write(getNbActivePlayers() + ",");
//		file.write(getActivePlayerRatio() + ",");
//		// Global player frequencies
//		// Global player frequencies
//		file.write(p.getGlobalStats().getBetFrequency(4) + ",");
//		file.write(p.getGlobalStats().getRaiseFrequency(4) + ",");
//		// Per-round player frequencies
//		file.write(p.getGlobalStats().getRoundBetFrequency(this,4) + ",");
//		file.write(p.getGlobalStats().getRoundRaiseFrequency(this,4) + ",");
//		// Game betting behaviour
//		file.write(p.isComitted() + ",");
//		file.write(getTableGameStats().getNbBetsRaises() + ",");
//		file.write(p.getGameStats().getNbBetsRaises() + ",");
//		file.write(p.getGameStats().getNbRoundBetsRaises(this) + ",");
//		file.write(rel(p.getGameStats().getNbBetsRaises(),getTableGameStats().getNbBetsRaises()) + ",");
//		file.write((float)Math.log1p(p.getGameStats().getTotalBetRaiseAmount()) + ",");
//		file.write(rel(p.getGameStats().getTotalBetRaiseAmount(),getTableGameStats().getTotalBetRaiseAmount()) + ",");
//		file.write(p.isLastActionWasRaise() + ",");
//		// PT Stats
//		file.write(p.getVPIP(4) + ",");
//		file.write(p.getPFR(4) + ",");
//		file.write(p.getGlobalStats().getAF(5) + ",");
//		file.write(p.getGlobalStats().getAFq(5) + ",");
//		file.write((float)Math.log(p.getGlobalStats().getAFAmount(5)) + ",");
//		file.write(p.getWtSD(4) + ",");
//		// Table PT stat averages
//		file.write(getAverageVPIP(p,4) + ",");
//		file.write(getAveragePFR(p,4) + ",");
//		file.write(getAverageAF(p,5) + ",");
//		file.write(getAverageAFq(p,5) + ",");
//		file.write(getAverageAFAmount(p,5) + ",");
//		file.write(getAverageWtSD(p,4) + ",");
//		// "@attribute W$SD real\n"+
//		// Raise Boundaries
//		file.write(minRaise + ",");
//		file.write(maxRaise + ",");
//		file.write(maxRaise-minRaise + ",");
//		// Target
		file.write(target + "\n");
		file.flush();
	}
	
	private void close() throws IOException {
		preCallRaiseAction.close();
		preCheckBetAction.close();
		postCallRaiseAction.close();
		postCheckBetAction.close();
		showdownPartitions.close();
		betSizeClass.close();
		betSize.close();
	}

	//	protected void logFold(PlayerData p) {
	//		try {
	//			if(getRound().equals("preflop")){
	//				preCallRaiseInstance(p, "1,0,0,fold");
	//			}else{
	//				postCallRaiseInstance(p, "1,0,0,fold");
	//			}	
	//		} catch (IOException e) {
	//			throw new IllegalStateException(e);
	//		}	
	//	}
	//
	//	protected void logCall(PlayerData p) {
	//		try {
	//			if(getRound().equals("preflop")){
	//				preCallRaiseInstance(p, "0,1,0,call");
	//			}else{
	//				postCallRaiseInstance(p, "0,1,0,call");
	//			}	
	//		} catch (IOException e) {
	//			throw new IllegalStateException(e);
	//		}
	//	}

	protected void logRaise(PlayerData p, float raiseAmount) {
		try {
			//			if(getRound().equals("preflop")){
			//				preCallRaiseInstance(p, "0,0,1,raise");
			//			}else{
			//				postCallRaiseInstance(p, "0,0,1,raise");
			//			}
			logRaiseAmount(p, raiseAmount);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
	//	protected void logCheck(PlayerData p) {
	//		try {
	//			if(getRound().equals("preflop")){
	//				preCheckBetInstance(p, "0,check");
	//			}else{
	//				postCheckBetInstance(p, "0,check");
	//			}	
	//		} catch (IOException e) {
	//			throw new IllegalStateException(e);
	//		}
	//	}
	//
	protected void logBet(PlayerData p, float raiseAmount) {
		try {
			//			if(getRound().equals("preflop")){
			//				preCheckBetInstance(p, "1,bet");
			//			}else{
			//				postCheckBetInstance(p, "1,bet");
			//			}	
			logRaiseAmount(p, raiseAmount);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	//	@Override
	//	protected void logShowdown(PlayerData p, float[] partitionDistr, int average, int minrank, int maxrank, int avgrank, int sigmarank) {
	//		try {
	//			showdownInstance(p, partitionDistr, average, minrank, maxrank, avgrank, sigmarank);	
	//		} catch (IOException e) {
	//			throw new IllegalStateException(e);
	//		}
	//	}


	private void logRaiseAmount(PlayerData p, float raiseAmount)
			throws IOException {
		float minRaise = (float)getMinRaise(p);
		float maxRaise = (float)getMaxRaise(p);
		raiseAmount = Math.min(raiseAmount, maxRaise);
		float logBetSize = (float)Math.log(raiseAmount);
		if(Math.abs(minRaise-maxRaise)>0.6) //only when we have a choice
		{
			if(Math.abs(minRaise-raiseAmount)<0.6) {
//				betSizeInstance(p, minRaise, maxRaise, raiseAmount+","+logBetSize+",0,1,0,0,minBet", betSizeClass);
				betSizeInstance(p, minRaise, maxRaise, 0+"", betSizeClass);
			}else if(Math.abs(maxRaise-raiseAmount)<0.6) {
//				betSizeInstance(p, minRaise, maxRaise, raiseAmount+","+logBetSize+",1,0,0,1,allin", betSizeClass);
				betSizeInstance(p, minRaise, maxRaise, 1+"", betSizeClass);
			}else{
				if(raiseAmount<minRaise || minRaise>maxRaise){
					System.out.println("Skipping illegal bet");
					return;
				}
				float relBetSize = (raiseAmount-minRaise)/(maxRaise-minRaise);
//				betSizeInstance(p, minRaise, maxRaise, raiseAmount+","+logBetSize+","+relBetSize+",0,1,0,avg", betSizeClass);
				betSizeInstance(p, minRaise, maxRaise, relBetSize+"", betSizeClass);
//				betSizeInstance(p, minRaise, maxRaise, raiseAmount+","+logBetSize+","+relBetSize+","+(float)Math.log(relBetSize), betSize);
			}
		}
	}

	public void run() throws IOException {
		try {
			String line;
			File dir1 = new File(
					"/home/guy/Werk/thesis/opponentmodel/data2/unzipped");
			String[] children1 = dir1.list();
			if (children1 == null) {
				// Either dir does not exist or is not a directory
			} else {
				for (String element : children1) {
					File child1 = new File(dir1, element);
					String[] children2 = child1.list();
					if (children2 == null) {
						// Either dir does not exist or is not a directory
					} else {
						for (String element2 : children2) {
							// Get filename of file or directory
							System.out.println("Starting file: " + element
									+ "/" + element2);
							BufferedReader r = new BufferedReader(
									new FileReader(new File(child1, element2)));
							while ((line = r.readLine()) != null) {
								try {
									doLine(line);
								} catch (IllegalStateException e) {
									System.out.println(line);
									throw e;
								}
							}
							r.close();
						}
					}
				}
			}
		} finally {
			close();
		}
	}

	private void doLine(String line) throws IOException {
		// inputRaise.write(line+"\n");
		// foldFile.write(line+"\n");
		// System.out.println(line);
		if (line.startsWith("Full Tilt Poker Game ")) {
			int temp = line.indexOf("/");
			int bb = (int)(100*Float.parseFloat(line.substring(temp + 2,
					line.indexOf(" ", temp + 2)).replaceAll(",", "")));
			forgetCurrentGame = false;
			signalNewGame();
			signalBBAmount(bb);
		} else if (!forgetCurrentGame) {
			if (line.startsWith("Seat ")) {
				if (line.endsWith("(0)")) {
					forgetCurrentGame = true;
				} else {
					int startName = line.indexOf(":") + 2;
					int startDollar = line.indexOf("(", startName);
					int stack = (int)(100*Float
					.parseFloat(line.substring(startDollar + 2,
							line.indexOf(")", startDollar)).replaceAll(
									",", "")));
					String name = line.substring(startName, startDollar - 1);
					signalSeatedPlayer(stack, name);
				}
			} else if (line.startsWith("*** ")) {
				if (line.startsWith("*** SUMMARY ***")) {
					forgetCurrentGame = true;
					signalShowdown();
				} else {
					if(line.startsWith(hole)){
						signalCommunityCards(EnumSet.noneOf(Card.class));
					}
					if (line.startsWith(flop)) {
						signalFlop();
						String[] cardsString = line.substring(line.indexOf("[")).replaceAll("\\[", "").replaceAll("\\]", "").split(" ");
						EnumSet<Card> cardsSet = EnumSet.of(cards.get(cardsString[0]),cards.get(cardsString[1]),cards.get(cardsString[2]));
						signalCommunityCards(cardsSet);
					} else if (line.startsWith(turn)) {
						signalTurn();
						String[] cardsString = line.substring(line.indexOf("[")).replaceAll("\\[", "").replaceAll("\\]", "").split(" ");
						EnumSet<Card> cardsSet = EnumSet.of(cards.get(cardsString[0]),cards.get(cardsString[1]),cards.get(cardsString[2]),cards.get(cardsString[3]));
						signalCommunityCards(cardsSet);
					} else if (line.startsWith(river)) {
						signalRiver();
						String[] cardsString = line.substring(line.indexOf("[")).replaceAll("\\[", "").replaceAll("\\]", "").split(" ");
						EnumSet<Card> cardsSet = EnumSet.of(cards.get(cardsString[0]),cards.get(cardsString[1]),cards.get(cardsString[2]),cards.get(cardsString[3]),cards.get(cardsString[4]));
						signalCommunityCards(cardsSet);
					}
				}
			} else if (line.contains(":")) {
				// ignore chat message
			} else {
				boolean isAllIn = line.contains("all in");
				if (line.contains(" posts the small blind")) {
					String id = line.substring(0, line
							.indexOf(" posts the small blind"));

					signalSmallBlind(isAllIn, id);
				} else if (line.contains(" posts the big blind")) {
					String id = line.substring(0, line
							.indexOf(" posts the big blind"));
					signalBigBlind(isAllIn, id);
				} else if (line.endsWith(" folds")) {
					String id = line
					.substring(0, line.indexOf(" folds"));
					signalFold(id);
				} else if (line.contains(" calls")) {
					int allinIndex = line.lastIndexOf(", and is all in");
					if (allinIndex <= 0) {
						allinIndex = line.length();
					}
					String id = line
					.substring(0, line.indexOf(" calls"));
					int movedAmount = (int)(100*Float.parseFloat(line.substring(
							line.indexOf("$") + 1, allinIndex).replaceAll(",", "")));
					signalCall(isAllIn, id, movedAmount);
				} else if (line.contains(" raises")) {
					int allinIndex = line.lastIndexOf(", and is all in");
					if (allinIndex <= 0) {
						allinIndex = line.length();
					}
					int maxBetParsed = (int)(100*Float.parseFloat(line.substring(
							line.indexOf("$") + 1, allinIndex).replaceAll(",",
							"")));
					String id = line.substring(0, line
							.indexOf(" raises"));
					signalRaise(id, isAllIn, maxBetParsed);
				} else if (line.endsWith(" checks")) {
					String id = line.substring(0, line
							.indexOf(" checks"));
					signalCheck(id);
				} else if (line.contains(" bets")) {
					String id = line.substring(0, line.indexOf(" bets"));
					int allinIndex = line.lastIndexOf(", and is all in");
					if (allinIndex <= 0) {
						allinIndex = line.length();
					}
					int maxBetParsed = (int)(100*Float.parseFloat(line.substring(line.indexOf("$") + 1,
							allinIndex).replaceAll(",", "")));
					//cannot be bet by big blind, is raise in dataset
					signalBet(isAllIn, id, maxBetParsed);
				} else if (line.contains(" shows [")){
					int showsIndex = line
					.indexOf(" shows [");
					String id = line.substring(0, showsIndex);

					int start = showsIndex+8;
					String[] cardStrings = line.substring(start, line.indexOf("]",start)).split(" ");
					signalCardShowdown(id,cards.get(cardStrings[0]),cards.get(cardStrings[1]));
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new PropositionalDataSetGenerator().run();
	}
}
