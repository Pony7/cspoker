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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicInteger;

import org.cspoker.common.game.AbstractPlayerCommunication;
import org.cspoker.common.game.IllegalActionException;
import org.cspoker.common.game.elements.table.TableId;
import org.cspoker.common.game.eventlisteners.game.GameMessageListener;
import org.cspoker.common.game.eventlisteners.game.NewCommunityCardsListener;
import org.cspoker.common.game.eventlisteners.game.NewDealListener;
import org.cspoker.common.game.eventlisteners.game.NewRoundListener;
import org.cspoker.common.game.eventlisteners.game.NextPlayerListener;
import org.cspoker.common.game.eventlisteners.game.PlayerJoinedGameListener;
import org.cspoker.common.game.eventlisteners.game.PlayerLeftTableListener;
import org.cspoker.common.game.eventlisteners.game.ShowHandListener;
import org.cspoker.common.game.eventlisteners.game.WinnerListener;
import org.cspoker.common.game.eventlisteners.game.actions.AllInListener;
import org.cspoker.common.game.eventlisteners.game.actions.BetListener;
import org.cspoker.common.game.eventlisteners.game.actions.BigBlindListener;
import org.cspoker.common.game.eventlisteners.game.actions.CallListener;
import org.cspoker.common.game.eventlisteners.game.actions.CheckListener;
import org.cspoker.common.game.eventlisteners.game.actions.FoldListener;
import org.cspoker.common.game.eventlisteners.game.actions.RaiseListener;
import org.cspoker.common.game.eventlisteners.game.actions.SmallBlindListener;
import org.cspoker.common.game.eventlisteners.game.privatelistener.NewPocketCardsListener;
import org.cspoker.common.game.eventlisteners.server.PlayerJoinedListener;
import org.cspoker.common.game.eventlisteners.server.PlayerLeftListener;
import org.cspoker.common.game.eventlisteners.server.ServerMessageListener;
import org.cspoker.common.game.eventlisteners.server.TableCreatedListener;
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
import org.cspoker.common.rmi.events.RemoteAllEventsListener;
import org.cspoker.common.rmi.game.RemotePlayerCommunication;

public class RMIPlayerCommunicationImpl extends AbstractPlayerCommunication{
	
	private RemotePlayerCommunication remotePlayerCommunication;
	
	private final ClientRMIPlayerCommunicationFactory factory;
	
	RMIPlayerCommunicationImpl(ClientRMIPlayerCommunicationFactory factory) {
		this.factory = factory;
	}
	
