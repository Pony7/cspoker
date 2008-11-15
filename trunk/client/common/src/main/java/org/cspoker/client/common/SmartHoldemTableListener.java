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
package org.cspoker.client.common;

import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.client.common.gamestate.BetState;
import org.cspoker.client.common.gamestate.CallState;
import org.cspoker.client.common.gamestate.CheckState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.RaiseState;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;

@ThreadSafe
public class SmartHoldemTableListener
		extends ForwardingHoldemTableListener {
	
	private final static Logger logger = Logger.getLogger(SmartHoldemTableListener.class);
	
	private TableState tableState;
	
	public SmartHoldemTableListener(HoldemTableListener holdemTableListener, TableState tableState) {
		super(holdemTableListener);
		this.tableState = tableState;
	}
	
	public GameState getGameState() {
		return tableState.getGameState();
	}
	
	@Override
	public void onBet(BetEvent betEvent) {
		tableState.setGameState(new BetState(tableState.getGameState(),betEvent));
		super.onBet(betEvent);
	}
	
	@Override
	public void onRaise(RaiseEvent raiseEvent) {
		tableState.setGameState(new RaiseState(tableState.getGameState(),raiseEvent));
		super.onRaise(raiseEvent);
	}
	
	@Override
	public void onCheck(CheckEvent checkEvent) {
		tableState.setGameState(new CheckState(tableState.getGameState(),checkEvent));
		super.onCheck(checkEvent);
	}
	
	@Override
	public void onCall(CallEvent callEvent) {
		tableState.setGameState(new CallState(tableState.getGameState(),callEvent));
		super.onCall(callEvent);
	}
	
}
