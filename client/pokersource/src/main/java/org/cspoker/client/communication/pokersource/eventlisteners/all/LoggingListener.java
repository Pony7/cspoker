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

import org.cspoker.client.communication.pokersource.JSONPacket;
import org.cspoker.client.communication.pokersource.commands.poker.Call;
import org.cspoker.client.communication.pokersource.commands.poker.Check;
import org.cspoker.client.communication.pokersource.commands.poker.Fold;
import org.cspoker.client.communication.pokersource.commands.poker.Raise;
import org.cspoker.client.communication.pokersource.commands.poker.Sit;
import org.cspoker.client.communication.pokersource.commands.poker.SitOut;
import org.cspoker.client.communication.pokersource.events.AuthOk;
import org.cspoker.client.communication.pokersource.events.Serial;
import org.cspoker.client.communication.pokersource.events.poker.AllinShowdown;
import org.cspoker.client.communication.pokersource.events.poker.AutoBlindAnte;
import org.cspoker.client.communication.pokersource.events.poker.AutoFold;
import org.cspoker.client.communication.pokersource.events.poker.BatchMode;
import org.cspoker.client.communication.pokersource.events.poker.BeginRound;
import org.cspoker.client.communication.pokersource.events.poker.BestCards;
import org.cspoker.client.communication.pokersource.events.poker.BetLimit;
import org.cspoker.client.communication.pokersource.events.poker.Blind;
import org.cspoker.client.communication.pokersource.events.poker.BoardCards;
import org.cspoker.client.communication.pokersource.events.poker.BuyInLimits;
import org.cspoker.client.communication.pokersource.events.poker.Chat;
import org.cspoker.client.communication.pokersource.events.poker.ChipsBet2Pot;
import org.cspoker.client.communication.pokersource.events.poker.ChipsPlayer2Bet;
import org.cspoker.client.communication.pokersource.events.poker.ChipsPot2Player;
import org.cspoker.client.communication.pokersource.events.poker.ChipsPotMerge;
import org.cspoker.client.communication.pokersource.events.poker.ChipsPotReset;
import org.cspoker.client.communication.pokersource.events.poker.DealCards;
import org.cspoker.client.communication.pokersource.events.poker.Dealer;
import org.cspoker.client.communication.pokersource.events.poker.EndRound;
import org.cspoker.client.communication.pokersource.events.poker.EndRoundLast;
import org.cspoker.client.communication.pokersource.events.poker.HighestBetIncrease;
import org.cspoker.client.communication.pokersource.events.poker.InGame;
import org.cspoker.client.communication.pokersource.events.poker.PlayerArrive;
import org.cspoker.client.communication.pokersource.events.poker.PlayerCards;
import org.cspoker.client.communication.pokersource.events.poker.PlayerChips;
import org.cspoker.client.communication.pokersource.events.poker.PlayerHandStrength;
import org.cspoker.client.communication.pokersource.events.poker.PlayerInfo;
import org.cspoker.client.communication.pokersource.events.poker.PlayerLeave;
import org.cspoker.client.communication.pokersource.events.poker.PlayerStats;
import org.cspoker.client.communication.pokersource.events.poker.PlayerWin;
import org.cspoker.client.communication.pokersource.events.poker.Position;
import org.cspoker.client.communication.pokersource.events.poker.PotChips;
import org.cspoker.client.communication.pokersource.events.poker.Rake;
import org.cspoker.client.communication.pokersource.events.poker.Seat;
import org.cspoker.client.communication.pokersource.events.poker.Seats;
import org.cspoker.client.communication.pokersource.events.poker.SelfInPosition;
import org.cspoker.client.communication.pokersource.events.poker.SelfLostPosition;
import org.cspoker.client.communication.pokersource.events.poker.Showdown;
import org.cspoker.client.communication.pokersource.events.poker.Start;
import org.cspoker.client.communication.pokersource.events.poker.State;
import org.cspoker.client.communication.pokersource.events.poker.StreamMode;
import org.cspoker.client.communication.pokersource.events.poker.Table;
import org.cspoker.client.communication.pokersource.events.poker.TimeoutWarning;
import org.cspoker.client.communication.pokersource.events.poker.UserInfo;
import org.cspoker.client.communication.pokersource.events.poker.WaitFor;
import org.cspoker.client.communication.pokersource.events.poker.Win;
import org.cspoker.client.communication.pokersource.events.poker.client.ClientPlayerChips;
import org.cspoker.client.communication.pokersource.events.poker.client.CurrentGames;

