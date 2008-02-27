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
package org.cspoker.client.gui.text.eventlistener;

import org.cspoker.client.gui.text.Console;
import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.gameevents.GameMessageEvent;
import org.cspoker.common.events.gameevents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameevents.NewDealEvent;
import org.cspoker.common.events.gameevents.NewRoundEvent;
import org.cspoker.common.events.gameevents.NextPlayerEvent;
import org.cspoker.common.events.gameevents.PlayerJoinedGameEvent;
import org.cspoker.common.events.gameevents.PlayerLeftTableEvent;
import org.cspoker.common.events.gameevents.ShowHandEvent;
import org.cspoker.common.events.gameevents.WinnerEvent;
import org.cspoker.common.events.gameevents.playeractionevents.AllInEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BetEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BigBlindEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CallEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CheckEvent;
import org.cspoker.common.events.gameevents.playeractionevents.FoldEvent;
import org.cspoker.common.events.gameevents.playeractionevents.RaiseEvent;
import org.cspoker.common.events.gameevents.playeractionevents.SmallBlindEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.events.serverevents.PlayerJoinedEvent;
import org.cspoker.common.events.serverevents.PlayerLeftEvent;
import org.cspoker.common.events.serverevents.ServerMessageEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;

public class ConsoleListener implements RemoteAllEventsListener {

	private Console console;

	public ConsoleListener(Console console) {
		this.console = console;
	}

	public void onAllInEvent(AllInEvent event) {
		console.print(event.toString());

	}

	public void onBetEvent(BetEvent event) {
		console.print(event.toString());

	}

	public void onBigBlindEvent(BigBlindEvent event) {
		console.print(event.toString());

	}

	public void onCallEvent(CallEvent event) {
		console.print(event.toString());

	}

	public void onCheckEvent(CheckEvent event) {
		console.print(event.toString());

	}

	public void onFoldEvent(FoldEvent event) {
		console.print(event.toString());

	}

	public void onRaiseEvent(RaiseEvent event) {
		console.print(event.toString());

	}

	public void onSmallBlindEvent(SmallBlindEvent event) {
		console.print(event.toString());

	}

	public void onNewPocketCardsEvent(NewPocketCardsEvent event) {
		console.print(event.toString());

	}

	public void onNewCommunityCardsEvent(NewCommunityCardsEvent event) {
		console.print(event.toString());

	}

	public void onNewDealEvent(NewDealEvent event) {
		console.print(event.toString());

	}

	public void onNewRoundEvent(NewRoundEvent event) {
		console.print(event.toString());

	}

	public void onNextPlayerEvent(NextPlayerEvent event) {
		console.print(event.toString());

	}

	public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event) {
		console.print(event.toString());

	}

	public void onPlayerLeftTableEvent(PlayerLeftTableEvent event) {
		console.print(event.toString());

	}

	public void onShowHandEvent(ShowHandEvent event) {
		console.print(event.toString());

	}

	public void onWinnerEvent(WinnerEvent event) {
		console.print(event.toString());

	}

	public void onGameMessageEvent(GameMessageEvent event) {
		console.print(event.toString());

	}

	public void onPlayerJoinedEvent(PlayerJoinedEvent event) {
		console.print(event.toString());

	}

	public void onPlayerLeftEvent(PlayerLeftEvent event) {
		console.print(event.toString());

	}

	public void onTableCreatedEvent(TableCreatedEvent event) {
		console.print(event.toString());

	}

	public void onServerMessageEvent(ServerMessageEvent event) {
		console.print(event.toString());

	}

}
