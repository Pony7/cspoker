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
import org.cspoker.common.events.gameevents.PlayerJoinedTableEvent;
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
import org.cspoker.common.events.gameevents.BrokePlayerKickedOutEvent;
import org.cspoker.common.events.gameevents.privateevents.NewPocketCardsEvent;
import org.cspoker.common.events.serverevents.TableChangedEvent;
import org.cspoker.common.events.serverevents.TableCreatedEvent;
import org.cspoker.common.events.serverevents.ServerMessageEvent;
import org.cspoker.common.events.serverevents.TableRemovedEvent;
import org.cspoker.client.gui.javafx.*;
import org.cspoker.client.gui.javafx.views.*;
import java.lang.*;
import org.cspoker.client.gui.javafx.views.CardView;
import org.cspoker.client.gui.javafx.views.TableView;
import org.cspoker.common.elements.cards.Card as JavaCard;
import org.cspoker.common.elements.cards.Rank;
import org.cspoker.common.elements.cards.Suit;
import java.rmi.RemoteException;
import org.cspoker.common.exceptions.IllegalActionException;
import javafx.ui.*;
import java.awt.EventQueue;
import org.cspoker.common.elements.pots.Pots;
import org.cspoker.common.player.Winner;

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
            EventQueue.invokeLater(new Runnable(){
                
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    
                    var p = ts.mytable.players[e.getPlayer().getSeatId().getId()];
                    p.lastaction = "All In";
                    p.amount = e.getPlayer().getBetChipsValue();
                    p.stack = e.getPlayer().getStackValue();
                    
                    ts.mytable.temppot = e.getPots().getTotalValue();
                }
            });
            
        }
        operation onBetEvent(e:BetEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;            
                    
                    var p = ts.mytable.players[e.getPlayer().getSeatId().getId()];
                    p.lastaction = "Bet";
                    p.amount = e.getPlayer().getBetChipsValue();
                    p.stack = e.getPlayer().getStackValue();
                    
                    ts.mytable.temppot = e.getPots().getTotalValue();
                }
            });
        }
        operation onCallEvent(e:CallEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    
                    var p = ts.mytable.players[e.getPlayer().getSeatId().getId()];
                    p.lastaction = "Call";
                    p.amount = e.getPlayer().getBetChipsValue();
                    p.stack = e.getPlayer().getStackValue();
                    
                    ts.mytable.temppot = e.getPots().getTotalValue();
                }
            });
        }
        operation onCheckEvent(e:CheckEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    
                    var p = ts.mytable.players[e.getPlayer().getSeatId().getId()];
                    p.lastaction = "Check";
                    p.amount = e.getPlayer().getBetChipsValue();
                    p.stack = e.getPlayer().getStackValue();
                }
            });
        }
        operation onFoldEvent(e:FoldEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    
                    var p = ts.mytable.players[e.getPlayer().getSeatId().getId()];
                    p.lastaction = "Fold";
                    p.amount = e.getPlayer().getBetChipsValue();
                    p.stack = e.getPlayer().getStackValue();
                    
                    //pretend cards are given back to dealer
                    p.cards[0].dealt = false;
                    p.cards[1].dealt = false;
                }
            });
        }
        operation onRaiseEvent(e:RaiseEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    
                    var p = ts.mytable.players[e.getPlayer().getSeatId().getId()];
                    p.lastaction = "Raise";
                    p.amount = e.getPlayer().getBetChipsValue();
                    p.stack = e.getPlayer().getStackValue();
                    
                    ts.mytable.temppot = e.getPots().getTotalValue();
                }
            });
        }
        operation onSmallBlindEvent(e:SmallBlindEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    
                    var p = ts.mytable.players[e.getPlayer().getSeatId().getId()];
                    p.lastaction = "Small Blind";
                    p.amount = e.getPlayer().getBetChipsValue();
                    p.stack = e.getPlayer().getStackValue();
                    
                    ts.mytable.temppot = e.getPots().getTotalValue();
                }
            });
        }
        
        operation onBigBlindEvent(e:BigBlindEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    
                    var p = ts.mytable.players[e.getPlayer().getSeatId().getId()];
                    p.lastaction = "Big Blind";
                    p.amount = e.getPlayer().getBetChipsValue();
                    p.stack = e.getPlayer().getStackValue();
                    
                    ts.mytable.temppot = e.getPots().getTotalValue();
                }
            });
        }
        
        operation onNewPocketCardsEvent(e:NewPocketCardsEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    System.out.println("00000");
                    
                    var cards = JavaFxClient.cardsToArray(e.getPocketCards());
                    
                    ts.me.cards[0].rank = cards[0].getRank().toString().toLowerCase();
                    ts.me.cards[0].suit = cards[0].getSuit().toString().toLowerCase();
                    ts.me.cards[0].visible = true;
                    
                    ts.me.cards[1].rank = cards[1].getRank().toString().toLowerCase();
                    ts.me.cards[1].suit = cards[1].getSuit().toString().toLowerCase();
                    ts.me.cards[1].visible = true;
                }
            });
        }
        operation onNewCommunityCardsEvent(e:NewCommunityCardsEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    var cards = JavaFxClient.cardsToArray(e.getCommonCards());
                    var mycards = ts.mytable.cards;
                    
                    if(mycards[0].visible==false){
                        System.out.println("11111");
                        
                        mycards[0].rank = cards[0].getRank().toString().toLowerCase();
                        mycards[0].suit = cards[0].getSuit().toString().toLowerCase();
                        mycards[0].visible= true;
                        mycards[0].dealt = true;
                        
                        mycards[1].rank = cards[1].getRank().toString().toLowerCase();
                        mycards[1].suit = cards[1].getSuit().toString().toLowerCase();
                        mycards[1].visible = true;
                        mycards[1].dealt = true;
                        
                        mycards[2].rank = cards[2].getRank().toString().toLowerCase();
                        mycards[2].suit = cards[2].getSuit().toString().toLowerCase();
                        mycards[2].visible = true;
                        mycards[2].dealt = true;
                        
                    }else if(mycards[3].visible==false){
                        System.out.println("22222");
                        
                        mycards[3].rank = cards[0].getRank().toString().toLowerCase();
                        mycards[3].suit = cards[0].getSuit().toString().toLowerCase();
                        mycards[3].visible = true;
                        mycards[3].dealt = true;
                        
                    }else if(mycards[4].visible==false){
                        System.out.println("33333");
                        
                        mycards[4].rank = cards[0].getRank().toString().toLowerCase();
                        mycards[4].suit = cards[0].getSuit().toString().toLowerCase();
                        mycards[4].visible = true;
                        mycards[4].dealt = true;
                        
                    }
                    
                }
            });
        }
        operation onNewDealEvent(e:NewDealEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    
                    ts.mytable.state = 2;
                    
                    ts.mytable.dealer = e.getDealer().getSeatId().getId();
                    
                    for(p in ts.mytable.players){
                        if (p.seated) {
                            for(c in p.cards){
                                c.dealt = true;
                                c.visible = false;
                            }
                        }else{
                            for(c in p.cards){
                                c.dealt = false;
                                c.visible = false;
                            }
                        }
                        p.lastaction="";
                        p.amount=0;
                    }
                    
                    for(c in ts.mytable.cards){
                        c.dealt = true;
                        c.visible = false;
                    }
                }
            });
        }
        operation onNewRoundEvent(e:NewRoundEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    for(p in ts.mytable.players){
                        p.next = false;
                        //this is optional...
                        //p.lastaction="";
                        p.amount=0;
                    }
                    ts.mytable.players[e.getInitialPlayer().getSeatId().getId()].next = true;
                    ts.mytable.pot = ts.mytable.temppot;
                }
            });
        }
        operation onNextPlayerEvent(e:NextPlayerEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    for(p in ts.mytable.players){
                        p.next = false;
                    }
                    ts.mytable.players[e.getPlayer().getSeatId().getId()].next = true;
                }
            });
        }
        operation onPlayerJoinedTableEvent(e:PlayerJoinedTableEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    ts.mytable.players[e.getPlayer().getSeatId().getId()] = PlayerView{}.toPlayerView(e.getPlayer());
                }
            });
        }
        operation onPlayerLeftTableEvent(e:PlayerLeftTableEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    ts.mytable.players[e.getPlayer().getSeatId().getId()] = PlayerView{
                        seated: false
                        cards: [CardView{
                            dealt: false
                        },CardView{
                            dealt: false
                        }]
                    };
                }
            });
        }
        operation onShowHandEvent(e:ShowHandEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    var player = ts.mytable.players[e.getShowdownPlayer().getPlayer().getSeatId().getId()];
                    var cards = JavaFxClient.cardsToArray(e.getShowdownPlayer().getHandCards());
                    player.cards[0].rank = cards[0].getRank().toString().toLowerCase();
                    player.cards[0].suit = cards[0].getSuit().toString().toLowerCase();
                    player.cards[0].visible = true;
                    player.cards[1].rank = cards[1].getRank().toString().toLowerCase();
                    player.cards[1].suit = cards[1].getSuit().toString().toLowerCase();
                    player.cards[1].visible = true;
                }
            });
        }
        operation onWinnerEvent(e:WinnerEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                    for(p in ts.mytable.players){
                        p.next = false;
                        p.lastaction="";
                        p.amount=0;
                    }
                    ts.mytable.temppot = 0; 
                    ts.mytable.pot = 0;
                    var winners = JavaFxClient.winnersToArray(e.getWinners());
                    for(winner in winners){
                        var player = ts.mytable.players[winner.getPlayer().getSeatId().getId()];
                        player.lastaction = "WINNER";
                        player.amount = winner.getGainedAmount();
                    }
                }
            });
        }
        operation onGameMessageEvent(e:GameMessageEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    ts.busy=false;
                    ts.events = ts.events.concat(e.toString()).concat("<br/>");
                    ts.busy=true;
                }
            });
        }
        
        operation onTableChangedEvent(e:TableChangedEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    var table = TableView{}.toTableViews(e.getTable());
                    ts.tables[t | t.id.equals(table.id)] = table;
                }
            });
        }
        
        operation onTableRemovedEvent(e:TableRemovedEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    delete ts.tables[t | t.id.equals(e.getTableId())];
                }
            });
        }
        
        operation onTableCreatedEvent(e:TableCreatedEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    var table = TableView{}.toTableViews(e.getTable());
                    insert table as last into ts.tables;
                }
            });
        }
        
        operation onServerMessageEvent(e:ServerMessageEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                }
            });
        }
        
        operation onBrokePlayerKickedOutEvent(e:BrokePlayerKickedOutEvent){
            EventQueue.invokeLater(new Runnable(){
                operation run(){
                    System.out.println(e.toString());
                    var player = ts.mytable.players[p | p.name.equals(e.getPlayer().getName())];
                    player.seated = false;
                    player.cards = [CardView{
                        dealt: false
                    },CardView{
                        dealt: false
                    }];
                    if(e.getPlayer().getName().equals(ts.me.name)){
                        m.relogin();
                        MessageDialog{
                            title: "You are broke."
                            visible: true
                            message: "You are broke. "
                            messageType: WARNING
                        }
                    }
                }
            });
        }
        
    };
}