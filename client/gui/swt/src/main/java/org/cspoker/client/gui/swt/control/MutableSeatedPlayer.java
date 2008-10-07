package org.cspoker.client.gui.swt.control;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

import org.cspoker.client.gui.swt.window.PlayerSeatComposite;
import org.cspoker.common.elements.player.SeatedPlayer;

public class MutableSeatedPlayer {
	
	PlayerSeatComposite seatComposite;
	private SeatedPlayer wrappedPlayer;
	
	private int stackValue;
	private int betChipsValue;
	private boolean dealer;
	private List<NavigableMap<Chip, Integer>> currentBetPile = new ArrayList<NavigableMap<Chip, Integer>>();
	
	private GameState gameState;
	
	public MutableSeatedPlayer(PlayerSeatComposite psc, SeatedPlayer player, GameState gameState) {
		seatComposite = psc;
		wrappedPlayer = player;
		setBetChipsValue(player.getBetChipsValue());
		setStackValue(player.getStackValue());
		this.gameState = gameState;
		
	}
	
	public void setPlayer(SeatedPlayer player) {
		wrappedPlayer = player;
	}
	
	public int getStackValue() {
		return stackValue;
	}
	
	public int getBetChipsValue() {
		return betChipsValue;
	}
	
	public String getName() {
		return wrappedPlayer.getName();
	}
	
	public long getSeatId() {
		return wrappedPlayer.getSeatId();
	}
	
	public List<NavigableMap<Chip, Integer>> getCurrentBetPile() {
		return currentBetPile;
	}
	
	public void setDealer(boolean b) {
		dealer = b;
		seatComposite.getParent().redraw();
	}
	
	public long getId() {
		return wrappedPlayer.getId();
	}
	
	public int getToCallAmount() {
		return Chip.getValue(currentBetPile) - getBetChipsValue();
	}
	
	public int getMinBetRaiseAmount() {
		return Math.max(0, Math.min(getStackValue() - getToCallAmount(), Math.max(gameState.getLastBetRaiseAmount(),
				getToCallAmount())));
	}
	
	public int getPotRaiseAmount() {
		return Math.max(0, Math.min(getStackValue() - getToCallAmount(), getToCallAmount()
				+ gameState.getPots().getTotalValue()));
	}
	
	public void updateStackAndBetChips(int betRaiseAmount) {
		stackValue = (getStackValue() - getToCallAmount() - betRaiseAmount);
		betChipsValue = getBetChipsValue() + getToCallAmount() + betRaiseAmount;
	}
	
	public void setBetChipsValue(int newBetChipsValue) {
		betChipsValue = newBetChipsValue;
		getCurrentBetPile().add(Chip.getDistribution(newBetChipsValue));
		
	}
	
	public void setStackValue(int newStackValue) {
		stackValue = newStackValue;
	}
	
	public boolean isDealer() {
		// TODO Auto-generated method stub
		return dealer;
	}
}
