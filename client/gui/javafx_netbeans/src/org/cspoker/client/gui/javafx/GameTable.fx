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
package org.cspoker.client.gui.javafx;
import javafx.ui.*;
import javafx.ui.canvas.*;
import javafx.ui.filter.*;
import java.lang.*;
import org.cspoker.client.gui.javafx.elements.*;
import org.cspoker.client.gui.javafx.game.*;
import java.awt.Dimension;
import org.cspoker.client.gui.javafx.elements.TableInterface;
import org.cspoker.client.gui.javafx.elements.TableItem;
import java.rmi.RemoteException;
import org.cspoker.common.exceptions.IllegalActionException;
import org.cspoker.common.player.Player;

class GameTable {
    attribute client:JavaFxClient;
    attribute main:Main inverse Main.gametable;
    
    attribute screen:Frame;
    
    attribute sqrw:Integer;
    attribute sqrh:Integer;
    attribute circr:Integer;
    
    attribute players:Player*;
    attribute state:Integer;
    attribute amount:String;
    attribute events:String;
    attribute busy:Boolean;
    
    attribute stateactions:WidgetArray*;
    attribute playingcards:PlayingCards;
    
    operation relogin();
    
    operation startgame();
    operation deal();
    operation bet();
    operation call();
    operation check();
    operation fold();
    operation raise();
    operation allin();
    
    function eventHtml():String;
}

