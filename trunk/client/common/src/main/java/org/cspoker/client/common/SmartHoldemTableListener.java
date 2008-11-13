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

import java.util.*;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.*;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.Chips;
import org.cspoker.common.elements.chips.IllegalValueException;
import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.player.Winner;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Rounds;

/**
 * A stateful listener which stores information of the current state of the
 * game. You can access this information via the object provided by
 * {@link #getTableInformationProvider()}
 */
@ThreadSafe
public class SmartHoldemTableListener
		extends ForwardingHoldemTableListener {
	
	private final static Logger logger = Logger.getLogger(SmartHoldemTableListener.class);
	private DetailedHoldemTable table;
	
	// The mutable state fields are package private and (hopefully thread-safe)
	// via volatile modifiers and wrapping them in synchronized collections
	volatile Pots pots = new Pots(null, 0);
	final Set<Card> communityCards = Collections.synchronizedSet(new HashSet<Card>());
	volatile Rounds round;
	volatile PlayerId dealer;
	volatile int lastBetRaiseAmount;
	List<Integer> betsInCurrentRound = Collections.synchronizedList(new ArrayList<Integer>());
	Map<PlayerId, Integer> inPotUntilBettingRound = Collections.synchronizedMap(new HashMap<PlayerId, Integer>());
	
	TableInformationProvider infoProvider;
	
	@GuardedBy("playersLock")
	final HashMap<PlayerId, MutableSeatedPlayer> players = new HashMap<PlayerId, MutableSeatedPlayer>();
	
	final Object playersLock = new Object();
	
	volatile PlayerId lastActed;
	
	/**
	 * Constructor
	 */
	public SmartHoldemTableListener(HoldemTableListener forwardToMe) {
		super(forwardToMe);
	}
	
	/**
	 * @param table The {@link DetailedHoldemTable} to initialize this listener
	 *            with
	 * @param forwardToMe
	 */
	public SmartHoldemTableListener(DetailedHoldemTable table, HoldemTableListener forwardToMe) {
		this(forwardToMe);
		initialize(table);
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
		lastBetRaiseAmount = 0;
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
		
		for (Winner winner : winnerEvent.getWinners()) {
			try {
				new Chips(winner.getGainedAmount()).transferAllChipsTo(players.get(winner.getPlayer().getId())
						.getStack());
			} catch (IllegalArgumentException e) {
				logger.error(e);
			}
		}
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
		addToBet(allInEvent.getPlayerId(), allInEvent.getAmount());
		lastBetRaiseAmount = Math.max(lastBetRaiseAmount, allInEvent.getAmount());
		super.onAllIn(allInEvent);
	}
	
	@Override
	public void onBet(BetEvent betEvent) {
		logger.trace(betEvent);
		addToBet(betEvent.getPlayerId(), betEvent.getAmount());
		lastBetRaiseAmount = betEvent.getAmount();
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
		addToBet(bigBlindEvent.getPlayerId(), bigBlindEvent.getAmount());
		lastBetRaiseAmount = bigBlindEvent.getAmount() / 2;
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
		addToBet(smallBlindEvent.getPlayerId(), smallBlindEvent.getAmount());
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
			addToBet(callEvent.getPlayerId(), lastBetRaiseAmount
					- getPlayers().get(callEvent.getPlayerId()).getBetChipsValue());
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
		lastBetRaiseAmount = raiseEvent.getMovedAmount()
				+ getPlayers().get(raiseEvent.getPlayerId()).getBetChipsValue();
		synchronized (playersLock) {
			addToBet(raiseEvent.getPlayerId(), raiseEvent.getAmount());
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
	protected void addToBet(PlayerId playerId, int amount) {
		synchronized (playersLock) {
			try {
				players.get(playerId).transferAmountToBetPile(
						amount + getTableInformationProvider().getToCall(playerId));
			} catch (IllegalValueException e) {
				logger.error(e);
				throw new IllegalStateException(e);
			}
		}
		// Special case small blind
		if (betsInCurrentRound.size() == 1
				&& betsInCurrentRound.get(0) == getTableInformationProvider().getTableConfiguration().getSmallBlind()) {
			amount = amount / 2;
		}
		betsInCurrentRound.add(amount);
		inPotUntilBettingRound.put(playerId, betsInCurrentRound.size());
		lastActed = playerId;
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
			if (table == null) {
				throw new IllegalStateException("No table available");
			}
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
	 * 
	 * @param table The initial table configuration
	 */
	public void initialize(DetailedHoldemTable table) {
		logger.trace("Initializing table state after joining");
		this.table = table;
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
