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

package org.cspoker.server.game.gameControl.rounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cspoker.server.game.GameMediator;
import org.cspoker.server.game.elements.cards.Card;
import org.cspoker.server.game.elements.chips.IllegalValueException;
import org.cspoker.server.game.elements.chips.pot.Pots;
import org.cspoker.server.game.elements.player.AllInPlayer;
import org.cspoker.server.game.events.WinnerEvent;
import org.cspoker.server.game.events.playerActionEvents.AllInEvent;
import org.cspoker.server.game.gameControl.Game;
import org.cspoker.server.game.gameControl.PlayerAction;
import org.cspoker.server.game.gameControl.actions.IllegalActionException;
import org.cspoker.server.game.gameControl.rules.BettingRules;
import org.cspoker.server.game.player.Player;
import org.cspoker.server.game.player.SavedWinner;


/**
 * An abstract class to represent rounds.
 * A player can do actions in a round,
 * such as checking, betting, ...
 *
 * A state pattern is used.
 *
 * @author Kenzo
 *
 */
public abstract class Round implements PlayerAction{

	/**********************************************************
	 * Variables
	 **********************************************************/

	/**
	 * The last event player is the last player
	 * that has done significant change,
	 * such as a raise.
	 *
	 * If the next player is the last event player,
	 * the round is over.
	 *
	 * It is initialized in each game as the first better
	 * after the big blind, in every next round,
	 * it is the player on to the left side of the player
	 * with the dealer-button.
	 */
	protected Player lastEventPlayer;

	/**
	 * The variable containing the game in which
	 * this round takes place.
	 */
	protected final Game game;

	/**
	 * The current bet in this round.
	 */
	private int bet;

	/**
	 * This list contains all players who go
	 * all-in in this round.
	 */
	private final List<AllInPlayer> allInPlayers;

	/**
	 * This list contains all players who folded,
	 * but who have placed chips on their betted chips pile.
	 */
	protected final List<Player> foldedPlayersWithBet;

	protected final GameMediator gameMediator;

	/**********************************************************
	 * Constructor
	 **********************************************************/

	/**
	 * Initialize a new round for given game.
	 *
	 * @param 	game
	 * 			The game to create a new round for.
	 */
	public Round(GameMediator gameMediator, Game game){
		this.gameMediator = gameMediator;
		this.game = game;
		allInPlayers = new ArrayList<AllInPlayer>();
		foldedPlayersWithBet = new ArrayList<Player>();
		setBet(0);
		getBettingRules().setBetPlaced(false);
		getBettingRules().clearNBRaises();
		lastEventPlayer = getGame().getFirstToActPlayer();
		getGame().setCurrentPlayer(getGame().getFirstToActPlayer());
	}

	/**
	 * Returns the game this round is part of.
	 *
	 * @return The game this round is part of.
	 */
	public Game getGame(){
		return game;
	}

	/**********************************************************
	 * Bet
	 **********************************************************/

	/**
	 * Returns the current bet of this round.
	 *
	 * @return The current bet of this round.
	 */
	public int getBet() {
		return bet;
	}

	/**
	 * Check whether rounds can have the given bet
	 * as their bet.
	 *
	 * @param	bet
	 * 			The bet to check.
	 * @return	The bet must be positive.
	 * 			| result == (bet>=0)
	 */
	public static boolean canHaveAsBet(int bet) {
		return bet>=0;
	}

	/**
	 * Set the bet of this round to the given bet.
	 *
	 * @param	bet
	 * 			The new bet for this round.
	 * @pre    	This round must be able to have the given bet
	 * 			as its bet.
	 * 			| canHaveAsBet(bet)
	 * @post	The bet of this round is set to the given
	 * 			bet.
	 * 			| new.getBet() == bet
	 */
	private void setBet(int bet) {
		this.bet = bet;
	}

	/**
	 * Returns true if someone has bet.
	 *
	 * @return 	True if someone has bet,
	 * 			False otherwise.
	 */
	public boolean someoneHasBet(){
		return bet>0;
	}

	/**
	 * Raise the bet with given amount.
	 *
	 * @param 	amount
	 * 			The amount to raise the bet with.
	 * @pre 	The amount should be positive.
	 *			|amount>0
	 * @effect	Set the bet of this round to the current bet
	 * 			raised with given amount.
	 *		   	|setBet(getBet()+amount)
	 */
	protected void raiseBetWith(int amount){
		setBet(getBet()+amount);
	}
	/**********************************************************
	 * Betting Rules
	 **********************************************************/
	/**
	 * Returns the betting game.rounds.rules for this round
	 */
	public BettingRules getBettingRules(){
		return getGame().getGameProperty().getBettingRules();
	}
	/**********************************************************
	 * Collect blinds
	 **********************************************************/

	/**
	 * Collect small blind from given player.
	 *
	 * @param 	player
	 * 			The player to collect the small blind from.
	 * @throws IllegalValueException
	 */
	protected void collectSmallBlind(Player player) throws IllegalValueException{
		if(player.getStack().getValue()<=getGame().getGameProperty().getSmallBlind())
			throw new IllegalValueException();
		player.transferAmountToBettedPile(getGame().getGameProperty().getSmallBlind());
		raiseBetWith(getGame().getGameProperty().getSmallBlind());
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(getGame().getGameProperty().getSmallBlind());
		playerMadeEvent(player);
	}

