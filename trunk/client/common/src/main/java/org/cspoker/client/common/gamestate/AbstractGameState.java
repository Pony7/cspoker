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
package org.cspoker.client.common.gamestate;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;

/**
 * Abstract GameState partial implementation. Only methods that are a simple
 * combination of other methods should be implemented here. This is the only
 * place where you can safely call other methods in the same state.
 * 
 * @author guy
 */
public abstract class AbstractGameState
implements GameState {

	private final static Logger logger = Logger.getLogger(AbstractGameState.class);

	public final int getDeficit(PlayerId playerId) {
		return getLargestBet() - getPlayer(playerId).getBet();
	}

	public final int getCallValue(PlayerId playerId) {
		PlayerState player = getPlayer(playerId);
		return Math.min(getLargestBet() - player.getBet(), player.getStack());
	}

	public final boolean isAllowedToRaise(PlayerId playerId) {
		PlayerState player = getPlayer(playerId);
		if (getLargestBet() - player.getBet() >= player.getStack()) {
			return false;
		}
		Set<PlayerState> otherPlayers = getAllSeatedPlayers();
		for (PlayerState otherPlayer : otherPlayers) {
			// check whether we are the only active player left in the game.
			if (!otherPlayer.getPlayerId().equals(playerId) && otherPlayer.isActivelyPlaying()) {
				return true;
			}
		}
		return false;
	}

	public final int getLowerRaiseBound(PlayerId playerId) {
		PlayerState player = getPlayer(playerId);
		return Math.min(getMinNextRaise(), player.getStack());
	}

	public final int getUpperRaiseBound(PlayerId playerId) {
		PlayerState player = getPlayer(playerId);
		return player.getStack() - (getLargestBet() - player.getBet());
	}

	public final int getGamePotSize() {
		return getPreviousRoundsPotSize() + getRoundPotSize();
	}

	public final boolean hasBet() {
		return getLargestBet() > 0;
	}

	public final Set<PlayerState> getAllSeatedPlayers() {
		Set<PlayerId> ids = getAllSeatedPlayerIds();
		HashSet<PlayerState> states = new HashSet<PlayerState>();
		for (PlayerId id : ids) {
			states.add(getPlayer(id));
		}
		return states;
	}



	public final PlayerState previewNextToAct() {
		if (getRound().equals(Round.PREFLOP)) {
			// TODO implement
			throw new UnsupportedOperationException("Not yet implemented");
		}
		PlayerId lastBettor = getLastBettor();
		PlayerId startId = getNextToAct();
		boolean newRound = false;
		if(startId==null){
			startId = getDealer();
			newRound = true;
		}else if(lastBettor==null && startId.equals(getDealer())){
			//everybody checked
			return null;
		}
		PlayerState startPlayer = getPlayer(startId);
		PlayerState nextPlayer;
		while(true){
			nextPlayer= getNextActivePlayerAfter(startPlayer.getSeatId());
			if(nextPlayer==null){
				if(newRound){
					//the dealer is the next one to act.
					PlayerState dealerState = getPlayer(startId);
					if(dealerState.sitsIn() && !dealerState.isAllIn()){
						return dealerState;
					}	
					//nobody does anything this round
					return null;
				}
				//nobody can act
				return null;
			}
			if(nextPlayer.getPlayerId().equals(lastBettor)){
				//everybody called
				return null;
			}
			return nextPlayer;
		}

	}

	public final PlayerState getNextActivePlayerAfter(SeatId startSeat) {		
		SeatId currentSeat = startSeat;
		PlayerState currentPlayer;
		int maxNbPlayers = getTableConfiguration().getMaxNbPlayers();
		do {
			PlayerId currentId;
			do{
				currentSeat = new SeatId((currentSeat.getId() + 1) % maxNbPlayers);
				currentId = getPlayerId(currentSeat);
			} while(currentId == null);
			if (startSeat.equals(currentSeat)) {
				//we went around the table
				return null;
			}
			currentPlayer = getPlayer(currentId);
		} while (!currentPlayer.sitsIn() || currentPlayer.isAllIn());
		return currentPlayer;
	}

}
