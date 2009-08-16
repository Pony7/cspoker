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
package org.cspoker.client.communication.pokersource.eventlisteners.poker;

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

public interface PokerEventListener {

	void onPlayerInfo(PlayerInfo playerInfo);

	void onTable(Table table);

	void onCurrentGames(CurrentGames currentGames);

	void onBuyInLimits(BuyInLimits buyInLimits);

	void onBatchMode(BatchMode batchMode);

	void onPlayerLeave(PlayerLeave playerLeave);

	void onPlayerArrive(PlayerArrive playerArrive);

	void onSeats(Seats seats);

	void onPlayerStats(PlayerStats playerStats);

	void onPlayerChips(PlayerChips playerChips);

	void onClientPlayerChips(ClientPlayerChips clientPlayerChips);

	void onSit(Sit sit);

	void onStreamMode(StreamMode streamMode);

	void onUserInfo(UserInfo userInfo);

	void onAutoBlindAnte(AutoBlindAnte autoBlindAnte);

	void onSeat(Seat seat);

}
