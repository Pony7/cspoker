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
package org.cspoker.ai.bots.bot;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;
import org.cspoker.ai.bots.listener.BotListener;
import org.cspoker.ai.bots.util.RunningStats;
import org.cspoker.client.common.SmartHoldemPlayerContext;
import org.cspoker.client.common.SmartHoldemTableContext;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ConfigChangeEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.JoinTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.Winner;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.util.Util;

public abstract class AbstractBot implements Bot {

	private final static Logger logger = Logger.getLogger(AbstractBot.class);

	protected final SmartLobbyContext lobbyContext;
	protected volatile SmartHoldemTableContext tableContext;
	protected volatile SmartHoldemPlayerContext playerContext;

	private volatile boolean started = false;

	public final TableId tableID;
	public final PlayerId botId;
	private final int buyIn;

	private final BotListener[] botListeners;

	protected final ExecutorService executor;

	private RunningStats profit = new RunningStats();
	private int gameInvested = 0;

	public AbstractBot(PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, int buyIn, ExecutorService executor,
			BotListener... botListeners) {
		this.botId = botId;
		tableID = tableId;
		lobbyContext = lobby;
		this.botListeners = botListeners;
		this.buyIn = buyIn;
		this.executor = executor;
	}
	
	public PlayerId getId() {
		return botId;
	}

