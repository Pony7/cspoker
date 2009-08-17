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
package org.cspoker.client.communication.pokersource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.cspoker.client.communication.pokersource.commands.Logout;
import org.cspoker.client.communication.pokersource.commands.poker.Call;
import org.cspoker.client.communication.pokersource.commands.poker.Check;
import org.cspoker.client.communication.pokersource.commands.poker.Fold;
import org.cspoker.client.communication.pokersource.commands.poker.Raise;
import org.cspoker.client.communication.pokersource.commands.poker.Sit;
import org.cspoker.client.communication.pokersource.commands.poker.SitOut;
import org.cspoker.client.communication.pokersource.eventlisteners.all.AllEventListener;
import org.cspoker.client.communication.pokersource.events.AuthOk;
import org.cspoker.client.communication.pokersource.events.Serial;
import org.cspoker.client.communication.pokersource.events.poker.AllinShowdown;
import org.cspoker.client.communication.pokersource.events.poker.AutoBlindAnte;
import org.cspoker.client.communication.pokersource.events.poker.AutoFold;
import org.cspoker.client.communication.pokersource.events.poker.BatchMode;
import org.cspoker.client.communication.pokersource.events.poker.BeginRound;
import org.cspoker.client.communication.pokersource.events.poker.BetLimit;
import org.cspoker.client.communication.pokersource.events.poker.Blind;
import org.cspoker.client.communication.pokersource.events.poker.BoardCards;
import org.cspoker.client.communication.pokersource.events.poker.BuyInLimits;
import org.cspoker.client.communication.pokersource.events.poker.Chat;
import org.cspoker.client.communication.pokersource.events.poker.ChipsBet2Pot;
import org.cspoker.client.communication.pokersource.events.poker.ChipsPlayer2Bet;
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
import org.cspoker.client.communication.pokersource.events.poker.Start;
import org.cspoker.client.communication.pokersource.events.poker.State;
import org.cspoker.client.communication.pokersource.events.poker.StreamMode;
import org.cspoker.client.communication.pokersource.events.poker.Table;
import org.cspoker.client.communication.pokersource.events.poker.UserInfo;
import org.cspoker.client.communication.pokersource.events.poker.WaitFor;
import org.cspoker.client.communication.pokersource.events.poker.Win;
import org.cspoker.client.communication.pokersource.events.poker.client.ClientPlayerChips;
import org.cspoker.client.communication.pokersource.events.poker.client.CurrentGames;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class PokersourceConnection extends RESTConnection{

	private final static Logger logger = Logger.getLogger(PokersourceConnection.class);

	public PokersourceConnection(String server) throws MalformedURLException {
		super(server);
	}

	public void send(JSONPacket command) throws IOException{
		logger.info("Sending: "+command.getClass().getSimpleName()+": "+command);
		String response = put(command.toString());
		logger.info("Received: "+response);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[]{"cookie", "length"});
		JSONArray jsonResponse = (JSONArray) JSONSerializer.toJSON(response,jsonConfig);
		signalEvents(jsonResponse);
	}

	private void signalEvents(JSONArray jsonResponse) {
		for(int i=0; i<jsonResponse.size();i++){
			JSONObject jsonEvent = jsonResponse.getJSONObject(i);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass( eventTypes.get(jsonEvent.get("type")) ); 
			try {
				JSONPacket event = (JSONPacket) JSONObject.toBean( jsonEvent, jsonConfig );  
				signal(event);
			} catch (ClassCastException e) {
				throw new IllegalStateException("Couldn't unmarshall: "+jsonEvent,e);
			}
		}
	}

	private final List<AllEventListener> listeners = new CopyOnWriteArrayList<AllEventListener>();

	public void addListener(AllEventListener listener){
		listeners.add(listener);
	}

	private void signal(JSONPacket event) {
		for(AllEventListener listener:listeners) event.signal(listener);
	}

	public void close() throws IOException {
		send(new Logout());
		super.close();
	}

	private final static ImmutableMap<String, Class<? extends JSONPacket>> eventTypes = 
		(new Builder<String, Class<? extends JSONPacket>>())
		.put(AuthOk.getStaticType(), AuthOk.class)
		.put(Serial.getStaticType(), Serial.class)
		.put(PlayerInfo.getStaticType(), PlayerInfo.class)
		.put(Table.getStaticType(), Table.class)
		.put(CurrentGames.getStaticType(), CurrentGames.class)
		.put(BuyInLimits.getStaticType(), BuyInLimits.class)
		.put(BatchMode.getStaticType(), BatchMode.class)
		.put(PlayerLeave.getStaticType(), PlayerLeave.class)
		.put(PlayerArrive.getStaticType(), PlayerArrive.class)
		.put(Seats.getStaticType(), Seats.class)
		.put(PlayerStats.getStaticType(), PlayerStats.class)
		.put(PlayerChips.getStaticType(), PlayerChips.class)
		.put(ClientPlayerChips.getStaticType(), ClientPlayerChips.class)
		.put(Sit.getStaticType(), Sit.class)
		.put(StreamMode.getStaticType(), StreamMode.class)
		.put(UserInfo.getStaticType(), UserInfo.class)
		.put(AutoBlindAnte.getStaticType(), AutoBlindAnte.class)
		.put(Seat.getStaticType(), Seat.class)
		.put(AutoFold.getStaticType(), AutoFold.class)
		.put(SitOut.getStaticType(), SitOut.class)
		.put(InGame.getStaticType(), InGame.class)
		.put(Dealer.getStaticType(), Dealer.class)
		.put(Fold.getStaticType(), Fold.class)
		.put(Start.getStaticType(), Start.class)
		.put(Position.getStaticType(), Position.class)
		.put(BoardCards.getStaticType(), BoardCards.class)
		.put(SelfLostPosition.getStaticType(), SelfLostPosition.class)
		.put(ChipsPotReset.getStaticType(), ChipsPotReset.class)
		.put(Blind.getStaticType(), Blind.class)
		.put(Chat.getStaticType(), Chat.class)
		.put(ChipsPlayer2Bet.getStaticType(), ChipsPlayer2Bet.class)
		.put(WaitFor.getStaticType(), WaitFor.class)
		.put(PlayerCards.getStaticType(), PlayerCards.class)
		.put(State.getStaticType(), State.class)
		.put(DealCards.getStaticType(), DealCards.class)
		.put(BetLimit.getStaticType(), BetLimit.class)
		.put(BeginRound.getStaticType(), BeginRound.class)
		.put(Call.getStaticType(), Call.class)
		.put(SelfInPosition.getStaticType(), SelfInPosition.class)
		.put(EndRound.getStaticType(), EndRound.class)
		.put(Raise.getStaticType(), Raise.class)
		.put(ChipsBet2Pot.getStaticType(), ChipsBet2Pot.class)
		.put(Check.getStaticType(), Check.class)
		.put(PotChips.getStaticType(), PotChips.class)
		.put(PlayerHandStrength.getStaticType(), PlayerHandStrength.class)
		.put(HighestBetIncrease.getStaticType(), HighestBetIncrease.class)
		.put(Rake.getStaticType(), Rake.class)
		.put(AllinShowdown.getStaticType(), AllinShowdown.class)
		.put(EndRoundLast.getStaticType(), EndRoundLast.class)
		.put(Win.getStaticType(), Win.class)
		.put(PlayerWin.getStaticType(), PlayerWin.class)
		.build();
}
