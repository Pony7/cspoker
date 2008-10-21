package org.cspoker.server.common;

import org.cspoker.common.api.lobby.holdemtable.context.HoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.server.common.elements.id.SeatId;
import org.cspoker.server.common.gamecontrol.PokerTable;
import org.cspoker.server.common.player.ServerPlayer;

public class HoldemTableContextImpl
		implements HoldemTableContext {
	
	private ServerPlayer player;
	
	private PokerTable table;
	
	public HoldemTableContextImpl(ServerPlayer player, PokerTable table) {
		this.player = player;
		this.table = table;
	}
	
	public void leaveTable() {
		table.leaveTable(player);
	}
	
	public HoldemPlayerContext sitIn(long seatId, int buyIn, HoldemPlayerListener holdemPlayerListener)
			throws IllegalActionException {
		table.subscribeHoldemPlayerListener(player.getId(), holdemPlayerListener);
		return table.sitIn(new SeatId(seatId), buyIn, player);
	}
	
}
