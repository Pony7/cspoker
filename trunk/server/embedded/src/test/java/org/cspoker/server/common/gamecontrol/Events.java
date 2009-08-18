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
package org.cspoker.server.common.gamecontrol;

import org.cspoker.common.api.lobby.holdemtable.event.AllInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.JoinTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitOutEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;

enum Events {
	
		allIn {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return AllInEvent.class;
			}
		},
 		
 		bet {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return BetEvent.class;
			}
		},
 		
 		bigBlind {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return BigBlindEvent.class;
			}
		},
 		
 		call {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return CallEvent.class;
			}
		},
 		
 		check {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return CheckEvent.class;
			}
		},
 		
 		fold {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return FoldEvent.class;
			}
		},
 		
 		joinTable {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return JoinTableEvent.class;
			}
		},
 		
 		leaveTable {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return LeaveTableEvent.class;
			}
		},
 		
 		newCommunityCards {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return NewCommunityCardsEvent.class;
			}
		},
 		
 		newDeal {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return NewDealEvent.class;
			}
		},
 		
 		newRound {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return NewRoundEvent.class;
			}
		},
 		
 		nextPlayer {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return NextPlayerEvent.class;
			}
		},
 		
 		raise {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return RaiseEvent.class;
			}
		},
 		
 		showHand {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return ShowHandEvent.class;
			}
		},
 		
 		sitIn {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return SitInEvent.class;
			}
		},
 		
 		sitOut {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return SitOutEvent.class;
			}
		},
 		
 		smallBlind {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return SmallBlindEvent.class;
			}
		},
 		
 		winner {
			@Override
			public Class<? extends HoldemTableEvent> getEventClass() {
				return WinnerEvent.class;
			}
		};
 		
 		public abstract Class<? extends HoldemTableEvent> getEventClass();
}
