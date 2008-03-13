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
package org.cspoker.client.gui.javafx.views;

import org.cspoker.common.elements.table.Table;
import org.cspoker.common.elements.table.TableId;
import org.cspoker.common.elements.GameProperty;
import java.lang.*;
import org.cspoker.common.player.Player;
import org.cspoker.client.gui.javafx.JavaFxClient;

class TableView {
    attribute name:String;
    
    attribute id:TableId;
    attribute smallBlind:Integer;
    attribute bigBlind:Integer;
    
    attribute pot:Integer;
    attribute temppot:Integer;
    
    attribute state:Integer;
    
    attribute dealer:Integer;
    
    attribute cards:CardView*;
    
    attribute players:PlayerView*;
    
    operation toTableViews(tables:Table*):TableView*;
}

operation TableView.toTableViews(tables:Table*){
    return foreach(t in tables)
    TableView{
        bigBlind: t.getGameProperty().getBigBlind()
        smallBlind: t.getGameProperty().getSmallBlind()
        id: t.getId()
        name: t.getName()
        temppot: 0
        pot: 0
        dealer: -1
        //Who invented this crazy JavaFX casting syntax??
        players: PlayerView{}.toPlayerViews(JavaFxClient.toArray(t.getPlayers()))
        cards: [CardView{
            visible: false
            dealt: false
        },CardView{
            visible: false
            dealt: false
        },CardView{
            visible: false
            dealt: false
        },CardView{
            visible: false
            dealt: false
        },CardView{
            visible: false
            dealt: false
        }]
    };
}