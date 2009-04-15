package org.cspoker.client.bots.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;

import org.cspoker.client.bots.bot.gametree.opponentmodel.weka.PlayerData;
import org.cspoker.client.bots.bot.gametree.opponentmodel.weka.Propositionalizer;
import org.cspoker.common.elements.cards.Card;

public class PropositionalDataSetGenerator extends Propositionalizer {

	protected static final String hole = "*** HOLE";
	protected static final String flop = "*** FLOP";
	protected static final String turn = "*** TURN";
	protected static final String river = "*** RIVER";

	protected final FileWriter preFoldProb;
	protected final FileWriter preCallProb;
	protected final FileWriter preRaiseProb;
	protected final FileWriter preBetProb;
	protected final FileWriter preCallRaiseAction;
	protected final FileWriter preCheckBetAction;	

	protected final FileWriter postFoldProb;
	protected final FileWriter postCallProb;
	protected final FileWriter postRaiseProb;
	protected final FileWriter postBetProb;
	protected final FileWriter postCallRaiseAction;
	protected final FileWriter postCheckBetAction;

	private boolean forgetCurrentGame = false;

	private final HashMap<String, Card> cards = new HashMap<String, Card>();

	public PropositionalDataSetGenerator() throws IOException {
		preFoldProb = new FileWriter("output/PreFoldProb2.arff");
		preFoldProb.write("@relation PreFoldProb\n" + callRaiseHeader
				+ "@attribute probability real\n" + "@data\n");
		preCallProb = new FileWriter("output/PreCallProb2.arff");
		preCallProb.write("@relation PreCallProb\n" + callRaiseHeader
				+ "@attribute probability real\n" + "@data\n");
		preRaiseProb = new FileWriter("output/PreRaiseProb2.arff");
		preRaiseProb.write("@relation PreRaiseProb\n" + callRaiseHeader
				+ "@attribute probability real\n" + "@data\n");
		preBetProb = new FileWriter("output/PreBetProb2.arff");
		preBetProb.write("@relation PreBetProb\n" + checkBetHeader
				+ "@attribute probability real\n" + "@data\n");
		preCallRaiseAction = new FileWriter("output/PreCallRaiseAction2.arff");
		preCallRaiseAction.write("@relation PreCallRaiseAction\n" + callRaiseHeader
				+ "@attribute action {fold,call,raise}\n" + "@data\n");
		preCheckBetAction = new FileWriter("output/PreCheckBetAction2.arff");
		preCheckBetAction.write("@relation PreCheckBetAction\n" + checkBetHeader
				+ "@attribute action {check,bet}\n" + "@data\n");

		postFoldProb = new FileWriter("output/PostFoldProb2.arff");
		postFoldProb.write("@relation PostFoldProb\n" + callRaiseHeader
				+ "@attribute probability real\n" + "@data\n");
		postCallProb = new FileWriter("output/PostCallProb2.arff");
		postCallProb.write("@relation PostCallProb\n" + callRaiseHeader
				+ "@attribute probability real\n" + "@data\n");
		postRaiseProb = new FileWriter("output/PostRaiseProb2.arff");
		postRaiseProb.write("@relation PostRaiseProb\n" + callRaiseHeader
				+ "@attribute probability real\n" + "@data\n");
		postBetProb = new FileWriter("output/PostBetProb2.arff");
		postBetProb.write("@relation PostBetProb\n" + checkBetHeader
				+ "@attribute probability real\n" + "@data\n");
		postCallRaiseAction = new FileWriter("output/PostCallRaiseAction2.arff");
		postCallRaiseAction.write("@relation PostCallRaiseAction\n" + callRaiseHeader
				+ "@attribute action {fold,call,raise}\n" + "@data\n");
		postCheckBetAction = new FileWriter("output/PostCheckBetAction2.arff");
		postCheckBetAction.write("@relation PostCheckBetAction\n" + checkBetHeader
				+ "@attribute action {check,bet}\n" + "@data\n");

		for(Card c:Card.values()){
			cards.put(c.getShortDescription(), c);
		}

	}

