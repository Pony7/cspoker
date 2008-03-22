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


import java.lang.*;
import org.cspoker.common.player.SeatedPlayer;
import org.cspoker.common.elements.table.SeatId;

class PlayerView {
    attribute name:String;
    attribute stack:Integer;
    
    attribute seated:Boolean;
    
    attribute lastaction:String;
    attribute amount:Integer;
    
    attribute cards:CardView*;
    
    attribute next:Boolean;
    
    attribute seatid:Integer;
    
    operation toPlayerView(players:SeatedPlayer):PlayerView;
    
    operation toPlayerViews(players:SeatedPlayer*):PlayerView*;
}

operation PlayerView.toPlayerViews(players:SeatedPlayer*){
    var temp = foreach(i in [0..7])
    PlayerView{
        seated: false
        cards: [CardView{
            dealt: false
        },CardView{
            dealt: false
        }]
        next:false
        seatid: i
    };
    for(p in players){
        temp[p.getSeatId().getId()] = toPlayerView(p);
    }
    return temp;
}

operation PlayerView.toPlayerView(p:SeatedPlayer){
    return PlayerView{
            name: p.getName()
            stack: p.getStackValue()
            amount: p.getBetChipsValue()
            lastaction: ""
            seated: true
            cards: [CardView{
                dealt: false
            },CardView{
                dealt: false
            }]
            next:false
            seatid: p.getSeatId().getId()
        };
}