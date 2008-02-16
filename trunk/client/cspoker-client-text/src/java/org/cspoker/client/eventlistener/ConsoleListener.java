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
package org.cspoker.client.eventlistener;

import org.cspoker.client.Console;
import org.cspoker.common.game.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.game.events.gameEvents.GameMessageEvent;
import org.cspoker.common.game.events.gameEvents.NewCommunityCardsEvent;
import org.cspoker.common.game.events.gameEvents.NewDealEvent;
import org.cspoker.common.game.events.gameEvents.NewRoundEvent;
import org.cspoker.common.game.events.gameEvents.NextPlayerEvent;
import org.cspoker.common.game.events.gameEvents.PlayerJoinedGameEvent;
import org.cspoker.common.game.events.gameEvents.PlayerLeftTableEvent;
import org.cspoker.common.game.events.gameEvents.ShowHandEvent;
import org.cspoker.common.game.events.gameEvents.WinnerEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.AllInEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.BetEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.BigBlindEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.CallEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.CheckEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.FoldEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.RaiseEvent;
import org.cspoker.common.game.events.gameEvents.playerActionEvents.SmallBlindEvent;
import org.cspoker.common.game.events.gameEvents.privateEvents.NewPocketCardsEvent;
import org.cspoker.common.game.events.serverEvents.PlayerJoinedEvent;
import org.cspoker.common.game.events.serverEvents.PlayerLeftEvent;
import org.cspoker.common.game.events.serverEvents.ServerMessageEvent;
import org.cspoker.common.game.events.serverEvents.TableCreatedEvent;

public class ConsoleListener implements RemoteAllEventsListener{

    private Console console;
    
    public ConsoleListener(Console console) {
	this.console=console;
    }
    
    public void onAllInEvent(AllInEvent event)  {
	console.print(event.toString());

    }

    public void onBetEvent(BetEvent event)  {
	console.print(event.toString());

    }

    public void onBigBlindEvent(BigBlindEvent event)
    {
	console.print(event.toString());

    }

    public void onCallEvent(CallEvent event)  {
	console.print(event.toString());

    }

    public void onCheckEvent(CheckEvent event)  {
	console.print(event.toString());

    }

    public void onFoldEvent(FoldEvent event)  {
	console.print(event.toString());

    }

    public void onRaiseEvent(RaiseEvent event)  {
	console.print(event.toString());

    }

    public void onSmallBlindEvent(SmallBlindEvent event)
    {
	console.print(event.toString());

    }

    public void onNewPocketCardsEvent(NewPocketCardsEvent event)
    {
	console.print(event.toString());

    }

    public void onNewCommunityCardsEvent(NewCommunityCardsEvent event)
    {
	console.print(event.toString());

    }

    public void onNewDealEvent(NewDealEvent event)
    {
	console.print(event.toString());

    }

    public void onNewRoundEvent(NewRoundEvent event)
    {
	console.print(event.toString());

    }

    public void onNextPlayerEvent(NextPlayerEvent event)
    {
	console.print(event.toString());

    }

    public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event)
    {
	console.print(event.toString());

    }

    public void onPlayerLeftTableEvent(PlayerLeftTableEvent event)
    {
	console.print(event.toString());

    }

    public void onShowHandEvent(ShowHandEvent event)
    {
	console.print(event.toString());

    }

    public void onWinnerEvent(WinnerEvent event)  {
	console.print(event.toString());

    }

    public void onGameMessageEvent(GameMessageEvent event)
    {
	console.print(event.toString());

    }

    public void onPlayerJoinedEvent(PlayerJoinedEvent event)
    {
	console.print(event.toString());

    }

    public void onPlayerLeftEvent(PlayerLeftEvent event)
    {
	console.print(event.toString());

    }

    public void onTableCreatedEvent(TableCreatedEvent event)
    {
	console.print(event.toString());

    }

    public void onServerMessageEvent(ServerMessageEvent event)
    {
	console.print(event.toString());

    }

}
