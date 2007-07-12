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
import game.chips.pot.Pot;
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
	
	private final Game game;
	
	private int bet;
	
	private final List<AllInPlayer> allInPlayers;
	
	private BettingRules bettingRules;
	
	/**********************************************************
	 * Constructor
	 **********************************************************/
	/**
	 * Initialize a new round for given game.
	 * 
	 * @param 	game
	 * 			The game to create a new round for.
	 */
	public Round(Game game,BettingRules bettingRules){
		this.game = game;
		setBettingRules(bettingRules);
		allInPlayers = new ArrayList<AllInPlayer>();
		bet = 0;
		lastEventPlayer = getGame().getFirstToActPlayer();
		getGame().setCurrentPlayer(getGame().getFirstToActPlayer());
	}
	
	protected Game getGame(){
		return game;
	}
	
	/**********************************************************
	 * Bet
	 **********************************************************/
	
	/**
	 * Set the bet of this round to the given value.
	 * 
	 */
	protected void setBet(int value){
		bet = value; 
	}
	
	/**
	 * Returns the current bet value of this round.
	 * 
	 * @return The current bet value of this round.
	 */
	public int getBet(){
		return bet;
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
		return bettingRules;
	}
	protected void setBettingRules(BettingRules bettingRules){
		this.bettingRules=bettingRules;
		this.bettingRules.setRound(this);
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
		setBet(getGame().getGameProperty().getBigBlind());
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
		
		game.nextPlayer();
	}
	
	public void raise(Player player, int amount) throws IllegalActionException{
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
		
		game.removePlayerFromCurrentDeal(player);
		
		//removing from game, automatically switches
		//to next player.
	}
	
	public void deal(Player player) throws IllegalActionException{
		if(!Action.DEAL.canDoAction(this, player))
			throw new IllegalActionException(player, Action.DEAL);
		
	}
	
	public void allIn(Player player) throws IllegalActionException {
		// TODO Auto-generated method stub
		
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
		return lastEventPlayer.equals(game.getCurrentPlayer());
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
		}
		
		//TODO problem if all-in player is
		//last event player?
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
		return (getGame().getNbCurrentDealPlayers()==1) && (allInPlayers.size()==0);
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
	
	private void makeSidePots(){
		Collections.sort(allInPlayers);
		List<Player> players = game.getCurrentHandPlayers();
		for(AllInPlayer player:allInPlayers){
			try {
				game.getPots().collectAmountFromPlayersToSidePot(player.getBetValue(), players);
			} catch (IllegalValueException e) {
				assert false;
			}
			game.getPots().addShowdownPlayer(player.getPlayer());
		}
		allInPlayers.clear();
	}
	
	private void collectBets(){
			game.getPots().collectChipsToPot(game.getCurrentHandPlayers());
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
	
	protected void winner(Pot pot, Player player){
		
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

}
