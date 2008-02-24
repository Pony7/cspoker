/*
 * JavaFxAllEventListener.fx
 *
 * Created on 20-feb-2008, 21:19:05
 */

package org.client.eventlisteners;

import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.gameEvents.GameMessageEvent;
import org.cspoker.common.events.gameEvents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameEvents.NewDealEvent;
import org.cspoker.common.events.gameEvents.NewRoundEvent;
import org.cspoker.common.events.gameEvents.NextPlayerEvent;
import org.cspoker.common.events.gameEvents.PlayerJoinedGameEvent;
import org.cspoker.common.events.gameEvents.PlayerLeftTableEvent;
import org.cspoker.common.events.gameEvents.ShowHandEvent;
import org.cspoker.common.events.gameEvents.WinnerEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.AllInEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.BetEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.BigBlindEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.CallEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.CheckEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.FoldEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.RaiseEvent;
import org.cspoker.common.events.gameEvents.playerActionEvents.SmallBlindEvent;
import org.cspoker.common.events.gameEvents.privateEvents.NewPocketCardsEvent;
import org.cspoker.common.events.serverEvents.PlayerJoinedEvent;
import org.cspoker.common.events.serverEvents.PlayerLeftEvent;
import org.cspoker.common.events.serverEvents.ServerMessageEvent;
import org.cspoker.common.events.serverEvents.TableCreatedEvent;
/**
 * @author Cedric
 */

class JavaFxAllEventListener extends RemoteAllEventsListener {

   operation onAllInEvent(e:AllInEvent);
   operation onBetEvent(e:BetEvent);
   operation onBigBlindEvent(e:BigBlindEvent);
   operation onCallEvent(e:CallEvent);
   operation onCheckEvent(e:CheckEvent);
   operation onFoldEvent(e:FoldEvent);
   operation onRaiseEvent(e:RaiseEvent);
   operation onSmallBlindEvent(e:SmallBlindEvent);
   operation onNewPocketCardsEvent(e:NewPocketCardsEvent);
   operation onNewCommunityCardsEvent(e:NewCommunityCardsEvent);
   operation onNewDealEvent(e:NewDealEvent);
   operation onNewRoundEvent(e:NewRoundEvent);
   operation onNextPlayerEvent(e:NextPlayerEvent);
   operation onPlayerJoinedGameEvent(e:PlayerJoinedGameEvent);
   operation onPlayerLeftTableEvent(e:PlayerLeftTableEvent);
   operation onShowHandEvent(e:ShowHandEvent);
   operation onWinnerEvent(e:WinnerEvent);
   operation onGameMessageEvent(e:GameMessageEvent);
   operation onPlayerJoinedEvent(e:PlayerJoinedEvent);
   operation onPlayerLeftEvent(e:PlayerLeftEvent);
   operation onTableCreatedEvent(e:TableCreatedEvent);
   operation onServerMessageEvent(e:ServerMessageEvent);
}