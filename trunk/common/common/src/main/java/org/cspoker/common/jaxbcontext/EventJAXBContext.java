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
package org.cspoker.common.jaxbcontext;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.cspoker.common.api.chat.event.MessageEvent;
import org.cspoker.common.api.chat.event.ServerChatEvents;
import org.cspoker.common.api.chat.event.TableChatEvents;
import org.cspoker.common.api.lobby.event.TableCreatedEvent;
import org.cspoker.common.api.lobby.event.TableRemovedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BetEvent;
import org.cspoker.common.api.lobby.holdemtable.event.BigBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CallEvent;
import org.cspoker.common.api.lobby.holdemtable.event.CheckEvent;
import org.cspoker.common.api.lobby.holdemtable.event.FoldEvent;
import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEventWrapper;
import org.cspoker.common.api.lobby.holdemtable.event.JoinTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.LeaveTableEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewCommunityCardsEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewDealEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NewRoundEvent;
import org.cspoker.common.api.lobby.holdemtable.event.NextPlayerEvent;
import org.cspoker.common.api.lobby.holdemtable.event.PotsChangedEvent;
import org.cspoker.common.api.lobby.holdemtable.event.RaiseEvent;
import org.cspoker.common.api.lobby.holdemtable.event.ShowHandEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SitInEvent;
import org.cspoker.common.api.lobby.holdemtable.event.SmallBlindEvent;
import org.cspoker.common.api.lobby.holdemtable.event.WinnerEvent;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.event.NewPocketCardsEvent;
import org.cspoker.common.api.shared.event.ActionPerformedEvent;
import org.cspoker.common.api.shared.event.IllegalActionEvent;
import org.cspoker.common.api.shared.event.RemoteExceptionEvent;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.TableList;

public class EventJAXBContext {

	private final static Logger logger = Logger
			.getLogger(EventJAXBContext.class);

	public final static JAXBContext context = initContext();

	private static JAXBContext initContext() {
		try {
			return JAXBContext.newInstance(getActions());
		} catch (JAXBException e) {
			logger.fatal(e);
			throw new IllegalStateException(e);
		}
	}

	public static Class<?>[] getActions() {
		return new Class<?>[] { 
				//chat
				ServerChatEvents.class,TableChatEvents.class, MessageEvent.class,
				//lobby
				TableCreatedEvent.class, TableRemovedEvent.class, TableList.class, DetailedHoldemTable.class,
				//table
				BetEvent.class, BigBlindEvent.class, CallEvent.class, CheckEvent.class, FoldEvent.class, 
				LeaveTableEvent.class, NewCommunityCardsEvent.class, NewDealEvent.class, NewRoundEvent.class, 
				NextPlayerEvent.class, RaiseEvent.class, ShowHandEvent.class, SitInEvent.class, SmallBlindEvent.class, 
				JoinTableEvent.class, WinnerEvent.class, HoldemTableTreeEventWrapper.class, PotsChangedEvent.class,
				//player
				NewPocketCardsEvent.class,
				//action
				ActionPerformedEvent.class, IllegalActionEvent.class, RemoteExceptionEvent.class,
		};
	}

}
