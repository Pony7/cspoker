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

import java.util.HashMap;
import java.util.List;
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
import org.cspoker.common.api.lobby.holdemtable.event.PotsChangedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.listener.ForwardingHoldemTableListener;
import org.cspoker.common.api.lobby.holdemtable.listener.HoldemTableListener;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.chips.Chips;
import org.cspoker.common.elements.chips.IllegalValueException;
import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.Rounds;

@ThreadSafe
public class SmartHoldemTableListener extends ForwardingHoldemTableListener {

	private final static Logger logger = Logger.getLogger(SmartHoldemTableListener.class);
	
	private volatile Pots pots;
	private volatile Set<Card> communityCards;
	private volatile Rounds round;
	
	@GuardedBy("playersLock")
	private final HashMap<PlayerId, MutableSeatedPlayer> players = new HashMap<PlayerId, MutableSeatedPlayer>();
	
	private final Object playersLock = new Object();

	public SmartHoldemTableListener(HoldemTableListener holdemTableListener) {
		super(holdemTableListener);
	}

	public Pots getPots() {
		return pots;
	}
	
	public Set<Card> getCommunityCards() {
		return communityCards;
	}
	
	public Rounds getCurrentRound() {
		return round;
	}
	
	@Override
	public void onPotsChanged(PotsChangedEvent potsChangedEvent) {
		pots = potsChangedEvent.getPots();
		super.onPotsChanged(potsChangedEvent);
	}
	
	@Override
	public void onNewCommunityCards(
			NewCommunityCardsEvent newCommunityCardsEvent) {
		this.communityCards = newCommunityCardsEvent.getCommunityCards();
		super.onNewCommunityCards(newCommunityCardsEvent);
	}

	@Override
	public void onNewRound(NewRoundEvent newRoundEvent) {
		this.round = newRoundEvent.getRound();
		synchronized (playersLock) {
			for(MutableSeatedPlayer player:players.values()){
				player.getBetChips().discard();
			}
		}
		super.onNewRound(newRoundEvent);
	}
	
	@Override
	public void onNewDeal(NewDealEvent newDealEvent) {
		List<SeatedPlayer> seatedPlayers = newDealEvent.getPlayers();
		synchronized (playersLock) {
			players.clear();
			for(SeatedPlayer seatedPlayer:seatedPlayers){
				try {
					players.put(seatedPlayer.getId(), new MutableSeatedPlayer(seatedPlayer));
				} catch (IllegalValueException e) {
					logger.error(e);
					throw new IllegalStateException(e);
				}
			}
		}
		super.onNewDeal(newDealEvent);
	}
	
	@Override
	public void onAllIn(AllInEvent allInEvent) {
		synchronized (playersLock) {
			try {
				players.get(allInEvent.getPlayer().getId()).transferAllChipsToBetPile();
			} catch (IllegalValueException e) {
				logger.error(e);
				throw new IllegalStateException(e);
			}
		}
		super.onAllIn(allInEvent);
	}
	
	@Override
	public void onBet(BetEvent betEvent) {
		addToBet(betEvent.getPlayer().getId(), betEvent.getAmount());
		super.onBet(betEvent);
	}
	
	@Override
	public void onBigBlind(BigBlindEvent bigBlindEvent) {
		addToBet(bigBlindEvent.getPlayer().getId(), bigBlindEvent.getAmount());
		super.onBigBlind(bigBlindEvent);
	}
	
	@Override
	public void onSmallBlind(SmallBlindEvent smallBlindEvent) {
		addToBet(smallBlindEvent.getPlayer().getId(), smallBlindEvent.getAmount());
		super.onSmallBlind(smallBlindEvent);
	}
	
	@Override
	public void onCall(CallEvent callEvent) {
		synchronized (playersLock) {
			addToBet(callEvent.getPlayer().getId(),getDeficit(callEvent.getPlayer().getId()));
		}
		super.onCall(callEvent);
	}
	
	@Override
	public void onRaise(RaiseEvent raiseEvent) {
		synchronized (playersLock) {
			addToBet(raiseEvent.getPlayer().getId(),getDeficit(raiseEvent.getPlayer().getId())+raiseEvent.getAmount());
		}
		super.onRaise(raiseEvent);
	}
	
	public int getDeficit(PlayerId playerId){
		synchronized (playersLock) {
			return getMaxBet()-players.get(playerId).getBetChips().getValue();
		}
	}
	
	private int getMaxBet() {
		synchronized (playersLock) {
			int max = 0;
			for(MutableSeatedPlayer player:players.values()){
				if(player.getBetChips().getValue()>max){
					max = player.getBetChips().getValue();
				}
			}
			return max;
		}
	}

	private void addToBet(PlayerId playerId, int amount) {
		synchronized (playersLock) {
			try {
				players.get(playerId).transferAmountToBetPile(amount);
			} catch (IllegalValueException e) {
				logger.error(e);
				throw new IllegalStateException(e);
			}
		}
	}
}
