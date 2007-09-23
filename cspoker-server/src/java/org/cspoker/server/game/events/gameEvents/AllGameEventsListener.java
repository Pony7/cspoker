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

package org.cspoker.server.game.events.gameEvents;

import org.cspoker.server.game.events.gameEvents.playerActionEvents.AllInListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BetListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.BigBlindListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CallListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.CheckListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.DealListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.FoldListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.RaiseListener;
import org.cspoker.server.game.events.gameEvents.playerActionEvents.SmallBlindListener;
import org.cspoker.server.game.events.gameEvents.privateEvents.NewPocketCardsListener;

public interface AllGameEventsListener extends AllInListener, BetListener,
	BigBlindListener, CallListener, CheckListener, DealListener,
	FoldListener, RaiseListener, SmallBlindListener,
	NewPocketCardsListener, NewCommunityCardsListener, NewDealListener,
	NewRoundListener, NextPlayerListener, PlayerJoinedGameListener,
	PlayerLeftTableListener, PotChangedListener, ShowHandListener,
	StackChangedListener, WinnerListener, GameMessageListener {

}