	public void allIn() throws IllegalActionException {
		try {
			remotePlayerCommunication.allIn();
		} catch (RemoteException e) {
			try {
				remoteExceptionHandler(e, getClass().getEnclosingMethod());
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}
	}

	public void bet(int amount) throws IllegalActionException {
		try {
			remotePlayerCommunication.bet(amount);
		} catch (RemoteException e) {
			try {
				remoteExceptionHandler(e, getClass().getEnclosingMethod(), amount);
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}
	}

	public void call() throws IllegalActionException {
		try {
			remotePlayerCommunication.call();
		} catch (RemoteException e) {
			try {
				remoteExceptionHandler(e, getClass().getEnclosingMethod());
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}
	}

	public void check() throws IllegalActionException {
		try {
			remotePlayerCommunication.check();
		} catch (RemoteException e) {
			try {
				remoteExceptionHandler(e, getClass().getEnclosingMethod());
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}
	}

	public TableId createTable() throws IllegalActionException {
		TableId tableId = null;
		try {
			tableId = remotePlayerCommunication.createTable();
		} catch (RemoteException e) {
			try {
				tableId = (TableId)remoteExceptionHandler(e, getClass().getEnclosingMethod());
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}
		return tableId;
	}

	public void deal() throws IllegalActionException {
		try {
			remotePlayerCommunication.deal();
		} catch (RemoteException e) {
			try {
				remoteExceptionHandler(e, getClass().getEnclosingMethod());
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}
		
	}

	public void destruct() {
		// TODO Auto-generated method stub
		
	}

	public void fold() throws IllegalActionException {
		try {
			remotePlayerCommunication.fold();
		} catch (RemoteException e) {
			try {
				remoteExceptionHandler(e, getClass().getEnclosingMethod());
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}
	}

	public void joinTable(TableId id) throws IllegalActionException {
		try {
			remotePlayerCommunication.joinTable(id);
		} catch (RemoteException e) {
			try {
				remoteExceptionHandler(e, getClass().getEnclosingMethod(), id);
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}	
	}

	public void leaveTable() throws IllegalActionException {
		try {
			remotePlayerCommunication.leaveTable();
		} catch (RemoteException e) {
			try {
				remoteExceptionHandler(e, getClass().getEnclosingMethod());
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}	
		
	}

	public void raise(int amount) throws IllegalActionException {
		try {
			remotePlayerCommunication.raise(amount);
		} catch (RemoteException e) {
			try {
				remoteExceptionHandler(e, getClass().getEnclosingMethod(), amount);
			} catch (Throwable e1) {
				if(e1 instanceof IllegalActionException)
					throw (IllegalActionException)e1;
				throw new IllegalStateException(e1);
			}
		}	
		
	}

	public void say(String message) {
		System.out.println("Hellow!");
		
	}

	public void startGame() throws IllegalActionException {
		System.out.println("Hellow!");
		
		
	}
	
	protected Object remoteExceptionHandler(RemoteException exception, Method method, Object...arguements) throws Throwable{
		if(nbRetry.getAndIncrement()>3)
			throw new IllegalStateException("Unable to reconnect");
		try {
			//TODO re-initialize connection.
			//Check if method has been completed or not...
			Object toReturn = method.invoke(this, arguements);
			nbRetry.set(0);
			return toReturn;
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}
	
	private final AtomicInteger nbRetry = new AtomicInteger(0);
	
	/**
	 * Package-accessible method which returns a remote listener.
	 * This remote listener should be registered with the server.
	 * All received events will be sent to the subscribed client listeners.
	 * 
	 * @return
	 */
	RemoteAllEventsListener getRemoteAllEventsListener(){
		return new RemoteAllEventsListener(){

			public void onAllInEvent(AllInEvent event) throws RemoteException {
				for(AllInListener listener:allInListeners){
					listener.onAllInEvent(event);
				}
				
			}

			public void onBetEvent(BetEvent event) throws RemoteException {
				for(BetListener listener:betListeners){
					listener.onBetEvent(event);
				}
				
			}

			public void onBigBlindEvent(BigBlindEvent event) throws RemoteException {
				for(BigBlindListener listener:bigBlindListeners){
					listener.onBigBlindEvent(event);
				}
				
			}

			public void onCallEvent(CallEvent event) throws RemoteException {
				for(CallListener listener:callListeners){
					listener.onCallEvent(event);
				}
				
			}

			public void onCheckEvent(CheckEvent event) throws RemoteException {
				for(CheckListener listener:checkListeners){
					listener.onCheckEvent(event);
				}
			}

			public void onFoldEvent(FoldEvent event) throws RemoteException {
				for(FoldListener listener:foldListeners){
					listener.onFoldEvent(event);
				}
				
			}

			public void onGameMessageEvent(GameMessageEvent event)
					throws RemoteException {
				for(GameMessageListener listener:gameMessageListeners){
					listener.onGameMessageEvent(event);
				}
				
			}

			public void onNewCommunityCardsEvent(NewCommunityCardsEvent event)
					throws RemoteException {
				for(NewCommunityCardsListener listener: newCommunityCardsListeners){
					listener.onNewCommunityCardsEvent(event);
				}
				
			}

			public void onNewDealEvent(NewDealEvent event) throws RemoteException {
				for(NewDealListener listener: newDealListeners){
					listener.onNewDealEvent(event);
				}
				
			}

			public void onNewPocketCardsEvent(NewPocketCardsEvent event)
					throws RemoteException {
				for(NewPocketCardsListener listener: newPocketCardsListeners){
					listener.onNewPocketCardsEvent(event);
				}
				
			}

			public void onNewRoundEvent(NewRoundEvent event) throws RemoteException {
				for(NewRoundListener listener: newRoundListeners){
					listener.onNewRoundEvent(event);
				}
				
			}

			public void onNextPlayerEvent(NextPlayerEvent event)
					throws RemoteException {
				for(NextPlayerListener listener: nextPlayerListeners){
					listener.onNextPlayerEvent(event);
				}
				
			}

			public void onPlayerJoinedGameEvent(PlayerJoinedGameEvent event)
					throws RemoteException {
				for(PlayerJoinedGameListener listener: playerJoinedGameListeners){
					listener.onPlayerJoinedGameEvent(event);
				}
			}

			public void onPlayerLeftTableEvent(PlayerLeftTableEvent event)
					throws RemoteException {
				for(PlayerLeftTableListener listener: playerLeftTableListeners){
					listener.onPlayerLeftTableEvent(event);
				}
				
			}

			public void onRaiseEvent(RaiseEvent event) throws RemoteException {
				for(RaiseListener listener: raiseListeners){
					listener.onRaiseEvent(event);
				}
				
			}

			public void onShowHandEvent(ShowHandEvent event) throws RemoteException {
				for(ShowHandListener listener:showHandListeners){
					listener.onShowHandEvent(event);
				}
			}

			public void onSmallBlindEvent(SmallBlindEvent event)
					throws RemoteException {
				for(SmallBlindListener listener: smallBlindListeners){
					listener.onSmallBlindEvent(event);
				}
			}

			public void onWinnerEvent(WinnerEvent event) throws RemoteException {
				for(WinnerListener listener: winnerListeners){
					listener.onWinnerEvent(event);
				}
			}

			public void onPlayerJoinedEvent(PlayerJoinedEvent event)
					throws RemoteException {
				for(PlayerJoinedListener listener: playerJoinedListeners){
					listener.onPlayerJoinedEvent(event);
				}
			}

			public void onPlayerLeftEvent(PlayerLeftEvent event)
					throws RemoteException {
				for(PlayerLeftListener listener: playerLeftListeners){
					listener.onPlayerLeftEvent(event);
				}
			}

			public void onServerMessageEvent(ServerMessageEvent event)
					throws RemoteException {
				for(ServerMessageListener listener: serverMessageListeners){
					listener.onServerMessageEvent(event);
				}
			}

			public void onTableCreatedEvent(TableCreatedEvent event)
					throws RemoteException {
				for(TableCreatedListener listener: tableCreatedListeners){
					listener.onTableCreatedEvent(event);
				}
			}
	    	
	    };
	}
	
	/**
	 * Package-accessible method to set the remote communication to communicate with the server.
	 * After a network failure it is possible to reset the remote communication,
	 * without interfering with client listeners.
	 * 
	 * @param remotePlayerCommunication
	 */
	void setRemotePlayerCommunication(RemotePlayerCommunication remotePlayerCommunication){
		this.remotePlayerCommunication = remotePlayerCommunication;
	}
	
}
