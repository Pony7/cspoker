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
import java.lang.*;
import org.cspoker.client.common.CommunicationProvider;
import org.cspoker.client.allcommunication.LoadProvidersFromXml;
import org.cspoker.client.gui.javafx.JavaFxClient;

class CreateTable {
    attribute ts:TableSelection;
    
    attribute screen:Frame;
    
    attribute name: String;
    
    operation cancel();
    operation create();
}
trigger on new CreateTable{
    screen=Frame{
        title: "Create Table"
        width: 230
        height: 100
        visible: true
        resizable: false
        centerOnScreen: true
        onClose: operation() {cancel();}
        content: BorderPanel{
            center:GroupPanel{
                var singleRow= Row{alignment: BASELINE}
                var labelsColumn= Column{ alignment:TRAILING}
                var fieldsColumn= Column {alignment:LEADING resizable:true }
                
                rows: [singleRow]
                columns: [labelsColumn,fieldsColumn]
                
                content:[
                SimpleLabel{
                    row: singleRow
                    column: labelsColumn
                    text: "Name:"
                    horizontalAlignment: LEADING
                },
                TextField{
                    row: singleRow
                    column: fieldsColumn
                    value: bind name
                }]
            }
            bottom:FlowPanel{
                content:[
                Button {
                    text: "Create Table"
                    verticalTextPosition: CENTER
                    horizontalTextPosition: LEADING
                    toolTipText: "Create a new table named {name}"
                    defaultButton: true
                    action: bind operation() {
                        create();
                    }
                },Button {
                    text: "Cancel"
                    verticalTextPosition: CENTER
                    horizontalTextPosition: LEADING
                    toolTipText: "Cancel"
                    defaultButton: true
                    action: bind operation() {
                        cancel();
                    }
                }]
            }
        }
    };
}
operation CreateTable.cancel(){
    this.screen.hide();
    ts.active = true;
}
operation CreateTable.create(){
    this.screen.hide();
    ts.create_table(name);
}