	/**
	 * Collect big blind from given player.
	 *
	 * @param 	player
	 * 			The player to collect the big blind from.
	 * @throws IllegalValueException
	 */
	protected void collectBigBlind(Player player) throws IllegalValueException{
		if(player.getStack().getValue()<=getGame().getGameProperty().getBigBlind())
			throw new IllegalValueException();
		player.transferAmountToBettedPile(getGame().getGameProperty().getBigBlind());
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(getGame().getGameProperty().getBigBlind());
		setBet(getGame().getGameProperty().getBigBlind());
		playerMadeEvent(player);
	}

	/**********************************************************
	 * Bidding methods
	 **********************************************************/

	//TODO onTurn() here.
	
	/**
	 * If there is no bet on the table and you do not wish to place a bet.
	 * You may only check when there are no prior bets.
	 *
	 * @param	player
	 * 			The player who checks.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
     * @see		PlayerAction
	 */
	public void check(Player player) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+"can not check in this round.");
	}

	/**
	 * The player puts money in the pot.
	 *
	 * @param 	player
	 * 			The player who puts a bet.
	 * @param 	amount
	 * 			The amount of the bet.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
     * @see		PlayerAction
	 */
	public void bet(Player player, int amount) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not bet "+amount+" chips in this round.");
	}

	/**
	 * To put into the pot an amount of money equal to
	 * the most recent bet or raise.
	 *
	 * @param 	player
	 * 			The player who calls.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 * @see		PlayerAction
	 */
	public void call(Player player) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not call in this round.");
	}

	/**
	 * The player puts money in the pot.
	 *
	 * @param 	player
	 * 			The player who puts a bet.
	 * @param 	amount
	 * 			The amount of the bet.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 * @see		PlayerAction
	 */
	public void raise(Player player, int amount) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not raise with "+amount+" chips in this round.");
	}

	/**
	 * The given player folds the cards.
	 *
	 * The player will not be able to take any actions
	 * in the coming rounds of the current deal.
	 *
	 * @param 	player
	 * 			The player who folds.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 */
	public void fold(Player player) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not fold in this round.");
	}

	/**
	 * The player who the dealer-button has been dealt to
	 * can choose to start the deal.
	 * From that moment, new players can not join the on-going deal.
	 *
	 * @param 	player
	 * 			The player who deals.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 * @see		PlayerAction
	 */
	public void deal(Player player) throws IllegalActionException{
		throw new IllegalActionException(player.getName()+" can not deal in this round.");
	}

	/**
	 * The given player goes all-in.
	 *
	 * @param 	player
	 * 			The player who goes all-in.
	 * @throws  IllegalActionException [must]
	 * 			It's not the turn of the given player.
	 * @throws  IllegalActionException [must]
     *          The action performed is not a valid action.
	 * @see		PlayerAction
	 */
	public void allIn(Player player) throws IllegalActionException {
		throw new IllegalActionException(player.getName()+" can not go all-in in this round.");
	}

	/**********************************************************
	 * Round Logic
	 **********************************************************/


	/**
	 * Check whether the round is ended or not.
	 *
	 * It is the case when there are no more active players,
	 * or when the last event player is the next player.
	 *
	 * @return 	True if the round is ended,
	 * 			false otherwise.
	 */
	public boolean isRoundEnded(){
		return (getGame().getNbCurrentDealPlayers()==0) || onlyOnePlayerLeft() || lastEventPlayer.equals(game.getCurrentPlayer());
	}

	/**
	 * End the current round.
	 *
	 */
	public abstract void endRound();

	/**
	 * Returns the next round.
	 *
	 */
	public abstract Round getNextRound();



	/**
	 * Set the last event player
	 * to the given player.
	 *
	 * @param 	player
	 * 			The player who did the last event.
	 */
	protected void playerMadeEvent(Player player){
		lastEventPlayer = player;
	}

	public boolean onTurn(Player player){
		return game.getCurrentPlayer().equals(player);
	}

	public boolean isBettingRound(){
		return true;
	}

	protected void goAllIn(Player player){
		try {
			player.transferAllChipsToBettedPile();
		} catch (IllegalValueException e) {
			assert false;
		}

		allInPlayers.add(new AllInPlayer(player));
		getGame().removePlayerFromCurrentDeal(player);
		if(player.getBettedChips().getValue()>getBet()){
			setBet(player.getBettedChips().getValue());
			playerMadeEvent(player);
		}
		gameMediator.publishAllInEvent(new AllInEvent(player.getSavedPlayer()));
		System.out.println(player.getName()+" goes all in with "+player.getBettedChips().getValue()+" chips.");
	}

	/**
	 * Returns true if there is only one
	 * player left, false otherwise.
	 *
	 * This also implies there are no all-in players,
	 * otherwise there will be a showdown.
	 *
	 * @return 	True if there is only one player left,
	 * 			False otherwise.
	 */
	public boolean onlyOnePlayerLeft(){
		return (getGame().getNbCurrentDealPlayers()+allInPlayers.size()+getGame().getPots().getNbShowdownPlayers()<=1);
	}
	public boolean onlyAllInPlayers(){
		return (getGame().getNbCurrentDealPlayers()==0);
	}

	/**********************************************************
	 * Collect Chips
	 **********************************************************/

	/**
	 * Collect the betted chips pile from all players.
	 * Also creates new side pots if necessary
	 * in the case of all-in players.
	 *
	 */
	protected void collectChips(){
		makeSidePots();
		collectBets();
	}

	/***
	 * Move all-in players to side pot.
	 *
	 */
	private void makeSidePots(){
		Collections.sort(allInPlayers);
		List<Player> players = game.getCurrentDealPlayers();
		for(AllInPlayer allInPlayer:allInPlayers){
			try {
				game.getPots().collectAmountFromPlayersToSidePot(allInPlayer.getBetValue(), players);
				int betValue = allInPlayer.getBetValue();
				for(AllInPlayer otherAllInPlayer:allInPlayers){
					if(otherAllInPlayer.getBetValue()>0){
						otherAllInPlayer.transferAmountTo(betValue,game.getPots().getNewestSidePot());
					}
				}
				for(Player foldedPlayer:foldedPlayersWithBet){
					if(foldedPlayer.getBettedChips().getValue()>allInPlayer.getBetValue()){
						foldedPlayer.getBettedChips().transferAmountTo(allInPlayer.getBetValue(), game.getPots().getNewestSidePot().getChips());
					}else{
						foldedPlayer.getBettedChips().transferAllChipsTo(game.getPots().getNewestSidePot().getChips());
						foldedPlayersWithBet.remove(foldedPlayer);
					}
				}

			} catch (IllegalValueException e) {
				assert false;
			}
			System.out.println(game.getPots());
			game.getPots().addShowdownPlayer(allInPlayer.getPlayer());
		}
		allInPlayers.clear();
	}

	/**
	 * Collect the bets from all players.
	 *
	 */
	private void collectBets(){
			game.getPots().collectChipsToPot(game.getCurrentDealPlayers());
			game.getPots().collectChipsToPot(foldedPlayersWithBet);
			foldedPlayersWithBet.clear();
	}

	/**
	 * Returns how many chips a player
	 * must transfer to the betted pile
	 * to equal the current bet.
	 *
	 * @param 	player
	 * 			The player who wants to know
	 * 			how many chips to transfer.
	 * @return	The number of chips the player
	 * 			must transfer to the betted pile
	 * 			to equal the current bet.
	 */
	protected int amountToIncreaseBettedPileWith(Player player){
		return getBet()-player.getBettedChips().getValue();
	}

	protected void winner(Pots pots){
		try {
			System.out.println("** Only One Player Left **");
			
			Player winner = pots.getPots().get(0).getPlayers().get(0);
			int gainedChipsValue = pots.getPots().get(0).getChips().getValue();
			List<SavedWinner> savedWinner = new ArrayList<SavedWinner>(1);
			savedWinner.add(new SavedWinner(winner.getSavedPlayer(),gainedChipsValue));
			pots.getPots().get(0).getChips().transferAllChipsTo(winner.getStack());
			
			gameMediator.publishWinner(new WinnerEvent(savedWinner));
			System.out.println("Winner: "+winner.getName());
		} catch (IllegalValueException e) {
			assert false;
		}
	}

	protected void removeBrokePlayers(){
		for(Player player:getGame().getTable().getPlayers()){
			if(player.getStack().getValue()==0){
				try {
					getGame().leaveGame(player);
				} catch (IllegalActionException e) {
					throw new IllegalStateException();
				}
			}
		}
	}

	/**********************************************************
	 * Cards
	 **********************************************************/

	/**
	 * Draw a card from the deck and send it to the muck.
	 *
	 */
	protected void drawMuckCard(){
		getGame().addMuckCard(drawCard());
	}

	/**
	 * Draw a card from the deck and add it to the community cards.
	 *
	 */
	protected void drawOpenCard(){
		getGame().addOpenCard(drawCard());
	}

	/**
	 * Draw a card from the deck.
	 *
	 * @return The top card from the deck is returned.
	 */
	protected Card drawCard(){
		return getGame().drawCard();
	}

	public abstract boolean isLowBettingRound();

	public abstract boolean isHighBettingRound();

	public int getCurrentPotValue(){
		int currentPlayerBets=0;
		for(Player player:game.getCurrentDealPlayers()){
			currentPlayerBets+=player.getBettedChips().getValue();
		}
		int foldedPlayerBets=0;
		for(Player player:foldedPlayersWithBet){
			foldedPlayerBets+=player.getBettedChips().getValue();
		}

		int allInPlayerBets=0;
		for(AllInPlayer player:allInPlayers){
			allInPlayerBets+=player.getBetValue();
		}
		return game.getPots().getTotalValue()+currentPlayerBets+foldedPlayerBets+allInPlayerBets;
	}
}
