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
package org.cspoker.client.communication.pokersource.events.poker;

import net.sf.json.JSONArray;

import org.cspoker.client.communication.pokersource.eventlisteners.poker.PokerEventListener;


public class Showdown extends Id{

	public String getType() {
		return getStaticType();
	}
	
	public static String getStaticType() {
		return "PacketPokerShowdown";
	}
	
//	{"showdown_stack":
//		[
//		 {"player_list":[20,21,22,12],
//	      "serial2share":{"21":228000},
//	      "pot":240000,
//	      "serial2delta":{"12":-100000,"20":-20000,"21":128000,"22":-20000},
//	      "serial2rake":{"21":12000},
//	      "serial2best":
//	      	{"12":{"hi":[834149,["NoPair",51,24,36,19,44]]},
//	    	 "21":{"hi":[34360064,["TwoPair",51,38,30,17,24]]}},
//	    	 "type":"game_state",
//	    	 "side_pots":{
//	    		 "building":0,
//	    		 "contributions":{
//	    		 	"1":{"0":{"12":0}},
//	    		 	"0":{"0":{"12":20000,"20":20000,"21":20000,"22":20000}},
//	    		 	"3":{"0":{"12":40000,"21":40000}},
//	    		 	"2":{"0":{"12":40000,"21":40000}},
//	    		 	"total":{"12":100000,"20":20000,"21":100000,"22":20000}},
//	    		 "last_round":3,
//	    		 "pots":[[240000,240000]]
//	    	 }
//	     },
//	     {"serials":[21,12],
//	      "pot":240000,
//	      "hi":[21],
//	      "chips_left":0,
//	      "type":"resolve",
//	      "serial2share":{"21":240000}
//	     }
//	     ],
//	"game_id":3,
//	"serial":0,
//	"type":"PacketPokerShowdown"
//	}

	private JSONArray showdown_stack;
	
	@Override
	public void signal(PokerEventListener listener) {
		listener.onShowdown(this);
	}

	public JSONArray getShowdown_stack() {
		return showdown_stack;
	}

	public void setShowdown_stack(JSONArray showdown_stack) {
		this.showdown_stack = showdown_stack;
	}
	
}
