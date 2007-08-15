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

package org.cspoker.server.game.gameControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cspoker.server.game.PlayerId;
import org.cspoker.server.game.elements.cards.Card;
import org.cspoker.server.game.elements.cards.hand.Hand;
import org.cspoker.server.game.elements.cards.hand.HandEvaluator;
import org.cspoker.server.game.elements.chips.IllegalValueException;
import org.cspoker.server.game.elements.chips.pot.Pot;
import org.cspoker.server.game.elements.player.ShowdownPlayer;
import org.cspoker.server.game.elements.player.Winner;
import org.cspoker.server.game.player.Player;

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
	
	private final Map<PlayerId, Winner> winnersMap = new HashMap<PlayerId, Winner>();

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

		//TODO The amounts the winner collects are collected elsewhere, so the total profit of the winner
		//can easily be returned.
		
		System.out.println("");
		System.out.println("*** Determine Winners ***");
		System.out.println("");
		System.out.println(game.getPots());
		for(Pot pot:game.getPots().getPots()){
			List<Player> winners = getWinners(pot);
			System.out.print("Winners: ");
			for(Player player:winners){
				System.out.print(player.getName()+" ");
			}
			System.out.println("");
			System.out.println("");
			splitPot(winners, pot);
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
//		for(Player winner:winners){
//			if(!winnersMap.containsKey(winner.getId())){
//				winnersMap.put(winner.getId(), new Winner(winner));
//			}
//		}
//
//		try {
//			Chips chips = new Chips(100);
//			chips.transferAllChipsTo(winnersMap.get(winners.get(0).getId()).getGainedChipsPile());
//			System.out.println(winnersMap.get(winners.get(0).getId()));
//		} catch (IllegalValueException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		int nbChips_per_winner = pot.getChips().getValue()/nbWinners;

		for(Player player:winners){
			try {
				pot.getChips().transferAmountTo(nbChips_per_winner, player.getStack());
			} catch (IllegalValueException e) {
				assert false;
			}
		}
		
		//TODO Only pocket cards must be considered.

		// the player with the single highest card gets the odd chips that can't be divided over the winners
		if(pot.getChips().getValue()!=0){
			Player playerWithHighestSingleCard=winners.get(0);
			Card highestCard = getBestFiveCardHand(playerWithHighestSingleCard).getHighestRankCard();
			for(Player player:winners){
				Card otherHighestCard = getBestFiveCardHand(player).getHighestRankCard();
				
				int compareSingleBestCard=highestCard.compareTo(otherHighestCard);
				if((compareSingleBestCard>0) || ((compareSingleBestCard==0) && (otherHighestCard.getSuit().compareTo(highestCard.getSuit())>0))){
					playerWithHighestSingleCard=player;
					highestCard = otherHighestCard;
				}
			}
			//playerWithHighestSingleCard gets the odd chip, that can't be divided over all winners
			try {
				System.out.println("Odd chips to player with highest card in hand");
				pot.getChips().transferAllChipsTo(playerWithHighestSingleCard.getStack());
			} catch (IllegalValueException e) {
				throw new IllegalStateException("Overflow");
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
		for(ShowdownPlayer player:players){
			System.out.println(player);
		}
		ShowdownPlayer winner = players.get(0);
		List<Player> winners = new ArrayList<Player>();
		int i=0;
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
		cards.addAll(getGame().getCommunityCards());
		cards.addAll(player.getPocketCards());
		return HandEvaluator.getBestHand(new Hand(cards));
	}
}
