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
package org.cspoker.client.gui.javafx.game;

import java.lang.*;
import javafx.ui.*;
import javafx.ui.canvas.*;

class PlayingCards {
    attribute c1:Card;
    attribute c2:Card;
    attribute c3:Card;
    attribute c4:Card;
    attribute c5:Card;
    
    attribute state:Integer;
    
    attribute cp1:Card;
    attribute cp2:Card;
    
    function getCard(card:Card):String;
}

function PlayingCards.getCard(card){
    return if(card.visible) then
    "./org/cspoker/client/gui/javafx/images/cards/simple/simple_{card.suit}_{card.rank}.svg.png"
    else
        "./org/cspoker/client/gui/javafx/images/cards/backs/back08.svg.png";
}