	public void start() {
		executor.execute(new Runnable() {

			public void run() {
				try {
					started = true;
					tableContext = lobbyContext.joinHoldemTable(tableID,
							AbstractBot.this);
					playerContext = tableContext.sitIn(buyIn, AbstractBot.this);
				} catch (IllegalActionException e) {
					e.printStackTrace();
					throw new IllegalStateException("Failed to join table.", e);
				} catch (RemoteException e) {
					logger.error(e);
					throw new IllegalStateException("Failed to join table.", e);
				} catch(Exception e){
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}
	
	public void reSitIn() {
		executor.execute(new Runnable() {

			public void run() {
				try {
					started = true;
					playerContext = tableContext.sitIn(buyIn, AbstractBot.this);
				} catch (IllegalActionException e) {
					e.printStackTrace();
					throw new IllegalStateException("Failed to join table.", e);
				} catch (RemoteException e) {
					logger.error(e);
					throw new IllegalStateException("Failed to join table.", e);
				} catch(Exception e){
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}

	public void startGame() {
		executor.execute(new Runnable() {

			public void run() {
				try {
					playerContext.startGame();
				} catch (IllegalActionException e) {
					e.printStackTrace();
					throw new IllegalStateException("Failed to start game.", e);
				} catch (RemoteException e) {
					logger.error(e);
					throw new IllegalStateException("Failed to start game.", e);
				} catch(Exception e){
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}
	
	public abstract void doNextAction() throws RemoteException, IllegalActionException;

	public void onNextPlayer(final NextPlayerEvent nextPlayerEvent) {
		logger.info("Next player is "+nextPlayerEvent.getPlayerId());
		if (started && nextPlayerEvent.getPlayerId().equals(botId)) {
			logger.info("Submitting task to run bot.");
			executor.execute(new Runnable(){
				@Override
				public void run() {
					logger.info("Running bot now!");
					try {
						doNextAction();
					} catch (IllegalActionException e) {
						logger.warn("Raise bounds: "
								+ tableContext.getGameState().getLowerRaiseBound(
										botId)
										+ " to "
										+ tableContext.getGameState().getUpperRaiseBound(
												botId));
						logger.error(e);
						throw new IllegalStateException("Action was not allowed.",
								e);
					} catch (RemoteException e) {
						logger.error(e);
						throw new IllegalStateException("Action failed.", e);
					} catch (StackOverflowError e) {
						e.printStackTrace();
						logger.error(e);
						try {
							playerContext.checkOrCall();
						} catch (RemoteException e1) {
							logger.error(e1);
							throw new IllegalStateException("Action failed.", e1);
						} catch (IllegalActionException e1) {
							logger.error(e1);
							throw new IllegalStateException(
									"Action was not allowed.", e1);
						}
					} catch(Exception e){
						logger.error(e);
						e.printStackTrace();
						throw new RuntimeException(e);
					}
					for (BotListener botListener : botListeners) {
						botListener.onActionPerformed();
					}
				}
			});
		}
	}

	/**
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener#onNewDeal(org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent)
	 */
	public void onNewDeal(NewDealEvent newDealEvent) {
		if (started) {
			executor.execute(new Runnable(){
				@Override
				public void run() {
					for (BotListener botListener : botListeners) {
						botListener.onNewDeal();
					}
				}
			});
		}
	}

	/**
	 * @see org.cspoker.ai.bots.bot.Bot#stop()
	 */
	public void stop() {
		started = false;
		executor.execute(new Runnable() {

			public void run() {
				try {
					tableContext.leaveTable();
				} catch (IllegalActionException e) {
					e.printStackTrace();
					throw new IllegalStateException("Failed to leave table.", e);
				} catch (RemoteException e) {
					logger.error(e);
					throw new IllegalStateException("Failed to leave table.", e);
				} catch(Exception e){
					logger.error(e);
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		});
	}

	/**
	 * @see org.cspoker.ai.bots.bot.Bot#getProfit()
	 */
	public RunningStats getProfit() {
		return profit;
	}

	public void onAllIn(AllInEvent allInEvent) {
		if(allInEvent.getPlayerId().equals(botId)){
			incrementGameInvestment(allInEvent.getMovedAmount());
		}
	}

	public void onBet(BetEvent betEvent) {
		if(betEvent.getPlayerId().equals(botId)){
			incrementGameInvestment(betEvent.getAmount());
		}
	}

	public void onBlind(BlindEvent blindEvent) {
		if(blindEvent.getPlayerId().equals(botId)){
			incrementGameInvestment(blindEvent.getAmount());
		}
	}

	public void onCall(CallEvent callEvent) {
		if(callEvent.getPlayerId().equals(botId)){
			incrementGameInvestment(callEvent.getMovedAmount());
		}
	}

	public void onCheck(CheckEvent checkEvent) {

	}

	public void onFold(FoldEvent foldEvent) {

	}

	public void onJoinTable(JoinTableEvent joinTableEvent) {

	}

	public void onLeaveTable(LeaveTableEvent leaveGameEvent) {

	}

	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) {

	}

	public void onNewRound(NewRoundEvent newRoundEvent) {

	}

	public void onRaise(RaiseEvent raiseEvent) {
		if(raiseEvent.getPlayerId().equals(botId)){
			incrementGameInvestment(raiseEvent.getMovedAmount());
		}
	}

	public void onShowHand(ShowHandEvent showHandEvent) {

	}

	public void onSitIn(SitInEvent sitInEvent) {

	}

	public void onSitOut(final SitOutEvent sitOutEvent) {
		if(sitOutEvent.getPlayerId().equals(this.botId)){
			playerContext = null;
		}
		if (started) {
			executor.execute(new Runnable(){
				@Override
				public void run() {
					for (BotListener botListener : botListeners) {
						botListener.onSitOut(sitOutEvent);
					}
				}
			});
		}
	}

	public void onWinner(WinnerEvent winnerEvent) {
		boolean done = false;
		for(Winner winner: winnerEvent.getWinners()){
			if(winner.getPlayerId().equals(botId)){
				//check if one can be a winner more than once (split pot?)
				if(done) throw new IllegalStateException();
				addProfit(winner.getGainedAmount()-gameInvested);
				if(logger.isDebugEnabled()){
					logger.debug("Incrementing profit for "+this+" with "+Util.parseDollars(winner.getGainedAmount()-gameInvested));
				}
				gameInvested = 0;
				done = true;
			}
		}
		if(!done){
			addProfit(-gameInvested);
			if(logger.isDebugEnabled()){
				logger.debug("Incrementing profit for "+this+" with "+Util.parseDollars(-gameInvested));
			}
			gameInvested = 0;
		}
	}

	private void addProfit(int p) {
		profit.add(p);
	}

	public void onNewPocketCards(NewPocketCardsEvent newPocketCardsEvent) {

	}

	public void onLeaveSeat(SitOutEvent leaveSeatEvent) {

	}

	@Override
	public void onConfigChange(ConfigChangeEvent configChangeEvent) {
	
	}
	
	private void incrementGameInvestment(int amount){
		gameInvested += amount;
		if(logger.isDebugEnabled()){
			logger.debug("Incrementing game investment for "+this+" with "+Util.parseDollars(amount)+ " to "+Util.parseDollars(gameInvested));
		}
	}

	@Override
	public String toString() {
		return "Abstract bot "+botId;
	}
}
