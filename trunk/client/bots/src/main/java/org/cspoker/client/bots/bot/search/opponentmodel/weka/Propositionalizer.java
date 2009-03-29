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
package org.cspoker.client.bots.bot.search.opponentmodel.weka;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Propositionalizer implements Cloneable {

	Map<Object, PlayerData> players = new HashMap<Object, PlayerData>();
	List<PlayerData> activePlayers = new LinkedList<PlayerData>();
	float bigBlind = 0;
	float maxBet = 0;
	boolean somebodyActedThisRound = false;
	String round = "preflop";
	float totalPot = 0;
	int nbRaisesThisGame = 0;
	int roundCompletion = 0;
	int nbSeatedPlayers = 0;

	public Propositionalizer() {
	}

	@Override
	public Propositionalizer clone() {
		try {
			Propositionalizer clone = (Propositionalizer)super.clone();
			Map<Object, PlayerData> playersClone = new HashMap<Object, PlayerData>(clone.getPlayers());
			
			List<PlayerData> activePlayers = clone.getActivePlayers();
			List<PlayerData> activePlayersClone = new LinkedList<PlayerData>();
			for (PlayerData player : activePlayers) {
				PlayerData playerClone = player.clone();
				activePlayersClone.add(playerClone);
				playersClone.put(playerClone.getId(), playerClone);
			}
			
			clone.players = playersClone;
			clone.activePlayers = activePlayersClone;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}


	public void signalAllIn(Object id, int chipsMoved) {
		if(round.equals("preflop") && !somebodyActedThisRound){
			if(maxBet==0){
				signalSmallBlind(true, id);
			}else{
				signalBigBlind(true, id);
			}
		}
		PlayerData p = players.get(id);
		//call raise or bet
		if(p.getBet()==0){
			signalBet(true, id, chipsMoved);
		}else if(p.getDeficit(this)<chipsMoved){
			signalRaise(id, true, p.getBet()+chipsMoved);
		}else{
			signalCall(true, id, chipsMoved);
		}
	}

	public void signalBet(boolean isAllIn, Object id, float maxBetParsed) {
		PlayerData p = players.get(id);
		if(p.getBet()>0){
			//Bet/Raise on the big blind
			signalRaise(id, isAllIn, maxBetParsed);
		}else{
			logBet(p);
			if(isAllIn) {
				activePlayers.remove(p);
			}

			maxBet = maxBetParsed;
			totalPot += maxBet;
			++nbRaisesThisGame;
			roundCompletion = 0;
			somebodyActedThisRound = true;

			p.bet = maxBet;
			p.stack -= maxBet;
			p.comitted = true;
			p.lastActionWasRaise = true;
			p.addBet(this);
			++p.nbPlayerRaisesThisGame;
		}
	}

	protected void logBet(PlayerData p){
		// no op
	}

	protected void logRaise(PlayerData p){
		// no op
	}

	protected void logFold(PlayerData p){
		// no op
	}

	protected void logCall(PlayerData p){
		// no op
	}

	protected void logCheck(PlayerData p){
		// no op
	}

	public void signalCheck(Object id) {
		PlayerData p = players.get(id);
		logCheck(p);

		++roundCompletion;
		somebodyActedThisRound = true;

		p.lastActionWasRaise = false;
		p.addCheck(this);
	}
	
	

	public void signalRaise(Object id, boolean isAllIn, float maxBetParsed) {
		PlayerData p = players.get(id);
		if (p.getDeficit(this) <= 0) {
			// raise by big blind, treat kinda like bet
			logBet(p);
			if(isAllIn) {
				activePlayers.remove(p);
			}
			maxBet = maxBetParsed;
			float movedAmount = maxBet - p.bet;
			totalPot += movedAmount;
			++nbRaisesThisGame;
			somebodyActedThisRound = true;

			p.bet = maxBet;
			p.stack -= movedAmount;
			p.lastActionWasRaise = true;
			p.addBet(this);
			p.updateVPIP(this);
			p.updatePFR(this);
			++p.nbPlayerRaisesThisGame;
			p.comitted = true;
		} else {
			logRaise(p);
			if(isAllIn) {
				activePlayers.remove(p);
			}
			maxBet = maxBetParsed;
			double movedAmount = maxBet - p.bet;
			totalPot += movedAmount;
			++nbRaisesThisGame;
			somebodyActedThisRound = true;

			p.bet = maxBet;
			p.stack -= movedAmount;
			p.addRaise(this);
			p.updateVPIP(this);
			p.updatePFR(this);
			++p.nbPlayerRaisesThisGame;
			p.lastActionWasRaise = true;
			p.comitted = true;
		}
		roundCompletion = 0;
	}


	public void signalCall(boolean isAllIn, Object id) {
		PlayerData p = players.get(id);
		signalCall(isAllIn, id, Math.min(p.stack, maxBet-p.bet));
	}
	public void signalCall(boolean isAllIn, Object id, float movedAmount) {
		PlayerData p = players.get(id);
		logCall(p);

		if(isAllIn){
			activePlayers.remove(p);
		}
		totalPot += movedAmount;
		++roundCompletion;
		somebodyActedThisRound = true;

		p.bet += movedAmount;
		p.stack -= movedAmount;
		p.lastActionWasRaise = false;
		p.updateVPIP(this);
		p.comitted = true;
		p.addCall(this);
	}

	public void signalFold(Object id) {
		PlayerData p = players.get(id);
		logFold(p);

		activePlayers.remove(p);
		somebodyActedThisRound = true;

		p.lastActionWasRaise = false;
		p.addFold(this);
	}

	public void signalBigBlind(boolean isAllIn, Object id) {
		PlayerData p = players.get(id);

		maxBet = bigBlind;
		totalPot += bigBlind;
		if (isAllIn) {
			activePlayers.remove(p);
		}

		p.bet = bigBlind;
		p.stack -= bigBlind;
		p.comitted = true;
	}

	public void signalSmallBlind(boolean isAllIn, Object id) {
		PlayerData p = players.get(id);
		maxBet = bigBlind / 2;
		totalPot = bigBlind / 2;
		if (isAllIn) {
			activePlayers.remove(p);
		}
		p.bet = bigBlind / 2;
		p.stack -= bigBlind / 2;
		p.comitted = true;
	}

	public void signalRiver() {
		if(!activePlayers.isEmpty()){
			round = "river";
			startNewRound();
		}
	}

	public void signalTurn() {
		if(!activePlayers.isEmpty()){
			round = "turn";
			startNewRound();
		}
	}

	public void signalFlop() {
		if(!activePlayers.isEmpty()){
			round = "flop";
			startNewRound();
			for (PlayerData p : activePlayers) {
				++p.flopCount;
			}
		}
	}

	public void signalShowdown() {
		if (round.equals("flop") || round.equals("turn")
				|| round.equals("river")) {
			for (PlayerData p : activePlayers) {
				++p.showdownCount;
			}
		}
	}

	public void signalSeatedPlayer(float stack, Object id) {
		PlayerData p = players.get(id);
		if (p == null) {
			p = new PlayerData(id);
			players.put(id, p);
		}
		activePlayers.add(p);
		++nbSeatedPlayers;
		p.startNewGame();
		p.stack = stack;
	}

	public void signalNewGame(float bb) {
		bigBlind = bb;
		round = "preflop";
		somebodyActedThisRound = false;
		nbRaisesThisGame = 0;
		roundCompletion = 0;
		nbSeatedPlayers = 0;
		activePlayers.clear();
	}

	protected void startNewRound() {
		for (PlayerData player : activePlayers) {
			player.startNewRound();
		}
		roundCompletion = 0;
		maxBet = 0;
		somebodyActedThisRound = false;
	}

	public boolean isSomebodyActedThisRound() {
		return somebodyActedThisRound;
	}

	public List<PlayerData> getActivePlayers() {
		return activePlayers;
	}

	public Map<Object, PlayerData> getPlayers() {
		return players;
	}

	public float getTotalPot() {
		return totalPot;
	}

	public float getMaxBet() {
		return maxBet;
	}

	public float getBigBlind() {
		return bigBlind;
	}

	public int getNbRaisesThisGame() {
		return nbRaisesThisGame;
	}

	protected int getNbGameRaises() {
		return nbRaisesThisGame;
	}

	protected int getNbSeatedPlayers() {
		return nbSeatedPlayers;
	}

	public int getNbActivePlayers() {
		return activePlayers.size();
	}

	public float getActivePlayerRatio() {
		return (float) getNbActivePlayers() / (float) getNbSeatedPlayers();
	}

	public float getPotSize() {
		return totalPot / bigBlind;
	}

	public int getPlayersToAct() {
		if (somebodyActedThisRound) {
			return getNbActivePlayers() - roundCompletion - 1;
		}
		return getNbActivePlayers() - roundCompletion;
	}

	public int getPlayersActed() {
		return roundCompletion;
	}

	public float getRoundCompletion() {
		if (isSomebodyActedThisRound()) {
			if (getNbActivePlayers() <= 1) {
				return 0;
			}
			return roundCompletion / (float) (getNbActivePlayers() - 1);
		}
		return roundCompletion / (float) getNbActivePlayers();
	}

	public String getRound() {
		return round;
	}

}