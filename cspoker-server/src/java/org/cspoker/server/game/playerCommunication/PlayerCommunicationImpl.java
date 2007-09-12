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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.server.game.GameManager;
import org.cspoker.server.game.TableId;
import org.cspoker.server.game.events.AllEventsListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.AllInEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.AllInListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BetEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BetListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BigBlindEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BigBlindListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CallEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CheckEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.DealEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.FoldEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.RaiseEvent;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.SmallBlindEvent;
import org.cspoker.server.game.gameControl.IllegalActionException;
import org.cspoker.server.game.player.Player;

/**
 * A class of player communications.
 *
 * It's the interface to all game control actions.
 *
 * @author Kenzo
 *
 */
public class PlayerCommunicationImpl implements PlayerCommunication {

	/**********************************************************
	 * Variables
	 **********************************************************/

	/**
	 * The variable containing the player.
	 */
	private final Player player;

	/**
	 * This variable contains the player communication state.
	 */
	private PlayerCommunicationState state;

	private final EventsCollector eventsCollector = new EventsCollector();


	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Construct a new player communication with given player.
	 *
	 * @param 	player
	 * 			The given player
	 */
	public PlayerCommunicationImpl(Player player){
		this.player = player;
		state = new InitialState(this);
		GameManager.getServerMediator().subscribeServerEventListener(getEventsCollector());

		//TODO Temporary...
		PlayerCommunicationManager.addPlayerCommunication(player.getId(), this);
	}

	/**
	 * Returns the player contained in this player communication.
	 *
	 * @return The player contained in this player communication.
	 */
	public Player getPlayer(){
		return player;
	}

	/**********************************************************
	 * Player Actions
	 **********************************************************/

	public void call() throws IllegalActionException{
		state.call();
	}

	public void bet(int amount) throws IllegalActionException{
		state.bet(amount);
	}

	public void fold() throws IllegalActionException{
		state.fold();
	}

	public void check() throws IllegalActionException{
		state.check();
	}

	public void raise(int amount) throws IllegalActionException{
		state.raise(amount);
	}

	public void deal() throws IllegalActionException{
		state.deal();
	}

	public void allIn() throws IllegalActionException {
		state.allIn();
	}

	public void say(String message){
		state.say(message);
	}

	/**********************************************************
	 * Leave/Join Game
	 **********************************************************/

	/**
	 * Join the table with given table id.
	 *
	 * @pre 	The given id should be effective.
	 *			|id!=null
	 * @throws  IllegalActionException [can]
	 *          This actions is not a valid action in the current state.
	 */
	public void join(TableId id) throws IllegalActionException{
		if(id==null)
			throw new IllegalArgumentException("The given table id is not effective.");
		state.join(id);
	}


	public void leaveTable() throws IllegalActionException{
		state.leaveTable();
	}

	public TableId createTable() throws IllegalActionException {
		return state.createTable();
	}

	public void startGame() throws IllegalActionException{
		state.startGame();
	}


	public Events getLatestEvents() throws IllegalActionException{
		return eventsCollector.getLatestEvents();
	}


	public Events getLatestEventsAndAck(int ack) throws IllegalActionException{
		return eventsCollector.getLatestEventsAndAck(ack);
	}

	EventsCollector getEventsCollector(){
		return eventsCollector;
	}

	void setPlayerCommunicationState(PlayerCommunicationState state){
		this.state = state;
	}

	@Override
	public String toString(){
		return "player communication of "+player.getName();
	}

	/**********************************************************
	 * Publisher
	 **********************************************************/


	/**
	 * Subscribe the given all-in listener for all-in events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeAllInListener(AllInListener listener) {
		allInListeners.add(listener);
	}

	/**
	 * Unsubscribe the given all-in listener for all-in events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeAllInListener(AllInListener listener) {
		allInListeners.remove(listener);
	}

	/**
	 * This list contains all all-in listeners that
	 * should be alerted on a all-in.
	 */
	private final List<AllInListener> allInListeners = new CopyOnWriteArrayList<AllInListener>();

	/**
	 * Subscribe the given bet listener for bet events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeBetListener(BetListener listener) {
		betListeners.add(listener);
	}

	/**
	 * Unsubscribe the given bet listener for bet events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeBetListener(BetListener listener) {
		betListeners.remove(listener);
	}

	/**
	 * This list contains all bet listeners that
	 * should be alerted on a bet.
	 */
	private final List<BetListener> betListeners = new CopyOnWriteArrayList<BetListener>();

	/**
	 * Subscribe the given big blind listener for big blind events.
	 *
	 * @param 	listener
	 * 			The listener to subscribe.
	 */
	public void subscribeBigBlindListener(BigBlindListener listener) {
		bigBlindListeners.add(listener);
	}

	/**
	 * Unsubscribe the given big blind listener for big blind events.
	 *
	 * @param 	listener
	 * 			The listener to unsubscribe.
	 */
	public void unsubscribeBigBlindListener(BigBlindListener listener) {
		bigBlindListeners.remove(listener);
	}

	/**
	 * This list contains all big blind listeners that
	 * should be alerted on a big blind.
	 */
	private final List<BigBlindListener> bigBlindListeners = new CopyOnWriteArrayList<BigBlindListener>();





	AllEventsListener getAllEventsListener(){
		return allEventsListener;
	}

	private final AllEventsListener allEventsListener = new AllEventsListenerImpl();

	private class AllEventsListenerImpl implements AllEventsListener{

		@Override
		public void onAllInEvent(AllInEvent event) {
			for(AllInListener listener:allInListeners){
				listener.onAllInEvent(event);
			}
		}

		@Override
		public void onBetEvent(BetEvent event) {
			for(BetListener listener: betListeners){
				listener.onBetEvent(event);
			}

		}

		@Override
		public void onBigBlindEvent(BigBlindEvent event) {
			for(BigBlindListener listener: bigBlindListeners){
				listener.onBigBlindEvent(event);
			}

		}

		@Override
		public void onCallEvent(CallEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCheckEvent(CheckEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDealEvent(DealEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFoldEvent(FoldEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onRaiseEvent(RaiseEvent event) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSmallBlindEvent(SmallBlindEvent event) {
			// TODO Auto-generated method stub

		}

	}

}
