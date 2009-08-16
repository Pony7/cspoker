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
package org.cspoker.client.communication.pokersource.eventlisteners.all;

import org.cspoker.client.communication.pokersource.events.general.AuthOk;
import org.cspoker.client.communication.pokersource.events.general.Serial;
import org.cspoker.client.communication.pokersource.events.poker.AutoBlindAnte;
import org.cspoker.client.communication.pokersource.events.poker.BatchMode;
import org.cspoker.client.communication.pokersource.events.poker.BuyInLimits;
import org.cspoker.client.communication.pokersource.events.poker.PlayerArrive;
import org.cspoker.client.communication.pokersource.events.poker.PlayerChips;
import org.cspoker.client.communication.pokersource.events.poker.PlayerInfo;
import org.cspoker.client.communication.pokersource.events.poker.PlayerLeave;
import org.cspoker.client.communication.pokersource.events.poker.PlayerStats;
import org.cspoker.client.communication.pokersource.events.poker.Seat;
import org.cspoker.client.communication.pokersource.events.poker.Seats;
import org.cspoker.client.communication.pokersource.events.poker.Sit;
import org.cspoker.client.communication.pokersource.events.poker.StreamMode;
import org.cspoker.client.communication.pokersource.events.poker.Table;
import org.cspoker.client.communication.pokersource.events.poker.UserInfo;
import org.cspoker.client.communication.pokersource.events.poker.client.ClientPlayerChips;
import org.cspoker.client.communication.pokersource.events.poker.client.CurrentGames;

public class DefaultListener implements AllEventListener{

	@Override
	public void onAuthOk(AuthOk authOk) {
	}

	@Override
	public void onSerial(Serial serial) {
	}
	
	@Override
	public void onPlayerInfo(PlayerInfo playerInfo) {
	}
	
	@Override
	public void onTable(Table table) {
	}
	
	@Override
	public void onCurrentGames(CurrentGames currentGames) {
	}
	
	@Override
	public void onBuyInLimits(BuyInLimits buyInLimits) {
	}
	
	@Override
	public void onBatchMode(BatchMode batchMode) {
	}

	@Override
	public void onPlayerLeave(PlayerLeave playerLeave) {
	}
	
	@Override
	public void onPlayerArrive(PlayerArrive playerArrive) {
	}
	
	@Override
	public void onSeats(Seats seats) {
	}
	
	@Override
	public void onPlayerStats(PlayerStats playerStats) {
	}
	
	@Override
	public void onPlayerChips(PlayerChips playerChips) {
	}
	
	@Override
	public void onClientPlayerChips(ClientPlayerChips clientPlayerChips) {
	}
	
	@Override
	public void onSit(Sit sit) {
	}
	
	@Override
	public void onStreamMode(StreamMode streamMode) {
	}
	
	@Override
	public void onUserInfo(UserInfo userInfo) {
	}
	
	@Override
	public void onAutoBlindAnte(AutoBlindAnte autoBlindAnte) {
	}
	
	@Override
	public void onSeat(Seat seat) {
	}
	
}
