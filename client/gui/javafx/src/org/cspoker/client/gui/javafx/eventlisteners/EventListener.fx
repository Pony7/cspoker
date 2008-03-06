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
import org.cspoker.client.gui.javafx.game.*;
import org.cspoker.common.elements.cards.Card as JavaCard;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;

class EventListener{
    attribute listener:RemoteAllEventsListener;
    attribute client:JavaFxClient;
    attribute tablestate:TableState;
}

trigger on new EventListener{
    var ts = bind tablestate;
    var cl = bind client;
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
            ts.playingcards.state = 1;
            var cards:JavaCard* = cl.toArray(e.getPocketCards());
            ts.playingcards.cp1 = Card{
                visible: true
                dealt: true
                rank: cards[0].getRank().toString().toLowerCase()
                suit: cards[0].getSuit().toString().toLowerCase()
            };
            ts.playingcards.cp2 = Card{
                visible: true
                dealt: true
                rank: cards[1].getRank().toString().toLowerCase()
                suit: cards[1].getSuit().toString().toLowerCase()
            };
        }
        operation onNewCommunityCardsEvent(e:NewCommunityCardsEvent){
            System.out.println(e.toString());
            var cards:JavaCard* = cl.toArray(e.getCommonCards());
            if(ts.playingcards.state == 1){
                ts.playingcards.c1 = Card{
                    visible: true
                    dealt: true
                    rank: cards[0].getRank().toString().toLowerCase()
                    suit: cards[0].getSuit().toString().toLowerCase()
                };
                ts.playingcards.c2 = Card{
                    visible: true
                    dealt: true
                    rank: cards[1].getRank().toString().toLowerCase()
                    suit: cards[1].getSuit().toString().toLowerCase()
                };
                ts.playingcards.c3 = Card{
                    visible: true
                    dealt: true
                    rank: cards[2].getRank().toString().toLowerCase()
                    suit: cards[2].getSuit().toString().toLowerCase()
                };
            }else if(ts.playingcards.state == 2){
                ts.playingcards.c4 = Card{
                    visible: true
                    dealt: true
                    rank: cards[0].getRank().toString().toLowerCase()
                    suit: cards[0].getSuit().toString().toLowerCase()
                };
            }else if(ts.playingcards.state == 3){
                ts.playingcards.c5 = Card{
                    visible: true
                    dealt: true
                    rank: cards[0].getRank().toString().toLowerCase()
                    suit: cards[0].getSuit().toString().toLowerCase()
                };
            }
            ts.playingcards.state = ts.playingcards.state+1;
            
        }
        operation onNewDealEvent(e:NewDealEvent){
            System.out.println(e.toString());
            ts.playingcards.state = 0;
            ts.playingcards.c1 = Card{
                visible: false
                dealt: true
            };
            ts.playingcards.c2 = Card{
                visible: false
                dealt: true
            };
            ts.playingcards.c3 = Card{
                visible: false
                dealt: true
            };
            ts.playingcards.c4 = Card{
                visible: false
                dealt: true
            };
            ts.playingcards.c5 = Card{
                visible: false
                dealt: true
            };
            ts.playingcards.cp1 = Card{
                visible: false
                dealt: true
            };
            ts.playingcards.cp2 = Card{
                visible: false
                dealt: true
            };
            ts.state = 1;
        }
        operation onNewRoundEvent(e:NewRoundEvent){
            System.out.println(e.toString()); ts.busy=false;
            ts.busy=true;
        }
        operation onNextPlayerEvent(e:NextPlayerEvent){
            System.out.println(e.toString());
        }
        operation onPlayerJoinedGameEvent(e:PlayerJoinedGameEvent){
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
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            System.out.println(e.toString());
        }
        operation onPlayerLeftEvent(e:PlayerLeftEvent){
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            System.out.println(e.toString());
        }
        operation onTableCreatedEvent(e:TableCreatedEvent){
            System.out.println(e.toString());
        }
        operation onServerMessageEvent(e:ServerMessageEvent){
            System.out.println(e.toString());
            ts.busy=false;
            ts.events = ts.events.concat(e.toString()).concat("<br/>");
            ts.busy=true;
        }
    };
}