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
package org.cspoker.client.rmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.cspoker.client.rmi.RemotePlayerCommunicationFactoryForRMI;
import org.cspoker.common.game.IllegalActionException;
import org.cspoker.common.game.RemotePlayerCommunication;
import org.cspoker.common.game.elements.table.TableId;
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


public class TestRMI {

    /**
     * @param args
     * @throws NotBoundException 
     * @throws RemoteException 
     * @throws AccessException 
     * @throws IllegalActionException 
     */
    public static void main(String[] args) throws AccessException, RemoteException, NotBoundException, IllegalActionException {
	RemotePlayerCommunicationFactoryForRMI f = new RemotePlayerCommunicationFactoryForRMI("localhost");
	RemotePlayerCommunication guy = f.login("guy", "test");
	System.out.println("Guy Logged In and will print events to sout");

	guy.subscribeAllEventsListener(new RemoteAllEventsListener(){

	    public void onAllInEvent(AllInEvent event)  {
		System.out.println(event.toString());

	    }

	    public void onBetEvent(BetEvent event)  {
		System.out.println(event.toString());

	    }

	    public void onBigBlindEvent(BigBlindEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onCallEvent(CallEvent event)  {
		System.out.println(event.toString());

	    }

	    public void onCheckEvent(CheckEvent event)  {
		System.out.println(event.toString());

	    }

	    public void onFoldEvent(FoldEvent event)  {
		System.out.println(event.toString());

	    }

	    public void onRaiseEvent(RaiseEvent event)  {
		System.out.println(event.toString());

	    }

	    public void onSmallBlindEvent(SmallBlindEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onNewPocketCardsEvent(NewPocketCardsEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onNewCommunityCardsEvent(NewCommunityCardsEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onNewDealEvent(NewDealEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onNewRoundEvent(NewRoundEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onNextPlayerEvent(NextPlayerEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onPlayerLeftTableEvent(PlayerLeftTableEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onShowHandEvent(ShowHandEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onWinnerEvent(WinnerEvent event)  {
		System.out.println(event.toString());

	    }

	    public void onGameMessageEvent(GameMessageEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onPlayerJoinedEvent(PlayerJoinedEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onPlayerLeftEvent(PlayerLeftEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onTableCreatedEvent(TableCreatedEvent event)
	    {
		System.out.println(event.toString());

	    }

	    public void onServerMessageEvent(ServerMessageEvent event)
	    {
		System.out.println(event.toString());

	    }

	});

	RemotePlayerCommunication kenzo = f.login("kenzo", "test");
	RemotePlayerCommunication cedric = f.login("cedric", "test");

	guy.createTable();

	kenzo.joinTable(new TableId(0));
	cedric.joinTable(new TableId(0));

	guy.startGame();

	for (int i = 0; i < 10; i++) {
	    try {
		guy.check();
	    } catch (IllegalActionException e) {
		try {
		    guy.call();
		} catch (IllegalActionException e2) {
		    
		}
	    }
	    try {
		kenzo.check();
	    } catch (IllegalActionException e) {
		try {
		    kenzo.call();
		} catch (IllegalActionException e2) {
		    
		}
	    }
	    try {
		cedric.check();
	    } catch (IllegalActionException e) {
		try {
		    cedric.call();
		} catch (IllegalActionException e2) {
		    
		}
	    }
	}

    }

}
