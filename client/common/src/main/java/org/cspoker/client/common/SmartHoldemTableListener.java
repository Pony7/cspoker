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
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.modifiers.*;
import org.cspoker.common.api.lobby.holdemtable.event.*;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;

@ThreadSafe
public class SmartHoldemTableListener
		extends ForwardingHoldemTableListener {
	
	private final static Logger logger = Logger.getLogger(SmartHoldemTableListener.class);
	
	private final TableState tableState;
	
	public SmartHoldemTableListener(HoldemTableListener holdemTableListener, TableState tableState) {
		super(holdemTableListener);
		this.tableState = tableState;
	}
	
	public GameState getGameState() {
		synchronized (this) {
			return tableState.getGameState();
		}
	}
	
	@Override
	public void onBet(BetEvent betEvent) {
		synchronized (this) {
			tableState.setGameState(new BetState(tableState.getGameState(), betEvent));
		}
		super.onBet(betEvent);
	}
	
	@Override
	public void onRaise(RaiseEvent raiseEvent) {
		synchronized (this) {
			tableState.setGameState(new RaiseState(tableState.getGameState(), raiseEvent));
		}
		super.onRaise(raiseEvent);
	}
	
	@Override
	public void onCheck(CheckEvent checkEvent) {
		synchronized (this) {
			tableState.setGameState(new CheckState(tableState.getGameState(), checkEvent));
		}
		super.onCheck(checkEvent);
	}
	
	@Override
	public void onCall(CallEvent callEvent) {
		synchronized (this) {
			tableState.setGameState(new CallState(tableState.getGameState(), callEvent));
		}
		super.onCall(callEvent);
	}
	
	@Override
	public void onAllIn(AllInEvent allInEvent) {
		synchronized (this) {
			tableState.setGameState(new AllInState(tableState.getGameState(), allInEvent));
		}
		super.onAllIn(allInEvent);
	}
	
	@Override
	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		synchronized (this) {
			tableState.setGameState(new SmallBlindState(tableState.getGameState(), smallBlindEvent));
		}
		super.onSmallBlind(smallBlindEvent);
	}
	
	@Override
	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		synchronized (this) {
			tableState.setGameState(new BigBlindState(tableState.getGameState(), bigBlindEvent));
		}
		super.onBigBlind(bigBlindEvent);
	}
	
	@Override
	public void onFold(FoldEvent foldEvent) {
		synchronized (this) {
			tableState.setGameState(new FoldState(tableState.getGameState(), foldEvent));
		}
		super.onFold(foldEvent);
	}
	
	@Override
	public void onSitOut(SitOutEvent sitOutEvent) {
		synchronized (this) {
			tableState.setGameState(new SitOutState(tableState.getGameState(), sitOutEvent));
		}
		super.onSitOut(sitOutEvent);
	}
	
	@Override
	public void onSitIn(SitInEvent sitInEvent) {
		synchronized (this) {
			tableState.setGameState(new SitInState(tableState.getGameState(), sitInEvent));
		}
		super.onSitIn(sitInEvent);
	}
	
	@Override
	public void onShowHand(ShowHandEvent showHandEvent) {
		synchronized (this) {
			tableState.setGameState(new ShowHandState(tableState.getGameState(), showHandEvent));
		}
		super.onShowHand(showHandEvent);
	}
	
	@Override
	public void onNewRound(NewRoundEvent newRoundEvent) {
		synchronized (this) {
			tableState.setGameState(new NewRoundState(tableState.getGameState(), newRoundEvent));
		}
		super.onNewRound(newRoundEvent);
	}
	
	@Override
	public void onNewDeal(NewDealEvent newDealEvent) {
		synchronized (this) {
			logger.trace(newDealEvent);
			tableState.setGameState(new NewDealState(tableState.getTableConfiguration(), newDealEvent));
		}
		super.onNewDeal(newDealEvent);
	}
	
	@Override
	public void onLeaveSeat(LeaveSeatEvent leaveSeatEvent) {
		synchronized (this) {
			tableState.setGameState(new LeaveSeatState(tableState.getGameState(), leaveSeatEvent));
		}
		super.onLeaveSeat(leaveSeatEvent);
	}
	
	@Override
	public void onNewCommunityCards(NewCommunityCardsEvent newCommunityCardsEvent) {
		synchronized (this) {
			tableState.setGameState(new NewCommunityCardsState(tableState.getGameState(), newCommunityCardsEvent));
		}
		super.onNewCommunityCards(newCommunityCardsEvent);
	}
	
	@Override
	public void onNextPlayer(NextPlayerEvent nextPlayerEvent) {
		synchronized (this) {
			tableState.setGameState(new NextPlayerState(tableState.getGameState(), nextPlayerEvent));
		}
		super.onNextPlayer(nextPlayerEvent);
	}
	
	@Override
	public void onWinner(WinnerEvent winnerEvent) {
		synchronized (this) {
			tableState.setGameState(new WinnerState(tableState.getGameState(), winnerEvent));
		}
		super.onWinner(winnerEvent);
	}
	
	@Override
	public void onLeaveTable(LeaveTableEvent leaveGameEvent) {
		synchronized (this) {
			tableState.setGameState(new LeaveTableState(tableState.getGameState(), leaveGameEvent));
		}
		super.onLeaveTable(leaveGameEvent);
	}
	
	@Override
	public void onJoinTable(JoinTableEvent joinTableEvent) {
		synchronized (this) {
			tableState.setGameState(new JoinTableState(tableState.getGameState(), joinTableEvent));
		}
		super.onJoinTable(joinTableEvent);
	}
}