	protected static final String callRaiseHeader = "@attribute roundCompletion real\n"
		+ "@attribute playersActed integer\n"
		+ "@attribute playersToAct integer\n"
		+ "@attribute round {preflop,flop,turn,river}\n"
		+ "@attribute gameCount integer\n"
		+ "@attribute somebodyActedThisRound {true,false}\n"
		+ "@attribute nbActionsThisRound integer\n"
		+
		// Amounts
		"@attribute potSize real\n"
		+ "@attribute deficit real\n"
		+ "@attribute potOdds real\n"
		+ "@attribute stack real\n"
		+
		// CommunityCards
		"@attribute minRank integer\n"
		+ "@attribute maxRank integer\n"
		+ "@attribute avgRank integer\n"
		+ "@attribute sigmaRank real\n"
		+
		// Player count
		"@attribute nbSeatedPlayers integer\n"
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
		"@attribute isComitted {true,false}\n"
		+ "@attribute nbAllPlayerRaises integer\n"
		+ "@attribute nbPlayerRaises integer\n"
		+ "@attribute gameRaisePercentage real\n"
		+ "@attribute lastActionWasRaise {true,false}\n"
		+
		// PT Stats
		"@attribute VPIP real\n" + "@attribute PFR real\n"
		+ "@attribute AF real\n" + "@attribute WtSD real\n";
	protected static final String checkBetHeader = "@attribute roundCompletion real\n"
		+ "@attribute playersActed integer\n"
		+ "@attribute playersToAct integer\n"
		+ "@attribute round {preflop,flop,turn,river}\n"
		+ "@attribute gameCount integer\n"
		+ "@attribute somebodyActedThisRound {true,false}\n"
		+ "@attribute nbActionsThisRound integer\n"
		+
		// Amounts
		"@attribute potSize real\n"
		+ "@attribute stack real\n"
		+
		// CommunityCards
		"@attribute minRank integer\n"
		+ "@attribute maxRank integer\n"
		+ "@attribute avgRank integer\n"
		+ "@attribute sigmaRank real\n"
		+
		// Player count
		"@attribute nbSeatedPlayers integer\n"
		+ "@attribute nbActivePlayers integer\n"
		+ "@attribute activePlayerRatio real\n"
		+
		// Global player frequencies
		"@attribute betFrequency real\n"
		+
		// Per-round player frequencies
		"@attribute betFrequencyRound real\n"
		+
		// Game betting behaviour
		"@attribute isComitted {true,false}\n"
		+ "@attribute nbAllPlayerRaises integer\n"
		+ "@attribute nbPlayerRaises integer\n"
		+ "@attribute gameRaisePercentage real\n"
		+ "@attribute lastActionWasRaise {true,false}\n"
		+
		// PT Stats
		"@attribute VPIP real\n" + "@attribute PFR real\n"
		+ "@attribute AF real\n" + "@attribute WtSD real\n";

	private void close() throws IOException {
		preFoldProb.close();
		preCallProb.close();
		preRaiseProb.close();
		preBetProb.close();
		preCallRaiseAction.close();
		preCheckBetAction.close();
		postFoldProb.close();
		postCallProb.close();
		postRaiseProb.close();
		postBetProb.close();
		postCallRaiseAction.close();
		postCheckBetAction.close();
	}

