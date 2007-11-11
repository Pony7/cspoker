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

package org.cspoker.common.rmi.events;

import java.rmi.Remote;
import java.rmi.RemoteException;

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

public interface RemoteAllGameEventsListener extends Remote{

    /**
     * This method is called when subscribed to inform an all-in event occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onAllInEvent(AllInEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a bet occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onBetEvent(BetEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a big blind has been bet.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onBigBlindEvent(BigBlindEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a call occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onCallEvent(CallEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a check occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onCheckEvent(CheckEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a fold occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onFoldEvent(FoldEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a raise occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onRaiseEvent(RaiseEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a small blind has been
     * bet.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onSmallBlindEvent(SmallBlindEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a new private cards event
     * occurred.
     * 
     * @param id
     *                The id of the player to inform.
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onNewPocketCardsEvent(NewPocketCardsEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a new community cards
     * event occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onNewCommunityCardsEvent(NewCommunityCardsEvent event) throws RemoteException;
	
    /**
     * This method is called when subscribed to inform a new deal event
     * occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onNewDealEvent(NewDealEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a new round event
     * occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onNewRoundEvent(NewRoundEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a next player event
     * occurred.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onNextPlayerEvent(NextPlayerEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a player joined the game.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event) throws RemoteException;
	
    /**
     * This method is called when subscribed to inform a player left the table.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onPlayerLeftTableEvent(PlayerLeftTableEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a player shows his hand.
     * 
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onShowHandEvent(ShowHandEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a player / players have
     * won the pot of this hand.
     *
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onWinnerEvent(WinnerEvent event) throws RemoteException;
    
    /**
     * This method is called when subscribed to inform a player sends
     * a chat message to all players seated at the table.
     *
     * @param event
     *                The event object containing all information of the
     *                occurred event.
     */
    public void onGameMessageEvent(GameMessageEvent event) throws RemoteException;
}