public abstract class LoggingListener implements AllEventListener {

	protected abstract void log(JSONPacket event);
	
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
	
	@Override
	public void onAutoFold(AutoFold autoFold) {
		log(autoFold);
	}
	
	@Override
	public void onSitOut(SitOut sitOut) {
		log(sitOut);
	}
	
	@Override
	public void onInGame(InGame inGame) {
		log(inGame);
	}
	
	@Override
	public void onDealer(Dealer dealer) {
		log(dealer);
	}
	
	@Override
	public void onFold(Fold fold) {
		log(fold);
	}
	
	@Override
	public void onStart(Start start) {
		log(start);
	}
	
	@Override
	public void onPosition(Position position) {
		log(position);
	}
	
	@Override
	public void onBoardCards(BoardCards boardCards) {
		log(boardCards);
	}
	
	@Override
	public void onSelfLostPosition(SelfLostPosition selfLostPosition) {
		log(selfLostPosition);
	}
	
	@Override
	public void onChipsPotReset(ChipsPotReset chipsPotReset) {
		log(chipsPotReset);
	}
	
	@Override
	public void onBlind(Blind blind) {
		log(blind);
	}
	
	@Override
	public void onChat(Chat chat) {
		log(chat);
	}
	
	@Override
	public void onChipsPlayer2Bet(ChipsPlayer2Bet chipsPlayer2Bet) {
		log(chipsPlayer2Bet);
	}
	
	@Override
	public void onWaitFor(WaitFor waitFor) {
		log(waitFor);
	}
	
	@Override
	public void onPlayerCards(PlayerCards playerCards) {
		log(playerCards);
	}

	@Override
	public void onState(State state) {
		log(state);
	}
	
	@Override
	public void onDealCards(DealCards dealCards) {
		log(dealCards);
	}
	
	@Override
	public void onBetLimit(BetLimit betLimit) {
		log(betLimit);
	}
	
	@Override
	public void onBeginRound(BeginRound beginRound) {
		log(beginRound);
	}
	
	@Override
	public void onCall(Call call) {
		log(call);
	}
	
	@Override
	public void onSelfInPosition(SelfInPosition selfInPosition) {
		log(selfInPosition);
	}
	
	@Override
	public void onEndRound(EndRound endRound) {
		log(endRound);
	}
	
	@Override
	public void onRaise(Raise raise) {
		log(raise);
	}
	
	@Override
	public void onChipsBet2Pot(ChipsBet2Pot chipsBet2Pot) {
		log(chipsBet2Pot);
	}

	@Override
	public void onCheck(Check check) {
		log(check);
	}
	
	@Override
	public void onPotChips(PotChips potChips) {
		log(potChips);
	}
	
	@Override
	public void onPlayerHandStrength(PlayerHandStrength playerHandStrength) {
		log(playerHandStrength);
	}
	
	@Override
	public void onHighestBetIncrease(HighestBetIncrease highestBetIncrease) {
		log(highestBetIncrease);
	}
	
	@Override
	public void onRake(Rake rake) {
		log(rake);
	}
	
	@Override
	public void onAllinShowdown(AllinShowdown allinShowdown) {
		log(allinShowdown);
	}
	
	@Override
	public void onEndRoundLast(EndRoundLast endRoundLast) {
		log(endRoundLast);
	}
	
	@Override
	public void onWin(Win win) {
		log(win);
	}
	
	@Override
	public void onPlayerWin(PlayerWin playerWin) {
		log(playerWin);
	}
	
	@Override
	public void onBestCards(BestCards bestCards) {
		log(bestCards);
	}
	
	@Override
	public void onShowdown(Showdown showdown) {
		log(showdown);
	}
	
	@Override
	public void onChipsPot2Player(ChipsPot2Player chipsPot2Player) {
		log(chipsPot2Player);
	}
	
	@Override
	public void onChipsPotMerge(ChipsPotMerge chipsPotMere) {
		log(chipsPotMere);
	}
	
	@Override
	public void onTimeoutWarning(TimeoutWarning timeoutWarning) {
		log(timeoutWarning);
	}
}
