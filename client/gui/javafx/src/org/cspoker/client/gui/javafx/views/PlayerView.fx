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
import org.cspoker.common.player.Player;

class PlayerView {
    attribute name:String;
    attribute stack:Integer;
    
    attribute seated:Boolean;
    
    attribute lastaction:String;
    attribute amount:Integer;
    
    attribute cards:CardView*;
    
    operation toPlayerView(players:Player*):PlayerView*;
}

operation PlayerView.toPlayerView(players:Player*){
    var temp = foreach(p in players)
    PlayerView{
        name: p.getName()
        stack: p.getStackValue()
        seated: true
        cards: [CardView{
            dealt: false
        },CardView{
            dealt: false
        }]
    };
    var temp2 = foreach(i in [1..8-sizeof players])
    PlayerView{
        seated: false
        cards: [CardView{
            dealt: false
        },CardView{
            dealt: false
        }]
    };
    insert temp2 as last into temp;
    return temp;
}