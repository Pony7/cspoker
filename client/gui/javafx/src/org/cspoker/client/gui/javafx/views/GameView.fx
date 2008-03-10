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

import org.cspoker.client.gui.javafx.*;
import java.lang.*;
import org.cspoker.client.gui.javafx.game.*;
import org.cspoker.client.gui.javafx.views.*;
import org.cspoker.common.elements.table.Table;

class GameView {
    attribute events:String;
    attribute busy:Boolean;
    
    attribute myname:String?;
    attribute mytableid:TableId?;
    
    function mytable:FXTable;
    function me:Player;
        
    attribute tables: FXTable*;
}

function GameView.me{
    var playerindex = select indexof player from player in mytable.players 
                                                        where player.name.equals(myname);
    return mytable.players[playerindex];
}

function GameView.mytable{
    var tableindex = select indexof t from t in tables
                              where t.getId().equals(mytableid);
    return tables[tableindex];
}