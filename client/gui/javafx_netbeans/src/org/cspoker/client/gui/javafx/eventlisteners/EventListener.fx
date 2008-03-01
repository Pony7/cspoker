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
package org.cspoker.client.gui.javafx.eventlisteners;

import org.cspoker.common.eventlisteners.RemoteAllEventsListener;
import org.cspoker.common.events.gameevents.GameMessageEvent;
import org.cspoker.common.events.gameevents.NewCommunityCardsEvent;
import org.cspoker.common.events.gameevents.NewDealEvent;
import org.cspoker.common.events.gameevents.NewRoundEvent;
import org.cspoker.common.events.gameevents.NextPlayerEvent;
import org.cspoker.common.events.gameevents.PlayerJoinedGameEvent;
import org.cspoker.common.events.gameevents.PlayerLeftTableEvent;
import org.cspoker.common.events.gameevents.ShowHandEvent;
import org.cspoker.common.events.gameevents.WinnerEvent;
import org.cspoker.common.events.gameevents.playeractionevents.AllInEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BetEvent;
import org.cspoker.common.events.gameevents.playeractionevents.BigBlindEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CallEvent;
import org.cspoker.common.events.gameevents.playeractionevents.CheckEvent;
import org.cspoker.common.events.gameevents.playeractionevents.FoldEvent;
import org.cspoker.common.events.gameevents.playeractionevents.RaiseEvent;
import org.cspoker.common.events.gameevents.playeractionevents.SmallBlindEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.events.serverevents.PlayerJoinedEvent;
import org.cspoker.common.events.serverevents.PlayerLeftEvent;
import org.cspoker.common.events.serverevents.ServerMessageEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;
import org.cspoker.client.gui.javafx.*;
/**
 * @author Cedric
 */

class EventListener{
   attribute listener:RemoteAllEventsListener;
   attribute state:Integer;
}

trigger on new EventListener{
    listener = new RemoteAllEventsListener {
        operation onAllInEvent(e:AllInEvent){
            
        }
        operation onBetEvent(e:BetEvent){
            
        }
        operation onBigBlindEvent(e:BigBlindEvent){
            
        }
        operation onCallEvent(e:CallEvent){
            
        }
        operation onCheckEvent(e:CheckEvent){
            
        }
        operation onFoldEvent(e:FoldEvent){
            
        }
        operation onRaiseEvent(e:RaiseEvent){
            
        }
        operation onSmallBlindEvent(e:SmallBlindEvent){
            
        }
        operation onNewPocketCardsEvent(e:NewPocketCardsEvent){
            
        }
        operation onNewCommunityCardsEvent(e:NewCommunityCardsEvent){
            
        }
        operation onNewDealEvent(e:NewDealEvent){
            
        }
        operation onNewRoundEvent(e:NewRoundEvent){
            
        }
        operation onNextPlayerEvent(e:NextPlayerEvent){
            
        }
        operation onPlayerJoinedGameEvent(e:PlayerJoinedGameEvent){
            
        }
        operation onPlayerLeftTableEvent(e:PlayerLeftTableEvent){
            
        }
        operation onShowHandEvent(e:ShowHandEvent){
            
        }
        operation onWinnerEvent(e:WinnerEvent){
            
        }
        operation onGameMessageEvent(e:GameMessageEvent){
            
        }
        operation onPlayerJoinedEvent(e:PlayerJoinedEvent){
            
        }
        operation onPlayerLeftEvent(e:PlayerLeftEvent){
            
        }
        operation onTableCreatedEvent(e:TableCreatedEvent){
            
        }
        operation onServerMessageEvent(e:ServerMessageEvent){
            
        }
    };
}