trigger on new GameTable{
    var padx = 30;
    var pady = 30;
    sqrw = 280;
    sqrh = 280;
    var logofontsize = sqrh/5;
    state = 0;
    events="Welcome to CSPoker!<br/>";
    playingcards=PlayingCards{
        c1: Card{
            visible: false
        }
        c2: Card{
            visible: false
        }
        c3: Card{
            visible: false
        }
        c4: Card{
            visible: false
        }
        c5: Card{
            visible: false
        }
        cp1: Card{
            visible: false
        }
        cp2: Card{
            visible: false
        }
        state: 0
    };
    screen = Frame{
        title: "Game Table"
        width: 2*padx+sqrw+sqrh
        height: 2*pady+sqrh+150
        visible: true
        centerOnScreen: true
        onClose: operation() {System.exit(0);}
        menubar: MenuBar {
            menus: Menu {
                text: "Options"
                mnemonic: F
                items:[
                MenuItem{
                    sizeToFitRow: true
                    text: "Logout"
                    mnemonic: L
                    action: operation() {
                        relogin();
                    }
                }]
            }
        }
        content: SplitPane{
            orientation: VERTICAL
            content:[SplitView{
                weight: 0.85
                content: Canvas {
                    content: Group {
                        transform: []
                        content:
                            [
                            Add {
                            shape1: Rect {
                                x: padx+sqrh/2
                                y: pady
                                width: sqrw
                                height: sqrh
                            }
                            shape2: Add {
                                shape1: Circle {
                                    cx: padx+sqrh/2+sqrw
                                    cy: pady+sqrh/2
                                    radius: sqrh/2
                                }
                                shape2: Circle {
                                    cx: padx+sqrh/2
                                    cy: pady+sqrh/2
                                    radius: sqrh/2
                                }
                            }
                            fill: LinearGradient {
                                x1: 0.1, y1: 0, x2: 0.9, y2: 1
                                stops:
                                    [Stop {
                                    offset: 0.0
                                    color: lightgreen
                                },
                                Stop {
                                    offset: 1.0
                                    color: green
                                }]
                                spreadMethod: PAD
                            }
                            stroke: darkgreen
                            strokeWidth: 1
                        },
                        Group{
                            transform: translate(padx+sqrh/2+sqrw/2, pady+sqrh/2)
                            content: [Text {
                                content: "CSPoker"
                                font: new Font("Tahoma", "BOLD", logofontsize)
                                stroke: darkorange
                                fill: orange
                                strokeWidth: 2
                                opacity: 0.25
                                halign: CENTER
                            }]
                        },
                        Group{
                            transform: [translate(padx+sqrh/2+sqrw/2, pady+sqrh/2),rotate(180, 0, 0)]
                            content: [Text {
                                content: "CSPoker"
                                font: new Font("Tahoma", "BOLD", logofontsize)
                                stroke: darkorange
                                fill: orange
                                strokeWidth: 2
                                opacity: 0.25
                                halign: CENTER
                            }]
                        },
                        Group{
                            transform: translate(padx+sqrh/2+sqrw/2, pady+sqrh/2)
                            content: [ImageView {
                                transform:  translate(-45*2,0)
                                image: Image { url: bind playingcards.getCard(playingcards.c1) }
                                valign: CENTER
                                halign: CENTER
                                visible: true
                            },ImageView {
                                transform:  translate(-45,0)
                                image: Image { url: bind playingcards.getCard(playingcards.c2) }
                                valign: CENTER
                                halign: CENTER
                                visible: true
                            },ImageView {
                                transform:  translate(0,0)
                                image: Image { url: bind playingcards.getCard(playingcards.c3) }
                                valign: CENTER
                                halign: CENTER
                                visible: true
                            },ImageView {
                                transform:  translate(45,0)
                                image: Image { url: bind playingcards.getCard(playingcards.c4) }
                                valign: CENTER
                                halign: CENTER
                                visible: true
                            },ImageView {
                                transform:  translate(45*2,0)
                                image: Image { url: bind playingcards.getCard(playingcards.c5) }
                                valign: CENTER
                                halign: CENTER
                                visible: true
                            }]
                            visible: bind(playingcards.state > 0)
                        },Group{
                            transform: translate(padx+sqrh/2+sqrw/2, pady+sqrh)
                            content: [ImageView {
                                transform:  translate(-25,0)
                                image: Image { url: bind playingcards.getCard(playingcards.cp1) }
                                valign: CENTER
                                halign: CENTER
                                visible: true
                            },ImageView {
                                transform:  translate(25,0)
                                image: Image { url: bind playingcards.getCard(playingcards.cp2) }
                                valign: CENTER
                                halign: CENTER
                                visible: true
                            }]
                            visible: bind(playingcards.state > 0)
                        },
                        Group{
                            transform:[]
                            content:[
                            
                            ]
                        }
                        ]
                    }
                    background: black
                }},
                SplitView{
                    weight:0.15
                    content:BorderPanel{
                        center:SplitPane{
                            orientation: HORIZONTAL
                            content:[
                            SplitView{
                                weight: 0.60
                                content:GroupPanel{
                                    var firstRow= Row{alignment: BASELINE}
                                    var secondRow= Row{alignment: BASELINE}
                                    var firstColumn= Column{ alignment:TRAILING}
                                    var secondColumn= Column{ alignment:TRAILING}
                                    var thirdColumn= Column{ alignment:TRAILING}
                                    var fourthColumn= Column{ alignment:TRAILING}
                                    
                                    rows: [firstRow,secondRow]
                                    columns: [firstColumn,secondColumn,thirdColumn,fourthColumn]
                                    
                                    content:[FlowPanel{
                                        row: firstRow
                                        column: firstColumn
                                        content: bind stateactions[state].widgets[0]
                                    },
                                    FlowPanel{
                                        row: firstRow
                                        column: secondColumn
                                        content: bind stateactions[state].widgets[1]
                                    },
                                    FlowPanel{
                                        row: firstRow
                                        column: thirdColumn
                                        content: bind stateactions[state].widgets[2]
                                    },
                                    FlowPanel{
                                        row: firstRow
                                        column: fourthColumn
                                        content: bind stateactions[state].widgets[3]
                                    },
                                    FlowPanel{
                                        row: secondRow
                                        column: firstColumn
                                        content: bind stateactions[state].widgets[4]
                                    },
                                    FlowPanel{
                                        row: secondRow
                                        column: secondColumn
                                        content: bind stateactions[state].widgets[5]
                                    },
                                    FlowPanel{
                                        row: secondRow
                                        column: thirdColumn
                                        content: bind stateactions[state].widgets[6]
                                    }]
                                }
                            },
                            SplitView{
                                weight: 0.40
                                content: Box {
                                    orientation: HORIZONTAL
                                    content: EditorPane{
                                        inUpdate: bind busy
                                        contentType: HTML
                                        editable: false
                                        text: bind events
                                        verticalScrollBarPolicy: AS_NEEDED
                                        maximumSize: {height: bind 0.12*screen.height width: bind 0.4*screen.width}
                                        doubleBuffered: true
                                    }
                                }
                                
                            }
                            ]
                        }
                    }}]
        }
    };
    stateactions = [
    WidgetArray{
        widgets: [Button {
            text: "Start Game"
            toolTipText: "Start the game at this table."
            action: operation() {
                startgame();
            }
        }]},
        WidgetArray{
        widgets: [
        Button {
            text: "Deal"
            toolTipText: "Deal"
            action: operation() {
                deal();
            }
        }
        ]},
        WidgetArray{
        widgets: [
        TextField{
            value: bind amount
            columns: 5
        },Button {
            text: "Bet"
            toolTipText: "Bet the entered amount"
            action: operation() {
                bet();
            }
        },Button {
            text: "Check"
            toolTipText: "Check"
            action: operation() {
                check();
            }
        },Button {
            text: "Call"
            toolTipText: "Call"
            action: operation() {
                call();
            }
        },Button {
            text: "Fold"
            toolTipText: "Fold"
            action: operation() {
                fold();
            }
        },Button {
            text: "Raise"
            toolTipText: "Raise"
            action: operation() {
                raise();
            }
        },Button {
            text: "All In"
            toolTipText: "Go All In"
            action: operation() {
                allin();
            }
        }
        ]}];
}

operation GameTable.relogin(){
    //TODO fix
    main.relogin();
}

operation GameTable.startgame(){
    try{
        client.startGame();
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to start game"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
}

operation GameTable.bet(){
    try{
        client.bet(amount);
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to bet {amount}"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
}

operation GameTable.call(){
    try{
        client.call();
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to bet {amount}"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
}

operation GameTable.fold(){
    try{
        client.fold();
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to bet {amount}"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
}

operation GameTable.check(){
    try{
        client.check();
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to bet {amount}"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
}

operation GameTable.raise(){
    try{
        client.raise(amount);
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to raise {amount}"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
}

operation GameTable.deal(){
    try{
        client.deal();
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to deal"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
}

operation GameTable.allin(){
    try{
        client.allIn();
    }catch(e:RemoteException){
        relogin();
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to go all in"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
}

function GameTable.eventHtml():String{
    return "<html><body>{events}</body></html>";
}