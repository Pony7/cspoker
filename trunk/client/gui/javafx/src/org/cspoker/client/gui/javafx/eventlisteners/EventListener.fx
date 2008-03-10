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
import org.cspoker.common.events.serverevents.PlayerJoinedTableEvent;
import org.cspoker.common.events.serverevents.PlayerLeftEvent;
import org.cspoker.common.events.serverevents.ServerMessageEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;
import org.cspoker.client.gui.javafx.*;
import org.cspoker.client.gui.javafx.views.*;
import java.lang.*;
import org.cspoker.client.gui.javafx.game.Card;
import org.cspoker.client.gui.javafx.game.FXTable;
import org.cspoker.common.elements.cards.Card as JavaCard;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import java.rmi.RemoteException;
import org.cspoker.common.exceptions.IllegalActionException;
import javafx.ui.*;

class EventListener{
    attribute listener:RemoteAllEventsListener;
    attribute main:Main inverse Main.listener;
}

trigger on new EventListener{
    var ts = bind main.state;
    var cl = bind main.client;
    var m = bind main;
    listener = new RemoteAllEventsListener {
        
        operation onAllInEvent(e:AllInEvent){
            System.out.println(e.toString());
            ts.busy=false;
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            ts.busy=true;
        }
        operation onBetEvent(e:BetEvent){
            System.out.println(e.toString());
            ts.busy=false;
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            ts.busy=true;
        }
        operation onBigBlindEvent(e:BigBlindEvent){
            System.out.println(e.toString());
            ts.busy=false;
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            ts.busy=true;
        }
        operation onCallEvent(e:CallEvent){
            System.out.println(e.toString());
            ts.busy=false;
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            ts.busy=true;
        }
        operation onCheckEvent(e:CheckEvent){
            System.out.println(e.toString());
            ts.busy=false;
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            ts.busy=true;
        }
        operation onFoldEvent(e:FoldEvent){
            System.out.println(e.toString());
            ts.busy=false;
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            ts.busy=true;
        }
        operation onRaiseEvent(e:RaiseEvent){
            System.out.println(e.toString());
            ts.busy=false;
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            ts.busy=true;
        }
        operation onSmallBlindEvent(e:SmallBlindEvent){
            System.out.println(e.toString());
            ts.busy=false;
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            ts.busy=true;
        }
        operation onNewPocketCardsEvent(e:NewPocketCardsEvent){
            System.out.println(e.toString());
            System.out.println("00000");
            
            var cards:JavaCard* = cl.toArray(e.getPocketCards());
            
            ts.me.cards[0].rank = cards[0].getRank().toString().toLowerCase();
            ts.me.cards[0].suit = cards[0].getSuit().toString().toLowerCase();
            ts.me.cards[0].visible = true;
            ts.me.cards[0].dealt = true;
            
            ts.me.cards[1].rank = cards[1].getRank().toString().toLowerCase();
            ts.me.cards[1].suit = cards[1].getSuit().toString().toLowerCase();
            ts.me.cards[1].visible = true;
            ts.me.cards[1].dealt = true;
        };
        
    }
    operation onNewCommunityCardsEvent(e:NewCommunityCardsEvent){
        System.out.println(e.toString());
        var cards:JavaCard* = cl.toArray(e.getCommonCards());
        if(ts.cards[0].visible==false){
            System.out.println("11111");
            
            ts.cards[0].visible: true;
            ts.cards[0].dealt = true;
            ts.cards[0].rank = cards[0].getRank().toString().toLowerCase();
            ts.cards[0].suit = cards[0].getSuit().toString().toLowerCase();
            
            ts.cards[1].rank = cards[1].getRank().toString().toLowerCase();
            ts.cards[1].suit = cards[1].getSuit().toString().toLowerCase();
            ts.cards[1].visible = true;
            ts.cards[1]. dealt = true;
            
            ts.cards[2].rank = cards[2].getRank().toString().toLowerCase();
            ts.cards[2].suit = cards[2].getSuit().toString().toLowerCase();
            ts.cards[2].visible = true;
            ts.cards[2].dealt = true;
            
        }else if(ts.cards[3].visible==false){
            System.out.println("22222");
            
            ts.cards[3].rank = cards[0].getRank().toString().toLowerCase();
            ts.cards[3].suit = cards[0].getSuit().toString().toLowerCase();
            ts.cards[3].visible = true;
            ts.cards[3].dealt = true;
            
        }else if(ts.cards[4].visible==false){
            System.out.println("33333");
            
            ts.cards[4].rank = cards[0].getRank().toString().toLowerCase();
            ts.cards[4].suit = cards[0].getSuit().toString().toLowerCase();
            ts.cards[4].visible = true;
            ts.cards[4].dealt = true;
            
        }
        
    }
    operation onNewDealEvent(e:NewDealEvent){
        System.out.println(e.toString());
        ts.state = 2;
        ts.me.cards[0].dealt = true;
        ts.me.cards[0].visible = false;
        ts.me.cards[1].dealt = true;
        ts.me.cards[1].visible = false;
        ts.mytable.cards[0].dealt = true;
        ts.mytable.cards[0].visible = false;
        ts.mytable.cards[1].dealt = true;
        ts.mytable.cards[1].visible = false;
        ts.mytable.cards[2].dealt = true;
        ts.mytable.cards[2].visible = false;
        ts.mytable.cards[3].dealt = true;
        ts.mytable.cards[3].visible = false;
        ts.mytable.cards[4].dealt = true;
        ts.mytable.cards[4].visible = false;
        
    }
    operation onNewRoundEvent(e:NewRoundEvent){
        System.out.println(e.toString());
        ts.busy=false;
        ts.events = ts.events.concat(e.toString()).concat("<br/>");
        ts.busy=true;
    }
    operation onNextPlayerEvent(e:NextPlayerEvent){
        System.out.println(e.toString());
    }
    operation onPlayerJoinedTableEvent(e:PlayerJoinedTableEvent){
        System.out.println(e.toString());
        ts.busy=false;
        ts.events = ts.events.concat(e.toString()).concat("<br/>");
        ts.busy=true;
    }
    operation onPlayerLeftTableEvent(e:PlayerLeftTableEvent){
        System.out.println(e.toString());
        ts.busy=false;
        ts.events = ts.events.concat(e.toString()).concat("<br/>");
        ts.busy=true;
    }
    operation onShowHandEvent(e:ShowHandEvent){
        System.out.println(e.toString());
        ts.busy=false;
        ts.events = ts.events.concat(e.toString()).concat("<br/>");
        ts.busy=true;
    }
    operation onWinnerEvent(e:WinnerEvent){
        System.out.println(e.toString());
        ts.busy=false;
        ts.events = ts.events.concat(e.toString()).concat("<br/>");
        ts.busy=true;
    }
    operation onGameMessageEvent(e:GameMessageEvent){
        System.out.println(e.toString());
        ts.busy=false;
        ts.events = ts.events.concat(e.toString()).concat("<br/>");
        ts.busy=true;
    }
    
    operation onPlayerJoinedEvent(e:PlayerJoinedEvent){
        System.out.println(e.toString());
        var id = e.getId().getID();
        ts.tables[id].nbPlayers = ts.tables[id].nbPlayers + 1;
    }
    
    operation onPlayerLeftEvent(e:PlayerLeftEvent){
        System.out.println(e.toString());
        var id = e.getId().getID();
        ts.tables[id].nbPlayers = ts.tables[id].nbPlayers - 1;
    }
    
    operation onTableCreatedEvent(e:TableCreatedEvent){
        System.out.println(e.toString());
            var t = TableView{}.toTableView(e.getTable());
            insert t as last into ts.tables;
    }
    
    operation onServerMessageEvent(e:ServerMessageEvent){
        System.out.println(e.toString());
    }
};
}