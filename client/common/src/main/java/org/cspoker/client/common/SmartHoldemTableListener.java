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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.Chips;
import org.cspoker.common.elements.chips.IllegalValueException;
import org.cspoker.common.elements.chips.Pot;
import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.player.Winner;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.TableId;

/**
 * A stateful listener which stores information of the current state of the
 * game. You can access this information via the object provided by
 * {@link #getTableInformationProvider()}
 */
@ThreadSafe
public class SmartHoldemTableListener
		extends ForwardingHoldemTableListener {
	
	private final static Logger logger = Logger.getLogger(SmartHoldemTableListener.class);
	private final DetailedHoldemTable table;
	
	// The mutable state fields are package private and (hopefully thread-safe)
	// via volatile modifiers and wrapping them in synchronized collections
	volatile Pots pots = new Pots(null, 0);
	final Set<Card> communityCards = Collections.synchronizedSet(new HashSet<Card>());
	volatile Round round;
	volatile PlayerId dealer;
	volatile int maxBet;
	List<Integer> betsInCurrentRound = Collections.synchronizedList(new ArrayList<Integer>());
	Map<PlayerId, Integer> inPotUntilBettingRound = Collections.synchronizedMap(new HashMap<PlayerId, Integer>());
	
	volatile TableInformationProvider infoProvider;
	
	@GuardedBy("playersLock")
	final HashMap<PlayerId, MutableSeatedPlayer> players = new HashMap<PlayerId, MutableSeatedPlayer>();
	
	final Object playersLock = new Object();
	
	volatile PlayerId lastActed;
	
	/**
	 * Constructor
	 * @throws IllegalActionException 
	 * @throws RemoteException 
	 */
	public SmartHoldemTableListener(TableId tableId, HoldemTableListener holdemTableListener, SmartLobbyContext lobbyContext) throws RemoteException, IllegalActionException {
		super(holdemTableListener);
		this.table = lobbyContext.getHoldemTableInformation(tableId);
		initialize();
	}
	
	/**
	 * @param holdemTableListener The {@link DetailedHoldemTable} to initialize this listener
	 *            with
	 * @param smartLobbyContext
	 */
	public SmartHoldemTableListener(HoldemTableListener holdemTableListener, DetailedHoldemTable holdemtable) {
		super(holdemTableListener);
		this.table = holdemtable;
		initialize();
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onNewCommunityCards(org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent)
	 */
	@Override
	public void onNewCommunityCards(NewCommunityCardsEvent newCommunityCardsEvent) {
		logger.trace(newCommunityCardsEvent);
		communityCards.addAll(newCommunityCardsEvent.getCommunityCards());
		super.onNewCommunityCards(newCommunityCardsEvent);
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onNewRound(org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent)
	 */
	@Override
	public void onNewRound(NewRoundEvent newRoundEvent) {
		super.onNewRound(newRoundEvent);
		logger.trace(newRoundEvent);
		this.round = newRoundEvent.getRound();
		pots = newRoundEvent.getPots();
		betsInCurrentRound.clear();
		for (Map.Entry<PlayerId, Integer> entry : inPotUntilBettingRound.entrySet()) {
			entry.setValue(0);
		}
		maxBet = 0;
		synchronized (playersLock) {
			for (MutableSeatedPlayer player : players.values()) {
				player.getBetChips().discard();
			}
		}
		
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onWinner(org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent)
	 */
	@Override
	public void onWinner(WinnerEvent winnerEvent) {
		logger.trace(winnerEvent);
		for (Winner winner : winnerEvent.getWinners()) {
			try {
				new Chips(winner.getGainedAmount()).transferAllChipsTo(players.get(winner.getPlayer().getId()).getStack());
				
			} catch (IllegalArgumentException e) {
				logger.error(e);
			}
		}
		List<Pot> emptyList = Collections.emptyList();
		pots = new Pots(emptyList,0);
		super.onWinner(winnerEvent);
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onNewDeal(org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent)
	 */
	@Override
	public void onNewDeal(NewDealEvent newDealEvent) {
		logger.trace(newDealEvent);
		List<SeatedPlayer> seatedPlayers = newDealEvent.getPlayers();
		pots = new Pots(null, 0);
		communityCards.clear();
		dealer = newDealEvent.getDealer();
		lastActed = dealer;
		synchronized (playersLock) {
			players.clear();
			for (SeatedPlayer seatedPlayer : seatedPlayers) {
				try {
					players.put(seatedPlayer.getId(), new MutableSeatedPlayer(seatedPlayer));
				} catch (IllegalArgumentException e) {
					logger.error(e);
					throw new IllegalStateException(e);
				}
			}
		}
		super.onNewDeal(newDealEvent);
		
		betsInCurrentRound.clear();
		for (SeatedPlayer seatedPlayer : seatedPlayers) {
			inPotUntilBettingRound.put(seatedPlayer.getId(), 0);
		}
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onAllIn(org.cspoker.common.api.lobby.holdemtable.event.AllInEvent)
	 */
	@Override
	public void onAllIn(AllInEvent allInEvent) {
		logger.trace(allInEvent);
		addToBetAbsolute(allInEvent.getPlayerId(), allInEvent.getAmount());
		maxBet = Math.max(maxBet, allInEvent.getAmount());
		super.onAllIn(allInEvent);
	}
	
	@Override
	public void onBet(BetEvent betEvent) {
		logger.trace(betEvent);
		addToBetRelative(betEvent.getPlayerId(), betEvent.getAmount());
		maxBet = betEvent.getAmount();
		super.onBet(betEvent);
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onBigBlind(org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent)
	 */
	@Override
	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		logger.trace(bigBlindEvent);
		addToBetAbsolute(bigBlindEvent.getPlayerId(), bigBlindEvent.getAmount());
		maxBet = bigBlindEvent.getAmount() / 2;
		super.onBigBlind(bigBlindEvent);
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onSmallBlind(org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent)
	 */
	@Override
	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		logger.trace(smallBlindEvent);
		addToBetAbsolute(smallBlindEvent.getPlayerId(), smallBlindEvent.getAmount());
		super.onSmallBlind(smallBlindEvent);
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onCall(org.cspoker.common.api.lobby.holdemtable.event.CallEvent)
	 */
	@Override
	public void onCall(CallEvent callEvent) {
		logger.trace(callEvent);
		synchronized (playersLock) {
			addToBetRelative(callEvent.getPlayerId(), 0);
		}
		super.onCall(callEvent);
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onRaise(org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent)
	 */
	@Override
	public void onRaise(RaiseEvent raiseEvent) {
		logger.trace(raiseEvent);
		maxBet = raiseEvent.getMovedAmount()
				+ getPlayers().get(raiseEvent.getPlayerId()).getBetChipsValue();
		synchronized (playersLock) {
			addToBetRelative(raiseEvent.getPlayerId(), raiseEvent.getAmount());
		}
		
		super.onRaise(raiseEvent);
	}
	
	/**
	 * Updates internal state by moving given amount to mutable player's bet
	 * pile
	 * 
	 * @param playerId
	 * @param amount
	 */
	protected void addToBetRelative(PlayerId playerId, int amount) {
		synchronized (playersLock) {
			addToBetAbsolute(playerId, amount + getTableInformationProvider().getToCall(playerId));
		}
		betsInCurrentRound.add(amount);
		inPotUntilBettingRound.put(playerId, betsInCurrentRound.size());
		lastActed = playerId;
	}
	
	/**
	 * Updates internal state by moving given amount to mutable player's bet
	 * pile
	 * 
	 * @param playerId
	 * @param amount
	 */
	protected void addToBetAbsolute(PlayerId playerId, int amount) {
		synchronized (playersLock) {
			try {
				players.get(playerId).transferAmountToBetPile(amount);
			} catch (IllegalValueException e) {
				logger.error(e);
				throw new IllegalStateException(e);
			}
		}
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onSitIn(org.cspoker.common.api.lobby.holdemtable.event.SitInEvent)
	 */
	@Override
	public void onSitIn(SitInEvent sitInEvent) {
		SeatedPlayer newPlayer = sitInEvent.getPlayer();
		synchronized (playersLock) {
			try {
				players.put(newPlayer.getId(), new MutableSeatedPlayer(newPlayer));
			} catch (IllegalArgumentException e) {
				logger.error(e);
				throw new IllegalStateException(e);
			}
			
		}
		inPotUntilBettingRound.put(newPlayer.getId(), 0);
		super.onSitIn(sitInEvent);
	}
	
	/**
	 * Updates internal state
	 * 
	 * @see org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener#onSitOut(org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent)
	 */
	@Override
	public void onSitOut(SitOutEvent sitOutEvent) {
		synchronized (playersLock) {
			players.remove(sitOutEvent.getPlayerId());
		}
		inPotUntilBettingRound.remove(sitOutEvent.getPlayerId());
		super.onSitOut(sitOutEvent);
	}
	
	/**
	 * This method returns a {@link TableInformationProvider}. Use this provider
	 * to query state information while the game is progressing, the
	 * {@link SmartHoldemTableListener} merely keeps track of the current table
	 * state
	 * 
	 * @return
	 */
	public TableInformationProvider getTableInformationProvider() {
		if (infoProvider == null) {
			infoProvider = new TableInformationProvider(table, this);
		}
		return infoProvider;
	}
	
	/**
	 * @return An immutable copy of the players map
	 */
	public Map<PlayerId, SeatedPlayer> getPlayers() {
		Map<PlayerId, SeatedPlayer> playersSnapshot = new HashMap<PlayerId, SeatedPlayer>();
		for (Map.Entry<PlayerId, MutableSeatedPlayer> entry : players.entrySet()) {
			playersSnapshot.put(entry.getKey(), entry.getValue().getMemento());
		}
		return Collections.unmodifiableMap(playersSnapshot);
	}
	
	/**
	 * Initializes the stateful listener with the {@link DetailedHoldemTable}
	 * from the server. This is important for example for players joining a
	 * table where a game is in progress
	 * <p>
	 * This method is called by the constructor, but the table can also be
	 * provided later. If the table has not been provided,
	 * {@link #getTableInformationProvider()} will throw a
	 * {@link IllegalStateException}
	 */
	private void initialize() {
		logger.trace("Initializing table state after joining");
		for (SeatedPlayer player : table.getPlayers()) {
			players.put(player.getId(), new MutableSeatedPlayer(player));
			if (table.getDealer() != null) {
				dealer = table.getDealer().getId();
			}
			communityCards.clear();
			communityCards.addAll(table.getCommunityCards());
			pots = table.getPots();
			round = table.getRound();
			inPotUntilBettingRound.put(player.getId(), 0);
		}
	}
	
}
