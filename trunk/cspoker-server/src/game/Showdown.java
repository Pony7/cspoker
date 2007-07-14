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

package game;

import game.cards.Card;
import game.cards.hand.Hand;
import game.cards.hand.HandEvaluator;
import game.chips.IllegalValueException;
import game.chips.pot.Pot;
import game.player.Player;
import game.player.ShowdownPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class to determine who has won each pot.
 * 
 * @author Kenzo
 *
 */
public class Showdown {
	
	/**
	 * The game in which the showdown takes place.
	 */
	private final Game game;
	
	/**
	 * Construct a new showdown with given game and pots.
	 * 
	 * @param 	game
	 * 			The game in which the showdown takes place.
	 * @param 	pots
	 * 			The pots to divide.
	 * @pre 	The game must be effective.
	 *			|game!=null
	 * @pre 	The pots must be closed.
	 *			|game!=null && game.getPots().isClosed()
	 */
	public Showdown(Game game){
		this.game = game;
	}
	
	/**
	 * Returns the game in which this showdown takes place.
	 * 
	 * @return The game in which this showdown takes place.
	 */
	public Game getGame(){
		return game;
	}
	
	/**
	 * Determine the winners of each pot.
	 * Each pot is splitted between all winners of that pot.
	 * 
	 */
	public void determineWinners(){
		for(Pot pot:game.getPots().getPots()){
			splitPot(getWinners(pot), pot);
		}
	}
	
	/**
	 * Split the pot between all winners.
	 * 
	 * If splitting a pot because of tied hands,
	 * award the odd chip to the hand that contains
	 * the highest-ranking single card,
	 * using suits to break ties if necessary
	 * (clubs ranking the lowest, followed by diamonds,
	 * hearts, and spades as in bridge).
	 * 
	 * http://en.wikipedia.org/wiki/Split_(poker)
	 * 
	 * @param 	winners
	 * 			The list of winners of the pot.
	 * @param 	pot
	 * 			The pot to divide between all winners.
	 */
	private void splitPot(List<Player> winners, Pot pot){
		int nbWinners = winners.size();
		
		//only one winner
		if(nbWinners ==1){
			try {
				pot.getChips().transferAllChipsTo(winners.get(0).getChips());
			} catch (IllegalValueException e) {
				assert false;
			}
			
		}else{
			int nbChips_per_winner = pot.getChips().getValue()/nbWinners;
			
			//can be divided easily.
			if(nbChips_per_winner*nbWinners==pot.getChips().getValue()){
				for(Player player:winners){
					try {
						pot.getChips().transferAmountTo(nbChips_per_winner, player.getChips());
					} catch (IllegalValueException e) {
						assert false;
					}
				}
				assert pot.getChips().getValue()==0;
			//more difficult logic.
			}else{
				// the player with the single highest card gets the odd chips that can't be divided over the winners
				Player playerWithHighestSingleCard=winners.get(0);
				for(Player player:winners){
					int compareSingleBestCard=getBestFiveCardHand(playerWithHighestSingleCard).getHighestRankCard().compareTo(
					getBestFiveCardHand(player).getHighestRankCard());
					if(compareSingleBestCard==1){
						playerWithHighestSingleCard=player;
					}else{
						if(compareSingleBestCard==0 && getBestFiveCardHand(player).getHighestRankCard().getSuit()
								.getValue()>getBestFiveCardHand(playerWithHighestSingleCard).getHighestRankCard().getSuit().getValue())
							playerWithHighestSingleCard=player;
					}
						
				}
				//transfer chips to all winners
				for(Player player:winners){
					try {
						pot.getChips().transferAmountTo(nbChips_per_winner, player.getChips());
					} catch (IllegalValueException e) {
						assert false;
					}
				}
				//playerWithHighestSingleCard gets the odd chip, that can't be divided over all winners
				int oddChipsValue=pot.getChips().getValue()-nbChips_per_winner*nbWinners;
				try {
					pot.getChips().transferAmountTo(oddChipsValue, playerWithHighestSingleCard.getChips());
				} catch (IllegalValueException e) {
					assert(false);
				}
			}
		}
	}
	
	/**
	 * Returns the list of winners of the given pot in the current game.
	 * 
	 * @param 	pot
	 * 			The pot in which the winner(s) must be chosen.
	 * @return	The list of winners of the pot in the current game.
	 */
	private List<Player> getWinners(Pot pot){
		List<ShowdownPlayer> players = getShowdownPlayersFromPot(pot);
		Collections.sort(players);
		ShowdownPlayer winner = players.get(0);
		List<Player> winners = new ArrayList<Player>();
		int i=1;
		while((i<players.size()) && winner.equals(players.get(i))){
			winners.add(players.get(i).getPlayer());
			i++;
		}
		return winners;
	}
	
	/**
	 * Returns the list of showdown players in the current game.
	 * 
	 * @param 	pot
	 * 			The pot from which the showdown players must be returned.
	 * @return	The list of showdown players in the current game.
	 */
	private List<ShowdownPlayer> getShowdownPlayersFromPot(Pot pot){
		List<ShowdownPlayer> showDownPlayers = new ArrayList<ShowdownPlayer>();
		for(Player player: pot.getPlayers()){
			showDownPlayers.add(new ShowdownPlayer(player, getBestFiveCardHand(player)));
		}
		return showDownPlayers;
	}
	
	/**
	 * Get the best hand for the given player in the current game.
	 * 
	 * @param 	player
	 * 			The player to determine the best hand for.
	 * @return	The best hand that can be made with both common and pocket cards.
	 * 
	 * @note 	By using this method, the recalculation of finding the best 5 card hand is omitted.
	 */
	private Hand getBestFiveCardHand(Player player){
		List<Card> cards = new ArrayList<Card>(7);
		cards.addAll(getGame().getOpenCards());
		cards.addAll(player.getPocketCards());
		return HandEvaluator.getBestHand(new Hand(cards));
	}
}
