package org.cspoker.server.common;

import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.server.common.gamecontrol.PokerTable;

public class HoldemPlayerContextImpl
		implements HoldemPlayerContext {
	
	private MutableSeatedPlayer player;
	
	private PokerTable table;
	
	public HoldemPlayerContextImpl(MutableSeatedPlayer player, PokerTable table) {
		this.player = player;
		this.table = table;
	}
	
	public void betOrRaise(int amount)
			throws IllegalActionException {
		try {
			table.bet(player, amount);
		} catch (IllegalActionException e) {
			table.raise(player, amount);
		}
		
	}
	
	public void checkOrCall()
			throws IllegalActionException {
		try {
			table.check(player);
		} catch (IllegalActionException e) {
			table.call(player);
		}
		
	}
	
	public void fold()
			throws IllegalActionException {
		table.fold(player);
	}
	
	public void sitOut() {
		table.sitOut(player);
	}
	
}
