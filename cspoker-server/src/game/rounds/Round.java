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

package game.rounds;

import game.Game;
import game.PlayerAction;
import game.actions.Action;
import game.actions.IllegalActionException;
import game.cards.Card;
import game.chips.IllegalValueException;
import game.chips.pot.Pots;
import game.player.AllInPlayer;
import game.player.Player;
import game.rules.BettingRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
	private Player lastEventPlayer;
	
	/**
	 * The variable containing the game in which
	 * this round takes place.
	 */
	private final Game game;
	
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
	private final List<Player> foldedPlayersWithBet;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	
	/**
	 * Initialize a new round for given game.
	 * 
	 * @param 	game
	 * 			The game to create a new round for.
	 */
	public Round(Game game){
		this.game = game;
		allInPlayers = new ArrayList<AllInPlayer>();
		foldedPlayersWithBet = new ArrayList<Player>();
		setBet(0);
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
	private void raiseBetWith(int amount){
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
		player.transferAmountToBettedPile(getGame().getGameProperty().getBigBlind());
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(getGame().getGameProperty().getBigBlind());
		setBet(getGame().getGameProperty().getBigBlind());
		playerMadeEvent(player);
	}
	
	/**********************************************************
	 * Bidding methods
	 **********************************************************/
	
	/**
	 * If there is no bet on the table and you do not wish to place a bet.
	 * You may only check when there are no prior bets.
	 * 
	 * @param	player
	 * 			The player who checks.
	 * @see PlayerAction
	 */
	public void check(Player player) throws IllegalActionException{
		if(!Action.CHECK.canDoAction(this, player))
			throw new IllegalActionException(player, Action.CHECK);
		game.nextPlayer();
	}
	
	public void bet(Player player, int amount) throws IllegalActionException{
		if(!getBettingRules().isValidBet(amount))
			throw new IllegalActionException(player, Action.BET,getBettingRules().getLastBetErrorMessage());
		if(!Action.BET.canDoAction(this, player))
			throw new IllegalActionException(player, Action.BET);
		if(amount==0)
			throw new IllegalActionException(player, Action.BET, "Can not bet with 0 chips. Did you mean check?");
		
		try {
			player.transferAmountToBettedPile(amountToIncreaseBettedPileWith(player)+amount);
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.BET, e.getMessage());
		}
		raiseBetWith(amount);
		getBettingRules().setBetPlaced(true);
		getBettingRules().setLastBetAmount(amount);
		playerMadeEvent(player);
		game.nextPlayer();
	}
	public void call(Player player) throws IllegalActionException{
		if(!Action.CALL.canDoAction(this, player))
			throw new IllegalActionException(player, Action.CALL);
		try {
			player.transferAmountToBettedPile(amountToIncreaseBettedPileWith(player));
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.CALL, e.getMessage());
		}
		if(!game.hasAsActivePlayer(lastEventPlayer))
			playerMadeEvent(player);
		game.nextPlayer();
	}
	
	public void raise(Player player, int amount) throws IllegalActionException{
		if(amount>player.getStack().getValue())
			throw new IllegalActionException(player, Action.RAISE,"Can not raise with more chips than your stack obtains");
		if(!getBettingRules().isValidRaise(amount))
			throw new IllegalActionException(player, Action.RAISE,getBettingRules().getLastRaiseErrorMessage());
		if(!Action.RAISE.canDoAction(this, player))
			throw new IllegalActionException(player, Action.RAISE);
		if(amount==0)
			throw new IllegalActionException(player, Action.RAISE, "Can not raise with 0 chips. Did you mean call?");
		
		try {
			player.transferAmountToBettedPile(amountToIncreaseBettedPileWith(player)+amount);
		} catch (IllegalValueException e) {
			throw new IllegalActionException(player, Action.RAISE, e.getMessage());
		}
		raiseBetWith(amount);
		getBettingRules().incrementNBRaises();
		getBettingRules().setLastBetAmount(amount);
		playerMadeEvent(player);
		game.nextPlayer();
	}
	
	public void fold(Player player) throws IllegalActionException{
		if(!Action.FOLD.canDoAction(this, player))
			throw new IllegalActionException(player, Action.FOLD);
		player.clearPocketCards();
		if(player.getBettedChips().getValue()>0){
			foldedPlayersWithBet.add(player);
		}
		game.removePlayerFromCurrentDeal(player);
		
		//removing from game, automatically switches
		//to next player.
	}
	
	public void deal(Player player) throws IllegalActionException{
		if(!Action.DEAL.canDoAction(this, player))
			throw new IllegalActionException(player, Action.DEAL);
		playerMadeEvent(player);
	}
	
	public void allIn(Player player) throws IllegalActionException {
		if(!Action.ALL_IN.canDoAction(this, player))
			throw new IllegalActionException(player, Action.ALL_IN);
		goAllIn(player);
	}
	
	/**********************************************************
	 * Round Logic
	 **********************************************************/
	
	
	/**
	 * Check whether the round is ended or not.
	 * 
	 * @return 	True if the round is ended,
	 * 			false otherwise.
	 */
	public boolean isRoundEnded(){
		return (getGame().getNbCurrentDealPlayers()==0) || lastEventPlayer.equals(game.getCurrentPlayer());
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
		System.out.println(player.getName()+" goes all-in.");
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
		return (getGame().getNbCurrentDealPlayers()+allInPlayers.size()==1);
	}
	public boolean onlyAllInPlayersAndAtMostOneActivePlayer(){
		return (getGame().getNbCurrentDealPlayers()<=1);
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
			pots.getPots().get(0).getChips().transferAllChipsTo(pots.getPots().get(0).getPlayers().get(0).getStack());
		} catch (IllegalValueException e) {
			assert false;
		}
	}
	
	protected void removeBrokePlayers(){
		for(Player player:getGame().getTable().getPlayers()){
			if(player.getStack().getValue()==0){
				getGame().leaveGame(player);
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
