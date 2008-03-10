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

class Card {
    attribute rank:String;
    attribute suit:String;
    attribute dealt:Boolean;
    attribute visible:Boolean;
    
    function getImage():String;
}

function Card.getImage(){
    return if(visible) then
        "./org/cspoker/client/gui/javafx/images/cards/simple/simple_{suit}_{rank}.svg.png"
    else
        "./org/cspoker/client/gui/javafx/images/cards/backs/back08.svg.png";
}