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
package org.cspoker.external.pokersource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.cspoker.external.pokersource.commands.Logout;
import org.cspoker.external.pokersource.commands.poker.Call;
import org.cspoker.external.pokersource.commands.poker.Check;
import org.cspoker.external.pokersource.commands.poker.Fold;
import org.cspoker.external.pokersource.commands.poker.Raise;
import org.cspoker.external.pokersource.commands.poker.Sit;
import org.cspoker.external.pokersource.commands.poker.SitOut;
import org.cspoker.external.pokersource.eventlisteners.all.AllEventListener;
import org.cspoker.external.pokersource.events.AuthOk;
import org.cspoker.external.pokersource.events.AuthRefused;
import org.cspoker.external.pokersource.events.Error;
import org.cspoker.external.pokersource.events.Serial;
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

public class PokersourceConnection extends RESTConnection{

	private final static Logger logger = Logger.getLogger(PokersourceConnection.class);

	public PokersourceConnection(String server) throws MalformedURLException {
		super(server);
	}

	public synchronized void send(JSONPacket command) throws IOException{
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
	public void addListeners(AllEventListener... listeners){
		for(AllEventListener listener:listeners)
			this.listeners.add(listener);
	}

	public void removeListener(AllEventListener listener) {
		listeners.remove(listener);
	}
	public void removeListeners(AllEventListener... listeners){
		for(AllEventListener listener:listeners)
			this.listeners.remove(listener);
	}

	private void signal(JSONPacket event) {
		for(AllEventListener listener:listeners) event.signal(listener);
	}

	public void close() throws IOException {
		send(new Logout());
		super.close();
	}

	private final static Map<String, Class<? extends JSONPacket>> eventTypes = new HashMap<String, Class<? extends JSONPacket>>();

	static{
		eventTypes.put(AuthOk.getStaticType(), AuthOk.class);
		eventTypes.put(Serial.getStaticType(), Serial.class);
		eventTypes.put(PlayerInfo.getStaticType(), PlayerInfo.class);
		eventTypes.put(Table.getStaticType(), Table.class);
		eventTypes.put(CurrentGames.getStaticType(), CurrentGames.class);
		eventTypes.put(BuyInLimits.getStaticType(), BuyInLimits.class);
		eventTypes.put(BatchMode.getStaticType(), BatchMode.class);
		eventTypes.put(PlayerLeave.getStaticType(), PlayerLeave.class);
		eventTypes.put(PlayerArrive.getStaticType(), PlayerArrive.class);
		eventTypes.put(Seats.getStaticType(), Seats.class);
		eventTypes.put(PlayerStats.getStaticType(), PlayerStats.class);
		eventTypes.put(PlayerChips.getStaticType(), PlayerChips.class);
		eventTypes.put(ClientPlayerChips.getStaticType(), ClientPlayerChips.class);
		eventTypes.put(Sit.getStaticType(), Sit.class);
		eventTypes.put(StreamMode.getStaticType(), StreamMode.class);
		eventTypes.put(UserInfo.getStaticType(), UserInfo.class);
		eventTypes.put(AutoBlindAnte.getStaticType(), AutoBlindAnte.class);
		eventTypes.put(Seat.getStaticType(), Seat.class);
		eventTypes.put(AutoFold.getStaticType(), AutoFold.class);
		eventTypes.put(SitOut.getStaticType(), SitOut.class);
		eventTypes.put(InGame.getStaticType(), InGame.class);
		eventTypes.put(Dealer.getStaticType(), Dealer.class);
		eventTypes.put(Fold.getStaticType(), Fold.class);
		eventTypes.put(Start.getStaticType(), Start.class);
		eventTypes.put(Position.getStaticType(), Position.class);
		eventTypes.put(BoardCards.getStaticType(), BoardCards.class);
		eventTypes.put(SelfLostPosition.getStaticType(), SelfLostPosition.class);
		eventTypes.put(ChipsPotReset.getStaticType(), ChipsPotReset.class);
		eventTypes.put(Blind.getStaticType(), Blind.class);
		eventTypes.put(Chat.getStaticType(), Chat.class);
		eventTypes.put(ChipsPlayer2Bet.getStaticType(), ChipsPlayer2Bet.class);
		eventTypes.put(WaitFor.getStaticType(), WaitFor.class);
		eventTypes.put(PlayerCards.getStaticType(), PlayerCards.class);
		eventTypes.put(State.getStaticType(), State.class);
		eventTypes.put(DealCards.getStaticType(), DealCards.class);
		eventTypes.put(BetLimit.getStaticType(), BetLimit.class);
		eventTypes.put(BeginRound.getStaticType(), BeginRound.class);
		eventTypes.put(Call.getStaticType(), Call.class);
		eventTypes.put(SelfInPosition.getStaticType(), SelfInPosition.class);
		eventTypes.put(EndRound.getStaticType(), EndRound.class);
		eventTypes.put(Raise.getStaticType(), Raise.class);
		eventTypes.put(ChipsBet2Pot.getStaticType(), ChipsBet2Pot.class);
		eventTypes.put(Check.getStaticType(), Check.class);
		eventTypes.put(PotChips.getStaticType(), PotChips.class);
		eventTypes.put(PlayerHandStrength.getStaticType(), PlayerHandStrength.class);
		eventTypes.put(HighestBetIncrease.getStaticType(), HighestBetIncrease.class);
		eventTypes.put(Rake.getStaticType(), Rake.class);
		eventTypes.put(AllinShowdown.getStaticType(), AllinShowdown.class);
		eventTypes.put(EndRoundLast.getStaticType(), EndRoundLast.class);
		eventTypes.put(Win.getStaticType(), Win.class);
		eventTypes.put(PlayerWin.getStaticType(), PlayerWin.class);
		eventTypes.put(BestCards.getStaticType(), BestCards.class);
		eventTypes.put(Showdown.getStaticType(), Showdown.class);
		eventTypes.put(ChipsPot2Player.getStaticType(), ChipsPot2Player.class);
		eventTypes.put(ChipsPotMerge.getStaticType(), ChipsPotMerge.class);
		eventTypes.put(TimeoutWarning.getStaticType(), TimeoutWarning.class);
		eventTypes.put(AuthRefused.getStaticType(), AuthRefused.class);
		eventTypes.put(Error.getStaticType(), Error.class);
	}


}
