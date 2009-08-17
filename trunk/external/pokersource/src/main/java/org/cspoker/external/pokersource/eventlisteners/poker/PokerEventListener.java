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
package org.cspoker.external.pokersource.eventlisteners.poker;

import org.cspoker.external.pokersource.commands.poker.Call;
import org.cspoker.external.pokersource.commands.poker.Check;
import org.cspoker.external.pokersource.commands.poker.Fold;
import org.cspoker.external.pokersource.commands.poker.Raise;
import org.cspoker.external.pokersource.commands.poker.Sit;
import org.cspoker.external.pokersource.commands.poker.SitOut;
import org.cspoker.external.pokersource.events.poker.AllinShowdown;
import org.cspoker.external.pokersource.events.poker.AutoBlindAnte;
import org.cspoker.external.pokersource.events.poker.AutoFold;
import org.cspoker.external.pokersource.events.poker.BatchMode;
import org.cspoker.external.pokersource.events.poker.BeginRound;
import org.cspoker.external.pokersource.events.poker.BestCards;
import org.cspoker.external.pokersource.events.poker.BetLimit;
import org.cspoker.external.pokersource.events.poker.Blind;
import org.cspoker.external.pokersource.events.poker.BoardCards;
import org.cspoker.external.pokersource.events.poker.BuyInLimits;
import org.cspoker.external.pokersource.events.poker.Chat;
import org.cspoker.external.pokersource.events.poker.ChipsBet2Pot;
import org.cspoker.external.pokersource.events.poker.ChipsPlayer2Bet;
import org.cspoker.external.pokersource.events.poker.ChipsPot2Player;
import org.cspoker.external.pokersource.events.poker.ChipsPotMerge;
import org.cspoker.external.pokersource.events.poker.ChipsPotReset;
import org.cspoker.external.pokersource.events.poker.DealCards;
import org.cspoker.external.pokersource.events.poker.Dealer;
import org.cspoker.external.pokersource.events.poker.EndRound;
import org.cspoker.external.pokersource.events.poker.EndRoundLast;
import org.cspoker.external.pokersource.events.poker.HighestBetIncrease;
import org.cspoker.external.pokersource.events.poker.InGame;
import org.cspoker.external.pokersource.events.poker.PlayerArrive;
import org.cspoker.external.pokersource.events.poker.PlayerCards;
import org.cspoker.external.pokersource.events.poker.PlayerChips;
import org.cspoker.external.pokersource.events.poker.PlayerHandStrength;
import org.cspoker.external.pokersource.events.poker.PlayerInfo;
import org.cspoker.external.pokersource.events.poker.PlayerLeave;
import org.cspoker.external.pokersource.events.poker.PlayerStats;
import org.cspoker.external.pokersource.events.poker.PlayerWin;
import org.cspoker.external.pokersource.events.poker.Position;
import org.cspoker.external.pokersource.events.poker.PotChips;
import org.cspoker.external.pokersource.events.poker.Rake;
import org.cspoker.external.pokersource.events.poker.Seat;
import org.cspoker.external.pokersource.events.poker.Seats;
import org.cspoker.external.pokersource.events.poker.SelfInPosition;
import org.cspoker.external.pokersource.events.poker.SelfLostPosition;
import org.cspoker.external.pokersource.events.poker.Showdown;
import org.cspoker.external.pokersource.events.poker.Start;
import org.cspoker.external.pokersource.events.poker.State;
import org.cspoker.external.pokersource.events.poker.StreamMode;
import org.cspoker.external.pokersource.events.poker.Table;
import org.cspoker.external.pokersource.events.poker.TimeoutWarning;
import org.cspoker.external.pokersource.events.poker.UserInfo;
import org.cspoker.external.pokersource.events.poker.WaitFor;
import org.cspoker.external.pokersource.events.poker.Win;
import org.cspoker.external.pokersource.events.poker.client.ClientPlayerChips;
import org.cspoker.external.pokersource.events.poker.client.CurrentGames;

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

	void onAutoFold(AutoFold autoFold);

	void onSitOut(SitOut sitOut);

	void onInGame(InGame inGame);

	void onDealer(Dealer dealer);

	void onFold(Fold fold);

	void onStart(Start start);

	void onPosition(Position position);

	void onBoardCards(BoardCards boardCards);

	void onSelfLostPosition(SelfLostPosition selfLostPosition);

	void onChipsPotReset(ChipsPotReset chipsPotReset);

	void onBlind(Blind blind);

	void onChat(Chat chat);

	void onChipsPlayer2Bet(ChipsPlayer2Bet chipsPlayer2Bet);

	void onWaitFor(WaitFor waitFor);

	void onPlayerCards(PlayerCards playerCards);

	void onState(State state);

	void onDealCards(DealCards dealCards);

	void onBetLimit(BetLimit betLimit);

	void onBeginRound(BeginRound beginRound);

	void onCall(Call call);

	void onSelfInPosition(SelfInPosition selfInPosition);

	void onEndRound(EndRound endRound);

	void onRaise(Raise raise);

	void onChipsBet2Pot(ChipsBet2Pot chipsBet2Pot);

	void onCheck(Check check);

	void onPotChips(PotChips potChips);

	void onPlayerHandStrength(PlayerHandStrength playerHandStrength);

	void onHighestBetIncrease(HighestBetIncrease highestBetIncrease);

	void onRake(Rake rake);

	void onAllinShowdown(AllinShowdown allinShowdown);

	void onEndRoundLast(EndRoundLast endRoundLast);

	void onWin(Win win);

	void onPlayerWin(PlayerWin playerWin);

	void onBestCards(BestCards bestCards);

	void onShowdown(Showdown showdown);

	void onChipsPot2Player(ChipsPot2Player chipsPot2Player);

	void onChipsPotMerge(ChipsPotMerge chipsPotMere);

	void onTimeoutWarning(TimeoutWarning timeoutWarning);

}
