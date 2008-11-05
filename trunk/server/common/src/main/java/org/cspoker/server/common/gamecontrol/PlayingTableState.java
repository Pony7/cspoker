/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.server.common.gamecontrol;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.PotsChangedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.HoldemPlayerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.elements.chips.Pots;
import org.cspoker.common.elements.player.MutableSeatedPlayer;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.util.threading.ScheduledRequestExecutor;
import org.cspoker.server.common.HoldemPlayerContextImpl;
import org.cspoker.server.common.elements.table.PlayerListFullException;
import org.cspoker.server.common.elements.table.SeatTakenException;
import org.cspoker.server.common.elements.table.ServerTable;
import org.cspoker.server.common.gamecontrol.rounds.BettingRound;
import org.cspoker.server.common.gamecontrol.rounds.Round;
import org.cspoker.server.common.gamecontrol.rounds.WaitingRound;
import org.cspoker.server.common.gamecontrol.rules.BettingRules;
import org.cspoker.server.common.gamecontrol.rules.NoLimit;

/**
 * This class is responsible to control the flow of the game. This class changes
 * the state (round) in which the players are.
 * 
 * @author Kenzo
 * 
 */
public class PlayingTableState
		extends TableState {
	
	private static Logger logger = Logger.getLogger(PlayingTableState.class);
	
	/***************************************************************************
	 * Variables
	 **************************************************************************/
	
	/**
	 * This variable contains all game elements, such as players and table.
	 */
	private final Game game;
	
	/**
	 * The variable containing the round in which the current game is.
	 * 
	 */
	private Round round;
	
	//DateFormat class is not thread safe.
	private static final String dateFormat = "yyyy/MM/dd - HH:mm:ss (z)";
	
	/***************************************************************************
	 * Constructor
	 **************************************************************************/
	
	/**
	 * Construct a new game control with given table.
	 * 
	 */
	public PlayingTableState(PokerTable gameMediator, ServerTable table) {
		this(gameMediator, table, table.getRandomPlayer());
	}
	
	public PlayingTableState(PokerTable gameMediator, ServerTable table, BettingRules rules) {
		this(gameMediator, table, table.getRandomPlayer(), rules);
	}
	
	public PlayingTableState(PokerTable gameMediator, ServerTable table, MutableSeatedPlayer dealer) {
		this(gameMediator, table, dealer, new NoLimit());
	}
	
	public PlayingTableState(PokerTable gameMediator, ServerTable table, MutableSeatedPlayer dealer, BettingRules rules) {
		super(gameMediator);
		
		game = new Game(table, gameMediator.getTableConfiguration(), dealer, rules);
		
		PlayingTableState.logger.info(getGame().getBettingRules().toString() + " " + "($"
				+ gameMediator.getTableConfiguration().getSmallBlind() + "/"
				+ gameMediator.getTableConfiguration().getBigBlind() + ") - "
				+ (new SimpleDateFormat(PlayingTableState.dateFormat)).format(new Date()));
		
		List<MutableSeatedPlayer> players = game.getCurrentDealPlayers();
		for (MutableSeatedPlayer player : players) {
			PlayingTableState.logger.info(player.toString());
		}
		
		round = new WaitingRound(gameMediator, game);
	}
	
	/**
	 * Returns the game controlled by this game controller.
	 * 
	 * @return The game controlled by this game controller.
	 */
	@Override
	public Game getGame() {
		return game;
	}
	
	public Round getRound() {
		return round;
	}
	
	/***************************************************************************
	 * Player methods
	 **************************************************************************/
	
	/**
	 * The player puts money in the pot.
	 * 
	 * @param player The player who puts a bet.
	 * @param amount The amount of the bet.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	@Override
	public synchronized void bet(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		round.bet(player, amount);
		mediatingTable.publishBetEvent(new BetEvent(player.getId(), amount));
		mediatingTable.publishPotsChangedEvent(new PotsChangedEvent(new Pots(round.getCurrentPotValue())));
		PlayingTableState.logger.info(player.getName() + " bets " + amount + ".");
		checkIfEndedAndChangeRound();
	}
	
	/**
	 * To put into the pot an amount of money equal to the most recent bet or
	 * raise.
	 * 
	 * @param player The player who calls.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	@Override
	public synchronized void call(MutableSeatedPlayer player)
			throws IllegalActionException {
		round.call(player);
		mediatingTable.publishCallEvent(new CallEvent(player.getId()));
		mediatingTable.publishPotsChangedEvent(new PotsChangedEvent(new Pots(round.getCurrentPotValue())));
		PlayingTableState.logger.info(player.getName() + " calls.");
		checkIfEndedAndChangeRound();
	}
	
	/**
	 * If there is no bet on the table and you do not wish to place a bet. You
	 * may only check when there are no prior bets.
	 * 
	 * @param player The player who checks.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	@Override
	public synchronized void check(MutableSeatedPlayer player)
			throws IllegalActionException {
		round.check(player);
		mediatingTable.publishCheckEvent(new CheckEvent(player.getId()));
		PlayingTableState.logger.info(player.getName() + " checks.");
		checkIfEndedAndChangeRound();
	}
	
	/**
	 * Raise the bet with given amount.
	 * 
	 * @param player The player who raises the current bet.
	 * @param amount The amount with which to raise the bet.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	@Override
	public synchronized void raise(MutableSeatedPlayer player, int amount)
			throws IllegalActionException {
		round.raise(player, amount);
		mediatingTable.publishRaiseEvent(new RaiseEvent(player.getId(), amount));
		mediatingTable.publishPotsChangedEvent(new PotsChangedEvent(new Pots(round.getCurrentPotValue())));
		PlayingTableState.logger.info(player.getName() + ": raises $" + amount + " to $"
				+ player.getMemento().getBetChipsValue());
		checkIfEndedAndChangeRound();
	}
	
	/**
	 * The given player folds the cards.
	 * 
	 * The player will not be able to take any actions in the coming rounds of
	 * the current deal.
	 * 
	 * @param player The player who folds.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	@Override
	public synchronized void fold(MutableSeatedPlayer player)
			throws IllegalActionException {
		round.fold(player);
		mediatingTable.publishFoldEvent(new FoldEvent(player.getId()));
		PlayingTableState.logger.info(player.getName() + ": folds");
		checkIfEndedAndChangeRound();
	}
	
	/**
	 * The player who the dealer-button has been dealt to can choose to start
	 * the deal. From that moment, new players can not join the on-going deal.
	 * 
	 * @param player The player who deals.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	@Override
	public synchronized void deal(MutableSeatedPlayer player)
			throws IllegalActionException {
		round.deal(player);
		checkIfEndedAndChangeRound();
	}
	
	public synchronized void deal() throws IllegalActionException{
		deal(game.getDealer());
	}
	
	/**
	 * The given player goes all-in.
	 * 
	 * @param player The player who goes all-in.
	 * @throws IllegalActionException [must] It's not the turn of the given
	 *             player.
	 * @throws IllegalActionException [must] The action performed is not a valid
	 *             action.
	 */
	@Override
	public synchronized void allIn(MutableSeatedPlayer player)
			throws IllegalActionException {
		round.allIn(player);
		checkIfEndedAndChangeRound();
	}
	
	@Override
	public synchronized HoldemPlayerContext sitIn(SeatId seatId, MutableSeatedPlayer player)
			throws IllegalActionException {
		try {
			game.sitIn(seatId, player);
		} catch (SeatTakenException e) {
			throw new IllegalActionException(e.getMessage());
		} catch (PlayerListFullException e) {
			throw new IllegalActionException(e.getMessage());
		}
		
		mediatingTable.publishSitInEvent(new SitInEvent(player.getMemento()));
		
		// auto-deal
		// try {
		// if (game.getNbSeatedPlayers() == 2) {
		//				
		// deal(game.getDealer());
		// }
		// } catch (IllegalActionException e) {
		// game.sitOut(player);
		// mediatingTable.publishSitOutEvent(new
		// SitOutEvent(player.getMemento(), true));
		// }
		
		return new HoldemPlayerContextImpl(player, mediatingTable);
	}
	
	@Override
	public synchronized HoldemPlayerContext sitIn(MutableSeatedPlayer player)
			throws IllegalActionException {
		try {
			game.sitIn(player);
		} catch (PlayerListFullException e) {
			throw new IllegalActionException(e.getMessage());
		}
		
		mediatingTable.publishSitInEvent(new SitInEvent(player.getMemento()));
		
		// auto-deal
		// try {
		// if (game.getNbSeatedPlayers() == 2) {
		//				
		// deal(game.getDealer());
		// }
		// } catch (IllegalActionException e) {
		// game.sitOut(player);
		// mediatingTable.publishSitOutEvent(new
		// SitOutEvent(player.getMemento(), true));
		// }
		
		return new HoldemPlayerContextImpl(player, mediatingTable);
	}
	
	@Override
	public synchronized void sitOut(MutableSeatedPlayer player) {
		if (!game.getTable().hasAsPlayer(player)) {
			return;
		}
		
		try {
			round.foldAction(player);
		} catch (IllegalActionException e) {
			// no op
		}
		SeatedPlayer immutablePlayer = player.getMemento();
		try {
			game.sitOut(player);
			mediatingTable.publishSitOutEvent(new SitOutEvent(immutablePlayer.getId(), false));
		} catch (IllegalActionException e) {}
		
		if (game.hasNoSeatedPlayers()) {
			// lobby.removeTable(table);
		} else {
			checkIfEndedAndChangeRound();
		}
	}
	
	@Override
	public List<SeatedPlayer> getSeatedPlayers() {
		return game.getTable().getSeatedPlayers();
	}
	
	@Override
	public List<MutableSeatedPlayer> getMutableSeatedPlayers() {
		return game.getTable().getMutableSeatedPlayers();
	}
	
	@Override
	public boolean isPlaying() {
		return true;
	}
	
	/***************************************************************************
	 * Round change logic
	 **************************************************************************/
	
	/**
	 * Check if this round has ended and change to the next round if it's the
	 * case.
	 */
	private void checkIfEndedAndChangeRound() {
		if (round.isRoundEnded()) {
			changeToNextRound();
		} else {
			MutableSeatedPlayer player = game.getCurrentPlayer();
			if (player != null) {
				mediatingTable.publishNextPlayerEvent(new NextPlayerEvent(player.getId()));
			}
		}
	}
	
	/**
	 * End this round and change the round to the next round.
	 * 
	 */
	private void changeToNextRound() {
		round.endRound();
		round = round.getNextRound();
		
		if (round instanceof WaitingRound && game.getNbSeatedPlayers() > 1) {
			submitAutoDealHandler();
		}
		
		if ((round instanceof BettingRound) && ((BettingRound) round).onlyOnePlayerLeftBesidesAllInPlayersAndCalled()) {
			changeToNextRound();
		}
		if ((round instanceof BettingRound) && ((BettingRound) round).onlyAllInPlayers()) {
			changeToNextRound();
		}
	}
	
	private void submitAutoDealHandler() {
		long delay = game.getTableConfiguration().getDelay();
		if (delay > 0) {
			delay = delay * (Math.min(game.getNbLastShowdown() + 1, 5));
			ScheduledRequestExecutor.getInstance().schedule(new AutoDealHandler(), delay, TimeUnit.MILLISECONDS);
			logger.info("There were " + game.getNbLastShowdown()
					+ " showdown players. Auto-deal handler submitted with a delay of " + delay + " ms.");
		} else {
			try {
				deal(game.getDealer());
			} catch (IllegalActionException e) {}
		}
		
	}
	
	private class AutoDealHandler
			implements Runnable {
		
		public AutoDealHandler() {}
		
		public void run() {
			try {
				PlayingTableState.logger.info(game.getDealer() + " auto-deal called.");
				deal(game.getDealer());
			} catch (IllegalActionException e) {
				PlayingTableState.logger.error(e);
			}
		}
	}
	
	@Override
	public TableState getNextState() {
		return this;
	}

	@Override
	public MutableSeatedPlayer getMutableSeatedPlayer(PlayerId id) {
		for(MutableSeatedPlayer player :game.getTable().getMutableSeatedPlayers()){
			if(player.getId().equals(id))
				return player;
		}
		return null;
	}
	
}
