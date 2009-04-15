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

public class PlayerData implements Cloneable{

	final Object id;
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

	public PlayerData(Object id) {
		this.id = id;
	}

	@Override
	protected PlayerData clone(){
		try {
			return (PlayerData) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	public Object getId() {
		return id;
	}

	public float getStackSize(Propositionalizer p) {
		return stack / p.bigBlind;
	}

	public float getBet() {
		return bet;
	}

	public int getGameCount() {
		return gameCount;
	}

	public float getDeficit(Propositionalizer p) {
		return Math.min(stack, (p.maxBet - bet)) / p.bigBlind;
	}

	public float getPotOdds(Propositionalizer p) {
		float potSize = p.getPotSize();
		float deficit = getDeficit(p);
		return deficit / (deficit + potSize);
	}

	public int getNbPlayerRaises() {
		return nbPlayerRaisesThisGame;
	}

	public float getGameRaisePercentage(Propositionalizer p) {
		if (p.nbRaisesThisGame == 0) {
			return 0;
		}
		return nbPlayerRaisesThisGame / (float) p.nbRaisesThisGame;
	}

	public float getRaiseFrequency() {
		float nbRaises = nbRaisePreFlop + nbRaiseFlop + nbRaiseTurn
		+ nbRaiseRiver;
		float nbFolds = nbFoldsPreFlop + nbFoldsFlop + nbFoldsTurn
		+ nbFoldsRiver;
		float nbCalls = nbCallsPreFlop + nbCallsFlop + nbCallsTurn
		+ nbCallsRiver;
		return (0.16F * 3 + nbRaises) / (3 + nbRaises + nbFolds + nbCalls);
	}

	public float getCallFrequency() {
		float nbRaises = nbRaisePreFlop + nbRaiseFlop + nbRaiseTurn
		+ nbRaiseRiver;
		float nbFolds = nbFoldsPreFlop + nbFoldsFlop + nbFoldsTurn
		+ nbFoldsRiver;
		float nbCalls = nbCallsPreFlop + nbCallsFlop + nbCallsTurn
		+ nbCallsRiver;
		return (0.13F * 3 + nbCalls) / (3 + nbRaises + nbFolds + nbCalls);
	}

	public float getFoldFrequency() {
		float nbRaises = nbRaisePreFlop + nbRaiseFlop + nbRaiseTurn
		+ nbRaiseRiver;
		float nbFolds = nbFoldsPreFlop + nbFoldsFlop + nbFoldsTurn
		+ nbFoldsRiver;
		float nbCalls = nbCallsPreFlop + nbCallsFlop + nbCallsTurn
		+ nbCallsRiver;
		return (0.71F * 3 + nbFolds) / (3 + nbRaises + nbFolds + nbCalls);
	}

	public float getCheckFrequency() {
		return 1 - getBetFrequency();
	}

	public float getBetFrequency() {
		float nbChecks = nbChecksPreFlop + nbChecksFlop + nbChecksTurn
		+ nbChecksRiver;
		float nbBets = nbBetsPreFlop + nbBetsFlop + nbBetsTurn
		+ nbBetsRiver;
		return (0.34F * 3 + nbBets) / (3 + nbChecks + nbBets);
	}

	protected final static int memory = 4;

	public float getRoundRaiseFrequency(Propositionalizer p) {
		if (p.round.equals("preflop")) {
			return (0.15F * memory + nbRaisePreFlop)
			/ (memory + nbFoldsPreFlop + nbCallsPreFlop + nbRaisePreFlop);
		} else if (p.round.equals("flop")) {
			return (0.12F * memory + nbRaiseFlop)
			/ (memory + nbFoldsFlop + nbCallsFlop + nbRaiseFlop);
		} else if (p.round.equals("turn")) {
			return (0.1F * memory + nbRaiseTurn)
			/ (memory + nbFoldsTurn + nbCallsTurn + nbRaiseTurn);
		} else if (p.round.equals("river")) {
			return (0.09F * memory + nbRaiseRiver)
			/ (memory + nbFoldsRiver + nbCallsRiver + nbRaiseRiver);
		} else {
			throw new IllegalStateException(p.round);
		}
	}

	public float getRoundCallFrequency(Propositionalizer p) {
		if (p.round.equals("preflop")) {
			return (0.14F * memory + nbCallsPreFlop)
			/ (memory + nbFoldsPreFlop + nbCallsPreFlop + nbRaisePreFlop);
		} else if (p.round.equals("flop")) {
			return (0.29F * memory + nbCallsFlop)
			/ (memory + nbFoldsFlop + nbCallsFlop + nbRaiseFlop);
		} else if (p.round.equals("turn")) {
			return (0.37F * memory + nbCallsTurn)
			/ (memory + nbFoldsTurn + nbCallsTurn + nbRaiseTurn);
		} else if (p.round.equals("river")) {
			return (0.35F * memory + nbCallsRiver)
			/ (memory + nbFoldsRiver + nbCallsRiver + nbRaiseRiver);
		} else {
			throw new IllegalStateException(p.round);
		}
	}

	public float getRoundFoldFrequency(Propositionalizer p) {
		if (p.round.equals("preflop")) {
			return (0.71F * memory + nbFoldsPreFlop)
			/ (memory + nbFoldsPreFlop + nbCallsPreFlop + nbRaisePreFlop);
		} else if (p.round.equals("flop")) {
			return (0.59F * memory + nbFoldsFlop)
			/ (memory + nbFoldsFlop + nbCallsFlop + nbRaiseFlop);
		} else if (p.round.equals("turn")) {
			return (0.53F * memory + nbFoldsTurn)
			/ (memory + nbFoldsTurn + nbCallsTurn + nbRaiseTurn);
		} else if (p.round.equals("river")) {
			return (0.56F * memory + nbFoldsRiver)
			/ (memory + nbFoldsRiver + nbCallsRiver + nbRaiseRiver);
		} else {
			throw new IllegalStateException(p.round);
		}
	}

	public float getRoundBetFrequency(Propositionalizer p) {
		if (p.round.equals("preflop")) {
			return (0.15F * memory + nbBetsPreFlop)
			/ (memory + nbBetsPreFlop + nbChecksPreFlop);
		} else if (p.round.equals("flop")) {
			return (0.38F * memory + nbBetsFlop)
			/ (memory + nbBetsFlop + nbChecksFlop);
		} else if (p.round.equals("turn")) {
			return (0.36F * memory + nbBetsTurn)
			/ (memory + nbBetsTurn + nbChecksTurn);
		} else if (p.round.equals("river")) {
			return (0.35F * memory + nbBetsRiver)
			/ (memory + nbBetsRiver + nbChecksRiver);
		} else {
			throw new IllegalStateException(p.round);
		}
	}

	public float getRoundCheckFrequency(Propositionalizer p) {
		return 1 - getRoundBetFrequency(p);
	}

	protected void addRaise(Propositionalizer p) {
		if (p.round.equals("preflop")) {
			++nbRaisePreFlop;
		} else if (p.round.equals("flop")) {
			++nbRaiseFlop;
		} else if (p.round.equals("turn")) {
			++nbRaiseTurn;
		} else if (p.round.equals("river")) {
			++nbRaiseRiver;
		} else {
			throw new IllegalStateException(p.round);
		}
	}

	protected void addCall(Propositionalizer p) {
		if (p.round.equals("preflop")) {
			++nbCallsPreFlop;
		} else if (p.round.equals("flop")) {
			++nbCallsFlop;
		} else if (p.round.equals("turn")) {
			++nbCallsTurn;
		} else if (p.round.equals("river")) {
			++nbCallsRiver;
		} else {
			throw new IllegalStateException(p.round);
		}
	}

	protected void addFold(Propositionalizer p) {
		if (p.round.equals("preflop")) {
			++nbFoldsPreFlop;
		} else if (p.round.equals("flop")) {
			++nbFoldsFlop;
		} else if (p.round.equals("turn")) {
			++nbFoldsTurn;
		} else if (p.round.equals("river")) {
			++nbFoldsRiver;
		} else {
			throw new IllegalStateException(p.round);
		}
	}

	protected void addBet(Propositionalizer p) {
		if (p.round.equals("preflop")) {
			++nbBetsPreFlop;
		} else if (p.round.equals("flop")) {
			++nbBetsFlop;
		} else if (p.round.equals("turn")) {
			++nbBetsTurn;
		} else if (p.round.equals("river")) {
			++nbBetsRiver;
		} else {
			throw new IllegalStateException(p.round);
		}
	}

	protected void addCheck(Propositionalizer p) {
		if (p.round.equals("preflop")) {
			++nbChecksPreFlop;
		} else if (p.round.equals("flop")) {
			++nbChecksFlop;
		} else if (p.round.equals("turn")) {
			++nbChecksTurn;
		} else if (p.round.equals("river")) {
			++nbChecksRiver;
		} else {
			throw new IllegalStateException(p.round);
		}
	}

	// "@attribute VPIP real\n"+
	public float getVPIP() {
		return (0.235F * memory + VPIPCount) / (memory + gameCount);
	}

	// "@attribute PFR real\n"+
	public float getPFR() {
		return (0.144F * memory + PFRCount) / (memory + gameCount);
	}

	// "@attribute AF real\n"+

	public float getAF() {
		float nbRaises = nbRaisePreFlop + nbRaiseFlop + nbRaiseTurn
		+ nbRaiseRiver;
		float nbCalls = nbCallsPreFlop + nbCallsFlop + nbCallsTurn
		+ nbCallsRiver;
		float nbBets = nbBetsPreFlop + nbBetsFlop + nbBetsTurn
		+ nbBetsRiver;
		return (2.5F * memory + nbRaises + nbBets) / (memory + nbCalls);
	}

	// "@attribute WtSD real\n"+
	public float getWtSD() {
		return (0.57F * memory + showdownCount) / (memory + flopCount);
	}

	public boolean isComitted() {
		return comitted;
	}

	public boolean isLastActionWasRaise() {
		return lastActionWasRaise;
	}

	protected void startNewGame() {
		didVPIP = false;
		comitted = false;
		nbPlayerRaisesThisGame = 0;
		lastActionWasRaise = false;
		++gameCount;
		startNewRound();
	}

	protected void startNewRound() {
		bet = 0;
		comitted = false;
	}

	protected boolean didVPIP = false;

	protected void updateVPIP(Propositionalizer p) {
		if (!didVPIP && p.round.equals("preflop")) {
			++VPIPCount;
			didVPIP = true;
		}
	}

	protected void updatePFR(Propositionalizer p) {
		if (nbPlayerRaisesThisGame == 0 && p.round.equals("preflop")) {
			++PFRCount;
		}
	}

}
