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

import org.cspoker.client.communication.pokersource.events.JSONEvent;
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

public abstract class LoggingListener implements AllEventListener {

	protected abstract void log(JSONEvent event);
	
	@Override
	public void onAuthOk(AuthOk authOk) {
		log(authOk);
	}

	@Override
	public void onPlayerInfo(PlayerInfo playerInfo) {
		log(playerInfo);
	}

	@Override
	public void onTable(Table table) {
		log(table);
	}

	@Override
	public void onSerial(Serial serial) {
		log(serial);
	}
	
	@Override
	public void onCurrentGames(CurrentGames currentGames) {
		log(currentGames);
	}
	
	@Override
	public void onBuyInLimits(BuyInLimits buyInLimits) {
		log(buyInLimits);
	}
	
	@Override
	public void onBatchMode(BatchMode batchMode) {
		log(batchMode);
	}
	
	@Override
	public void onPlayerLeave(PlayerLeave playerLeave) {
		log(playerLeave);
	}
	
	@Override
	public void onPlayerArrive(PlayerArrive playerArrive) {
		log(playerArrive);
	}
	
	@Override
	public void onSeats(Seats seats) {
		log(seats);
	}
	
	@Override
	public void onPlayerStats(PlayerStats playerStats) {
		log(playerStats);
	}
	
	@Override
	public void onPlayerChips(PlayerChips playerChips) {
		log(playerChips);
	}
	
	@Override
	public void onClientPlayerChips(ClientPlayerChips clientPlayerChips) {
		log(clientPlayerChips);
	}
	
	@Override
	public void onSit(Sit sit) {
		log(sit);
	}
	
	@Override
	public void onStreamMode(StreamMode streamMode) {
		log(streamMode);
	}
	
	@Override
	public void onUserInfo(UserInfo userInfo) {
		log(userInfo);
	}
	
	@Override
	public void onAutoBlindAnte(AutoBlindAnte autoBlindAnte) {
		log(autoBlindAnte);
	}
	
	@Override
	public void onSeat(Seat seat) {
		log(seat);
	}
	
}
