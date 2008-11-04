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
package org.cspoker.client.bots;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.client.common.SmartHoldemPlayerContext;
import org.cspoker.client.common.SmartHoldemTableContext;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.event.*;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.listener.HoldemPlayerListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

public class DefaultBot
		implements HoldemTableListener, HoldemPlayerListener {
	
	private final static Logger logger = Logger.getLogger(DefaultBot.class);
	
	protected final SmartLobbyContext lobbyContext;
	protected final SmartHoldemTableContext tableContext;
	protected final SmartHoldemPlayerContext playerContext;
	
	protected final TableId tableID;
	protected final PlayerId playerID;
	protected long deals = 1;
	protected final boolean doOutput;
	protected final long startTime;
	protected final ExecutorService executor;
	
	public DefaultBot(SmartLobbyContext lobbyContext, PlayerId playerID, TableId tableID, ExecutorService executor,
			boolean doOutput) {
		this.playerID = playerID;
		this.doOutput = doOutput;
		this.tableID = tableID;
		this.startTime = System.currentTimeMillis();
		this.executor = executor;
		this.lobbyContext = lobbyContext;
		try {
			tableContext = lobbyContext.joinHoldemTable(tableID,this);
			playerContext = tableContext.sitIn(5000,this);
		} catch (IllegalActionException e) {
			logger.error(e);
			throw new IllegalStateException("Failed to join table.", e);
		} catch (RemoteException e) {
			logger.error(e);
			throw new IllegalStateException("Failed to join table.", e);
		}
	}
	
	public void doNextAction() {

	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onNextPlayer(org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent)
	 */
	public void onNextPlayer(final NextPlayerEvent nextPlayerEvent) {
		if (nextPlayerEvent.getPlayer().getId().equals(playerID)) {
			doNextAction();
		}
	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onNewDeal(org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent)
	 */
	public void onNewDeal(NewDealEvent newDealEvent) {
		if (doOutput) {
			++deals;
			if (deals % 32 == 0) {
				System.out.println("deals " + (deals) + ": " + newDealEvent);
				System.out.println((deals * 1000.0) / (1 + System.currentTimeMillis() - startTime)
						+ " deals per second");
			}
		}
	}
	
	public void onAllIn(AllInEvent allInEvent) {

	}
	
	public void onBet(BetEvent betEvent) {

	}
	
	public void onBigBlind(BigBlindEvent bigBlindEvent) {

	}
	
	public void onCall(CallEvent callEvent) {

	}
	
	public void onCheck(CheckEvent checkEvent) {

	}
	
	public void onFold(FoldEvent foldEvent) {

	}
	
	public void onJoinTable(JoinTableEvent joinTableEvent) {

	}
	
	public void onLeaveTable(LeaveTableEvent leaveGameEvent) {

	}
	
	public void onNewCommunityCards(NewCommunityCardsEvent newCommunityCardsEvent) {

	}
	
	public void onNewRound(NewRoundEvent newRoundEvent) {

	}
	
	public void onRaise(RaiseEvent raiseEvent) {

	}
	
	public void onShowHand(ShowHandEvent showHandEvent) {

	}
	
	public void onSitIn(SitInEvent sitInEvent) {

	}
	
	public void onSitOut(SitOutEvent sitOutEvent) {

	}
	
	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {

	}
	
	public void onWinner(WinnerEvent winnerEvent) {

	}
	
	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {

	}
	
	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onPotsChanged(org.cspoker.common.api.lobby.holdemtable.event.PotsChangedEvent)
	 */
	public void onPotsChanged(PotsChangedEvent potsChangedEvent) {

	}
	
}
