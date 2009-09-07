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
package org.cspoker.client.common.gamestate.modifiers;

import org.cspoker.client.common.gamestate.ForwardingGameState;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.GameStateVisitor;
import org.cspoker.client.common.playerstate.ForwardingPlayerState;
import org.cspoker.client.common.playerstate.PlayerState;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.Round;

public class CheckState extends ForwardingGameState {

	private final CheckEvent checkEvent;

	private final PlayerState playerState;

	private final boolean bigBlindNoRaiseCase;

	private final int newPotSize;

	public CheckState(final GameState gameState, final CheckEvent checkEvent) {
		super(gameState);
		this.checkEvent = checkEvent;
		final PlayerState player = gameState.getPlayer(checkEvent.getPlayerId());

		//case if big blind checks after all opponents called
		bigBlindNoRaiseCase = Round.PREFLOP.equals(gameState.getRound()) 
		&& player.isBigBlind() && gameState.getDeficit(checkEvent.getPlayerId())<=0;

		this.newPotSize = super.getRoundPotSize();

		playerState = new ForwardingPlayerState(player){

			@Override
			public boolean hasChecked() {
				return true;
			}

			@Override
			public int getBet() {
				if(bigBlindNoRaiseCase) return super.getBet();
				return 0;
			}

			@Override
			public PlayerId getPlayerId() {
				return checkEvent.getPlayerId();
			}

			@Override
			public boolean hasFolded() {
				return false;
			}

			@Override
			public boolean hasBeenDealt() {
				return true;
			}

			@Override
			public boolean isBigBlind() {
				if(bigBlindNoRaiseCase) return true;
				return false;
			}

			@Override
			public boolean isSmallBlind() {
				return false;
			}

		};

	}

	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		if (checkEvent.getPlayerId().equals(playerId)) {
			return playerState;
		}
		return super.getPlayer(playerId);
	}

	public HoldemTableEvent getLastEvent() {
		return checkEvent;
	}

	@Override
	public int getLargestBet() {
		if(bigBlindNoRaiseCase){
			return super.getLargestBet();
		}else{
			return 0;
		}
	}

	@Override
	public PlayerId getLastBettor() {
		if(bigBlindNoRaiseCase) return super.getLastBettor();
		return null;
	}

	@Override
	public int getNbRaises() {
		return 0;
	}

	@Override
	public void acceptVisitor(GameStateVisitor visitor) {
		visitor.visitCheckState(this);
	}

	public CheckEvent getEvent() {
		return checkEvent;
	}

	@Override
	public int getRoundPotSize() {
		return newPotSize;
	}

}
