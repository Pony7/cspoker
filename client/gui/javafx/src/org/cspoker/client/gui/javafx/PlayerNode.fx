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

class PlayerNode extends CompositeNode {
    attribute player: Player;
}

function PlayerNode.composeNode(){
    return if(player.seated) then
    Group {
        content:
            [Group{
            transform: translate(0, -5)
            content: [ImageView {
                transform:  translate(-25,0)
                image: Image { url: bind player.cards[0].getImage() }
                valign: BOTTOM
                halign: CENTER
                visible: bind player.cards[0].dealt
            },ImageView {
                transform:  translate(25,0)
                image: Image { url: bind player.cards[1].getImage() }
                valign: BOTTOM
                halign: CENTER
                visible: bind player.cards[1].dealt
            }]
        },Group{
            transform: translate(0, 5)
            content: [Text {
                content: bind player.name
                font: new Font("Tahoma", "PLAIN",12)
                fill: orange
                halign: CENTER
                valign: TOP
            }]
        }]
    }
    else
        Group {
            content:
                [Group{
                transform: translate(0, 5)
                content: [Text {
                    content: "?"
                    font: new Font("Tahoma", "PLAIN",12)
                    fill: orange
                    halign: CENTER
                    valign: TOP
                }]
            }]
        };
}
