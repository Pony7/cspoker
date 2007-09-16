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

package org.cspoker.server.game.playerCommunication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cspoker.server.game.events.AllEventsListener;
import org.cspoker.server.game.events.Event;
import org.cspoker.server.game.events.gameEvents.GameMessageEvent;
import org.cspoker.server.game.events.gameEvents.NewCommunityCardsEvent;
import org.cspoker.server.game.events.gameEvents.NewDealEvent;
import org.cspoker.server.game.events.gameEvents.NewRoundEvent;
import org.cspoker.server.game.events.gameEvents.NextPlayerEvent;
import org.cspoker.server.game.events.gameEvents.PlayerJoinedGameEvent;
import org.cspoker.server.game.events.gameEvents.PlayerLeftTableEvent;
import org.cspoker.server.game.events.gameEvents.PotChangedEvent;
import org.cspoker.server.game.events.gameEvents.ShowHandEvent;
import org.cspoker.server.game.events.gameEvents.StackChangedEvent;
import org.cspoker.server.game.events.gameEvents.WinnerEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.AllInEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BetEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BigBlindEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CallEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CheckEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.DealEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.FoldEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.RaiseEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.SmallBlindEvent;
import org.cspoker.server.game.events.gameEvents.privateEvents.NewPocketCardsEvent;
import org.cspoker.server.game.events.serverEvents.PlayerJoinedEvent;
import org.cspoker.server.game.events.serverEvents.PlayerLeftEvent;
import org.cspoker.server.game.events.serverEvents.ServerMessageEvent;
import org.cspoker.server.game.events.serverEvents.TableCreatedEvent;

/**
 * A class to collect and to manage
 * game events.
 *
 * @author Kenzo
 *
 */
public class EventsCollector implements AllEventsListener{

	private int ackedToNumber=0;

	private int latestEventNumber=0;

	/**
	 * This variable contains the game events.
	 */
	private final List<Event> events = new ArrayList<Event>();

	/**
	 * This method is called when subscribed to inform a new game event occurred.
	 *
	 * @param 	event
	 * 			The event object containing all information of the occurred event.
	 */
	private synchronized void onEvent(Event event) {
		events.add(event);
		latestEventNumber++;
	}

	/**
	 * Returns the latest game events.
	 *
	 * @return The latest game events.
	 */
	public synchronized Events getLatestEvents(){
		return new Events(Collections.unmodifiableList(new ArrayList<Event>(events)), latestEventNumber);
	}

	public synchronized Events getLatestEventsAndAck(int ack){
		if(ack<=ackedToNumber)
			return getLatestEvents();
		if((ack>latestEventNumber))
			ack = latestEventNumber;
		events.subList(0, ack-ackedToNumber).clear();
		ackedToNumber = ack;
		return getLatestEvents();
	}

	@Override
	public void onAllInEvent(AllInEvent event) {
		onEvent(event);
	}

	@Override
	public void onBetEvent(BetEvent event) {
		onEvent(event);
	}

	@Override
	public void onBigBlindEvent(BigBlindEvent event) {
		onEvent(event);
	}

	@Override
	public void onCallEvent(CallEvent event) {
		onEvent(event);
	}

	@Override
	public void onCheckEvent(CheckEvent event) {
		onEvent(event);
	}

	@Override
	public void onDealEvent(DealEvent event) {
		onEvent(event);
	}

	@Override
	public void onFoldEvent(FoldEvent event) {
		onEvent(event);
	}

	@Override
	public void onRaiseEvent(RaiseEvent event) {
		onEvent(event);
	}

	@Override
	public void onSmallBlindEvent(SmallBlindEvent event) {
		onEvent(event);
	}

	@Override
	public void onNewPocketCardsEvent(NewPocketCardsEvent event) {
		onEvent(event);
	}

	@Override
	public void onNewCommunityCardsEvent(NewCommunityCardsEvent event) {
		onEvent(event);
	}

	@Override
	public void onNewDealEvent(NewDealEvent event) {
		onEvent(event);
	}

	@Override
	public void onNewRoundEvent(NewRoundEvent event) {
		onEvent(event);
	}

	@Override
	public void onNextPlayerEvent(NextPlayerEvent event) {
		onEvent(event);
	}

	@Override
	public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event) {
		onEvent(event);
	}

	@Override
	public void onPlayerLeftTableEvent(PlayerLeftTableEvent event) {
		onEvent(event);
	}

	@Override
	public void onPotChangedEvent(PotChangedEvent event) {
		onEvent(event);
	}

	@Override
	public void onShowHandEvent(ShowHandEvent event) {
		onEvent(event);
	}

	@Override
	public void onStackChangedEvent(StackChangedEvent event) {
		onEvent(event);
	}

	@Override
	public void onWinnerEvent(WinnerEvent event) {
		onEvent(event);
	}

	@Override
	public void onGameMessageEvent(GameMessageEvent event) {
		onEvent(event);
	}

	@Override
	public void onPlayerJoinedEvent(PlayerJoinedEvent event) {
		onEvent(event);
	}

	@Override
	public void onPlayerLeftEvent(PlayerLeftEvent event) {
		onEvent(event);
	}

	@Override
	public void onTableCreatedEvent(TableCreatedEvent event) {
		onEvent(event);
	}

	@Override
	public void onServerMessageEvent(ServerMessageEvent event) {
		onEvent(event);
	}
}
