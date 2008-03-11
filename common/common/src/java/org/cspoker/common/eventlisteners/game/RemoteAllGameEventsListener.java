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

package org.cspoker.common.eventlisteners.game;

import java.rmi.Remote;

import org.cspoker.common.eventlisteners.game.actions.RemoteAllInListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteBetListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteBigBlindListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteCallListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteCheckListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteFoldListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteRaiseListener;
import org.cspoker.common.eventlisteners.game.actions.RemoteSmallBlindListener;
import org.cspoker.common.eventlisteners.game.privatelistener.RemoteNewPocketCardsListener;

public interface RemoteAllGameEventsListener extends Remote,
		RemoteAllInListener, RemoteBetListener, RemoteBigBlindListener,
		RemoteCallListener, RemoteCheckListener, RemoteFoldListener,
		RemoteRaiseListener, RemoteSmallBlindListener,
		RemoteNewPocketCardsListener, RemoteNewCommunityCardsListener,
		RemoteNewDealListener, RemoteNewRoundListener,
		RemoteNextPlayerListener, RemotePlayerJoinedGameListener,
		RemotePlayerLeftTableListener, RemoteShowHandListener,
		RemoteWinnerListener, RemoteGameMessageListener, RemoteBrokePlayerKickedOutListener {

}
