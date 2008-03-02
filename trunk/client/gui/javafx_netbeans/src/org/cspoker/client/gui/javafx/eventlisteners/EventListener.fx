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
import java.lang.*;

class EventListener{
   attribute listener:RemoteAllEventsListener;
   attribute mainstate:Integer;
   attribute events:String;
}

trigger on new EventListener{
    var state = bind mainstate;
    var txt = bind events;
    
    listener = new RemoteAllEventsListener {
        
        operation onAllInEvent(e:AllInEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onBetEvent(e:BetEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onBigBlindEvent(e:BigBlindEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onCallEvent(e:CallEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onCheckEvent(e:CheckEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onFoldEvent(e:FoldEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onRaiseEvent(e:RaiseEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onSmallBlindEvent(e:SmallBlindEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onNewPocketCardsEvent(e:NewPocketCardsEvent){
            System.out.println(e.toString());
        }
        operation onNewCommunityCardsEvent(e:NewCommunityCardsEvent){
            System.out.println(e.toString());
        }
        operation onNewDealEvent(e:NewDealEvent){
            System.out.println(e.toString());
        }
        operation onNewRoundEvent(e:NewRoundEvent){
            System.out.println(e.toString());
            state = 2;
        }
        operation onNextPlayerEvent(e:NextPlayerEvent){
            System.out.println(e.toString());
        }
        operation onPlayerJoinedGameEvent(e:PlayerJoinedGameEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onPlayerLeftTableEvent(e:PlayerLeftTableEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onShowHandEvent(e:ShowHandEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onWinnerEvent(e:WinnerEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
            state = 1;
        }
        operation onGameMessageEvent(e:GameMessageEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
        operation onPlayerJoinedEvent(e:PlayerJoinedEvent){
            System.out.println(e.toString());
        }
        operation onPlayerLeftEvent(e:PlayerLeftEvent){
            System.out.println(e.toString());
        }
        operation onTableCreatedEvent(e:TableCreatedEvent){
            System.out.println(e.toString());
        }
        operation onServerMessageEvent(e:ServerMessageEvent){
            System.out.println(e.toString());
            txt = txt.concat(e.toString()).concat("<br/>");
        }
    };
}