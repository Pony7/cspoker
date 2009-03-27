package org.cspoker.client.bots.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PropositionalDataSetgenerator {

	private FileWriter foldProb;
	private FileWriter callProb;
	private FileWriter raiseProb;
	private FileWriter betProb; 
	private FileWriter callRaiseAction;
	private FileWriter checkBetAction; 

	public PropositionalDataSetgenerator() throws IOException {
		foldProb = new FileWriter("output/FoldProb.arff");
		foldProb.write(
				"@relation FoldProb\n"+
				callRaiseHeader+
				"@attribute probability real\n"+
				"@data\n"
		);
		callProb = new FileWriter("output/CallProb.arff");
		callProb.write(
				"@relation CallProb\n"+
				callRaiseHeader+
				"@attribute probability real\n"+
				"@data\n"
		);
		raiseProb = new FileWriter("output/RaiseProb.arff");
		raiseProb.write(
				"@relation RaiseProb\n"+
				callRaiseHeader+
				"@attribute probability real\n"+
				"@data\n"
		);
		betProb = new FileWriter("output/BetProb.arff");
		betProb.write(
				"@relation BetProb\n"+
				checkBetHeader+
				"@attribute probability real\n"+
				"@data\n"
		);
		callRaiseAction = new FileWriter("output/CallRaiseAction.arff");
		callRaiseAction.write(
				"@relation CallRaiseAction\n"+
				callRaiseHeader+
				"@attribute action {fold,call,raise}\n"+
				"@data\n"
		);
		checkBetAction = new FileWriter("output/CheckBetAction.arff");
		checkBetAction.write(
				"@relation CheckBetAction\n"+
				checkBetHeader+
				"@attribute action {check,bet}\n"+
				"@data\n"
		);

	}

	private void close() throws IOException {
		foldProb.close();
		callProb.close();
		raiseProb.close();
		betProb.close(); 
		callRaiseAction.close();
		checkBetAction.close(); 
	}

	private void logFold(Player p) throws IOException {
		callRaiseInstance(p, "1", foldProb);
		callRaiseInstance(p, "0", callProb);
		callRaiseInstance(p, "0", raiseProb);
		callRaiseInstance(p, "fold", callRaiseAction);
	}

	private void logCall(Player p) throws IOException {
		callRaiseInstance(p, "0", foldProb);
		callRaiseInstance(p, "1", callProb);
		callRaiseInstance(p, "0", raiseProb);
		callRaiseInstance(p, "call", callRaiseAction);
	}

	private void logCheck(Player p) throws IOException {
		checkBetInstance(p, "0", betProb);
		checkBetInstance(p, "check", checkBetAction);
	}

	private void logRaise(Player p) throws IOException {
		callRaiseInstance(p, "0", foldProb);
		callRaiseInstance(p, "0", callProb);
		callRaiseInstance(p, "1", raiseProb);
		callRaiseInstance(p, "raise", callRaiseAction);
	}

	private void logBet(Player p) throws IOException {
		checkBetInstance(p, "1", betProb);
		checkBetInstance(p, "bet", checkBetAction);
	}

	private static final String callRaiseHeader = 
		//Timing
		"@attribute roundCompletion real\n"+
		"@attribute playersActed integer\n"+
		"@attribute playersToAct integer\n"+
		"@attribute round {preflop,flop,turn,river}\n"+
		"@attribute gameCount integer\n"+
		"@attribute somebodyActedThisRound {true,false}\n"+
		//Amounts
		"@attribute potSize real\n"+
		"@attribute deficit real\n"+
		"@attribute potOdds real\n"+
		"@attribute stack real\n"+
		//Player count
		"@attribute nbSeatedPlayers integer\n"+
		"@attribute nbActivePlayers integer\n"+
		"@attribute activePlayerRatio real\n"+
		//Global player frequencies
		"@attribute foldFrequency real\n"+
		"@attribute callFrequency real\n"+
		"@attribute raiseFrequency real\n"+
		//Per-round player frequencies
		"@attribute foldFrequencyRound real\n"+
		"@attribute callFrequencyRound real\n"+
		"@attribute raiseFrequencyRound real\n"+
		//Game betting behaviour
		"@attribute isComitted {true,false}\n"+
		"@attribute nbAllPlayerRaises integer\n"+
		"@attribute nbPlayerRaises integer\n"+
		"@attribute gameRaisePercentage real\n"+
		"@attribute lastActionWasRaise {true,false}\n"+
		//PT Stats
		"@attribute VPIP real\n"+
		"@attribute PFR real\n"+
		"@attribute AF real\n"+
		"@attribute WtSD real\n";
	//"@attribute W$SD real\n"+

	private void callRaiseInstance(Player p, String target, FileWriter file) throws IOException {
		//Timing
		file.write(getRoundCompletion()+",");
		file.write(getPlayersActed()+",");
		file.write(getPlayersToAct()+",");
		file.write(getRound()+",");
		file.write(p.getGameCount()+",");
		file.write(isSomebodyActedThisRound()+",");
		//Amounts
		file.write(getPotSize()+",");
		file.write(p.getDeficit()+",");
		file.write(p.getPotOdds()+",");	
		file.write(p.getStackSize()+",");
		//Player count
		file.write(getNbSeatedPlayers()+",");
		file.write(getNbActivePlayers()+",");
		file.write(getActivePlayerRatio()+",");
		//Global player frequencies
		file.write(p.getFoldFrequency()+",");
		file.write(p.getCallFrequency()+",");
		file.write(p.getRaiseFrequency()+",");
		//Per-round player frequencies
		file.write(p.getRoundFoldFrequency()+",");
		file.write(p.getRoundCallFrequency()+",");
		file.write(p.getRoundRaiseFrequency()+",");
		//Game betting behaviour
		file.write(p.isComitted()+",");
		file.write(getNbGameRaises()+",");
		file.write(p.getNbPlayerRaises()+",");
		file.write(p.getGameRaisePercentage()+",");
		file.write(p.isLastActionWasRaise()+",");
		//PT Stats
		file.write(p.getVPIP()+",");
		file.write(p.getPFR()+",");
		file.write(p.getAF()+",");
		file.write(p.getWtSD()+",");
		//"@attribute W$SD real\n"+
		//Target
		file.write(target+"\n");
		file.flush();
	}	

	private static final String checkBetHeader = 
		//Timing
		"@attribute roundCompletion real\n"+
		"@attribute playersActed integer\n"+
		"@attribute playersToAct integer\n"+
		"@attribute round {preflop,flop,turn,river}\n"+
		"@attribute gameCount integer\n"+
		"@attribute somebodyActedThisRound {true,false}\n"+
		//Amounts
		"@attribute potSize real\n"+
		"@attribute stack real\n"+
		//Player count
		"@attribute nbSeatedPlayers integer\n"+
		"@attribute nbActivePlayers integer\n"+
		"@attribute activePlayerRatio real\n"+
		//Global player frequencies
		"@attribute betFrequency real\n"+
		//Per-round player frequencies
		"@attribute betFrequencyRound real\n"+
		//Game betting behaviour
		"@attribute isComitted {true,false}\n"+
		"@attribute nbAllPlayerRaises integer\n"+
		"@attribute nbPlayerRaises integer\n"+
		"@attribute gameRaisePercentage real\n"+
		"@attribute lastActionWasRaise {true,false}\n"+
		//PT Stats
		"@attribute VPIP real\n"+
		"@attribute PFR real\n"+
		"@attribute AF real\n"+
		"@attribute WtSD real\n";
	//"@attribute W$SD real\n"+

	private void checkBetInstance(Player p, String target, FileWriter file) throws IOException {
		//Timing
		file.write(getRoundCompletion()+",");
		file.write(getPlayersActed()+",");
		file.write(getPlayersToAct()+",");
		file.write(getRound()+",");
		file.write(p.getGameCount()+",");
		file.write(isSomebodyActedThisRound()+",");
		//Amounts
		file.write(getPotSize()+",");
		file.write(p.getStackSize()+",");
		//Player count
		file.write(getNbSeatedPlayers()+",");
		file.write(getNbActivePlayers()+",");
		file.write(getActivePlayerRatio()+",");
		//Global player frequencies
		file.write(p.getBetFrequency()+",");
		//Per-round player frequencies
		file.write(p.getRoundBetFrequency()+",");
		//Game betting behaviour
		file.write(p.isComitted()+",");
		file.write(getNbGameRaises()+",");
		file.write(p.getNbPlayerRaises()+",");
		file.write(p.getGameRaisePercentage()+",");
		file.write(p.isLastActionWasRaise()+",");
		//PT Stats
		file.write(p.getVPIP()+",");
		file.write(p.getPFR()+",");
		file.write(p.getAF()+",");
		file.write(p.getWtSD()+",");
		//"@attribute W$SD real\n"+
		//Target
		file.write(target+"\n");
		file.flush();
	}	

	public void run() throws IOException {
		try{
			String line;
			File dir1 = new File("/home/guy/Werk/thesis/opponentmodel/data2/unzipped");
			String[] children1 = dir1.list();
			if (children1 == null) {
				// Either dir does not exist or is not a directory
			} else {
				for (int i=0; i<children1.length; i++) {
					File child1 = new File(dir1,children1[i]);
					String[] children2 = child1.list();
					if (children2 == null) {
						// Either dir does not exist or is not a directory
					} else {
						for (int j=0; j<children2.length; j++) {
							// Get filename of file or directory
							System.out.println("Starting file: "+children1[i]+"/"+children2[j]);
							BufferedReader r = new BufferedReader(new FileReader(new File(child1,children2[j])));
							while((line=r.readLine())!=null){
								doLine(line);
							}
							r.close();
						}
					}
				}
			}
		}finally{
			close();
		}
	}

	private final static String hole = "*** HOLE";
	private final static String flop = "*** FLOP";
	private final static String turn = "*** TURN";
	private final static String river = "*** RIVER";

	private class Player{

		final String name;
		float stack;
		float bet;
		boolean comitted;
		int nbPlayerRaisesThisGame;
		boolean lastActionWasRaise;
		int gameCount = 0;
		int VPIPCount = 0;
		int PFRCount = 0;
		int flopCount = 0;
		int showdownCount = 0;

		int nbBetsPreFlop = 0;
		int nbChecksPreFlop = 0;
		
		int nbFoldsPreFlop = 0;
		int nbCallsPreFlop = 0;
		int nbRaisePreFlop = 0;

		int nbBetsFlop = 0;
		int nbChecksFlop = 0;
		
		int nbFoldsFlop = 0;
		int nbCallsFlop = 0;
		int nbRaiseFlop = 0;

		int nbBetsTurn = 0;
		int nbChecksTurn = 0;
		
		int nbFoldsTurn = 0;
		int nbCallsTurn = 0;
		int nbRaiseTurn = 0;

		int nbBetsRiver = 0;
		int nbChecksRiver = 0;
		
		int nbFoldsRiver = 0;
		int nbCallsRiver = 0;
		int nbRaiseRiver = 0;


		public Player(String name) {
			this.name = name;
		}

		private float getStackSize() {
			return stack/bigBlind;
		}

		public int getGameCount() {
			return gameCount;
		}

		private float getDeficit() {
			return Math.min(stack, (maxBet-bet))/bigBlind;
		}

		private float getPotOdds() {
			float potSize = getPotSize(); 
			float deficit = getDeficit();
			return deficit/(deficit+potSize);
		}

		private int getNbPlayerRaises(){
			return nbPlayerRaisesThisGame;
		}

		private float getGameRaisePercentage(){
			if(nbRaisesThisGame==0){
				return 0;
			}
			return nbPlayerRaisesThisGame/(float)nbRaisesThisGame;
		}

		private float getRaiseFrequency() {
			float nbRaises = nbRaisePreFlop+nbRaiseFlop+nbRaiseTurn+nbRaiseRiver;
			float nbFolds = nbFoldsPreFlop+nbFoldsFlop+nbFoldsTurn+nbFoldsRiver;
			float nbCalls = nbCallsPreFlop+nbCallsFlop+nbCallsTurn+nbCallsRiver;
			return (0.16F*3+nbRaises)/(3+nbRaises+nbFolds+nbCalls);
		}

		private float getCallFrequency() {
			float nbRaises = nbRaisePreFlop+nbRaiseFlop+nbRaiseTurn+nbRaiseRiver;
			float nbFolds = nbFoldsPreFlop+nbFoldsFlop+nbFoldsTurn+nbFoldsRiver;
			float nbCalls = nbCallsPreFlop+nbCallsFlop+nbCallsTurn+nbCallsRiver;
			return (0.13F*3+nbCalls)/(3+nbRaises+nbFolds+nbCalls);
		}

		private float getFoldFrequency() {
			float nbRaises = nbRaisePreFlop+nbRaiseFlop+nbRaiseTurn+nbRaiseRiver;
			float nbFolds = nbFoldsPreFlop+nbFoldsFlop+nbFoldsTurn+nbFoldsRiver;
			float nbCalls = nbCallsPreFlop+nbCallsFlop+nbCallsTurn+nbCallsRiver;
			return (0.71F*3+nbFolds)/(3+nbRaises+nbFolds+nbCalls);
		}

		private float getCheckFrequency() {
			return 1-getBetFrequency();
		}

		private float getBetFrequency() {
			float nbChecks = nbChecksPreFlop+nbChecksFlop+nbChecksTurn+nbChecksRiver;
			float nbBets = nbBetsPreFlop+nbBetsFlop+nbBetsTurn+nbBetsRiver;
			return (0.34F*3+nbBets)/(3+nbChecks+nbBets);
		}

		private final static int memory = 4;
		
		private float getRoundRaiseFrequency() {
			if(round.equals("preflop")){
				return (0.15F*memory+nbRaisePreFlop)/(memory+nbFoldsPreFlop+nbCallsPreFlop+nbRaisePreFlop);
			}else if(round.equals("flop")){
				return (0.12F*memory+nbRaiseFlop)/(memory+nbFoldsFlop+nbCallsFlop+nbRaiseFlop);
			}else if(round.equals("turn")){
				return (0.1F*memory+nbRaiseTurn)/(memory+nbFoldsTurn+nbCallsTurn+nbRaiseTurn);
			}else if(round.equals("river")){
				return (0.09F*memory+nbRaiseRiver)/(memory+nbFoldsRiver+nbCallsRiver+nbRaiseRiver);
			}
			else throw new IllegalStateException(round);
		}

		private float getRoundCallFrequency() {
			if(round.equals("preflop")){
				return (0.14F*memory+nbCallsPreFlop)/(memory+nbFoldsPreFlop+nbCallsPreFlop+nbRaisePreFlop);
			}else if(round.equals("flop")){
				return (0.29F*memory+nbCallsFlop)/(memory+nbFoldsFlop+nbCallsFlop+nbRaiseFlop);
			}else if(round.equals("turn")){
				return (0.37F*memory+nbCallsTurn)/(memory+nbFoldsTurn+nbCallsTurn+nbRaiseTurn);
			}else if(round.equals("river")){
				return (0.35F*memory+nbCallsRiver)/(memory+nbFoldsRiver+nbCallsRiver+nbRaiseRiver);
			}
			else throw new IllegalStateException(round);
		}

		private float getRoundFoldFrequency() {
			if(round.equals("preflop")){
				return (0.71F*memory+nbFoldsPreFlop)/(memory+nbFoldsPreFlop+nbCallsPreFlop+nbRaisePreFlop);
			}else if(round.equals("flop")){
				return (0.59F*memory+nbFoldsFlop)/(memory+nbFoldsFlop+nbCallsFlop+nbRaiseFlop);
			}else if(round.equals("turn")){
				return (0.53F*memory+nbFoldsTurn)/(memory+nbFoldsTurn+nbCallsTurn+nbRaiseTurn);
			}else if(round.equals("river")){
				return (0.56F*memory+nbFoldsRiver)/(memory+nbFoldsRiver+nbCallsRiver+nbRaiseRiver);
			}
			else throw new IllegalStateException(round);
		}

		private float getRoundBetFrequency() {
			if(round.equals("preflop")){
				return (0.15F*memory+nbBetsPreFlop)/(memory+nbBetsPreFlop+nbChecksPreFlop);
			}else if(round.equals("flop")){
				return (0.38F*memory+nbBetsFlop)/(memory+nbBetsFlop+nbChecksFlop);
			}else if(round.equals("turn")){
				return (0.36F*memory+nbBetsTurn)/(memory+nbBetsTurn+nbChecksTurn);
			}else if(round.equals("river")){
				return (0.35F*memory+nbBetsRiver)/(memory+nbBetsRiver+nbChecksRiver);
			}
			else throw new IllegalStateException(round);
		}

		private float getRoundCheckFrequency() {
			return 1-getRoundBetFrequency();
		}

		private void addRaise() {
			if(round.equals("preflop")){
				++nbRaisePreFlop;
			}else if(round.equals("flop")){
				++nbRaiseFlop;
			}else if(round.equals("turn")){
				++nbRaiseTurn;
			}else if(round.equals("river")){
				++nbRaiseRiver;
			}
			else throw new IllegalStateException(round);
		}

		private void addCall() {
			if(round.equals("preflop")){
				++nbCallsPreFlop;
			}else if(round.equals("flop")){
				++nbCallsFlop;
			}else if(round.equals("turn")){
				++nbCallsTurn;
			}else if(round.equals("river")){
				++nbCallsRiver;
			}
			else throw new IllegalStateException(round);
		}

		private void addFold() {
			if(round.equals("preflop")){
				++nbFoldsPreFlop;
			}else if(round.equals("flop")){
				++nbFoldsFlop;
			}else if(round.equals("turn")){
				++nbFoldsTurn;
			}else if(round.equals("river")){
				++nbFoldsRiver;
			}
			else throw new IllegalStateException(round);
		}

		private void addBet() {
			if(round.equals("preflop")){
				++nbBetsPreFlop;
			}else if(round.equals("flop")){
				++nbBetsFlop;
			}else if(round.equals("turn")){
				++nbBetsTurn;
			}else if(round.equals("river")){
				++nbBetsRiver;
			}
			else throw new IllegalStateException(round);
		}

		private void addCheck() {
			if(round.equals("preflop")){
				++nbChecksPreFlop;
			}else if(round.equals("flop")){
				++nbChecksFlop;
			}else if(round.equals("turn")){
				++nbChecksTurn;
			}else if(round.equals("river")){
				++nbChecksRiver;
			}
			else throw new IllegalStateException(round);
		}

		//"@attribute VPIP real\n"+
		private float getVPIP(){
			return (0.235F*memory+VPIPCount)/(memory+gameCount);
		}

		//"@attribute PFR real\n"+
		private float getPFR(){
			return (0.144F*memory+PFRCount)/(memory+gameCount);
		}

		//"@attribute AF real\n"+

		public float getAF() {
			float nbRaises = nbRaisePreFlop+nbRaiseFlop+nbRaiseTurn+nbRaiseRiver;
			float nbCalls = nbCallsPreFlop+nbCallsFlop+nbCallsTurn+nbCallsRiver;
			float nbBets = nbBetsPreFlop+nbBetsFlop+nbBetsTurn+nbBetsRiver;
			return (2.5F*memory+nbRaises+nbBets)/(memory+nbCalls);
		}

		//"@attribute WtSD real\n"+
		public float getWtSD() {
			return (0.57F*memory+showdownCount)/(memory+flopCount);
		}

		private boolean isComitted() {
			return comitted;
		}

		private boolean isLastActionWasRaise() {
			return lastActionWasRaise;
		}

		public void startNewGame(){
			didVPIP = false;
			comitted = false;
			nbPlayerRaisesThisGame = 0;
			lastActionWasRaise = false;
			++gameCount;
			startNewRound();
		}

		public void startNewRound() {
			bet = 0;
			comitted = false;
		}

		private boolean didVPIP = false;

		public void updateVPIP() {
			if(!didVPIP && round.equals("preflop")){
				++VPIPCount;
				didVPIP = true;
			}
		}
		public void updatePFR() {
			if(nbPlayerRaisesThisGame==0 && round.equals("preflop")){
				++PFRCount;
			}
		}

	}
	private Map<String,Player> players = new HashMap<String, Player>();
	private List<Player> activePlayers = new LinkedList<Player>();

	private float bigBlind = 0;
	private float maxBet = 0;
	private boolean forgetCurrentGame = false;
	private boolean somebodyActedThisRound = false;

	private String round="preflop";
	private float totalPot = 0;
	private int nbRaisesThisGame = 0;
	private int roundCompletion = 0;
	private int nbSeatedPlayers = 0;;


	private void doLine(String line) throws IOException {
		//inputRaise.write(line+"\n");
		//		foldFile.write(line+"\n");
		//		System.out.println(line);
		if(line.startsWith("Full Tilt Poker Game ")){
			int temp = line.indexOf("/");
			bigBlind = Float.parseFloat(line.substring(temp+2, line.indexOf(" ", temp+2)).replaceAll(",", ""));
			round = "preflop";
			forgetCurrentGame = false;
			somebodyActedThisRound = false;
			nbRaisesThisGame = 0;
			roundCompletion = 0;
			nbSeatedPlayers = 0;
			activePlayers.clear();
		}else if(!forgetCurrentGame){
			if(line.startsWith("Seat ")){
				if(line.endsWith("(0)")){
					forgetCurrentGame = true;
				}else{
					int startName = line.indexOf(":")+2;
					int startDollar = line.indexOf("(",startName);
					String name = line.substring(startName, startDollar-1);
					Player p = players.get(name);
					if(p==null){
						p = new Player(name);
						players.put(name, p);
					}
					activePlayers.add(p);
					++nbSeatedPlayers;
					p.startNewGame();
					p.stack = Float.parseFloat(line.substring(startDollar+2, line.indexOf(")", startDollar)).replaceAll(",", ""));
				}
			}else if(line.startsWith("*** ")){
				if(line.startsWith("*** SUMMARY ***")){
					forgetCurrentGame = true;
					if(round.equals("flop")||round.equals("turn")||round.equals("river")){
						for (Player p : activePlayers) {
							++p.showdownCount;
						}
					}
				}else{
					if(line.startsWith(flop)){
						round = "flop";
						startNewRound();
						for (Player p : activePlayers) {
							++p.flopCount;
						}
					}else if(line.startsWith(turn)){
						round = "turn";
						startNewRound();
					}else if(line.startsWith(river)){
						round = "river";
						startNewRound();
					}
				}
			}else if(line.contains(":")){
				//ignore chat message
			}else if(line.contains(" posts the small blind")){
				Player p = players.get(line.substring(0, line.indexOf(" posts the small blind")));

				maxBet = bigBlind/2;
				totalPot = bigBlind/2;
				if(line.contains("all in")){
					activePlayers.remove(p);
				}

				p.bet = bigBlind/2;
				p.stack -=bigBlind/2;
				p.comitted = true;
			}else if(line.contains(" posts the big blind")){
				Player p = players.get(line.substring(0, line.indexOf(" posts the big blind")));

				maxBet = bigBlind;
				totalPot += bigBlind;
				if(line.contains("all in")){
					activePlayers.remove(p);
				}

				p.bet = bigBlind;
				p.stack -=bigBlind;
				p.comitted = true;
			}else if(line.endsWith(" folds")){
				Player p = players.get(line.substring(0, line.indexOf(" folds")));
				logFold(p);

				activePlayers.remove(p);
				somebodyActedThisRound = true;

				p.lastActionWasRaise = false;
				p.addFold();
			}else if(line.contains(" calls")){
				Player p = players.get(line.substring(0, line.indexOf(" calls")));
				logCall(p);

				int allinIndex = line.lastIndexOf(", and is all in");
				if(allinIndex<=0){
					allinIndex = line.length();
				}else{
					activePlayers.remove(p);
				}
				float movedAmount = Float.parseFloat(line.substring(line.indexOf("$")+1,allinIndex).replaceAll(",", ""));
				totalPot += movedAmount;
				++roundCompletion;
				somebodyActedThisRound = true;

				p.bet += movedAmount;
				p.stack -= movedAmount;
				p.lastActionWasRaise = false;
				p.updateVPIP();
				p.comitted = true;
				p.addCall();
			}else if(line.contains(" raises")){
				Player p = players.get(line.substring(0, line.indexOf(" raises")));
				if(p.getDeficit()<=0){
					//raise by big blind, treat kinda like bet
					logBet(p);
					int allinIndex = line.lastIndexOf(", and is all in");
					if(allinIndex<=0){
						allinIndex = line.length();
					}else{
						activePlayers.remove(p);
					}
					maxBet = Float.parseFloat(line.substring(line.indexOf("$")+1,allinIndex).replaceAll(",", ""));
					float movedAmount = maxBet - p.bet;
					if(movedAmount<0){
						throw new IllegalStateException(line);
					}
					totalPot += movedAmount;
					++nbRaisesThisGame;
					somebodyActedThisRound = true;

					p.bet = maxBet;
					p.stack -= movedAmount;
					p.lastActionWasRaise = true;
					p.addBet();
					p.updateVPIP();
					p.updatePFR();
					++p.nbPlayerRaisesThisGame;
					p.comitted = true;
				}else{
					logRaise(p);
					int allinIndex = line.lastIndexOf(", and is all in");
					if(allinIndex<=0){
						allinIndex = line.length();
					}else{
						activePlayers.remove(p);
					}
					maxBet = Float.parseFloat(line.substring(line.indexOf("$")+1,allinIndex).replaceAll(",", ""));
					double movedAmount = maxBet - p.bet;
					totalPot += movedAmount;
					++nbRaisesThisGame;
					somebodyActedThisRound = true;

					p.bet = maxBet;
					p.stack -= movedAmount;
					p.addRaise();
					p.updateVPIP();
					p.updatePFR();
					++p.nbPlayerRaisesThisGame;
					p.lastActionWasRaise = true;
					p.comitted = true;
				}
				roundCompletion=0;
			}else if(line.endsWith(" checks")){
				Player p = players.get(line.substring(0, line.indexOf(" checks")));
				logCheck(p);
				
				++roundCompletion;
				somebodyActedThisRound = true;

				p.lastActionWasRaise = false;
				p.addCheck();
			}else if(line.contains(" bets")){
				Player p = players.get(line.substring(0, line.indexOf(" bets")));
				logBet(p);

				int allinIndex = line.lastIndexOf(", and is all in");
				if(allinIndex<=0){
					allinIndex = line.length();
				}else{
					activePlayers.remove(p);
				}
				maxBet = Float.parseFloat(line.substring(line.indexOf("$")+1,allinIndex).replaceAll(",", ""));
				totalPot += maxBet;
				++nbRaisesThisGame;
				roundCompletion = 0;
				somebodyActedThisRound = true;

				p.bet = maxBet;
				p.stack -= maxBet;
				p.comitted = true;
				p.lastActionWasRaise = true;
				p.addBet();
				++p.nbPlayerRaisesThisGame;
			}
		}
	}

	private void startNewRound() {
		for(Player player :activePlayers){
			player.startNewRound();
		}
		roundCompletion = 0;
		maxBet = 0;
		somebodyActedThisRound = false;
	}

	public boolean isSomebodyActedThisRound() {
		return somebodyActedThisRound;
	}

	private int getNbGameRaises() {
		return nbRaisesThisGame;
	}

	private int getNbSeatedPlayers() {
		return nbSeatedPlayers ;
	}

	public int getNbActivePlayers() {
		return activePlayers.size();
	}

	private float getActivePlayerRatio() {
		return (float)getNbActivePlayers()/((float)getNbSeatedPlayers());
	}

	private float getPotSize() {
		return totalPot/bigBlind;
	}

	private int getPlayersToAct() {
		if(somebodyActedThisRound){
			return getNbActivePlayers()-roundCompletion-1;
		}
		return getNbActivePlayers()-roundCompletion;
	}

	private int getPlayersActed() {
		return roundCompletion;
	}

	private float getRoundCompletion(){
		if(isSomebodyActedThisRound()){
			if(getNbActivePlayers()<=1){
				return 0;
			}
			return (roundCompletion)/(((float)(getNbActivePlayers()-1)));
		}
		return roundCompletion/(((float)(getNbActivePlayers())));
	}

	public String getRound() {
		return round;
	}

	public static void main(String[] args) throws IOException {
		(new PropositionalDataSetgenerator()).run();
	}
}

