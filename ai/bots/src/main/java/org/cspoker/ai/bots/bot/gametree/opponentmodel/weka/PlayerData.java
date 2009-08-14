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
package org.cspoker.ai.bots.bot.gametree.opponentmodel.weka;

/**
 * Class that tracks player statistics when playing games. 
 * 
 * @author guy
 *
 */
public class PlayerData implements Cloneable{

	private final Object id;
	private float stack;
	private float bet;
	private boolean comitted;
	
	private boolean lastActionWasRaise;
	private int gameCount = 0;
	private int VPIPCount = 0;
	private int PFRCount = 0;
	
	private int flopCount = 0;
	private int showdownCount = 0;

	private BetStatistics gameStats = new BetStatistics();
	private BetStatistics globalStats = new BetStatistics();
	
	public PlayerData(Object id) {
		this.id = id;
	}

	@Override
	protected PlayerData clone(){
		try {
			PlayerData clone = (PlayerData) super.clone();
			clone.gameStats = clone.gameStats.clone();
			clone.globalStats = clone.globalStats.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	public Object getId() {
		return id;
	}

	public float getStack() {
		return stack;
	}

	public float getBet() {
		return bet;
	}

	public int getGameCount() {
		return gameCount;
	}
	
	public BetStatistics getGameStats() {
		return gameStats;
	}
	
	public BetStatistics getGlobalStats() {
		return globalStats;
	}

	public float getDeficit(Propositionalizer p) {
		return Math.min(stack, (p.getMaxBet() - bet));
	}

	public float getPotOdds(Propositionalizer p) {
		float potSize = p.getPotSize();
		float deficit = getDeficit(p);
		return deficit / (deficit + potSize);
	}

	// "@attribute VPIP real\n"+
	public float getVPIP(int memory) {
		return (0.3F * memory + VPIPCount) / (memory + gameCount);
	}

	// "@attribute PFR real\n"+
	public float getPFR(int memory) {
		return (0.16F * memory + PFRCount) / (memory + gameCount);
	}

	// "@attribute WtSD real\n"+
	public float getWtSD(int memory) {
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
		lastActionWasRaise = false;
		++gameCount;
		gameStats = new BetStatistics();
		startNewRound();
	}

	protected void startNewRound() {
		bet = 0;
		comitted = false;
	}

	protected boolean didVPIP = false;

	protected void updateVPIP(Propositionalizer p) {
		if (!didVPIP && p.inPreFlopRound()) {
			++VPIPCount;
			didVPIP = true;
		}
	}

	protected void updatePFR(Propositionalizer p) {
		if (p.inPreFlopRound() && gameStats.getNbBetsRaisesPreFlop()==0) {
			++PFRCount;
		}
	}

	public void signalBet(Propositionalizer p, float amount) {
		bet += amount;
		decreaseStack(amount);
		comitted = true;
		lastActionWasRaise = true;
		updateVPIP(p);
		updatePFR(p); //before gameStats
		gameStats.addBet(p,amount);
		globalStats.addBet(p, amount);
	}

	private void decreaseStack(float amount) {
		stack -= amount;
		if(Math.abs(stack)<0.001) stack = 0;
		if(stack<0) {
			throw new IllegalStateException("Bad stack: "+stack);
		}
	}

	public void signalCheck(Propositionalizer p) {
		lastActionWasRaise = false;
		gameStats.addCheck(p);
		globalStats.addCheck(p);
	}

	public void signalRaise(Propositionalizer p,
			float raiseAmount, float movedAmount) {
		bet += movedAmount;
		decreaseStack(movedAmount);
		updateVPIP(p);
		updatePFR(p);
		lastActionWasRaise = true;
		comitted = true;
		gameStats.addRaise(p, (movedAmount-raiseAmount), raiseAmount);
		globalStats.addRaise(p, (movedAmount-raiseAmount), raiseAmount);
	}

	public void signalCall(Propositionalizer p,
			float movedAmount) {
		bet += movedAmount;
		decreaseStack(movedAmount);
		lastActionWasRaise = false;
		updateVPIP(p);
		comitted = true;
		gameStats.addCall(p, movedAmount);
		globalStats.addCall(p, movedAmount);
	}

	public void signalFold(Propositionalizer p) {
		lastActionWasRaise = false;
		gameStats.addFold(p);
		globalStats.addFold(p);
	}

	public void signalBB() {
		bet = 1;
		decreaseStack(1);
		comitted = true;
	}

	public void signalSB() {
		bet = 0.5F;
		decreaseStack(0.5F);
		comitted = true;
	}

	public void signalFlop() {
		++flopCount;
	}

	public void signalShowdown() {
		++showdownCount;
	}

	public void resetStack(float stack) {
		this.stack = stack;
		if(stack<0) {
			throw new IllegalStateException("Bad stack: "+stack);
		}
	}

	@Override
	public String toString() {
		return "PlayerData "+Long.toHexString(hashCode())+" for "+id.toString();
	}
	
}
