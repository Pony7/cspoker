package org.cspoker.client.bots.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.cspoker.client.bots.bot.search.opponentmodel.weka.PlayerData;
import org.cspoker.client.bots.bot.search.opponentmodel.weka.Propositionalizer;

public class PropositionalDataSetgenerator extends Propositionalizer {

	protected static final String hole = "*** HOLE";
	protected static final String flop = "*** FLOP";
	protected static final String turn = "*** TURN";
	protected static final String river = "*** RIVER";
	
	protected final FileWriter foldProb;
	protected final FileWriter callProb;
	protected final FileWriter raiseProb;
	protected final FileWriter betProb;
	protected final FileWriter callRaiseAction;
	protected final FileWriter checkBetAction;
	
	private boolean forgetCurrentGame = false;

	public PropositionalDataSetgenerator() throws IOException {
		foldProb = new FileWriter("output/FoldProb.arff");
		foldProb.write("@relation FoldProb\n" + callRaiseHeader
				+ "@attribute probability real\n" + "@data\n");
		callProb = new FileWriter("output/CallProb.arff");
		callProb.write("@relation CallProb\n" + callRaiseHeader
				+ "@attribute probability real\n" + "@data\n");
		raiseProb = new FileWriter("output/RaiseProb.arff");
		raiseProb.write("@relation RaiseProb\n" + callRaiseHeader
				+ "@attribute probability real\n" + "@data\n");
		betProb = new FileWriter("output/BetProb.arff");
		betProb.write("@relation BetProb\n" + checkBetHeader
				+ "@attribute probability real\n" + "@data\n");
		callRaiseAction = new FileWriter("output/CallRaiseAction.arff");
		callRaiseAction.write("@relation CallRaiseAction\n" + callRaiseHeader
				+ "@attribute action {fold,call,raise}\n" + "@data\n");
		checkBetAction = new FileWriter("output/CheckBetAction.arff");
		checkBetAction.write("@relation CheckBetAction\n" + checkBetHeader
				+ "@attribute action {check,bet}\n" + "@data\n");

	}

	protected static final String callRaiseHeader = "@attribute roundCompletion real\n"
		+ "@attribute playersActed integer\n"
		+ "@attribute playersToAct integer\n"
		+ "@attribute round {preflop,flop,turn,river}\n"
		+ "@attribute gameCount integer\n"
		+ "@attribute somebodyActedThisRound {true,false}\n"
		+
		// Amounts
		"@attribute potSize real\n"
		+ "@attribute deficit real\n"
		+ "@attribute potOdds real\n"
		+ "@attribute stack real\n"
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
		+
		// Amounts
		"@attribute potSize real\n"
		+ "@attribute stack real\n"
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
		foldProb.close();
		callProb.close();
		raiseProb.close();
		betProb.close();
		callRaiseAction.close();
		checkBetAction.close();
	}

	protected void logFold(PlayerData p) {
		try {
			callRaiseInstance(p, "1", foldProb);
			callRaiseInstance(p, "0", callProb);
			callRaiseInstance(p, "0", raiseProb);
			callRaiseInstance(p, "fold", callRaiseAction);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	protected void logCall(PlayerData p) {
		try {
			callRaiseInstance(p, "0", foldProb);
			callRaiseInstance(p, "1", callProb);
			callRaiseInstance(p, "0", raiseProb);
			callRaiseInstance(p, "call", callRaiseAction);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	protected void logCheck(PlayerData p) {
		try {
			checkBetInstance(p, "0", betProb);
			checkBetInstance(p, "check", checkBetAction);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	protected void logRaise(PlayerData p) {
		try {
			callRaiseInstance(p, "0", foldProb);
			callRaiseInstance(p, "0", callProb);
			callRaiseInstance(p, "1", raiseProb);
			callRaiseInstance(p, "raise", callRaiseAction);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	protected void logBet(PlayerData p) {
		try {
			checkBetInstance(p, "1", betProb);
			checkBetInstance(p, "bet", checkBetAction);
		} catch (IOException e) {
			throw new IllegalStateException(e);
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
		// Amounts
		file.write(getPotSize() + ",");
		file.write(p.getDeficit(this) + ",");
		file.write(p.getPotOdds(this) + ",");
		file.write(p.getStackSize(this) + ",");
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
		// Amounts
		file.write(getPotSize() + ",");
		file.write(p.getStackSize(this) + ",");
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
					if (line.startsWith(flop)) {
						signalFlop();
					} else if (line.startsWith(turn)) {
						signalTurn();
					} else if (line.startsWith(river)) {
						signalRiver();
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
		new PropositionalDataSetgenerator().run();
	}
}