	protected void logFold(PlayerData p) {
		if(getRound().equals("preflop")){
			try {
				callRaiseInstance(p, "1", preFoldProb);
				callRaiseInstance(p, "0", preCallProb);
				callRaiseInstance(p, "0", preRaiseProb);
				callRaiseInstance(p, "fold", preCallRaiseAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}else{
			try {
				callRaiseInstance(p, "1", postFoldProb);
				callRaiseInstance(p, "0", postCallProb);
				callRaiseInstance(p, "0", postRaiseProb);
				callRaiseInstance(p, "fold", postCallRaiseAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}		
	}

	protected void logCall(PlayerData p) {
		if(getRound().equals("preflop")){
			try {
				callRaiseInstance(p, "0", preFoldProb);
				callRaiseInstance(p, "1", preCallProb);
				callRaiseInstance(p, "0", preRaiseProb);
				callRaiseInstance(p, "call", preCallRaiseAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}else{
			try {
				callRaiseInstance(p, "0", postFoldProb);
				callRaiseInstance(p, "1", postCallProb);
				callRaiseInstance(p, "0", postRaiseProb);
				callRaiseInstance(p, "call", postCallRaiseAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}

		}
	}

	protected void logCheck(PlayerData p) {
		if(getRound().equals("preflop")){
			try {
				checkBetInstance(p, "0", preBetProb);
				checkBetInstance(p, "check", preCheckBetAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}else{
			try {
				checkBetInstance(p, "0", postBetProb);
				checkBetInstance(p, "check", postCheckBetAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}			
		}
	}

	protected void logRaise(PlayerData p) {
		if(getRound().equals("preflop")){
			try {
				callRaiseInstance(p, "0", preFoldProb);
				callRaiseInstance(p, "0", preCallProb);
				callRaiseInstance(p, "1", preRaiseProb);
				callRaiseInstance(p, "raise", preCallRaiseAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}	
		}else{
			try {
				callRaiseInstance(p, "0", postFoldProb);
				callRaiseInstance(p, "0", postCallProb);
				callRaiseInstance(p, "1", postRaiseProb);
				callRaiseInstance(p, "raise", postCallRaiseAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

	}

	protected void logBet(PlayerData p) {
		if(getRound().equals("preflop")){
			try {
				checkBetInstance(p, "1", preBetProb);
				checkBetInstance(p, "bet", preCheckBetAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}else{
			try {
				checkBetInstance(p, "1", postBetProb);
				checkBetInstance(p, "bet", postCheckBetAction);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	private void callRaiseInstance(PlayerData p, String target, FileWriter file)
	throws IOException {
		// Timing
		file.write(getRoundCompletion() + ",");
		file.write(getPlayersActed() + ",");
		file.write(getPlayersToAct() + ",");
		file.write(getRound() + ",");
		file.write(p.getGameCount() + ",");
		file.write(isSomebodyActedThisRound() + ",");
		file.write(getNbActionsThisRound() + ",");
		// Amounts
		file.write(getPotSize() + ",");
		file.write(p.getDeficit(this) + ",");
		file.write(p.getPotOdds(this) + ",");
		file.write(p.getStackSize(this) + ",");
		// CommunityCards
		file.write(getMinRank() + ",");
		file.write(getMaxRank() + ",");
		file.write(getAverageRank() + ",");
		file.write(getSigmaRank() + ",");
		// Player count
		file.write(getNbSeatedPlayers() + ",");
		file.write(getNbActivePlayers() + ",");
		file.write(getActivePlayerRatio() + ",");
		// Global player frequencies
		file.write(p.getFoldFrequency() + ",");
		file.write(p.getCallFrequency() + ",");
		file.write(p.getRaiseFrequency() + ",");
		// Per-round player frequencies
		file.write(p.getRoundFoldFrequency(this) + ",");
		file.write(p.getRoundCallFrequency(this) + ",");
		file.write(p.getRoundRaiseFrequency(this) + ",");
		// Game betting behaviour
		file.write(p.isComitted() + ",");
		file.write(getNbGameRaises() + ",");
		file.write(p.getNbPlayerRaises() + ",");
		file.write(p.getGameRaisePercentage(this) + ",");
		file.write(p.isLastActionWasRaise() + ",");
		// PT Stats
		file.write(p.getVPIP() + ",");
		file.write(p.getPFR() + ",");
		file.write(p.getAF() + ",");
		file.write(p.getWtSD() + ",");
		// "@attribute W$SD real\n"+
		// Target
		file.write(target + "\n");
		file.flush();
	}

	private void checkBetInstance(PlayerData p, String target, FileWriter file)
	throws IOException {
		// Timing
		file.write(getRoundCompletion() + ",");
		file.write(getPlayersActed() + ",");
		file.write(getPlayersToAct() + ",");
		file.write(getRound() + ",");
		file.write(p.getGameCount() + ",");
		file.write(isSomebodyActedThisRound() + ",");
		file.write(getNbActionsThisRound() + ",");
		// Amounts
		file.write(getPotSize() + ",");
		file.write(p.getStackSize(this) + ",");
		// CommunityCards
		file.write(getMinRank() + ",");
		file.write(getMaxRank() + ",");
		file.write(getAverageRank() + ",");
		file.write(getSigmaRank() + ",");
		// Player count
		file.write(getNbSeatedPlayers() + ",");
		file.write(getNbActivePlayers() + ",");
		file.write(getActivePlayerRatio() + ",");
		// Global player frequencies
		file.write(p.getBetFrequency() + ",");
		// Per-round player frequencies
		file.write(p.getRoundBetFrequency(this) + ",");
		// Game betting behaviour
		file.write(p.isComitted() + ",");
		file.write(getNbGameRaises() + ",");
		file.write(p.getNbPlayerRaises() + ",");
		file.write(p.getGameRaisePercentage(this) + ",");
		file.write(p.isLastActionWasRaise() + ",");
		// PT Stats
		file.write(p.getVPIP() + ",");
		file.write(p.getPFR() + ",");
		file.write(p.getAF() + ",");
		file.write(p.getWtSD() + ",");
		// "@attribute W$SD real\n"+
		// Target
		file.write(target + "\n");
		file.flush();
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
								doLine(line);
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
			float bb = Float.parseFloat(line.substring(temp + 2,
					line.indexOf(" ", temp + 2)).replaceAll(",", ""));
			forgetCurrentGame = false;
			signalNewGame(bb);
		} else if (!forgetCurrentGame) {
			if (line.startsWith("Seat ")) {
				if (line.endsWith("(0)")) {
					forgetCurrentGame = true;
				} else {
					int startName = line.indexOf(":") + 2;
					int startDollar = line.indexOf("(", startName);
					float stack = Float
					.parseFloat(line.substring(startDollar + 2,
							line.indexOf(")", startDollar)).replaceAll(
									",", ""));
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
					float movedAmount = Float.parseFloat(line.substring(
							line.indexOf("$") + 1, allinIndex).replaceAll(",", ""));
					signalCall(isAllIn, id, movedAmount);
				} else if (line.contains(" raises")) {
					int allinIndex = line.lastIndexOf(", and is all in");
					if (allinIndex <= 0) {
						allinIndex = line.length();
					}
					float maxBetParsed = Float.parseFloat(line.substring(
							line.indexOf("$") + 1, allinIndex).replaceAll(",",
							""));
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
					float maxBetParsed = Float.parseFloat(line.substring(line.indexOf("$") + 1,
							allinIndex).replaceAll(",", ""));

					signalBet(isAllIn, id, maxBetParsed);
				}
			}
		}
	}



	public static void main(String[] args) throws IOException {
		new PropositionalDataSetGenerator().run();
	}
}
