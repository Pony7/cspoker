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
import org.cspoker.client.gui.javafx.views.*;
import org.cspoker.client.gui.javafx.game.*;
import java.rmi.RemoteException;
import org.cspoker.common.exceptions.IllegalActionException;

class GameTable {
    attribute main:Main inverse Main.gametable;
    
    attribute screen:Frame;
    
    attribute sqrw:Integer;
    attribute sqrh:Integer;
    attribute padx:Integer;
    attribute pady:Integer;
    
    attribute amount:String;
    
    function parametrizeX(position:Number):Number;
    function parametrizeY(position:Number):Number;
    function parametrizeRadial(position:Number):Number;
    
    operation relogin();
    operation leavetable();
    
    operation changeButtons(s:Integer);
    
    operation startgame();
    operation deal();
    operation bet();
    operation call();
    operation check();
    operation fold();
    operation raise();
    operation allin();
}

trigger on new GameTable{
    padx = 50;
    pady = 50;
    sqrw = 280;
    sqrh = 280;
    var logofontsize = sqrh/4;
    var circleup = 20;
    var cardsup = -25;
    screen = Frame{
        title: bind main.state.mytable.name
        width: 2*padx+sqrw+sqrh
        height: 2*pady+sqrh+200
        visible: true
        centerOnScreen: true
        onClose: operation() {
            leavetable();
            System.exit(0);
        }
        menubar: MenuBar {
            menus: Menu {
                text: "Options"
                mnemonic: F
                items:[
                MenuItem{
                    sizeToFitRow: true
                    text: bind "Logout {main.state.myname}"
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
                weight: 0.785
                content: Canvas {
                    doubleBuffered: true
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
                                antialias: true
                                transform:  translate(-45*2,0)
                                image: Image { url: bind main.state.mytable.cards[0].getImage() }
                                valign: CENTER
                                halign: CENTER
                                visible: bind main.state.mytable.cards[0].dealt
                            },ImageView {
                                antialias: true
                                transform:  translate(-45,0)
                                image: Image { url: bind main.state.mytable.cards[1].getImage() }
                                valign: CENTER
                                halign: CENTER
                                visible: bind main.state.mytable.cards[1].dealt
                            },ImageView {
                                antialias: true
                                transform:  translate(0,0)
                                image: Image { url: bind main.state.mytable.cards[2].getImage() }
                                valign: CENTER
                                halign: CENTER
                                visible: bind main.state.mytable.cards[2].dealt
                            },ImageView {
                                antialias: true
                                transform:  translate(45,0)
                                image: Image { url: bind main.state.mytable.cards[3].getImage() }
                                valign: CENTER
                                halign: CENTER
                                visible: bind main.state.mytable.cards[3].dealt
                            },ImageView {
                                antialias: true
                                transform:  translate(45*2,0)
                                image: Image { url: bind main.state.mytable.cards[4].getImage() }
                                valign: CENTER
                                halign: CENTER
                                visible: bind main.state.mytable.cards[4].dealt
                            }]
                        },
                        Group{
                            transform: [translate(padx,pady)]
                            content: bind foreach(i in [0..7])
                            Group {content: [
                            Group {
                                transform: bind translate(circleup*Math.cos(parametrizeRadial(i/8.0)),-circleup*Math.sin(parametrizeRadial(i/8.0)))
                                content: [
                                Circle{
                                    cx: 0
                                    cy: 0
                                    radius: 30
                                    fill: orange
                                    stroke: darkorange
                                    strokeWidth: 2
                                    opacity: bind if(main.state.mytable.players[i].seated==false) then 0.2
                                    else 0.8
                                },Text {
                                    x: 0
                                    y: 0
                                    visible: bind main.state.mytable.players[i].seated
                                    content: bind main.state.mytable.players[i].name
                                    font: new Font("Tahoma", "PLAIN",11)
                                    fill: black
                                    halign: CENTER
                                    valign: CENTER
                                    opacity: 0.9
                                },
                                Text {
                                    x: 0
                                    y: 0
                                    visible: bind main.state.mytable.players[i].seated==false
                                    content: "?"
                                    font: new Font("Tahoma", "PLAIN",30)
                                    fill: darkorange
                                    halign: CENTER
                                    valign: CENTER
                                    opacity: 0.5
                                }]
                            },
                            Group{
                                visible: bind main.state.mytable.players[i].seated
                                transform: bind rotate(Math.round(180*(Math.PI/2.0-parametrizeRadial(i/8.0))/Math.PI)%360,0,0)
                                content: [ImageView {
                                    antialias: true
                                    transform: translate(-22,-cardsup)
                                    image: Image { url: bind main.state.mytable.players[i].cards[0].getImage() }
                                    visible: bind main.state.mytable.players[i].cards[0].dealt
                                    halign: CENTER
                                },ImageView {
                                    antialias: true
                                    transform: translate(22,-cardsup)
                                    image: Image { url: bind main.state.mytable.players[i].cards[1].getImage() }
                                    visible: bind main.state.mytable.players[i].cards[1].dealt
                                    halign: CENTER
                                }]
                            }]
                            transform: bind [translate(
                            Math.round(parametrizeX(i/8.0)),
                            Math.round(parametrizeY(i/8.0)))]
                            }
                        }
                        ]
                    }
                    background: black
                }},
                SplitView{
                    weight:0.215
                    content:BorderPanel{
                        center:SplitPane{
                            orientation: HORIZONTAL
                            content:[
                            SplitView{
                                weight: 0.60
                                content: FlowPanel{
                                    content: [FlowPanel{
                                        content: Button {
                                            text: "Start Game"
                                            toolTipText: "Start the game at this table."
                                            action: operation() {
                                                startgame();
                                            }
                                        }
                                        visible: bind(main.state.mytable.state == 0)
                                    },SimpleLabel{
                                        text: "Please wait for the next game to start."
                                        visible: bind(main.state.mytable.state == 1)
                                    },
                                    GroupPanel{
                                        var firstRow= Row{alignment: BASELINE}
                                        var secondRow= Row{alignment: BASELINE}
                                        var firstColumn= Column{ alignment:TRAILING}
                                        var secondColumn= Column{ alignment:TRAILING}
                                        var thirdColumn= Column{ alignment:TRAILING}
                                        var fourthColumn= Column{ alignment:TRAILING}
                                        
                                        rows: [firstRow,secondRow]
                                        columns: [firstColumn,secondColumn,thirdColumn,fourthColumn]
                                        
                                        visible: bind(main.state.mytable.state==2)
                                        
                                        content:[TextField{
                                            row: firstRow
                                            column: firstColumn
                                            value: bind amount
                                            columns: 5
                                        },
                                        Button{
                                            row: firstRow
                                            column: secondColumn
                                            text: "Bet"
                                            toolTipText: "Bet the entered amount"
                                            action: operation() {
                                                bet();
                                            }
                                        },
                                        Button{
                                            row: firstRow
                                            column: thirdColumn
                                            text: "Check"
                                            toolTipText: "Check"
                                            action: operation() {
                                                check();
                                            }
                                        },
                                        Button{
                                            row: firstRow
                                            column: fourthColumn
                                            text: "Call"
                                            toolTipText: "Call"
                                            action: operation() {
                                                call();
                                            }
                                        },
                                        Button{
                                            row: secondRow
                                            column: firstColumn
                                            text: "Fold"
                                            toolTipText: "Fold"
                                            action: operation() {
                                                fold();
                                            }
                                        },
                                        Button{
                                            row: secondRow
                                            column: secondColumn
                                            text: "Raise"
                                            toolTipText: "Raise"
                                            action: operation() {
                                                raise();
                                            }
                                        },
                                        Button{
                                            row: secondRow
                                            column: thirdColumn
                                            text: "All In"
                                            toolTipText: "Go All In"
                                            action: operation() {
                                                allin();
                                            }
                                        }]
                                    }
                                    ]
                                }
                            },
                            SplitView{
                                weight: 0.40
                                content: Box {
                                    orientation: HORIZONTAL
                                    content: EditorPane{
                                        inUpdate: bind main.state.busy
                                        contentType: HTML
                                        editable: false
                                        text: bind "<html><body><div style='font-size:x-small;'>{main.state.events}<div></body></html>"
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
}
operation GameTable.changeButtons(s:Integer){
    System.out.println("State changed to {s}");
}

operation GameTable.startgame(){
    try{
        main.client.startGame();
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
        main.client.bet(amount);
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
        main.client.call();
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
        main.client.fold();
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
        main.client.check();
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
        main.client.raise(amount);
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

operation GameTable.allin(){
    try{
        main.client.allIn();
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

operation GameTable.relogin(){
    leavetable();
    main.relogin();
}

operation GameTable.leavetable(){
    try{
        main.client.leaveTable();
    }catch(e:RemoteException){
        // no op
    }catch(e:IllegalActionException){
        MessageDialog{
            title: "Failed to leave table"
            visible: true
            message: e.getMessage()
            messageType: ERROR
        }
    }
}

function GameTable.parametrizeX(position:Number):Number{
    var Ltot:Number = 2*sqrw+Math.PI*sqrh;
    var plaats:Number = position*Ltot;
    var arc1:Number = (plaats-sqrw/2.0)/(sqrh/2.0);
    var arc2:Number = (Ltot-plaats-sqrw/2.0)/(sqrh/2.0);
    return if(plaats < sqrw/2.0) then
    sqrh/2.0+sqrw/2.0+plaats
    else if (plaats > Ltot-sqrw/2.0) then
    sqrh/2.0+sqrw/2.0-Ltot+plaats
    else if(plaats < (Ltot+sqrw)/2.0 and plaats > (Ltot-sqrw)/2.0) then
    sqrh/2.0+sqrw-(plaats-(Ltot-sqrw)/2.0)
    else if(plaats < Ltot/2.0) then
    sqrh/2.0+sqrw+Math.sin(arc1)*sqrh/2.0
    else
        sqrh/2.0-Math.sin(arc2)*sqrh/2.0;
}

function GameTable.parametrizeY(position:Number):Number{
    var Ltot:Number = 2*sqrw+Math.PI*sqrh;
    var plaats:Number = position*Ltot;
    var arc1:Number = (plaats-sqrw/2.0)/(sqrh/2.0);
    var arc2:Number = (Ltot-plaats-sqrw/2.0)/(sqrh/2.0);
    return if(plaats < sqrw/2.0 or plaats > Ltot-sqrw/2.0) then
    0
    else if(plaats < (Ltot+sqrw)/2.0 and plaats > (Ltot-sqrw)/2.0) then
    sqrh
    else if(plaats < Ltot/2.0) then
    sqrh/2.0*(1-Math.cos(arc1))
    else
        sqrh/2.0*(1-Math.cos(arc2));
}

function GameTable.parametrizeRadial(position:Number):Number{
    var Ltot:Number = 2*sqrw+Math.PI*sqrh;
    var plaats:Number = position*Ltot;
    var arc1:Number = (plaats-sqrw/2.0)/(sqrh/2.0);
    var arc2:Number = (Ltot-plaats-sqrw/2.0)/(sqrh/2.0);
    return if(plaats < sqrw/2.0 or plaats > Ltot-sqrw/2.0) then
    Math.PI*0.5
    else if(plaats < (Ltot+sqrw)/2.0 and plaats > (Ltot-sqrw)/2.0) then
    Math.PI*1.5
    else if(plaats < Ltot/2.0) then
    Math.PI*0.5-arc1
    else
        Math.PI*0.5+arc2;
}